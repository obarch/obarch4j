package org.qjson.any;

import java.util.LinkedHashMap;

public class AnyMap<K, V> extends LinkedHashMap<K, V> implements Any {

    public AnyMap(Object... kv) {
        for (int i = 0; i < kv.length; i += 2) {
            put((K) kv[i], (V) kv[i + 1]);
        }
    }

    @Override
    public Any set(Object key, Object value) {
        put((K) key, (V) value);
        return this;
    }
}
