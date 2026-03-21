package HeThongQuanLyDonHang.StrategyPattern;

public class NewOrderStrategy implements OrderStrategy{
    @Override
    public void process(OrderContext context) {
        System.out.println("Kiểm tra thông tin đơn hàng...");
    }
}
