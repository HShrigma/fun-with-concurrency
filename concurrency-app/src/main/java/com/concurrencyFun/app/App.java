package com.concurrencyFun.app;

import com.concurrencyFun.Threads.MyRunnable;
import com.concurrencyFun.Threads.MyThread;
public class App {
    public static void main(String[] args) {
        MyThread thread1 = new MyThread();
        thread1.start();

        Thread thread2 = new Thread(new MyRunnable());
        thread2.start();

        Thread thread3 = new Thread(() -> System.out.println("lambda thread"));
        thread3.start();
    }
}
