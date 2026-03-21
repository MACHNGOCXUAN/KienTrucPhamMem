package abstractFactory;

public class DomesticInvoice implements Bill{
    @Override
    public void xuatHoaDon() {
        System.out.println("Xuất hóa đơn thanh toán nội địa");
    }
}
