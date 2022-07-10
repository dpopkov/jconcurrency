package learn.jconcurrency.research;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class MemoryLayoutExample {
    private final Simple lock = new Simple();

    public static void main(String[] args) {
        System.out.println(VM.current().details());

        System.out.println(ClassLayout.parseClass(Simple.class).toPrintable());

        System.out.println(ClassLayout.parseClass(SimpleInt.class).toPrintable());

        new MemoryLayoutExample().researchLock();
    }

    private void researchLock() {
        System.out.println(">>>>>>>>>>> Before synchronized and identityHashCode");
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());
        System.out.println(">>>>>>>>>>> Before synchronized and After identityHashCode");
        System.out.println("identityHashCode = " + Integer.toHexString(System.identityHashCode(lock)));
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        System.out.println(">>>>>>>>>>> After synchronized");
        synchronized (lock) {
            System.out.println(">>>>>>>>>>> within synchronized");
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
            System.out.println(">>>>>>>>>>> before identityHashCode");
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
            System.out.println("identityHashCode = " + Integer.toHexString(System.identityHashCode(lock)));
            System.out.println(">>>>>>>>>>> after identityHashCode");
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
        }
    }
}
