package org.mytoys.one;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static java.util.FormatProcessor.FMT;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JEP 430. String Templates
 */
public class StringTemplatesTest {

    @Test
    void testStringTemplatesBefore() {
        var john = new Person("john", "doe", 42);

        // hard to read
        // concatenation using +
        var introduction = "My name is " + john.firstName +"  and I am "
                + john.age +" years old.";
        System.out.println("[testStringTemplatesBefore] -> + : " + introduction);

        // still hard to read
        // concatenation using String#concat
        introduction = "My name is ".concat(john.firstName).concat("  and I am ")
                .concat(String.valueOf(john.age)).concat(" years old.");
        System.out.println("[testStringTemplatesBefore] -> String#concat : " + introduction);

        // verbose, hard to read as well
        // concatenation using StringBuilder
        introduction = new StringBuilder("My name is ").append(john.firstName).append("  and I am ")
                .append(john.age ).append(" years old.").toString();
        System.out.println("[testStringTemplatesBefore] -> StringBuilder : " + introduction);

        // no of arguments mismatch
        // concatenation using String.format
        introduction = String.format("My name is %s and I am %d years old.", john.firstName , john.age);
        System.out.println("[testStringTemplatesBefore] -> String.format : " + introduction);

        // no of arguments mismatch
        // concatenation using MessageFormat.format
        introduction = MessageFormat.format("My name is {0} and I am {1} years old.", john.firstName , john.age);
        System.out.println("[testStringTemplatesBefore] -> MessageFormat.format : " + introduction);
    }

    @Test
    void testStringTemplates15 () {
        var myString= "My name is %s and I am %d years old.";
        var john = new Person("john", "doe", 42);

        // Java 15
        // concatenation using String#formatted
        var introduction = myString.formatted(john.firstName , john.age);
        System.out.println("[testStringTemplates15]:  -> String#formatted :" + introduction);

    }

    @Test
    void testStringTemplates21 () {
        // Java 21 (preview) : template expressions
        var john = new Person("john", "doe", 42);
        var introduction = STR."My name is \{john.firstName}, and I am \{john.age} years old.";
        System.out.println("[testStringTemplates21]: introduction = " + introduction);
    }


    @Test
    void testStringTemplates21Multiline () {
        // Java 21 (preview) : template expressions
        var john = new Person("john", "doe", 42);

        var introduction = STR."""
            My name is \{john.firstName},
            and I am \{john.age} years old.
            """;
        System.out.println("[testStringTemplates21Multiline]: introduction = " + introduction);
    }


    @Test
    void testStringTemplates21JSONProcessor () {
        /// CREATE NEW TEMPLATE PROCESSOR
        var JSON = StringTemplate.Processor.of(
                (StringTemplate template) -> new JSONObject(template.interpolate())
        );
        var john = new Person("john", "doe", 42);

        var introduction = JSON."""
            {
                "message1" : "My name is \{john.firstName}",
                "message2" : "and I am \{john.age} years old."
            }
            """;
        assertTrue(introduction instanceof JSONObject);
        System.out.println("[testStringTemplates21JSONProcessor]: introduction = " + introduction);
    }

    @Test
    void testFMTTemplates21Processor () {
        // Java 21 (preview) : FMT = STR + interprets format specifiers
        var john = new Person("john", "doe", 42);

        var introduction = FMT."""
            My name is \{john.firstName},
            and I am %05d\{john.age} years old.
            """;
        System.out.println("[testStringTemplates21Multiline]: introduction = " + introduction);
    }
}
