package HeThongQuanLyDonHang.StrategyPattern;

public class ProcessingStrategy implements OrderStrategy{
    @Override
    public void process(OrderContext context) {
        System.out.println("Đóng gói và vận chuyển...");
    }
}
