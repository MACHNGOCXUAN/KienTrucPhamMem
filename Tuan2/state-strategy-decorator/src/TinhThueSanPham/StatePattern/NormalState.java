package TinhThueSanPham.StatePattern;

public class NormalState implements TaxState{
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice + basePrice * 0.1;
    }
}
