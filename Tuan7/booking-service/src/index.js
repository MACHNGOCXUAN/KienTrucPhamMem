import express from "express";
import bookingRoutes from "./routes/booking.routes.js";
import { connectDB } from "./config/db.js";
import { connectKafka } from "./config/kafka.js";
import cors from "cors"

const app = express();
app.use(cors());
app.use(express.json());

const PORT = process.env.PORT;

async function startServer() {
  try {
    await connectDB();
    await connectKafka();

    app.use("/bookings", bookingRoutes);

    app.listen(PORT, () => {
      console.log(`Booking service running on port ${PORT}`);
    });
  } catch (err) {
    console.error("Startup error:", err);
    process.exit(1);
  }
}

startServer();