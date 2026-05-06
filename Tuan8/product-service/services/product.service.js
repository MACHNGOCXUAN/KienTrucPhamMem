import redis from "../config/redis.js";

const PRODUCTS_LIST_KEY = "products:list";

const getProductKey = (id) => `product:${id}`;

/* =========================
   CHECK LIST KEY TYPE
========================= */
const ensureProductsListKeyType = async () => {
  const type = await redis.type(PRODUCTS_LIST_KEY);

  if (type === "none") {
    await redis.del(PRODUCTS_LIST_KEY);
    return;
  }

  if (type !== "list") {
    await redis.del(PRODUCTS_LIST_KEY);
  }
};

/* =========================
   GET PRODUCT RAW STRING
========================= */
const getProductStringById = async (id) => {
  const key = getProductKey(id);
  const type = await redis.type(key);

  if (type !== "string") return null;

  return redis.get(key);
};

/* =========================
   GET ALL PRODUCTS (FIXED)
========================= */
export const getProducts = async () => {
  // Cách ổn định: dùng SCAN thay vì phụ thuộc list
  let cursor = "0";
  const keys = [];

  do {
    const [nextCursor, result] = await redis.scan(
      cursor,
      "MATCH",
      "product:*",
      "COUNT",
      100
    );

    cursor = nextCursor;
    keys.push(...result);
  } while (cursor !== "0");

  const products = await Promise.all(
    keys.map(async (key) => {
      const type = await redis.type(key);
      if (type !== "string") return null;

      const data = await redis.get(key);
      return data ? JSON.parse(data) : null;
    })
  );

  return products.filter(Boolean);
};

/* =========================
   GET BY ID
========================= */
export const getProductById = async (id) => {
  const data = await getProductStringById(id);
  return data ? JSON.parse(data) : null;
};

/* =========================
   CREATE PRODUCT
========================= */
export const createProduct = async (product) => {
  const key = getProductKey(product.id);

  await ensureProductsListKeyType();

  const exists = await redis.exists(key);
  if (exists) throw new Error("Product already exists");

  await redis.set(key, JSON.stringify(product));

  // optional list tracking
  await redis.rpush(PRODUCTS_LIST_KEY, product.id);

  return product;
};

/* =========================
   UPDATE PRODUCT
========================= */
export const updateProduct = async (id, newData) => {
  const existing = await getProductStringById(id);
  if (!existing) return null;

  const updated = {
    ...JSON.parse(existing),
    ...newData,
  };

  await redis.set(getProductKey(id), JSON.stringify(updated));

  return updated;
};

/* =========================
   UPDATE STOCK
========================= */
export const updateStock = async (id, quantity) => {
  const existing = await getProductStringById(id);
  if (!existing) return null;

  const product = JSON.parse(existing);

  const newStock = product.stock - quantity;

  if (newStock < 0) {
    throw new Error("Không đủ hàng tồn");
  }

  product.stock = newStock;

  await redis.set(getProductKey(id), JSON.stringify(product));

  return product;
};

/* =========================
   DELETE PRODUCT
========================= */
export const deleteProduct = async (id) => {
  const exists = await getProductStringById(id);
  if (!exists) return false;

  await redis.del(getProductKey(id));
  await redis.lrem(PRODUCTS_LIST_KEY, 0, id);

  return true;
};