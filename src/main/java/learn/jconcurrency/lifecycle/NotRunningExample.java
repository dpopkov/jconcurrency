package learn.jconcurrency.lifecycle;

import static learn.jconcurrency.Utils.infiniteSleep;
import static learn.jconcurrency.Utils.print;

public class NotRunningExample {

    @SuppressWarnings("CallToThreadRun")
    public static void main(String[] args) {
        Thread thread = new Thread("NotRunningThread") {
            @Override
            public void run() {
                print(Thread.currentThread());
                infiniteSleep();
            }
        };
        print(Thread.currentThread());

        /*
            Calling method run() will NOT start execution of "NotRunningThread".
            To get ID of Java process use command 'jps -l'.
            To view all threads in the specified process use command 'jstack'.
         */
        thread.run();
        /*
            To start thread invoke method start().
         */
//        thread.start();
//        print(thread);
    }
}
