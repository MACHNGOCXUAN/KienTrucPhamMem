package HeThongThanhToan.DecoratorPattern;

public class PaypalPayment implements Payment {
    public double pay(double amount) {
        System.out.println("Pay with PayPal");
        return amount;
    }
}
