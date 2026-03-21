package HeThongThanhToan.StrategyPattern;

public class PercentageDiscount implements DiscountStrategy{
    private double percent;

    public PercentageDiscount(double percent) {
        this.percent = percent;
    }

    @Override
    public double applyDiscount(double amount) {
        return amount - (amount * percent);
    }
}
