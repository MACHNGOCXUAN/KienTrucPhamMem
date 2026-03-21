package HeThongThanhToan.DecoratorPattern;

public class ProcessingFeeDecorator extends PaymentDecorator {

    public ProcessingFeeDecorator(Payment payment) {
        super(payment);
    }

    public double pay(double amount) {
        double result = super.pay(amount);
        double fee = result * 0.02;
        System.out.println("Processing Fee: " + fee);
        return result + fee;
    }
}