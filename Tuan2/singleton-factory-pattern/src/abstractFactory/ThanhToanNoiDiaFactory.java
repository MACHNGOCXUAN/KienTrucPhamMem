package abstractFactory;

public class ThanhToanNoiDiaFactory implements PaymentFactory{
    @Override
    public Payment createPayment() {
        return new PaymentMomo();
    }

    @Override
    public Bill createBill() {
        return new DomesticInvoice();
    }
}
