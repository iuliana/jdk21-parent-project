package org.mytoys.one;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JEP 6. Sequenced Collections : methods in the java.util.SequencedCollection<E> interface
 */
public class SequencedCollectionsTest {

    static List<String> list = new ArrayList<>();

    @Test
    void testSequencedCollections() {
        list.add("one");
        list.add("two");
        list.add("three");

        printl("[original list]");
        list.addFirst("zero");
        printl("[after addFirst(..)]");
        assertEquals(4, list.size());
        list.removeFirst();
        printl("[after removeFirst(..)]");
        assertEquals(3, list.size());
        System.out.println("---------------------------------------------------");
        list.addLast("four");
        printl("[after addLast(..)]");
        assertEquals(4, list.size());
        list.removeLast();
        printl("[after removeLast(..)]");
        assertEquals(3, list.size());
        System.out.println("---------------------------------------------------");
        var reversed = list.reversed();
        System.out.println("[after reversed()]:"  + reversed);
    }

    public static void printl(String... opt){
        System.out.println((opt.length > 0 ? opt[0] + ": " : "") + list);
    }
}
