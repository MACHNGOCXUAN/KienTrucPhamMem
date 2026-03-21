package HeThongThanhToan.StrategyPattern;

public class CreditCardPayment implements PaymentStrategy{
    @Override
    public double pay(double amount) {
        double fee = amount * 0.02; // phí 2%
        System.out.println("Thanh toán bằng thẻ tín dụng");
        System.out.println("Phí xử lý: " + fee);
        return amount + fee;
    }
}
