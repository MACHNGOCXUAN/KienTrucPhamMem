package HeThongThanhToan.StatePattern;

public class PaypalPayment implements PaymentState{
    @Override
    public void handle(PaymentContext context, double amount) {
        double fee = 1.5;
        System.out.println("State: PayPal - Fee: " + fee);
        System.out.println("Total: " + (amount + fee));

        context.setState(new CompletedState());
    }
}
