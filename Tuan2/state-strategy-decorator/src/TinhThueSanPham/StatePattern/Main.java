package TinhThueSanPham.StatePattern;

public class Main {
    public static void main(String[] args) {
        Product product = new Product(1000);

        // Hàng thường
        product.setState(new NormalState());
        System.out.println("Hàng thường: " + product.getFinalPrice());

        // Hàng tiêu dùng
        product.setState(new ConsumptionState());
        System.out.println("Hàng tiêu dùng: " + product.getFinalPrice());

        // Hàng xa xỉ
        product.setState(new LuxuryState());
        System.out.println("Hàng xa xỉ: " + product.getFinalPrice());
    }
}
