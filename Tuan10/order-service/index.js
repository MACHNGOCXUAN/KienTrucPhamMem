import express from 'express';
import cors from 'cors';
import axios from 'axios';
import mariadb from 'mariadb';

import rateLimit from 'express-rate-limit';
import axiosRetry from 'axios-retry';
import CircuitBreaker from 'opossum';

const app = express();
const PORT = 3003;

/*
|--------------------------------------------------------------------------
| MIDDLEWARE
|--------------------------------------------------------------------------
*/

app.use(cors());
app.use(express.json());

/*
|--------------------------------------------------------------------------
| RATE LIMITER
|--------------------------------------------------------------------------
| Chống spam API
*/

const limiter = rateLimit({
  windowMs: 60 * 1000, // 1 phút
  max: 100, // 100 request/IP
  message: {
    success: false,
    message: 'Too many requests'
  }
});

app.use(limiter);

/*
|--------------------------------------------------------------------------
| AXIOS INSTANCE
|--------------------------------------------------------------------------
| timeout + retry
*/

const api = axios.create({
  timeout: 3000
});

axiosRetry(api, {
  retries: 3,

  retryDelay: (retryCount) => {
    console.log(`Retry attempt: ${retryCount}`);
    return axiosRetry.exponentialDelay(retryCount);
  },

  retryCondition: (error) => {
    return (
      axiosRetry.isNetworkError(error) ||
      error.code === 'ECONNABORTED' ||
      error.response?.status >= 500
    );
  }
});

/*
|--------------------------------------------------------------------------
| DATABASE
|--------------------------------------------------------------------------
*/

const dbConfig = {
  host: 'localhost',
  user: 'root',
  password: 'sapassword',
  port: 3306
};

let pool;

async function initDB() {
  try {
    const conn = await mariadb.createConnection(dbConfig);

    await conn.query(`
      CREATE DATABASE IF NOT EXISTS order_service_db
      CHARACTER SET utf8mb4
      COLLATE utf8mb4_unicode_ci
    `);

    await conn.end();

    pool = mariadb.createPool({
      ...dbConfig,
      database: 'order_service_db',
      connectionLimit: 5
    });

    /*
    |--------------------------------------------------------------------------
    | ORDERS TABLE
    |--------------------------------------------------------------------------
    */

    await pool.query(`
      CREATE TABLE IF NOT EXISTS orders (
        id INT AUTO_INCREMENT PRIMARY KEY,
        userId INT NOT NULL,
        username VARCHAR(255) NOT NULL,
        total INT NOT NULL,
        status VARCHAR(50) DEFAULT 'Pending',
        createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      )
    `);

    /*
    |--------------------------------------------------------------------------
    | ORDER ITEMS TABLE
    |--------------------------------------------------------------------------
    */

    await pool.query(`
      CREATE TABLE IF NOT EXISTS order_items (
        id INT AUTO_INCREMENT PRIMARY KEY,
        orderId INT NOT NULL,
        foodId INT NOT NULL,
        name VARCHAR(255) NOT NULL,
        quantity INT NOT NULL,
        price INT NOT NULL,

        FOREIGN KEY (orderId)
        REFERENCES orders(id)
        ON DELETE CASCADE
      )
    `);

    console.log('✅ Order Service Database initialized');
  } catch (error) {
    console.error('❌ DB ERROR:', error.message);
  }
}

initDB();

/*
|--------------------------------------------------------------------------
| MICROSERVICE FUNCTIONS
|--------------------------------------------------------------------------
*/

async function getUsers() {
  console.log('Calling User Service...');

  const res = await api.get(
    'http://localhost:3001/api/users'
  );

  return res.data;
}

async function getFoods() {
  console.log('Calling Food Service...');

  const res = await api.get(
    'http://localhost:3002/api/foods'
  );

  return res.data;
}

/*
|--------------------------------------------------------------------------
| CIRCUIT BREAKER
|--------------------------------------------------------------------------
*/

const breakerOptions = {
  timeout: 5000,

  // nếu >50% request fail => open circuit
  errorThresholdPercentage: 50,

  // 10s sau thử lại
  resetTimeout: 10000
};

const userBreaker = new CircuitBreaker(
  getUsers,
  breakerOptions
);

const foodBreaker = new CircuitBreaker(
  getFoods,
  breakerOptions
);

/*
|--------------------------------------------------------------------------
| FALLBACK
|--------------------------------------------------------------------------
*/

userBreaker.fallback(() => {
  throw new Error('User Service unavailable');
});

foodBreaker.fallback(() => {
  throw new Error('Food Service unavailable');
});

/*
|--------------------------------------------------------------------------
| EVENTS
|--------------------------------------------------------------------------
*/

userBreaker.on('open', () => {
  console.log('🚨 User Circuit OPEN');
});

userBreaker.on('halfOpen', () => {
  console.log('⚠️ User Circuit HALF OPEN');
});

userBreaker.on('close', () => {
  console.log('✅ User Circuit CLOSED');
});

foodBreaker.on('open', () => {
  console.log('🚨 Food Circuit OPEN');
});

foodBreaker.on('halfOpen', () => {
  console.log('⚠️ Food Circuit HALF OPEN');
});

