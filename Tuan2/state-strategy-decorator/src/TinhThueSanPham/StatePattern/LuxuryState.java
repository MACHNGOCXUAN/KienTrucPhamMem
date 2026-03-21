package TinhThueSanPham.StatePattern;

public class LuxuryState implements TaxState{
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice
                + basePrice * 0.1   // VAT
                + basePrice * 0.05  // Consumption
                + basePrice * 0.2;  // Luxury
    }
}
