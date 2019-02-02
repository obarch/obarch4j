package org.qjson.any;

import java.util.Arrays;

public interface Any {

    default Object get(Object... path) {
        if (path.length == 0) {
            return this;
        }
        Object elem = get(path[0]);
        if (path.length == 1) {
            return elem;
        }
        Any any = (Any) elem;
        return any.get(Arrays.copyOfRange(path, 1, path.length));
    }

    default Any at(Object... path) {
        return (Any) get(path);
    }

    default Object get(Object key) {
        throw new UnsupportedOperationException("object is not indexable");
    }

    default Any set(Object key, Object value) {
        throw new UnsupportedOperationException("object is immutable");
    }
}
