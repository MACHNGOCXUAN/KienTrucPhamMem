import mysql from "mysql2/promise";
import dotenv from "dotenv";

dotenv.config();

let connection;

export const connectDB = async () => {
  try {
    connection = await mysql.createConnection({
      host: process.env.DB_HOST || "127.0.0.1",
      user: process.env.DB_USER || "root",
      password: process.env.DB_PASSWORD || "",
      database: process.env.DB_NAME,
      port: process.env.DB_PORT || 3306,
    });

    // Tạo bảng với đầy đủ các trường để demo kiến trúc Event-Driven
    await connection.execute(`
      CREATE TABLE IF NOT EXISTS bookings (
        id VARCHAR(255) PRIMARY KEY,
        user_id VARCHAR(255) NOT NULL,
        movie_id VARCHAR(255) NOT NULL,
        seat_number VARCHAR(50) NOT NULL,
        total_price DECIMAL(10, 2) NOT NULL,
        status ENUM('PENDING', 'CONFIRMED', 'FAILED') DEFAULT 'PENDING',
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
      )
    `);

    console.log("✅ MariaDB connected & Table 'bookings' is ready!");
  } catch (error) {
    console.error("❌ Lỗi kết nối MariaDB:", error.message);
    // Lưu ý: Đảm bảo bạn đã tạo Database tên là 'booking_db' (hoặc tên trong file .env) trước khi chạy
    process.exit(1); 
  }
};

export const getDB = () => connection;