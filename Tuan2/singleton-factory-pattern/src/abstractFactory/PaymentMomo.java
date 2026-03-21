package abstractFactory;

public class PaymentMomo implements Payment{
    @Override
    public void thanhToan() {
        System.out.println("Thanh toán bằng ví Momo");
    }
}
