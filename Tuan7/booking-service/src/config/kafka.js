import { Kafka } from "kafkajs";

const kafka = new Kafka({
  clientId: "booking-service",
  // Xóa "http://" ở đây
  brokers: ["172.28.0.1:9092"], 
});

export const producer = kafka.producer();

export const connectKafka = async () => {
  try {
    await producer.connect();
    console.log("✅ Kafka connected");
  } catch (error) {
    console.error("❌ Kafka connection error:", error);
  }
};