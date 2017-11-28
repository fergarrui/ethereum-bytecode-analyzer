package net.nandgr.eth.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lists {

    /**
     * Creates a mutable list of predefined elements
     */
    public static <T> List<T> of(T... objects) {
        return new ArrayList<>(Arrays.asList(objects));
    }
}
