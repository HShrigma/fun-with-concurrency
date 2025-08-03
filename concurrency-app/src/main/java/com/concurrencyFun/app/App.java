package com.concurrencyFun.app;

import com.concurrencyFun.app.demo.ThreadSyncDemo;
import com.concurrencyFun.app.demo.ThreadSyncDemo.threadRunType;

public class App {
    public static void main(String[] args) throws InterruptedException {
        ThreadSyncDemo syncDemo = new ThreadSyncDemo(1000000, threadRunType.WITHLOCK);
        syncDemo.RunAll();
    }
}