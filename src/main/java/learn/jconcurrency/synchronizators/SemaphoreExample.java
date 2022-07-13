package learn.jconcurrency.synchronizators;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import static learn.jconcurrency.Utils.countState;
import static learn.jconcurrency.Utils.sleep;

public class SemaphoreExample {
    private static final int MAX_WORKERS = 10;
    private static final int TOTAL_WORKERS = 100;

    private static final HardWork hardWork = new HardWork(10_000);

    public static void main(String[] args) {
        final var latch = new CountDownLatch(TOTAL_WORKERS);
        final var semaphore = new Semaphore(MAX_WORKERS);
        Set<Thread> workers = new HashSet<>();

        Thread printer = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                if (!workers.isEmpty()) {
                    long waiting = countState(workers, Thread.State.WAITING);
                    long runnable = countState(workers, Thread.State.RUNNABLE);
                    System.out.printf("WAITING: %2d, RUNNABLE: %2d%n", waiting, runnable);
                }
                sleep(1000L);
            }
        }, "PrinterThread");
        printer.setDaemon(true);
        printer.start();

        for (int i = 1; i <= TOTAL_WORKERS; i++) {
            Thread t = new Thread(() -> {
                try {
                    latch.countDown();
                    semaphore.acquire();
                    hardWork.doIt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                    System.out.println(Thread.currentThread().getName() + " released semaphore");
                }
            }, "Worker-" + i);
            workers.add(t);
            t.start();
        }
    }
}
