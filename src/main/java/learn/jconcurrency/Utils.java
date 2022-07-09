package learn.jconcurrency;

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
        for (int i = 0; i < 100; i++) {
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
}
