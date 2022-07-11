package learn.jconcurrency.lifecycle;

import static learn.jconcurrency.Utils.print;

public class TerminatedThreadExample {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("Doing some work");
        }, "ThreadToTerminate");
        print(thread);

        thread.start();
        print(thread);

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        print(thread);
    }
}
