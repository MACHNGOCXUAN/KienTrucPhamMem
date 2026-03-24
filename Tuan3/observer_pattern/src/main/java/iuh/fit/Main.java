package iuh.fit;

import iuh.fit.stock.Investor;
import iuh.fit.stock.Stock;
import iuh.fit.task.Developer;
import iuh.fit.task.Manager;
import iuh.fit.task.Task;
import iuh.fit.task.Tester;

public class Main {
    public static void main(String[] args) {
        // stock
        System.out.println("=== STOCK ===");
        Stock stock = new Stock();

        stock.attach(new Investor("A"));
        stock.attach(new Investor("B"));

        stock.setPrice(100);
        stock.setPrice(150);

        // task
        System.out.println("\n=== TASK ===");
        Task task = new Task();

        task.attach(new Developer());
        task.attach(new Tester());
        task.attach(new Manager());

        task.setStatus("In Progress");
        task.setStatus("Completed");
    }
}
