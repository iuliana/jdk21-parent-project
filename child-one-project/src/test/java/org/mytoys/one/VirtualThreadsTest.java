package org.mytoys.one;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.*;

// JEP 444. Virtual Threads are designed to replace reactive code
// virtual threads allow programming in the familiar, sequential thread-per-request style.
// Sequential code is not only easier to write and read but also easier to debug since we can use a debugger to trace the program flow step by step,
// and stack traces reflect the expected call stack.
// Writing scalable applications with sequential code is made possible by allowing many virtual threads to share a platform thread (the name given to the conventional threads provided by the operating system).
// When a virtual thread has to wait or is blocked, the platform thread will execute another virtual thread.
// The best part is that we don’t have to change existing Java code.

// >> Virtual threads: sum = 47880; time = 1026 ms
// >> OS threads: sum = 50507; time = 10058 ms

@Disabled("For Maven demo")
public class VirtualThreadsTest {

    @Test
    void myThreadsWithThreadInfo() {
        // Os Thread
        new Thread( () ->
                System.out.println(" >> Hi, I am " + Thread.currentThread() + " and I am an Platform thread! "))
                .start();

        // Virtual thread
        Thread.startVirtualThread(() -> System.out.println(" >> Hi, I am " + Thread.currentThread() + " and I am a Virtual thread!"));
        // or using VirtualThreadBuilder
        Thread.ofVirtual().start(() -> System.out.println(" >> Hi, I am " + Thread.currentThread() + " and I am also a Virtual thread! "));
        // or creating it unstarted
        Thread vt = Thread.ofVirtual().unstarted(() -> System.out.println(" >> Hi, I am " + Thread.currentThread() + " and I am also a Virtual thread! "));
        vt.start();
    }

    @Test
    void myThreadsWithCustomNames() {
        // Os Thread
        Thread.ofPlatform()
                .name("Jimmy-P")
                .start(() -> System.out.println(" >> Hi, I am " + Thread.currentThread().getName() + " and I am an Platform thread! "));
        // Virtual thread
        Thread.startVirtualThread(() -> System.out.println(" >> Hi, I am " + Thread.currentThread() + " and I am a Virtual thread!"));
        // or using VirtualThreadBuilder
        Thread.ofVirtual()
                .name("Jimmy-V1")
                .start(() -> System.out.println(" >> Hi, I am " + Thread.currentThread().getName() + " and I am also a Virtual thread! "));

        // or creating it unstarted
        Thread vt = Thread.ofVirtual()
                .name("Jimmy-V2")
                .unstarted(() -> System.out.println(" >> Hi, I am " + Thread.currentThread().getName() + " and I am also a Virtual thread! "));
        vt.start();
    }
    @Test
    void myVirtualThreads(){
        // Os Thread
        new Thread( () -> System.out.println(" >> Hi, I am " + Thread.currentThread().getName() + " and I am an Platform thread! ")).start();
        // Virtual thread
        Thread.startVirtualThread(() -> System.out.println(" >> Hi, I am " + Thread.currentThread().getName() + " and I am a Virtual thread!"));
        // or using VirtualThreadBuilder
        Thread.ofVirtual().start(() -> System.out.println(" >> Hi, I am " + Thread.currentThread().getName() + " and I am also a Virtual thread! "));
        // or creating it unstarted
        Thread vt = Thread.ofVirtual().unstarted(() -> System.out.println(" >> Hi, I am " + Thread.currentThread().getName() + " and I am also a Virtual thread! "));
        vt.start();

        // HEAP of 16GB with ZGC
        // 13,200,000 virtual threads started, 13,102,467 virtual threads running after 61,759 ms
        // System: Java Threads 21
    }

    @Test
    void osThreadsPlay() throws InterruptedException, ExecutionException {
        try (ExecutorService executor = Executors.newFixedThreadPool(100)) {
            var tasks = new ArrayList<MyTask>();
            for (int i = 0; i < 1_000; i++) {
                tasks.add(new MyTask(i));
            }

            long time = System.currentTimeMillis();
            var futures = executor.invokeAll(tasks);

            long sum = 0;
            for (Future<Integer> future : futures) {
                sum += future.get();
            }

            time = System.currentTimeMillis() - time;
            System.out.println(">> OS threads: sum = " + sum + "; time = " + time + " ms");
            // OS threads: sum = 49599; time = 10043 ms
        }
    }

    @Test
    void virtualThreadsPlay() throws InterruptedException, ExecutionException {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var tasks = new ArrayList<MyTask>();
            for (int i = 0; i < 1_000; i++) {
                tasks.add(new MyTask(i));
            }

            long time = System.currentTimeMillis();
            var futures = executor.invokeAll(tasks);

            long sum = 0;
            for (Future<Integer> future : futures) {
                sum += future.get();
            }

            time = System.currentTimeMillis() - time;
            System.out.println(">> Virtual threads: sum = " + sum + "; time = " + time + " ms");
            // Virtual threads: sum = 48838; time = 1049 ms
        }
    }
}
