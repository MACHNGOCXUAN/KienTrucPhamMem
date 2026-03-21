package HeThongThanhToan.StrategyPattern;

public class PaypalPayment implements PaymentStrategy{
    @Override
    public double pay(double amount) {
        double fee = 1.5; // phí cố định
        System.out.println("Thanh toán bằng PayPal");
        System.out.println("Phí xử lý: " + fee);
        return amount + fee;
    }
}
