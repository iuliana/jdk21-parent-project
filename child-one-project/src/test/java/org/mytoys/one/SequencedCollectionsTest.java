package org.mytoys.one;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JEP 431. Sequenced Collections : methods in the java.util.SequencedCollection<E> interface
 */
public class SequencedCollectionsTest {

    static List<String> list = new ArrayList<>();

    @Test
    void traversalPlay(){
        var list = List.of("one", "two", "three", "eleven", "thirteen", "twenty one", "thirty two" );

        System.out.println("-------- fori ----------"); // imperative
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }

        // JDK 5 (maybe !?)
        System.out.println("\n--------- enhanced for ---------"); // imperative
        for (var str : list) {
            System.out.print(str + " ");
        }

        System.out.println("\n-------- Iterator ----------"); // imperative
        for (var it = list.iterator(); it.hasNext();) {
            System.out.print(it.next() + " ");
        }

        // imperative, can traverse it starting at the end, gen access index, can remove and add elements
        System.out.println("\n-------- ListIterator ----------");
        for (var it = list.listIterator(); it.hasNext();) {
            System.out.print(it.next() + " ");
        }

        // JDK 1.8
        System.out.println("\n--------- forEach ---------"); // most simple functional traversal
        list.forEach(s -> System.out.print(s + " "));

        // functional, converts to stream before traversal;
        // advantage: possibility to stream operations like filter, map, distinct, etc.
        System.out.println("\n--------- stream().forEach ---------");
        list.stream().forEach(s -> System.out.print(s + " "));

        // JDK 1.8
        // functional, using a range to generate indexes for the traversal
        System.out.println("\n-------- range ----------");
        IntStream.range(0, list.size())
                .forEach(i -> System.out.print(list.get(i) + " "));

        // functional, using a rangeClosed() to generate indexes for the traversal
        System.out.println("\n-------- rangeClosed ----------");
        IntStream.rangeClosed(0, list.size()-1)
                .forEach(i -> System.out.print(list.get(i) + " "));

    }


    // all new in JDK 21
    @Test
    void testSequencedCollections() {
        list.add("one");
        list.add("two");
        list.add("three");

        var reversed = list.reversed();
        System.out.println("[after reversed()]:"  + reversed);
        System.out.println("---------------------------------------------------");

        printl("[first element]", list.getFirst()); // list.get(0)
        printl("[last element]", list.getLast());   // list.get(list.size()-1)
        System.out.println("---------------------------------------------------");

        printl("[original list]");
        list.addFirst("zero");                        // list.add(0, "zero");
        printl("[after addFirst(..)]");
        assertEquals(4, list.size());
        list.removeFirst();                             // list.remove(0);
        printl("[after removeFirst(..)]");
        assertEquals(3, list.size());
        System.out.println("---------------------------------------------------");

        list.addLast("four");                       // list.add("four");
        printl("[after addLast(..)]");
        assertEquals(4, list.size());
        list.removeLast();                             //list.remove(list.size()-1);
        printl("[after removeLast(..)]");
        assertEquals(3, list.size());

    }

    public static void printl(String... opt){
        System.out.println((opt.length > 0 ? opt[0] + ": " : "") + list);
    }
}
