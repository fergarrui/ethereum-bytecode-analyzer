package net.nandgr.eth.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lists {

    /**
     * Creates a mutable list of predefined elements
     * Will be removed when upgraded to Java 9
     */
    public static <T> List<T> of(T... objects) {
        return new ArrayList<>(Arrays.asList(objects));
    }
}
