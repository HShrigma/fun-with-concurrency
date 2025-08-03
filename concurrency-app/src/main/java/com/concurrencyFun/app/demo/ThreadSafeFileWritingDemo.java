package com.concurrencyFun.app.demo;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ThreadSafeFileWritingDemo {
    private static final String FILE_PATH = "thread_safe_demo.txt";
    private static final int THREAD_COUNT = 5;
    private static final int WRITES_PER_THREAD = 10;

    public static void run() throws InterruptedException, IOException {
        // Clean up previous run
        Files.deleteIfExists(Paths.get(FILE_PATH));

        System.out.println("Starting synchronized writer demo...");
        demoWithSynchronizedWriter();
        System.out.println();
        

        System.out.println("\nStarting ReentrantLock writer demo...");
        demoWithReentrantLockWriter();
        System.out.println();
        
        System.out.println("\nStarting single-threaded executor demo...");
        demoWithExecutorWriter();
        System.out.println();
        
        System.out.println("\nStarting NIO FileChannel demo...");
        demoWithNioWriter();
        System.out.println();
        
        System.out.println("\nAll demos completed. Check " + FILE_PATH + " for results.");
    }

    // 1. Synchronized approach
    private static void demoWithSynchronizedWriter() throws InterruptedException {
        SynchronizedFileWriter writer = new SynchronizedFileWriter(FILE_PATH);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.execute(() -> {
                for (int j = 0; j < WRITES_PER_THREAD; j++) {
                    try {
                        writer.writeToFile(String.format(
                            "[SYNC] Thread-%d write %d", threadId, j));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
    }

    // 2. ReentrantLock approach
    private static void demoWithReentrantLockWriter() throws InterruptedException {
        LockedFileWriter writer = new LockedFileWriter(FILE_PATH);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.execute(() -> {
                for (int j = 0; j < WRITES_PER_THREAD; j++) {
                    try {
                        writer.writeToFile(String.format(
                            "[LOCK] Thread-%d write %d", threadId, j));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
    }

    // 3. Single-threaded executor approach
    private static void demoWithExecutorWriter() throws InterruptedException {
        ExecutorFileWriter writer = new ExecutorFileWriter(FILE_PATH);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            new Thread(() -> {
                for (int j = 0; j < WRITES_PER_THREAD; j++) {
                    writer.writeToFile(String.format(
                        "[EXEC] Thread-%d write %d", threadId, j));
                }
            }).start();
        }
        
        // Wait for writes to complete
        Thread.sleep(1000);
        writer.shutdown();
    }

    // 4. NIO FileChannel approach
    private static void demoWithNioWriter() throws InterruptedException, IOException {
        NioFileWriter writer = new NioFileWriter(Paths.get(FILE_PATH));
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.execute(() -> {
                for (int j = 0; j < WRITES_PER_THREAD; j++) {
                    try {
                        writer.writeToFile(String.format(
                            "[NIO] Thread-%d write %d", threadId, j));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        writer.close();
    }

    // Implementation classes from previous examples
    // 1. Synchronized writer
static class SynchronizedFileWriter {
    private final Object lock = new Object();
    private final String filePath;

    public SynchronizedFileWriter(String filePath) {
        this.filePath = filePath;
    }

    public void writeToFile(String data) throws IOException {
        synchronized (lock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(data);
                writer.newLine();
            }
        }
    }
}

// 2. ReentrantLock writer
static class LockedFileWriter {
    private final ReentrantLock lock = new ReentrantLock();
    private final String filePath;

    public LockedFileWriter(String filePath) {
        this.filePath = filePath;
    }

    public void writeToFile(String data) throws IOException {
        lock.lock();
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(data);
                writer.newLine();
            }
        } finally {
            lock.unlock();
        }
    }
}

// 3. Single-threaded executor writer
static class ExecutorFileWriter {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final String filePath;

    public ExecutorFileWriter(String filePath) {
        this.filePath = filePath;
    }

    public void writeToFile(String data) {
        executor.execute(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(data);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}

// 4. NIO FileChannel writer
static class NioFileWriter {
    private final FileChannel channel;
    private final Object lock = new Object();

    public NioFileWriter(Path filePath) throws IOException {
        this.channel = FileChannel.open(filePath, 
            StandardOpenOption.CREATE, 
            StandardOpenOption.WRITE,
            StandardOpenOption.APPEND);
    }

    public void writeToFile(String data) throws IOException {
        synchronized (lock) {
            byte[] bytes = (data + System.lineSeparator()).getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            channel.write(buffer);
        }
    }

    public void close() throws IOException {
        channel.close();
    }
}
}
