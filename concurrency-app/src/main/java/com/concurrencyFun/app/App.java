package com.concurrencyFun.app;

import java.io.*;
import java.nio.file.*;

import com.concurrencyFun.app.demo.ThreadSafeFileWritingDemo;
import com.concurrencyFun.app.demo.ThreadSyncDemo;
import com.concurrencyFun.app.demo.ThreadSyncDemo.threadRunType;

public class App {

    public static final String FILENAME = "counter.txt";

    public static void runThreadSyncDemo() throws InterruptedException {
        System.out.println("Thread Sync Demo:");
        ThreadSyncDemo syncDemo = new ThreadSyncDemo(1000000, threadRunType.WITHLOCK);
        syncDemo.RunAll();
    }

    public static void runFileLockingDemo() throws Exception {
       ThreadSafeFileWritingDemo.run(); 
    }

    public static void main(String[] args) throws Exception {
        // Uncomment to test thread sync
        // runThreadSyncDemo();
        // Uncomment to test file locking
        runFileLockingDemo();
    }
}