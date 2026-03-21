package HeThongQuanLyDonHang.StrategyPattern;

public class Main {
    public static void main(String[] args) {
        OrderContext order = new OrderContext();

        order.setStrategy(new NewOrderStrategy());
        order.process();

        order.setStrategy(new ProcessingStrategy());
        order.process();

        order.setStrategy(new DeliveredStrategy());
        order.process();

        order.setStrategy(new CancelledStrategy());
        order.process();
    }
}
