const express = require("express");
const mysql = require("mysql2/promise");
const { Kafka } = require("kafkajs");
const { v4: uuidv4 } = require("uuid");

const app = express();
app.use(express.json());

// ENV
const DB_CONFIG = {
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
};

const kafka = new Kafka({
  clientId: "booking-service",
  brokers: [process.env.KAFKA_BROKER],
});

const producer = kafka.producer();

let db;

// Init
async function init() {
  db = await mysql.createConnection(DB_CONFIG);

  await db.execute(`
    CREATE TABLE IF NOT EXISTS bookings (
      id VARCHAR(255) PRIMARY KEY,
      user_id VARCHAR(255),
      event_id VARCHAR(255),
      status VARCHAR(50),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    )
  `);

  await producer.connect();

  console.log("Booking Service ready");
}

init();

// POST /bookings
app.post("/bookings", async (req, res) => {
  try {
    const { user_id, event_id } = req.body;

    const id = uuidv4();

    await db.execute(
      "INSERT INTO bookings (id, user_id, event_id, status) VALUES (?, ?, ?, ?)",
      [id, user_id, event_id, "CREATED"]
    );

    const event = {
      event: "BOOKING_CREATED",
      data: {
        id,
        user_id,
        event_id,
      },
    };

    await producer.send({
      topic: "booking-topic",
      messages: [{ value: JSON.stringify(event) }],
    });

    res.json({ id, status: "CREATED" });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: "Create booking failed" });
  }
});

// GET /bookings
app.get("/bookings", async (req, res) => {
  try {
    const [rows] = await db.execute("SELECT * FROM bookings");
    res.json(rows);
  } catch (err) {
    res.status(500).json({ error: "Fetch failed" });
  }
});

app.listen(3000, () => {
  console.log("Booking service running on port 3000");
});