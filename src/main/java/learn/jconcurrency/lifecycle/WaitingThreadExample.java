package learn.jconcurrency.lifecycle;

import static learn.jconcurrency.Utils.print;
import static learn.jconcurrency.Utils.waitUntilExpectedState;

public class WaitingThreadExample {

    public static void main(String[] args) {
        Object monitor = new Object();
        Thread thread = new Thread(() -> {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "WaitingThread");
        print(thread);  // State: NEW

        thread.start();
        print(thread);  // State: RUNNABLE

        waitUntilExpectedState(thread, Thread.State.WAITING);
        print(thread);  // State: WAITING

        synchronized (monitor) {
            monitor.notify();
        }
    }
}
