package learn.jconcurrency.synchronizators;

import java.util.List;
import java.util.concurrent.Phaser;

public class PhaserExample {

    public static void main(String[] args) {
        final List<Runnable> tasks = List.of(
                () -> printInThread("First Task"),
                () -> printInThread("Second Task"),
                () -> printInThread("Third Task")
        );
//        runVariableNumberOfParties(tasks);
        repeatedlyPerformActions(tasks, 3);
    }

    private static void repeatedlyPerformActions(List<Runnable> tasks, int iterations) {
        final Phaser phaser = new Phaser() {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                boolean shouldTerminate = phase >= iterations - 1 || registeredParties == 0;
                printInThread(String.format("onAdvance(%d, %d): %s%n", phase, registeredParties, shouldTerminate));
                return shouldTerminate;
            }
        };
        phaser.register();
        for (int i = 0; i < tasks.size(); i++) {
            var task = tasks.get(i);
            phaser.register();
            new Thread(() -> {
                do {
                    task.run();
                    phaser.arriveAndAwaitAdvance();
                } while (!phaser.isTerminated());
            }, "Worker-" + (i + 1)).start();
        }
        phaser.arriveAndDeregister();
        printInThread("repeatedlyPerformActions() finished");
    }

    private static void runVariableNumberOfParties(List<Runnable> tasks) {
        final Phaser phaser = new Phaser(1);
        for (int j = 0; j < tasks.size(); j++) {
            Runnable task = tasks.get(j);
            int phaseRegistered = phaser.register();
            printInThread("registration phase = " + phaseRegistered);
            Thread t = new Thread(() -> {
                for (int i = 1; i <= 3; i++) {
                    printInThread("before awaiting on phase " + phaser.getPhase());
                    int currentPhase = phaser.arriveAndAwaitAdvance();
                    printInThread("after awaiting on phase " + currentPhase);
                    task.run();
                }
            }, "Worker-" + (j + 1));
            t.start();
        }
        phaser.arriveAndDeregister();
    }

    private static void printInThread(String message) {
        System.out.println(Thread.currentThread().getName() + ": " + message);
    }
}
