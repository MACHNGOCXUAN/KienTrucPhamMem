package HeThongThanhToan.DecoratorPattern;

public class Main {
    public static void main(String[] args) {

        Payment payment = new DiscountDecorator(
                new ProcessingFeeDecorator(
                        new CreditCardPayment()
                )
        );

        double total = payment.pay(100);
        System.out.println("Final Total: " + total);

        System.out.println("----------");

        Payment payment2 = new ProcessingFeeDecorator(
                new PaypalPayment()
        );

        double total2 = payment2.pay(200);
        System.out.println("Final Total: " + total2);
    }
}
