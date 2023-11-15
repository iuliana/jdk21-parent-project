package org.mytoys.one.tlocal;

import java.security.SecureRandom;
import java.util.concurrent.Callable;

public class KidAssignment implements Runnable, Callable<Boolean> {

    private static final ThreadLocal<ChickenEgg> egg = ThreadLocal.withInitial(ChickenEgg::produce);

    public KidAssignment() {
        egg.set(ChickenEgg.produce());
    }

    @Override
    public Boolean call() throws Exception {
        run();
        return true; // assume done
    }

    public String currentThreadName(){
        var ctn = Thread.currentThread().getName();
        return ctn.isBlank()? Thread.currentThread().toString() : ctn;
    }

    @Override
    public void run() {
        System.out.print(STR."\{currentThreadName()} , egg: \{egg.get().toString()}");
        try {
            Thread.sleep(new SecureRandom().nextInt(1000));
        } catch (InterruptedException _) {
        }
        // the most simple example to show mutability problems of the ThreadLocal instance -> we do not know what happens to the egg
        randomSetOfActions();
        System.out.print(STR."\{currentThreadName()} , egg: \{egg.get().toString()}");
    }

    private void randomSetOfActions() { // each kid gets 4
        for (int i = 0; i < 4; ++i) {
            var action  = new SecureRandom().nextInt(6) +1;
            switch(action) {
                case 1 ->  renameIt();
                case 2 ->   tattooIt();
                case 3 ->   dressIt();
                case 4 ->   crackIt();
                case 5 ->  boilIt();
                default -> loseIt();
            }
        }
    }

    public void tattooIt(){
        egg.get().setState(EggState.TATTOOED);
    }
    public void renameIt(){
        var theEgg = egg.get();
        theEgg.setName("Jimmy");
        theEgg.setState(EggState.RENAMED);
    }
    public void dressIt(){
        egg.get().setState(EggState.DRESSED);
    }
    public void crackIt(){
        egg.get().setState(EggState.CRACKED);
    }
    public void boilIt(){
        egg.get().setState(EggState.BOILED);
    }
    public void loseIt(){
        System.out.println(STR. "!! \{ currentThreadName() } lost the egg!" );
        egg.set(ChickenEgg.produce());  // kid replaces the egg
    }

}
