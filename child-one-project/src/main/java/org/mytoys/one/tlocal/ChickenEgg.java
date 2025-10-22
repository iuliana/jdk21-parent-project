package org.mytoys.one.tlocal;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/* {@code ChickenEgg} instances are obviously not thread safe.*/
public class ChickenEgg {
    private static AtomicInteger seed = new AtomicInteger(0);

    final int id = seed.incrementAndGet();
    String name;
    EggShellType type;
    EggState state;

    private ChickenEgg(){}

    public static ChickenEgg produce() {
        var egg = new ChickenEgg();
        egg.setName(UUID.randomUUID().toString().substring(0,8));
        egg.setType(EggShellType.any());
        egg.state = EggState.INITIAL;
        return egg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(EggShellType type) {
        this.type = type;
    }

    public void setState(EggState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return STR."""
               { "id" = \{this.id}, "name" = "\{this.name}", "type" = "\{this.type.toString().toLowerCase(Locale.ROOT)}", "state" = "\{this.state.toString().toLowerCase(Locale.ROOT)}" }
                """;
    }
}
