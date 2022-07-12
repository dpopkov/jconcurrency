package learn.jconcurrency.synchronizators;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import static learn.jconcurrency.Utils.print;

public class CountDownLatchExample {

    private static final AtomicLong NON_IMPORTANT_DATA = new AtomicLong();
    private static final int TOTAL_WORKERS = 10;

    public static void main(String[] args) {
        var jobCompletedLatch = new CountDownLatch(TOTAL_WORKERS);

        Thread printer = new Thread(() -> {
            try {
                jobCompletedLatch.await();
                System.out.println("All workers completed job.");
                System.out.println("CountDownLatch count = " + jobCompletedLatch.getCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "PrinterThread");
        printer.start();

        for (int i = 1; i <= TOTAL_WORKERS; i++) {
            Thread t = new Thread(() -> {
                doHardWork();
                System.out.println(Thread.currentThread().getName() + " completed job.");
                jobCompletedLatch.countDown();
            }, "Worker-" + i);
            t.start();
        }
    }

    private static void doHardWork() {
        print(Thread.currentThread());
        for (int i = 0; i < 7_000; i++) {
            for (int j = 0; j < 7_000; j++) {
                NON_IMPORTANT_DATA.set(i * j);
            }
        }
    }
}
