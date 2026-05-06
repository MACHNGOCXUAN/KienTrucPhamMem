import express from "express";
import cors from "cors";
import productRoutes from "./routes/product.routes.js";

const app = express();

app.use(cors({
  origin: "*",
  methods: ["GET", "POST", "PUT", "DELETE"]
}));

app.use(express.json());

app.use("/products", productRoutes);

const PORT = 8081;

app.listen(PORT, () => {
  console.log(`🚀 Product Service chạy tại http://localhost:${PORT}`);
});