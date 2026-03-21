package HeThongQuanLyDonHang.DecoratorPattern;

public class NewOrderDecorator extends OrderDecorator{
    public NewOrderDecorator(Order order) {
        super(order);
    }

    public void process() {
        super.process();
        System.out.println("→ Kiểm tra thông tin đơn hàng");
    }
}
