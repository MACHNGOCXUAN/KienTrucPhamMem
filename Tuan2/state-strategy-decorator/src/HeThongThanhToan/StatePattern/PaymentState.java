package HeThongThanhToan.StatePattern;

public interface PaymentState {
    void handle(PaymentContext context, double amount);
}
