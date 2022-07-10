package learn.jconcurrency.lifecycle;

import static learn.jconcurrency.Utils.print;
import static learn.jconcurrency.Utils.waitUntilExpectedState;

public class InterruptedThreadExample {

    public static void main(String[] args) throws InterruptedException {
        /*
            The 1st variant throws InterruptedException.
         */
        // Thread thread = new Thread(Utils::infiniteSleep, "InterruptedThread");

        /*
            The 2nd variant finishes working thread.
         */
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    printStackTraceElements(Thread.currentThread().getStackTrace());
                    return;
                }
            }
        }, "InterruptedThread");
        print(thread);

        thread.start();
        print(thread);

        waitUntilExpectedState(thread, Thread.State.TIMED_WAITING, 3);
        thread.interrupt();

        thread.join();
        print(thread);
    }

    private static void printStackTraceElements(StackTraceElement[] stackTraces) {
        System.out.println("\nStackTrace elements:");
        for (StackTraceElement e : stackTraces) {
            System.out.println(e);
        }
        System.out.println();
    }
}
