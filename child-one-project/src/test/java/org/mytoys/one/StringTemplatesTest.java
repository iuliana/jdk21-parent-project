package org.mytoys.one;

import org.junit.jupiter.api.Test;

/**
 * JEP 1. String Templates
 */
public class StringTemplatesTest {
    @Test
    void testStringTemplates15 () {
        var myString= "My name is %s and I am %d years old.  ";
        var john = new Person("john", "doe", 42);

        // Java 15
        var introduction = myString.formatted(john.firstName , john.age);
        System.out.println("[testStringTemplates15]: introduction = " + introduction);

    }

    @Test
    void testStringTemplates21 () {
        // Java 21 (preview) : template expressions
        var john = new Person("john", "doe", 42);
        var introduction = STR."My name is \{john.firstName}, and I am \{john.age} years old.";
        System.out.println("[testStringTemplates21]: introduction = " + introduction);
    }
}
