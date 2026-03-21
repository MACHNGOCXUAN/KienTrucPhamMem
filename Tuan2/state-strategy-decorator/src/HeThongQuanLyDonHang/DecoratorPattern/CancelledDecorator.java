package HeThongQuanLyDonHang.DecoratorPattern;

public class CancelledDecorator extends OrderDecorator{
    public CancelledDecorator(Order order) {
        super(order);
    }

    public void process() {
        super.process();
        System.out.println("→ Hủy đơn hàng và hoàn tiền");
    }
}
