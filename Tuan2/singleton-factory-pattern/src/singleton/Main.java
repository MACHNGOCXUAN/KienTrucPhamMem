package singleton;

public class Main {
    public static void main(String[] args) {
        Printer p1 = Printer.getInstance();
        Printer p2 = Printer.getInstance();

        p1.print("Document 1");
        p2.print("Document 2");

        System.out.println(p1 == p2);
    }
}
