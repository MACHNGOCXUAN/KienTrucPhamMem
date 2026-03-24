package iuh.fit.task;

import iuh.fit.common.Observer;

public class Manager implements Observer<String> {
    public void update(String status) {
        System.out.println("Manager nhận: " + status);
    }
}
