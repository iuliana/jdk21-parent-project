package org.mytoys.one;

import org.junit.jupiter.api.Test;
import org.mytoys.one.tlocal.KidAssignment;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalTest {

    @Test
    void classicThreads() throws InterruptedException {
        System.out.println(" ->> Starting platform threads using classic code!");
        KidAssignment ka = new KidAssignment();
        for (int i = 0; i < 10; i++) {
            var t = new Thread(ka, "Kid " + i);
            t.start();
        }
        Thread.sleep(3_000);
    }

    @Test
    void classicThreadsJava21Syntax() throws InterruptedException {
        System.out.println(" ->> Starting platform threads using Java 21 code!");
        KidAssignment ka = new KidAssignment();
        for (int i = 0; i < 10; i++) {
            Thread.ofPlatform().name("Kid " + i).start(ka);
        }
        Thread.sleep(3_000);
    }

    @Test
    void classicThreadPool(){
        System.out.println(" ->> Starting platform threads using executor!");
        KidAssignment ka = new KidAssignment();
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            var assignments = new ArrayList<KidAssignment>();
            for (int i = 0; i < 10; i++) {
                assignments.add(ka);
            }
            try {executor.invokeAll(assignments);} catch (InterruptedException _) {}
        }
    }

    @Test
    void virtualThreads(){
        System.out.println(" ->> Starting virtual threads using Java 21 code!");
        KidAssignment ka = new KidAssignment();
        for (int i = 0; i < 10; i++) {
            Thread.ofVirtual().name("Kid " + i).start(ka);
        }
        try {Thread.sleep(3000);} catch (InterruptedException _) {}
    }

    @Test
    void virtualThreadPool() {
        System.out.println(" ->> Starting virtual threads using executor!");
        KidAssignment ka = new KidAssignment();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var assignments = new ArrayList<KidAssignment>();
            for (int i = 0; i < 10; i++) {
                assignments.add(ka);
                // cannot name my virtual threads here!!
            }
            try {executor.invokeAll(assignments);} catch (InterruptedException _) {}
        }
    }
}
