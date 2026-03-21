package TinhThueSanPham.DecoratorPattern;

public class LuxuryDecorator extends TaxDecorator{
    public LuxuryDecorator(Product product) {
        super(product);
    }

    @Override
    public double getPrice() {
        return product.getPrice() + getBasePrice() * 0.2;
    }
}
