package learn.jconcurrency.problems;

import java.util.concurrent.CountDownLatch;

import static learn.jconcurrency.Utils.*;

public class DeadLockExample {

    public static void main(String[] args) {
        var firstIsCaptured = new CountDownLatch(1);
        var secondIsCaptured = new CountDownLatch(1);

        Object resource1 = new Object();
        Object resource2 = new Object();

        Thread first = new Thread(() -> {
            try {
                synchronized (resource1) {
                    firstIsCaptured.countDown();
                    secondIsCaptured.await();
                    synchronized (resource2) {
                        infiniteSleep();    // unreachable statement because resource2 is captured by 2nd thread
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "FirstThread");
        Thread second = new Thread(() -> {
            try {
                synchronized (resource2) {
                    secondIsCaptured.countDown();
                    firstIsCaptured.await();
                    synchronized (resource1) {
                        infiniteSleep();    // unreachable statement because resource1 is captured by 1st thread
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "SecondThread");
        Thread printer = new Thread(() -> {
            while (true) {
                print(first);
                print(second);
                sleep(1000L);
            }
        }, "PrinterThread");
        printer.setDaemon(true);
        printer.start();

        first.start();
        second.start();

        try {
            first.join();   // this join will never be finished because the 1st thread will be blocked
            second.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
