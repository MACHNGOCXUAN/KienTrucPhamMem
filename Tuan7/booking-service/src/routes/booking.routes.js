import express from "express";
import { v4 as uuidv4 } from "uuid";
import { getDB } from "../config/db.js";
import { producer } from "../config/kafka.js";

const router = express.Router();

/**
 * @route   POST /bookings
 * @desc    Tạo đơn đặt vé mới và gửi event sang Kafka
 */
router.post("/", async (req, res) => {
  try {
    // Lấy đầy đủ các trường từ body (phù hợp với thiết kế DB mới)
    const { user_id, movie_id, seat_number, total_price } = req.body;

    // 1. Kiểm tra dữ liệu đầu vào
    if (!user_id || !movie_id || !seat_number || !total_price) {
      return res.status(400).json({ 
        message: "Thiếu thông tin: user_id, movie_id, seat_number, total_price là bắt buộc." 
      });
    }

    const db = getDB();
    const bookingId = uuidv4(); // Tạo ID duy nhất bằng UUID
    const status = "PENDING";    // Trạng thái mặc định ban đầu

    // 2. Lưu vào MariaDB
    await db.execute(
      `INSERT INTO bookings (id, user_id, movie_id, seat_number, total_price, status) 
       VALUES (?, ?, ?, ?, ?, ?)`,
      [bookingId, user_id, movie_id, seat_number, total_price, status]
    );

    // 3. Chuẩn bị dữ liệu Event để gửi sang Kafka
    // Lưu ý: Gửi thêm amount để Payment Service có dữ liệu xử lý
    const bookingEvent = {
      eventType: "BOOKING_CREATED",
      payload: { 
        booking_id: bookingId, 
        user_id, 
        movie_id, 
        amount: total_price 
      },
    };

    // 4. Publish event lên Kafka
    // await producer.send({
    //   topic: "booking-topic",
    //   messages: [
    //     { 
    //       key: bookingId, // Dùng ID làm key để đảm bảo thứ tự message nếu cần
    //       value: JSON.stringify(bookingEvent) 
    //     }
    //   ],
    // });

    // 5. Phản hồi cho Frontend
    console.log(`✅ Booking created: ${bookingId}`);
    res.status(201).json({ 
      message: "Đặt vé đang được xử lý...",
      booking_id: bookingId, 
      status: status 
    });

  } catch (err) {
    console.error("❌ Error creating booking:", err);
    res.status(500).json({ error: "Không thể tạo đơn đặt vé. Vui lòng thử lại!" });
  }
});

/**
 * @route   GET /bookings
 * @desc    Lấy danh sách tất cả các đơn đặt vé
 */
router.get("/", async (req, res) => {
  try {
    const db = getDB();
    // Lấy và sắp xếp theo thời gian mới nhất lên đầu
    const [rows] = await db.execute("SELECT * FROM bookings ORDER BY created_at DESC");
    res.json(rows);
  } catch (err) {
    console.error("❌ Error fetching bookings:", err);
    res.status(500).json({ error: "Lấy danh sách thất bại" });
  }
});

export default router;