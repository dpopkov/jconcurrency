package learn.jconcurrency.lifecycle;

import static learn.jconcurrency.Utils.*;

public class JoinThreadExample {
    public static void main(String[] args) {
        Object monitor = new Object();
        Thread mainThread = Thread.currentThread();
        Thread printer = new Thread(() -> {
            loopWhileStateIsRunnable(mainThread);
            System.out.printf("%s: main thread state changed%n", mainThread);
            print(mainThread);
            synchronized (monitor) {
                monitor.notify();
            }
        }, "PrintingThread");
        printer.setDaemon(true);
        printer.start();

        Thread target = new Thread(() -> {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "TargetThread");
        print(target);

        target.start();
        print(target);

        waitUntilExpectedState(target, Thread.State.WAITING);
        print(target);

        System.out.printf("Waiting for the %s to complete...%n", target);
        try {
            target.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"StatementWithEmptyBody", "LoopConditionNotUpdatedInsideLoop"})
    private static void loopWhileStateIsRunnable(Thread thread) {
        while (thread.getState() == Thread.State.RUNNABLE) {
            // loop until state changes
        }
    }
}
