package TinhThueSanPham.StrategyPattern;

public class Main {
    public static void main(String[] args) {
        double price = 1000;
        TaxCalculator calculator = new TaxCalculator();

        calculator.setStrategy(new NormalTaxStrategy());
        System.out.println("Hàng thường: " + calculator.calculate(price));

        calculator.setStrategy(new ConsumptionTaxStrategy());
        System.out.println("Hàng tiêu dùng: " + calculator.calculate(price));

        calculator.setStrategy(new LuxuryTaxStrategy());
        System.out.println("Hàng xa xỉ: " + calculator.calculate(price));
    }
}
