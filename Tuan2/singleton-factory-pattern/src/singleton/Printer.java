package singleton;

public class Printer {
    private static Printer instance;

    private Printer() {
        System.out.println("Printer created");
    }

    public static Printer getInstance() {

        if (instance == null) {
            instance = new Printer();
        }

        return instance;
    }

    public void print(String document) {
        System.out.println("Printing: " + document);
    }
}
