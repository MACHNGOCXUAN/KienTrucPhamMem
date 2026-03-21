package HeThongQuanLyDonHang.StrategyPattern;

public class CancelledStrategy implements OrderStrategy{
    @Override
    public void process(OrderContext context) {
        System.out.println("Hủy đơn hàng và hoàn tiền!");
    }
}
