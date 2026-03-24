package iuh.fit.task;

import iuh.fit.common.Observer;

public class Tester implements Observer<String> {
    public void update(String status) {
        System.out.println("Tester nhận: " + status);
    }
}
