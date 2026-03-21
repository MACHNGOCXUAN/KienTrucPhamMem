package HeThongThanhToan.DecoratorPattern;

public class PaymentDecorator implements Payment {
    protected Payment payment;

    public PaymentDecorator(Payment payment) {
        this.payment = payment;
    }

    public double pay(double amount) {
        return payment.pay(amount);
    }
}
