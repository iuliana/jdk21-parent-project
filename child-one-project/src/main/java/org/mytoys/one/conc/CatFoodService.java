package org.mytoys.one.conc;

import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Stream;

public interface CatFoodService {
    Double getPrice();

    default void waitRandom() {
        try { //Wait for random amount of time
            Thread.sleep(new SecureRandom().nextInt(1000));
        } catch (InterruptedException _) {
        }
    }

    default double genPrice(Double bound) {
        return new SecureRandom().nextDouble(bound);
    }
}


class AmazonService implements CatFoodService{
    @Override
    public Double getPrice() {
        waitRandom();
        return genPrice(50.0);
    }
}

class PetsAtHomeService implements  CatFoodService{
    @Override
    public Double getPrice() {
        waitRandom();
        return genPrice(59.0);
    }
}

class PurinaService implements  CatFoodService{
    @Override
    public Double getPrice() {
        waitRandom();
        return genPrice(39.0);
    }
}

class UnreliableService implements  CatFoodService{
    @Override
    public Double getPrice() {
        //waitRandom(); I want this to fail fast
        var price =  genPrice(59.0);
        if (price < 20) {
            throw new IllegalStateException("Price too low! (Product expired or fake!");
        }
        return price;
    }
}

class IntermediaryService implements CatFoodService{
    @Override
    public Double getPrice() {
        try(var scopedTask = new StructuredTaskScope.ShutdownOnFailure()) {
            Stream<StructuredTaskScope.Subtask<Double>> subTasks = Stream.of(
                    scopedTask.fork(() -> new PetsAtHomeService().getPrice()),
                    scopedTask.fork(() -> new PurinaService().getPrice()),
                    scopedTask.fork(() -> new UnreliableService().getPrice())
            );
            try {
                scopedTask.join();
                return subTasks
                        .peek(st -> System.out.println(STR."[child]Task: \{st.task()} ,State: \{st.state()}"))
                        .map(StructuredTaskScope.Subtask::get).min(Double::compareTo).orElse(-1.0);
            } catch (InterruptedException e) {
                throw new RuntimeException("Could not join executing sun tasks!", e);
            }
        }
    }
}





