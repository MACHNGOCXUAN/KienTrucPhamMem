import redis from "../config/redis.js";

const products = [
  {
    id: "p1",
    name: "iPhone 15 Pro Max",
    description: "Siêu phẩm flagship của Apple với chip A17 Pro, camera 48MP, thiết kế Titanium cao cấp",
    price: 25000000,
    salePrice: 20990000,
    imageUrl: "https://images.unsplash.com/photo-1695048133142-1a20484d2569",
    stock: 50,
    category: "Smartphone",
    skuId: "SKU-IPHONE15-PROMAX",
    discount: 16
  },
  {
    id: "p2",
    name: "Samsung Galaxy S24 Ultra",
    description: "Flagship Android mạnh mẽ với Snapdragon 8 Gen 3, camera zoom 100x, bút S-Pen tiện lợi",
    price: 24000000,
    salePrice: 19490000,
    imageUrl: "https://images.unsplash.com/photo-1705585173452-1c6b9c0b7b5d",
    stock: 45,
    category: "Smartphone",
    skuId: "SKU-SAMSUNG-S24U",
    discount: 19
  },
  {
    id: "p3",
    name: "Xiaomi 14 Ultra",
    description: "Camera Leica chuyên nghiệp, hiệu năng đỉnh cao, thiết kế sang trọng",
    price: 15000000,
    salePrice: 12490000,
    imageUrl: "https://images.unsplash.com/photo-1707157281160-5a45d5d6d4dd",
    stock: 80,
    category: "Smartphone",
    skuId: "SKU-XIAOMI-14U",
    discount: 17
  },
  {
    id: "p4",
    name: "iPad Pro 12.9 inch M2",
    description: "Máy tính bảng cao cấp với chip M2, màn hình Liquid Retina XDR 120Hz siêu mượt",
    price: 18000000,
    salePrice: 15190000,
    imageUrl: "https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04",
    stock: 35,
    category: "Tablet",
    skuId: "SKU-IPAD-PRO129",
    discount: 15
  },
  {
    id: "p5",
    name: "MacBook Pro 16 M3 Max",
    description: "Laptop hiệu năng cực khủng với chip M3 Max, màn hình Retina, pin trâu cho dân chuyên nghiệp",
    price: 45000000,
    salePrice: 38490000,
    imageUrl: "https://images.unsplash.com/photo-1517336714731-489689fd1ca8",
    stock: 20,
    category: "Laptop",
    skuId: "SKU-MBP-16M3",
    discount: 14
  }
];

async function seed() {
  await redis.del("products:list");

  for (const p of products) {
    await redis.set(`product:${p.id}`, JSON.stringify(p));
    await redis.rpush("products:list", p.id);
  }

  console.log("✅ Seed dữ liệu thành công với " + products.length + " sản phẩm");
  process.exit();
}

seed();