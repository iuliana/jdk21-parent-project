package org.mytoys.one.scoped;

import java.security.SecureRandom;

/**
 * JEP 446 - TODO - later, currently there is no way to bind the value.
 */
public class ScopedTask implements Runnable {
    private static final ScopedValue<String> USER = ScopedValue.newInstance();
    private final ScopedValue.Carrier carrier;

    private final String owner;

    public ScopedTask(String owner) {
        this.owner = owner;
        carrier = ScopedValue.where(USER, owner);
    }

    @Override
    public void run() {
        System.out.println(STR."Is the SV bound: \{ USER.isBound()}");
       carrier.run(() -> System.out.println(STR."\{USER.get()} of \{currentThreadName()} working hard."));
        try {
            Thread.sleep(new SecureRandom().nextInt(1000));
        } catch (InterruptedException _) {
        }
    }

    public String currentThreadName(){
        var ctn = Thread.currentThread().getName();
        return ctn.isBlank()? Thread.currentThread().toString() : ctn;
    }
}
