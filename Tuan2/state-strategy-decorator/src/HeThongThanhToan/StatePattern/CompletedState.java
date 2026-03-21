package HeThongThanhToan.StatePattern;

public class CompletedState implements PaymentState {
    public void handle(PaymentContext context, double amount) {
        System.out.println("Payment already completed!");
    }
}
