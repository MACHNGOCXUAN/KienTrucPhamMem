package HeThongThanhToan.StatePattern;

public class CreditCardState implements PaymentState{
    @Override
    public void handle(PaymentContext context, double amount) {
        double fee = amount * 0.02;
        System.out.println("State: Credit Card - Fee: " + fee);
        System.out.println("Total: " + (amount + fee));

        // chuyển state (giả lập)
        context.setState(new CompletedState());
    }
}
