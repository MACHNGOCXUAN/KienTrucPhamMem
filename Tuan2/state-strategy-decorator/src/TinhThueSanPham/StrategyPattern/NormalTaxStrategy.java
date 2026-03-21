package TinhThueSanPham.StrategyPattern;

public class NormalTaxStrategy implements TaxStrategy{
    @Override
    public double calculateTax(double price) {
        return price + price * 0.1;
    }
}
