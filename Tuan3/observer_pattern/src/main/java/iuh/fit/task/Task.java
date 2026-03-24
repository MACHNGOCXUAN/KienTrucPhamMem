package iuh.fit.task;

import iuh.fit.common.Observer;
import iuh.fit.common.Subject;

import java.util.ArrayList;
import java.util.List;

public class Task implements Subject<String> {

    private List<Observer<String>> members = new ArrayList<>();
    private String status;

    @Override
    public void attach(Observer<String> o) {
        members.add(o);
    }

    @Override
    public void detach(Observer<String> o) {
        members.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer<String> m : members) {
            m.update(status);
        }
    }

    public void setStatus(String status) {
        this.status = status;
        notifyObservers();
    }
}
