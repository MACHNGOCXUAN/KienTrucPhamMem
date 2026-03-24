package iuh.fit.stock;

import iuh.fit.common.Observer;
import iuh.fit.common.Subject;

import java.util.ArrayList;
import java.util.List;

public class Stock implements Subject<Double> {
    private List<Observer<Double>> investors = new ArrayList<>();
    private double price;

    @Override
    public void attach(Observer<Double> o) {
        investors.add(o);
    }

    @Override
    public void detach(Observer<Double> o) {
        investors.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer<Double> investor : investors) {
            investor.update(price);
        }
    }

    public void setPrice(double price) {
        this.price = price;
        notifyObservers();
    }
}
