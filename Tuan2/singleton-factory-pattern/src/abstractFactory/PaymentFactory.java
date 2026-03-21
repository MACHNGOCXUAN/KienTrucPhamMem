package abstractFactory;

public interface PaymentFactory {
    Payment createPayment();
    Bill createBill();
}