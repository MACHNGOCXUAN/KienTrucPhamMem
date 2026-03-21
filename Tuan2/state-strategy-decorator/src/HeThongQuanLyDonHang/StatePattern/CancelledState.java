package HeThongQuanLyDonHang.StatePattern;

public class CancelledState implements OrderState{

    @Override
    public void handle(OrderContext context) {
        System.out.println("Hủy đơn và hoàn tiền!");
    }
}
