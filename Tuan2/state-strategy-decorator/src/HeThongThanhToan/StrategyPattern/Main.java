package HeThongThanhToan.StrategyPattern;

public class Main {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();

        context.setStrategy(new CreditCardPayment());
        context.checkout(100);

        System.out.println("-----");

        context.setStrategy(new PaypalPayment());
        context.checkout(100);
    }
}
