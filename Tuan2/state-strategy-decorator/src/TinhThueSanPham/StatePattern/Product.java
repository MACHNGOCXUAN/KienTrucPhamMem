package TinhThueSanPham.StatePattern;

public class Product {
    private final double basePrice;
    private TaxState state;

    public Product(double basePrice) {
        this.basePrice = basePrice;
    }

    public void setState(TaxState state) {
        this.state = state;
    }

    public double getFinalPrice() {
        return state.calculatePrice(basePrice);
    }
}
