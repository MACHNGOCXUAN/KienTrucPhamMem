package HeThongThanhToan.StrategyPattern;

public class PaymentContext {
    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void checkout(double amount) {
        double result = strategy.pay(amount);
        System.out.println("Total: " + result);
    }
}
