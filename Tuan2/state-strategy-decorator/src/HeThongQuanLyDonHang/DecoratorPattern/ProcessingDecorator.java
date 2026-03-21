package HeThongQuanLyDonHang.DecoratorPattern;

public class ProcessingDecorator extends OrderDecorator{
    public ProcessingDecorator(Order order) {
        super(order);
    }

    public void process() {
        super.process();
        System.out.println("→ Đóng gói và vận chuyển");
    }
}
