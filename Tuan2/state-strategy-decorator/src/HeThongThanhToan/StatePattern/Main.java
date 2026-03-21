package HeThongThanhToan.StatePattern;

public class Main {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();

        context.setState(new CreditCardState());
        context.process(100);

        System.out.println("-----");

        context.process(100); // đã complete rồi
    }
}
