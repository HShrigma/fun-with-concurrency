package com.concurrencyFun.Threads;

public class MyThread extends Thread {

    public void run() {
        var greeting = new StringBuilder().append("Created thread: ");
        System.out.println(greeting.toString());
    }
}
