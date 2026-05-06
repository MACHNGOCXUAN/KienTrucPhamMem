import Redis from "ioredis";

const redis = new Redis({
  host: "192.168.137.41",
  port: 6379,
});

redis.on("connect", () => {
  console.log("✅ Redis connected");
});

export default redis;