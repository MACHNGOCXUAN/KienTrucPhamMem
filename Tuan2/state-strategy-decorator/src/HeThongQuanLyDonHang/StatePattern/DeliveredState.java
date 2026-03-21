package HeThongQuanLyDonHang.StatePattern;

public class DeliveredState implements OrderState{

    @Override
    public void handle(OrderContext context) {
        System.out.println("Đơn hàng đã giao!");
    }
}
