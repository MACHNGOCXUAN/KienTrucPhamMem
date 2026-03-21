package TinhThueSanPham.DecoratorPattern;

public abstract class TaxDecorator implements Product{
    protected Product product;

    public TaxDecorator(Product product) {
        this.product = product;
    }

    @Override
    public double getBasePrice() {
        return product.getBasePrice();
    }
}
