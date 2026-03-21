package abstractFactory;

public class Main {
    public static void main(String[] args) {

        PaymentFactory factory = new ThanhToanNoiDiaFactory();
        Payment payment = factory.createPayment();
        Bill bill = factory.createBill();

        payment.thanhToan();
        bill.xuatHoaDon();
    }
}
