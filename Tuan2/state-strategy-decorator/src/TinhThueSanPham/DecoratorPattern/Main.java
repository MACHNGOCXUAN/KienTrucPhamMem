package TinhThueSanPham.DecoratorPattern;

public class Main {
    public static void main(String[] args) {
        // ===== Hàng thường (VAT) =====
        Product normal = new VATDecorator(
                new BasicProduct(1000)
        );
        System.out.println("Hàng thường: " + normal.getPrice()); // 1100


        // ===== Hàng tiêu dùng (VAT + Consumption) =====
        Product consumption = new ConsumptionDecorator(
                new VATDecorator(
                        new BasicProduct(1000)
                )
        );
        System.out.println("Hàng tiêu dùng: " + consumption.getPrice()); // 1150


        // ===== Hàng xa xỉ (VAT + Consumption + Luxury) =====
        Product luxury = new LuxuryDecorator(
                new ConsumptionDecorator(
                        new VATDecorator(
                                new BasicProduct(1000)
                        )
                )
        );
        System.out.println("Hàng xa xỉ: " + luxury.getPrice()); // 1350
    }
}
