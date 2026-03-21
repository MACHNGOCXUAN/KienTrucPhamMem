package HeThongQuanLyDonHang.StatePattern;

public class ProcessingState implements OrderState{
    @Override
    public void handle(OrderContext context) {
        System.out.println("Đóng gói và vận chuyển...");
        context.setState(new DeliveredState());
    }
}
