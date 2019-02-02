package org.qjson.any;

import java.util.ArrayList;

public class AnyList<E> extends ArrayList<E> implements Any {

    public AnyList(E... elements) {
        super(elements == null ? 1 : elements.length);
        if (elements == null) {
            add(null);
        } else {
            for (E element : elements) {
                add(element);
            }
        }
    }

    @Override
    public Object get(Object key) {
        Integer index = (Integer) key;
        return super.get(index);
    }

    @Override
    public Any set(Object key, Object value) {
        Integer index = (Integer) key;
        super.set(index, (E) value);
        return this;
    }
}
