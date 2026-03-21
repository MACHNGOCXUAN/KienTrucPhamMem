package HeThongQuanLyDonHang.DecoratorPattern;

public abstract class OrderDecorator implements Order{
    protected Order order;

    public OrderDecorator(Order order) {
        this.order = order;
    }

    @Override
    public void process() {
        order.process();
    }
}
