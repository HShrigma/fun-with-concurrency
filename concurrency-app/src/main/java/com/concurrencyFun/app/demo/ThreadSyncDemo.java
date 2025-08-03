package com.concurrencyFun.app.demo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSyncDemo {
    private int counter = 0;
    private final AtomicInteger atomicCounter = new AtomicInteger();
    private final Lock lock = new ReentrantLock();
    private final int iterations;

    public static enum threadRunType {
        UNSYNCED,
        SYNCED,
        ATOMIC,
        WITHLOCK
    }

    private threadRunType runType;

    public ThreadSyncDemo(int iterations, threadRunType runType) {
        this.iterations = iterations;
        this.runType = runType;
    }

    public void RunAll() throws InterruptedException {
        try {
            System.out.println("Running Unsynchronized:");
            runUnsynchronized();
            System.out.println("Running Synchronized:");
            runSynchronized();
            System.out.println("Running Atomic:");
            runAtomic();
            System.out.println("Running With Lock:");
            runWithLock();
        } catch (InterruptedException anyException) {
            System.err.println("Error during execution");
        }
    }

    public void Run() throws InterruptedException {
        try {
            switch (runType) {
                case UNSYNCED:
                    runUnsynchronized();
                    break;
                case SYNCED:
                    runSynchronized();
                    break;
                case ATOMIC:
                    runAtomic();
                    break;
                case WITHLOCK:
                    runWithLock();
                    break;
                default:
                    System.out.println("Unrecognized runType: " + runType.toString());
                    break;
            }
        } catch (InterruptedException anyException) {
            System.err.println("Error during execution");
        }
    }

    public void runUnsynchronized() throws InterruptedException {
        counter = 0;
        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                counter++;
            }
        };
        runThreads(task, false);
    }

    public void runSynchronized() throws InterruptedException {
        counter = 0;
        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                synchronized (this) {
                    counter++;
                }
            }
        };
        runThreads(task, false);
    }

    public void runAtomic() throws InterruptedException {
        atomicCounter.set(0);
        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                atomicCounter.incrementAndGet();
            }
        };
        runThreads(task, true);
    }

    public void runWithLock() throws InterruptedException {
        counter = 0;
        Runnable task = () -> {
            for (int i = 0; i < iterations; i++) {
                lock.lock();
                try {
                    counter++;
                } finally {
                    lock.unlock();
                }
            }
        };
        runThreads(task, false);
    }

    private void runThreads(Runnable task, boolean isAtomic) throws InterruptedException {
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        long start = System.currentTimeMillis();
        t1.start();
        t2.start();

        t1.join(); // Wait for t1 to finish
        t2.join(); // Wait for t2 to finish

        long duration = System.currentTimeMillis() - start;

        System.out.println("Expected: " + (2 * iterations));
        System.out.println("Actual: " + (isAtomic ? atomicCounter.get() : counter));
        System.out.println("Duration: " + duration + "ms\n");
    }
}
