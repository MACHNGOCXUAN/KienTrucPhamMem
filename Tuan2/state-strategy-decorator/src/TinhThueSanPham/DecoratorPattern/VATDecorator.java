package TinhThueSanPham.DecoratorPattern;

public class VATDecorator extends TaxDecorator{

    public VATDecorator(Product product) {
        super(product);
    }

    @Override
    public double getPrice() {
        return product.getPrice() + getBasePrice() * 0.1;
    }
}
