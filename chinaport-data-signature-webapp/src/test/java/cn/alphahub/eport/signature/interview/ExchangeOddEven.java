package cn.alphahub.eport.signature.interview;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class ExchangeOddEven {

    final static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {

        Print print1 = new Print();
        Print print2 = new Print();

        Thread t1 = new Thread(print1);
        Thread t2 = new Thread(print2);

        print1.setCurrentThread(t2);
        print2.setCurrentThread(t1);

        t1.start();
        t2.start();

        LockSupport.unpark(t1);
    }

    static class Print implements Runnable {
        private volatile Thread currentThread;

        public void setCurrentThread(Thread currentThread) {
            this.currentThread = currentThread;
        }

        @Override
        public void run() {
            for (; ; ) {
                LockSupport.park();
                if (atomicInteger.get() > 1000) {
                    LockSupport.unpark(currentThread);
                    return;
                }
                System.out.println(Thread.currentThread().getName() + ":" + atomicInteger.getAndIncrement());
                LockSupport.unpark(currentThread);
            }
        }
    }
}
