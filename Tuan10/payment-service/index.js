import express from 'express';
import cors from 'cors';
import axios from 'axios';
import mariadb from 'mariadb';

const app = express();
const PORT = 3004;

app.use(cors());
app.use(express.json());

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
      CREATE DATABASE IF NOT EXISTS payment_service_db
    `);

    await conn.end();

    pool = mariadb.createPool({
      ...dbConfig,
      database: 'payment_service_db',
      connectionLimit: 5
    });

    await pool.query(`
      CREATE TABLE IF NOT EXISTS payments (
        id INT AUTO_INCREMENT PRIMARY KEY,
        orderId INT NOT NULL,
        method VARCHAR(50),
        status VARCHAR(50) DEFAULT 'Success',
        createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      )
    `);

    console.log('✅ Payment DB initialized');

  } catch (error) {

    console.error('❌ Payment DB Error:', error.message);
  }
}

initDB();

/*
|--------------------------------------------------------------------------
| HEALTH CHECK
|--------------------------------------------------------------------------
*/

app.get('/health', (req, res) => {
  res.json({
    service: 'payment-service',
    status: 'UP'
  });
});

/*
|--------------------------------------------------------------------------
| CREATE PAYMENT
|--------------------------------------------------------------------------
*/

app.post('/api/payments', async (req, res) => {

  try {

    const { orderId, method } = req.body;

    /*
    |--------------------------------------------------------------------------
    | VALIDATION
    |--------------------------------------------------------------------------
    */

    if (!orderId) {
      return res.status(400).json({
        success: false,
        message: 'orderId is required'
      });
    }

    /*
    |--------------------------------------------------------------------------
    | SAVE PAYMENT
    |--------------------------------------------------------------------------
    */

    const result = await pool.query(
      `
      INSERT INTO payments
      (orderId, method)
      VALUES (?, ?)
      `,
      [orderId, method || 'COD']
    );

    /*
    |--------------------------------------------------------------------------
    | UPDATE ORDER STATUS
    |--------------------------------------------------------------------------
    */

    console.log('Updating order status...');

    const orderRes = await axios.patch(
      `http://localhost:3003/api/orders/${orderId}/status`,
      {
        status: 'Paid'
      }
    );

    /*
    |--------------------------------------------------------------------------
    | ORDER DATA
    |--------------------------------------------------------------------------
    */

    const order = orderRes.data.data;

    /*
    |--------------------------------------------------------------------------
    | NOTIFICATION
    |--------------------------------------------------------------------------
    */

    console.log(`
========================================
🔔 PAYMENT SUCCESS
👤 User: ${order.username}
🧾 Order: #${order.id}
💰 Total: ${order.total}
========================================
`);

    /*
    |--------------------------------------------------------------------------
    | RESPONSE
    |--------------------------------------------------------------------------
    */

    res.json({
      success: true,

      message: 'Payment success',

      data: {
        paymentId: result.insertId,
        order
      }
    });

  } catch (error) {

    console.error('❌ PAYMENT ERROR:', {

      message: error.message,

      response: error.response?.data,

      status: error.response?.status
    });

    res.status(500).json({
      success: false,

      message:
        error.response?.data?.message ||
        error.message
    });
  }
});

/*
|--------------------------------------------------------------------------
| GET PAYMENTS
|--------------------------------------------------------------------------
*/

app.get('/api/payments', async (req, res) => {

  try {

    const payments = await pool.query(`
      SELECT * FROM payments
    `);

    res.json({
      success: true,
      data: payments
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
🚀 PAYMENT SERVICE RUNNING
🌐 PORT: ${PORT}
========================================
`);
});