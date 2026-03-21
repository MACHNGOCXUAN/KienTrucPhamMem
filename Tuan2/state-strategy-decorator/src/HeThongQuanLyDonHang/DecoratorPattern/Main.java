package HeThongQuanLyDonHang.DecoratorPattern;

public class Main {
    public static void main(String[] args) {
        Order order = new BasicOrder();

        // đi qua các "trạng thái"
        order = new NewOrderDecorator(order);
        order = new ProcessingDecorator(order);
        order = new DeliveredDecorator(order);

        order.process();
    }
}
