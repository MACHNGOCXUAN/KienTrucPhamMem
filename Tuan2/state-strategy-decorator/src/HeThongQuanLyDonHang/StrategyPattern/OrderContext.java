package HeThongQuanLyDonHang.StrategyPattern;

public class OrderContext {
    private OrderStrategy strategy;

    public void setStrategy(OrderStrategy strategy) {
        this.strategy = strategy;
    }

    public void process() {
        if (strategy != null) {
            strategy.process(this);
        } else {
            System.out.println("Chưa có strategy!");
        }
    }
}
