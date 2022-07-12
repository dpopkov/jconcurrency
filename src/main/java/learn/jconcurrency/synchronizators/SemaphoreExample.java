package learn.jconcurrency.synchronizators;

import learn.jconcurrency.Utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

public class SemaphoreExample {
    private static final AtomicLong NON_IMPORTANT_DATA = new AtomicLong();
    private static final int MAX_WORKERS = 10;
    private static final int TOTAL_WORKERS = 100;

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
                Utils.sleep(1000L);
            }
        }, "PrinterThread");
        printer.setDaemon(true);
        printer.start();

        for (int i = 1; i <= TOTAL_WORKERS; i++) {
            Thread t = new Thread(() -> {
                try {
                    latch.countDown();
                    semaphore.acquire();
                    doHardWork();
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

    private static long countState(Set<Thread> workers, Thread.State state) {
        return workers.stream().filter(w -> w.getState() == state).count();
    }

    private static void doHardWork() {
        for (int i = 0; i < 10_000; i++) {
            for (int j = 0; j < 10_000; j++) {
                NON_IMPORTANT_DATA.set(i * j);
            }
        }
    }
}
