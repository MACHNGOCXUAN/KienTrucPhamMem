package iuh.fit.task;

import iuh.fit.common.Observer;

public class Developer implements Observer<String> {
    public void update(String status) {
        System.out.println("Developer nhận: " + status);
    }
}
