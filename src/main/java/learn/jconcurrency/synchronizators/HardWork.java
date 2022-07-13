package learn.jconcurrency.synchronizators;

import java.util.concurrent.atomic.AtomicLong;

public class HardWork {

    private final AtomicLong nonImportantData = new AtomicLong();
    private final int amount;
    private String afterMessage;

    public HardWork(int amount) {
        this.amount = amount;
    }

    public HardWork(int amount, String afterMessage) {
        this(amount);
        this.afterMessage = afterMessage;
    }

    public void doIt() {
        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                nonImportantData.set(i * j);
            }
        }
        if (afterMessage != null) {
            System.out.printf("%s %s%n", Thread.currentThread().getName(), afterMessage);
        }
    }
}