foodBreaker.on('close', () => {
  console.log('✅ Food Circuit CLOSED');
});

/*
|--------------------------------------------------------------------------
| HEALTH CHECK
|--------------------------------------------------------------------------
*/

app.get('/health', (req, res) => {
  res.json({
    success: true,
    service: 'order-service',
    status: 'UP',
    time: new Date()
  });
});

/*
|--------------------------------------------------------------------------
| CREATE ORDER
|--------------------------------------------------------------------------
*/

app.post('/api/orders', async (req, res) => {
  try {
    const { userId, items } = req.body;

    /*
    |--------------------------------------------------------------------------
    | VALIDATION
    |--------------------------------------------------------------------------
    */

    if (!userId) {
      return res.status(400).json({
        success: false,
        message: 'userId is required'
      });
    }

    if (!items || !Array.isArray(items)) {
      return res.status(400).json({
        success: false,
        message: 'items must be array'
      });
    }

    /*
    |--------------------------------------------------------------------------
    | GET USERS
    |--------------------------------------------------------------------------
    */

    const users = await userBreaker.fire();

    const user = users.find(
      (u) => u.id === Number(userId)
    );

    if (!user) {
      return res.status(400).json({
        success: false,
        message: 'Invalid user'
      });
    }

    /*
    |--------------------------------------------------------------------------
    | GET FOODS
    |--------------------------------------------------------------------------
    */

    const allFoods = await foodBreaker.fire();

    /*
    |--------------------------------------------------------------------------
    | CALCULATE TOTAL
    |--------------------------------------------------------------------------
    */

    let total = 0;

    const itemsToProcess = items.map((item) => {
      const food = allFoods.find(
        (f) => f.id === Number(item.foodId)
      );

      if (!food) {
        throw new Error(
          `Food ${item.foodId} not found`
        );
      }

      total += food.price * item.quantity;

      return {
        foodId: food.id,
        name: food.name,
        quantity: item.quantity,
        price: food.price
      };
    });

    /*
    |--------------------------------------------------------------------------
    | CREATE ORDER
    |--------------------------------------------------------------------------
    */

    const orderResult = await pool.query(
      `
      INSERT INTO orders
      (userId, username, total)
      VALUES (?, ?, ?)
      `,
      [
        userId,
        user.username,
        total
      ]
    );

    const orderId = Number(orderResult.insertId);

    /*
    |--------------------------------------------------------------------------
    | INSERT ORDER ITEMS
    |--------------------------------------------------------------------------
    */

    for (const item of itemsToProcess) {
      await pool.query(
        `
        INSERT INTO order_items
        (orderId, foodId, name, quantity, price)
        VALUES (?, ?, ?, ?, ?)
        `,
        [
          orderId,
          item.foodId,
          item.name,
          item.quantity,
          item.price
        ]
      );
    }

    /*
    |--------------------------------------------------------------------------
    | RESPONSE
    |--------------------------------------------------------------------------
    */

    res.status(201).json({
      success: true,

      data: {
        id: orderId,
        userId,
        username: user.username,
        total,
        status: 'Pending',
        items: itemsToProcess
      }
    });

  } catch (error) {

    console.error('CREATE ORDER ERROR:', error.message);

    res.status(500).json({
      success: false,
      message: error.message
    });
  }
});

/*
|--------------------------------------------------------------------------
| GET ORDERS
|--------------------------------------------------------------------------
*/

app.get('/api/orders', async (req, res) => {
  try {

    const orders = await pool.query(
      'SELECT * FROM orders'
    );

    const enrichedOrders = [];

    for (const order of orders) {

      const items = await pool.query(
        `
        SELECT *
        FROM order_items
        WHERE orderId = ?
        `,
        [order.id]
      );

      enrichedOrders.push({
        ...order,

        id: Number(order.id),

        items: items.map((item) => ({
          ...item,
          id: Number(item.id),
          orderId: Number(item.orderId)
        }))
      });
    }

    res.json({
      success: true,
      data: enrichedOrders
    });

  } catch (error) {

    res.status(500).json({
      success: false,
      message: error.message
    });
  }
});

/*
|--------------------------------------------------------------------------
| UPDATE ORDER STATUS
|--------------------------------------------------------------------------
*/

app.patch('/api/orders/:id/status', async (req, res) => {
  try {

    const { id } = req.params;
    const { status } = req.body;

    await pool.query(
      `
      UPDATE orders
      SET status = ?
      WHERE id = ?
      `,
      [status, id]
    );

    const rows = await pool.query(
      `
      SELECT *
      FROM orders
      WHERE id = ?
      `,
      [id]
    );

    if (!rows.length) {
      return res.status(404).json({
        success: false,
        message: 'Order not found'
      });
    }

    res.json({
      success: true,
      data: rows[0]
    });

  } catch (error) {

    res.status(500).json({
      success: false,
      message: error.message
    });
  }
});

/*
|--------------------------------------------------------------------------
| START SERVER
|--------------------------------------------------------------------------
*/

app.listen(PORT, () => {
  console.log(`
========================================
🚀 ORDER SERVICE RUNNING
🌐 PORT: ${PORT}
========================================
`);
});