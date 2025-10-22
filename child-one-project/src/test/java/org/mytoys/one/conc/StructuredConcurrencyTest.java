package org.mytoys.one.conc;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StructuredConcurrencyTest {

    @Test
    void anyWouldDo() {
        try(var scopedTask = new StructuredTaskScope.ShutdownOnSuccess<Double>()) { // initialization
            // Tasks can be submitted to the StructuredTaskScope instance using the fork method.
            StructuredTaskScope.Subtask<Double> res1 = scopedTask.fork(() -> new AmazonService().getPrice());
            StructuredTaskScope.Subtask<Double> res2 = scopedTask.fork(() -> new PetsAtHomeService().getPrice());
            StructuredTaskScope.Subtask<Double> res3 = scopedTask.fork(() -> new PurinaService().getPrice());
            try {
                scopedTask.join();
                System.out.println(STR."Scoped task result: \{scopedTask.result()}");
            } catch (InterruptedException e) {
                throw new RuntimeException("Could not join executing sun tasks!", e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Could not get result of the scoped task!", e);
            }
        }
    }

    @RepeatedTest(10)
    void anyWouldDoWithFailure() {
        try(var scopedTask = new StructuredTaskScope.ShutdownOnSuccess<Double>()) {
            scopedTask.fork(() -> new AmazonService().getPrice());
            scopedTask.fork(() -> new PetsAtHomeService().getPrice());
            scopedTask.fork(() -> new PurinaService().getPrice());
            // this fails a lot
            scopedTask.fork(() -> new UnreliableService().getPrice());
            try {
                scopedTask.join(); // since I need at least one task to be successful this does not fail. Ever ;)
                System.out.println(STR."Scoped task result: \{scopedTask.result()}");
            } catch (InterruptedException e) {
                throw new RuntimeException("Could not join executing sub-tasks!", e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Could not get result of the scoped task!", e);
            }
        }
    }

    @Test
    void allOrNone() {
        try(var scopedTask = new StructuredTaskScope.ShutdownOnFailure()) {
            Stream<StructuredTaskScope.Subtask<Double>> subTasks = Stream.of(
                    scopedTask.fork(() -> new AmazonService().getPrice()),
                    scopedTask.fork(() -> new PetsAtHomeService().getPrice()),
                    scopedTask.fork(() -> new PurinaService().getPrice())
            );

            try {
                scopedTask.join();
                var minPrice = subTasks
                        .peek(st -> System.out.println(STR."Task: \{st.task()} ,State: \{st.state()}")) // problem -> no way to figure what which service this task corresponds to
                        .map(StructuredTaskScope.Subtask::get).min(Double::compareTo);
                System.out.println(STR."Minimum price: \{minPrice.get()}");
            } catch (InterruptedException e) {
                throw new RuntimeException("Could not join executing sub-tasks!", e);
            }
        }
    }

    @RepeatedTest(10)
    void allOrNoneWithFailure() {
        try(var scopedTask = new StructuredTaskScope.ShutdownOnFailure()) {
            Stream<StructuredTaskScope.Subtask<Double>> subTasks = Stream.of(
                    scopedTask.fork(() -> new AmazonService().getPrice()),
                    scopedTask.fork(() -> new PetsAtHomeService().getPrice()),
                    scopedTask.fork(() -> new PurinaService().getPrice()),
                    scopedTask.fork(() -> new UnreliableService().getPrice())
            );

            try {
                scopedTask.join().throwIfFailed(); // propagating exceptions from subtasks
                var minPrice = subTasks
                        .peek(st -> System.out.println(STR. "Task: \{ st.task() } , State: \{ st.state() }" ))
                        .map(StructuredTaskScope.Subtask::get).min(Double::compareTo).orElse(-2.0);
                System.out.println(STR."Minimum price: \{minPrice}");
                assertTrue(minPrice > 0.0);
            } catch (InterruptedException e) {
                throw new RuntimeException("Could not join executing sub-tasks!", e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Subtask failed to execute!", e);
            }
        }
    }

    @RepeatedTest(10)
    void allOrNoneWithNestedScopesAndFailures() {
        try(var scopedTask = new StructuredTaskScope.ShutdownOnFailure()) {
            Stream<StructuredTaskScope.Subtask<Double>> subTasks = Stream.of(
                    scopedTask.fork(() -> new AmazonService().getPrice()),
                    scopedTask.fork(() -> new IntermediaryService().getPrice())
            );

            try {
                scopedTask.join();
                var minPrice = subTasks
                        .peek(st -> System.out.println(STR. "[parent]Task: \{ st.task() } ,State: \{ st.state() }" )) // problem -> no way to figure what which service this task corresponds to, thus cannot figure out which subtask failed.
                        .map(StructuredTaskScope.Subtask::get).min(Double::compareTo).orElse(-2.0);
                System.out.println(STR."Minimum price: \{minPrice}");
                assertTrue(minPrice > 0.0);
            } catch (InterruptedException e) {
                throw new RuntimeException("Could not join executing sub-tasks!", e);
            }
        }
    }

    @RepeatedTest(10)
    void allOrNoneWithNestedScopesAndFailuresPropagated() {
        try(var scopedTask = new StructuredTaskScope.ShutdownOnFailure()) {
            Stream<StructuredTaskScope.Subtask<Double>> subTasks = Stream.of(
                    scopedTask.fork(() -> new AmazonService().getPrice()),
                    scopedTask.fork(() -> new IntermediaryService().getPrice())
            );

            try {
                scopedTask.join().throwIfFailed();
                var minPrice = subTasks
                        .peek(st -> System.out.println(STR. "[parent]Task: \{ st.task() } ,State: \{ st.state() }" )) // problem -> no way to figure what which service this task corresponds to, thus cannot figure out which subtask failed.
                        .map(StructuredTaskScope.Subtask::get).min(Double::compareTo).orElse(-2.0);
                System.out.println(STR."Minimum price: \{minPrice}");
                assertTrue(minPrice > 0.0);
            } catch (InterruptedException e) {
                throw new RuntimeException("Could not join executing sun tasks!", e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Subtask failed to execute!", e); // conclusion does not apply to grand-children!!!
            }
        }
    }

}
