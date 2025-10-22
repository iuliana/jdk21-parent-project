package org.mytoys.one.tlocal;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum EggShellType {
    WHITE,
    BROWN,
    BLUE,
    GREEN,
    PINK,
    SPECKLED;

    private static final List<EggShellType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final SecureRandom random = new SecureRandom();
    public static EggShellType any(){
        return VALUES.get(random.nextInt(SIZE));
    }
}

