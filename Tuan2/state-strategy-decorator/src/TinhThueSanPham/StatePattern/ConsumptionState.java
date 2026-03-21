package TinhThueSanPham.StatePattern;

public class ConsumptionState implements TaxState{
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice + basePrice * 0.1 + basePrice * 0.05;
    }
}
