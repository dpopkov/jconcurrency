package learn.jconcurrency.synchronizators;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import static learn.jconcurrency.Utils.countState;
import static learn.jconcurrency.Utils.sleep;

public class CyclicBarrierExample {

    private static final int TOTAL_CYCLES = 5;
    private static final int TOTAL_WORKERS = 9;

    private static final HardWork hardWork = new HardWork(10_000,
            "completed work cycle (before barrier)");

    public static void main(String[] args) {
        final var allStartedLatch = new CountDownLatch(TOTAL_WORKERS);
        final var sharedBarrier = new CyclicBarrier(TOTAL_WORKERS,
                () -> System.out.printf("%s %s - all workers started work cycle%n",
                        Thread.currentThread().getName(), LocalTime.now()));
        final var workers = new HashSet<Thread>();

        final Thread printer = new Thread(() -> {
            try {
                allStartedLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                long waiting = countState(workers, Thread.State.WAITING);
                long runnable = countState(workers, Thread.State.RUNNABLE);
                System.out.printf("WAITING: %2d, RUNNABLE: %2d%n", waiting, runnable);
                sleep(5000L);
            }
        }, "PrinterThread");
        printer.setDaemon(true);
        printer.start();

        for (int i = 1; i <= TOTAL_WORKERS; i++) {
            Thread t = new Thread(() -> {
                allStartedLatch.countDown();
                for (int cycle = 1; cycle <= TOTAL_CYCLES; cycle++) {
                    hardWork.doIt();
                    try {
                        sharedBarrier.await();
                        System.out.printf("%s finished some work on cycle %d (after barrier)%n",
                                Thread.currentThread().getName(), cycle);
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }, "Worker-" + i);
            workers.add(t);
            t.start();
        }
    }
}
