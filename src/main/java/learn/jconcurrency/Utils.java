package learn.jconcurrency;

import java.util.Set;

public class Utils {

    public static void print(Thread thread) {
        System.out.printf("Thread[id=%s, name=%s, state=%s, alive=%s, daemon=%s, interrupted=%s]%n",
                thread.getId(),
                thread.getName(),
                thread.getState(),
                thread.isAlive(),
                thread.isDaemon(),
                thread.isInterrupted()
        );
    }

    public static void waitUntilExpectedState(Thread thread, Thread.State expectedState) {
        waitUntilExpectedState(thread, expectedState, 10);
    }

    public static void waitUntilExpectedState(Thread thread, Thread.State expectedState, int seconds) {
        for (int i = 0; i < seconds * 10; i++) {
            if (thread.getState() == expectedState) {
                break;
            }
            sleep(100);
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void infiniteSleep() {
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long countState(Set<Thread> workers, Thread.State state) {
        return workers.stream().filter(w -> w.getState() == state).count();
    }
}
