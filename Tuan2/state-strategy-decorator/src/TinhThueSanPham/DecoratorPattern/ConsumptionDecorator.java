package TinhThueSanPham.DecoratorPattern;

public class ConsumptionDecorator extends TaxDecorator{
    public ConsumptionDecorator(Product product) {
        super(product);
    }

    @Override
    public double getPrice() {
        return product.getPrice() + getBasePrice() * 0.05;
    }
}