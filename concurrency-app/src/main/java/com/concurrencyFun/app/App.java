package com.concurrencyFun.app;

import java.util.concurrent.atomic.AtomicInteger;

public class App {

    static int counter = 0;
    static final int ITERATIONS = 1_000_000; // Increased from 1000
    private static AtomicInteger atomicCounter = new AtomicInteger();

    public static void printExpectedCounter() {
        System.out.println("Expected: " + (2 * ITERATIONS));
    }

    public static void printActualCounter(int inCount) {
        System.out.println("Actual: " + inCount);
    }

    public static Runnable unsynchronizedRunnable() throws InterruptedException {
        return () -> {
            for (int i = 0; i < ITERATIONS; i++) {
                counter++; // This is NOT atomic
            }
        };
    }
    
    public static Runnable synchronizedRunnable() throws InterruptedException {
        return () -> {
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (App.class) {
                    counter++;
                }
            }
        };
    }

    public static Runnable AtomicIntegerRunnable() throws InterruptedException {
         return () -> {
            for (int i = 0; i < ITERATIONS; i++) {
                atomicCounter.incrementAndGet();
            }
        };
    }

    public static void runThreads(Runnable runnable) throws InterruptedException{
        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        printExpectedCounter();
    }
    public static void main(String[] args) throws InterruptedException {
        try {
            // Uncomment for unsynchronized demo
            // runThreads(unsynchronizedRunnable());

            // Uncomment for synchronized demo
            runThreads(synchronizedRunnable());


            // Uncomment for atomic demo
            // runThreads(AtomicIntegerRunnable());

            // Uncomment for non-atomic Counter
            printActualCounter(counter);

            // Uncomment for atomic Counter
            // printActualCounter(atomicCounter.get());
        } catch (InterruptedException anyException) {
            System.err.println("Error during execution");
        }

    }
}