# Fun with Java Concurrency 🚀

A hands-on learning project exploring Java's concurrency features through practical implementations and benchmarks.

## Features

### 🔄 Thread Synchronization Demos
- Race condition demonstrations
- `synchronized` block implementation
- `AtomicInteger` usage
- `ReentrantLock` examples
- Performance comparison between approaches

### 📁 Thread-Safe File Writing
Four implementations for concurrent file access:
1. **Synchronized** - Traditional monitor locks
2. **ReentrantLock** - Explicit lock control
3. **Single-Thread Executor** - Sequentialized writes
4. **NIO FileChannel** - Non-blocking I/O with synchronization

## Project Structure
```bash
src/
├── main/
│ └── java/
│ └── com/
│ └── concurrencyFun/
│ ├── app/
│ │ ├── App.java # Main entry point
│ │ └── demo/
│ │ ├── ThreadSyncDemo.java # Counter synchronization demos
│ │ └── ThreadSafeFileWritingDemo.java # File I/O implementations
└── test/
```

## Usage

1. **Run Thread Sync Demo** (Counter examples):
```java
// In App.java
runThreadSyncDemo();
```
2. **Run File Writing Demo**:
```java
// In App.java
runFileLockingDemo();
```

## Key Concepts Demonstrated

- ✔️ Thread creation and management
- ✔️ Shared resource synchronization
- ✔️ Atomic operations vs locks
- ✔️ Concurrent file I/O patterns
- ✔️ ExecutorService usage
- ✔️ NIO Channel operations

## Requirements

- Java 21+
- Maven (for building)

## Contributing
This is a learning project - suggestions and PRs welcome! Please open an issue first to discuss changes.