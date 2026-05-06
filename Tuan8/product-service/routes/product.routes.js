import express from "express";
import {
  getProducts,
  getProductById,
  createProduct,
  updateProduct,
  updateStock,
  deleteProduct
} from "../services/product.service.js";

const router = express.Router();

// GET ALL
router.get("/", async (req, res) => {
  try {
    const products = await getProducts();
    res.json(products);
  } catch (error) {
    res.status(500).json({ message: "Lỗi server", error: error.message });
  }
});

// GET BY ID
router.get("/:id", async (req, res) => {
  try {
    const product = await getProductById(req.params.id);

    if (!product) {
      return res.status(404).json({ message: "Không tìm thấy sản phẩm" });
    }

    res.json(product);
  } catch (error) {
    res.status(500).json({ message: "Lỗi server", error: error.message });
  }
});

// CREATE
router.post("/", async (req, res) => {
  try {
    const {
      name,
      description,
      price,
      salePrice,
      imageUrl,
      stock,
      category,
      skuId,
      discount
    } = req.body;

    if (
      !name ||
      !price ||
      salePrice === undefined ||
      !imageUrl ||
      stock === undefined ||
      !category ||
      !skuId ||
      discount === undefined
    ) {
      return res.status(400).json({ message: "Thiếu dữ liệu bắt buộc" });
    }

    const product = await createProduct({
      id: Date.now().toString(),
      name,
      description,
      price,
      salePrice,
      imageUrl,
      stock,
      category,
      skuId,
      discount
    });

    res.status(201).json(product);
  } catch (error) {
    res.status(500).json({ message: "Lỗi server", error: error.message });
  }
});

// UPDATE
router.put("/:id", async (req, res) => {
  try {
    const updated = await updateProduct(req.params.id, req.body);

    if (!updated) {
      return res.status(404).json({ message: "Không tìm thấy sản phẩm" });
    }

    res.json(updated);
  } catch (error) {
    res.status(500).json({ message: "Lỗi server", error: error.message });
  }
});

// UPDATE STOCK
router.put("/:id/stock", async (req, res) => {
  try {
    const { quantity } = req.body;

    if (quantity === undefined || typeof quantity !== "number") {
      return res.status(400).json({ message: "Vui lòng cung cấp quantity (số)" });
    }

    const updated = await updateStock(req.params.id, quantity);

    if (!updated) {
      return res.status(404).json({ message: "Không tìm thấy sản phẩm" });
    }

    res.json({
      message: "Cập nhật kho thành công",
      product: updated
    });
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
});

// DELETE
router.delete("/:id", async (req, res) => {
  try {
    const success = await deleteProduct(req.params.id);

    if (!success) {
      return res.status(404).json({ message: "Không tìm thấy sản phẩm" });
    }

    res.json({ message: "Đã xóa sản phẩm" });
  } catch (error) {
    res.status(500).json({ message: "Lỗi server", error: error.message });
  }
});

export default router;