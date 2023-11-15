package org.mytoys.one;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mytoys.one.scoped.ScopedTask;

@Disabled
public class ScopedValuesTest {

    @Test
    void runCarriers() throws InterruptedException {
        ScopedTask st = new ScopedTask("gigi");
        for (int i = 0; i < 10; i++) {
            Thread.ofPlatform().name("Kid " + i).start(st);
        }
        Thread.sleep(3_000);

    }
}
