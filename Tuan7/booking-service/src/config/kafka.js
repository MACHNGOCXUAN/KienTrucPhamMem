import { Kafka } from "kafkajs";

const kafka = new Kafka({
  clientId: "booking-service",
  brokers: ["localhost:9092"],
  retry: {
    retries: 10
  }
});

export const producer = kafka.producer();

export const connectKafka = async () => {
  try {
    await producer.connect();
    console.log("Kafka producer connected successfully");
  } catch (error) {
    console.error("Kafka producer connection error:", error);
  }
};