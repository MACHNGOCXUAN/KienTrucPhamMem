package HeThongQuanLyDonHang.StrategyPattern;

public class DeliveredStrategy implements OrderStrategy{
    @Override
    public void process(OrderContext context) {
        System.out.println("Đơn hàng đã giao thành công!");
    }
}
