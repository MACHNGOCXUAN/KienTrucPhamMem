package HeThongQuanLyDonHang.DecoratorPattern;

public class DeliveredDecorator extends OrderDecorator{
    public DeliveredDecorator(Order order) {
        super(order);
    }

    public void process() {
        super.process();
        System.out.println("→ Cập nhật trạng thái: Đã giao");
    }
}
