package org.mytoys.one;

import org.junit.jupiter.api.Test;


record FullName (String firstName, String lastName){ }

record PersonRecord (FullName fullName, Integer age) {}

record WrapperBeing<T>(T t, String description) { }

/**
 * Record Patterns
 */
public class RecordPatternsTest {

    @Test
    void testRecordPatterns16 () {
        // Java 16: Pattern Matching for instanceof
        Being being = new Person("john", "doe", 42);
        if (being instanceof Person person) {
            System.out.println("FirstName: " + person.getFirstName());
        }
    }

    @Test
    void testRecordPatterns () {
        // JEP 440. Pattern Matching for instanceof with Records (Java 19)
        Object  john =  new FullName("John", "Doe");
        if (john instanceof FullName full) {
            System.out.println("> FirstName: " + full.firstName());
        }

        // extracting the values of the pattern variable using a deconstruction pattern
        if (john instanceof FullName (String firstName, String lastName)) {
            System.out.println(">> FirstName: " + firstName);
            System.out.println(">> LastName: " + lastName);
        }

        Object johnRecord =  new PersonRecord((FullName) john, 42);
        // works with `var` and composed records too
        if (johnRecord instanceof PersonRecord (FullName (var firstName, String lastName), var age)) {
            System.out.println(">>> FirstName: " + firstName);
            System.out.println(">>> LastName: " + lastName);
            System.out.println(">>> Age: " + age);
        }

        // works with generics too
        WrapperBeing<PersonRecord> wrapper = new WrapperBeing<>((PersonRecord) johnRecord, "is mise John");
        if (wrapper instanceof WrapperBeing<PersonRecord>(var personRecord, var description)) {
            System.out.println(">>> PersonRecord: " + personRecord);
        }

        // JEP 441. Pattern Matching for switch
        var result = switch (john) {
            case FullName j -> j.firstName();
            default -> "default";
        };
        System.out.println(">>>> Switch FullName: " +  result);

        var resultAge =  switch (johnRecord) {
            case PersonRecord(FullName (var firstName, String lastName), var age) -> age;
            default -> "-1";
        };
        System.out.println(">>>> Switch Age: " +  resultAge);

        // using the "when"
        var resultWithGuard = switch (john) {
            case FullName (String firstName, String lastName)
                    when firstName.equals("John") -> (new FullName("John", "Toe")).lastName();
            case FullName (String firstName, String lastName) -> lastName;
            default -> "default";
        };
        System.out.println(">>>> resultWithGuard: " +  resultWithGuard);
    }

    /**
     * JEP 443. Unnamed Pattern & Unnamed Variables
     * eliminate useless info, make to code easier to read and understand
     */
    @Test
    void testUnnamedPattern(){
        var  john =  new FullName("John", "Doe");

        Object johnRecord =  new PersonRecord(john, 42);
        //  we are only interested in the 'age'
        if (johnRecord instanceof PersonRecord (FullName (var firstName, String lastName), var age)) {
            System.out.println(">>> Age: " + age);
        }

        // Unnamed Pattern - stolen from Go :D
        if (johnRecord instanceof PersonRecord (_, var age)) {
            System.out.println(">>> [Unnamed Pattern]Age : " + age);
        }
        if (johnRecord instanceof PersonRecord (FullName (var _, String lastName), var _)) {
            System.out.println(">>> [Unnamed Variable] LastName: " + lastName);
        }

        // Unnamed Variable
        try {
            Thread.sleep(20);
        } catch (InterruptedException _) {
        }
    }
}

