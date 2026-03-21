package HeThongThanhToan.DecoratorPattern;

public class CreditCardPayment implements Payment{
    public double pay(double amount) {
        System.out.println("Pay with Credit Card");
        return amount;
    }
}
