package TinhThueSanPham.StrategyPattern;

public class ConsumptionTaxStrategy implements TaxStrategy{
    @Override
    public double calculateTax(double price) {
        return price + price * 0.1 + price * 0.05;
    }
}
