import express from "express";
import { v4 as uuidv4 } from "uuid";
import { getDB } from "../config/db.js";
import { producer } from "../config/kafka.js";

const router = express.Router();

// Tạo đơn đặt vé mới
router.post("/", async (req, res) => {
  try {
    const { user_id, movie_id, seat_number, total_price } = req.body;

    if (!user_id || !movie_id || !seat_number || !total_price) {
      return res.status(400).json({ 
        message: "Thiếu thông tin: user_id, movie_id, seat_number, total_price là bắt buộc." 
      });
    }

    const db = getDB();
    const bookingId = uuidv4();
    const status = "PENDING";

    await db.execute(
      `INSERT INTO bookings (id, user_id, movie_id, seat_number, total_price, status) 
       VALUES (?, ?, ?, ?, ?, ?)`,
      [bookingId, user_id, movie_id, seat_number, total_price, status]
    );

    // Tạo event object để gửi lên Kafka
    const bookingEvent = {
      eventType: "BOOKING_CREATED",
      payload: { 
        booking_id: bookingId, 
        user_id, 
        movie_id, 
        amount: total_price 
      },
    };

    // Gửi event lên Kafka
    await producer.send({
      topic: "booking-topic",
      messages: [
        { 
          key: bookingId,// Sử dụng bookingId làm key để đảm bảo ordering theo booking
          value: JSON.stringify(bookingEvent) 
        }
      ],
    });

    res.status(201).json({ 
      message: "Đặt vé đang được xử lý...",
      booking_id: bookingId, 
      status: status 
    });

  } catch (err) {
    res.status(500).json({ error: "Không thể tạo đơn đặt vé. Vui lòng thử lại!" });
  }
});

router.get("/", async (req, res) => {
  try {
    const db = getDB();
    const [rows] = await db.execute("SELECT * FROM bookings ORDER BY created_at DESC");
    res.json(rows);
  } catch (err) {
    res.status(500).json({ error: "Lấy danh sách thất bại" });
  }
});

export default router;