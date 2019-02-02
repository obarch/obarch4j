package io.obarch.codec.usage.container;

import java.util.*;

public class MyObjects implements Iterable<Object> {

    public final Object obj1;
    public final Object obj2;

    public MyObjects(Object obj1, Object obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    public Iterator<Object> iterator() {
        return new Iterator<Object>() {

            private int i = 1;

            @Override
            public boolean hasNext() {
                return i == 1 || i == 2;
            }

            @Override
            public Object next() {
                if (i == 1) {
                    i = 2;
                    return obj1;
                }
                if (i == 2) {
                    i = 3;
                    return obj2;
                }
                return null;
            }
        };
    }
}