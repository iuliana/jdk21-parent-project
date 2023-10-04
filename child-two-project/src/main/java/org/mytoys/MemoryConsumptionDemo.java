package org.mytoys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemoryConsumptionDemo {
    private static final Logger log = LoggerFactory.getLogger(MemoryConsumptionDemo.class);
    private static NameGenerator nameGenerator = new NameGenerator();
    private static final Random random = new Random();

    public static void main(String... args) {
        printTotalMemory(log);
        List<Singer> singers = new ArrayList<>();
        for (int i = 0; i < 1_000_000; ++i) {
            singers.add(genSinger());
            if (i % 1000 == 0) {
                printBusyMemory(log);
            }
        }
    }
    private static Singer genSinger() {
        return new Singer(nameGenerator.genName(), random.nextDouble(), LocalDate.now());
    }

    private static final long MEGABYTE = 1024L * 1024L;
    private static final Runtime runtime = Runtime.getRuntime();

    public static void printBusyMemory(Logger log) {
        long memory = runtime.totalMemory() - runtime.freeMemory();
        //log.info("Occupied memory: {} MB", (memory / MEGABYTE));
    }
    public static void printTotalMemory(Logger log) {
        //log.info("Total Program memory: {} MB", (runtime.totalMemory()/MEGABYTE));
        //log.info("Max Program memory: {} MB", (runtime.maxMemory()/MEGABYTE));
    }

}

record Singer(String name, Double rating, LocalDate dob) {}

class NameGenerator {
    private static final String letters = "abcdefghijk lmnopqrstuvwzyz";
    private Random random = new Random();
    private final char[] name;

    public NameGenerator() {
        name = new char[15];
    }

    public NameGenerator(int capacity) {
        name = new char[capacity];
    }

    public String genName() {
        for (int i = 0; i < name.length; ++i) {
            name[i] = letters.charAt(random.nextInt(letters.length()));
        }
        name[0] = (char) (name[0] - 32);
        return new String(name);
    }
}