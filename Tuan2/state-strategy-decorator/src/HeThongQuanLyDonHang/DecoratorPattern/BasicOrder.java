package HeThongQuanLyDonHang.DecoratorPattern;

public class BasicOrder implements Order{
    @Override
    public void process() {
        System.out.println("Tạo đơn hàng");
    }
}
