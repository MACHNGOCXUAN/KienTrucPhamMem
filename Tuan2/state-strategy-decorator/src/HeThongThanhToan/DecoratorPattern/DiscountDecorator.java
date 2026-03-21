package HeThongThanhToan.DecoratorPattern;

public class DiscountDecorator extends PaymentDecorator {

    public DiscountDecorator(Payment payment) {
        super(payment);
    }

    public double pay(double amount) {
        double result = super.pay(amount);
        double discount = result * 0.1;
        System.out.println("Discount: " + discount);
        return result - discount;
    }
}