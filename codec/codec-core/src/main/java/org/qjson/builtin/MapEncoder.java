package org.qjson.builtin;

import org.qjson.encode.CurrentPath;
import org.qjson.encode.StringEncoderSink;
import org.qjson.spi.Encoder;
import org.qjson.spi.EncoderSink;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class MapEncoder implements Encoder {

    final static Set<Class> VALID_KEY_CLASSES = new HashSet<Class>() {{
        add(String.class);
        add(Character.class);
        add(byte[].class);
        add(Byte.class);
        add(Short.class);
        add(Integer.class);
        add(Long.class);
        add(Float.class);
        add(Double.class);
    }};

    public static void init() {
    }

    private final static EncoderSink.AttachmentKey<StringEncoderSink> TEMP_KEY = EncoderSink.AttachmentKey.assign();
    private final Map<Class, Encoder> keyEncoderCache = new ConcurrentHashMap<>();
    private final Encoder.Provider spi;

    MapEncoder(Encoder.Provider spi) {
        this.spi = spi;
    }

    @Override
    public void encode(EncoderSink sink, Object val) {
        if (val == null) {
            sink.encodeNull();
            return;
        }
        Map<Object, Object> map = (Map<Object, Object>) val;
        sink.write('{');
        boolean isFirst = true;
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sink.write(',');
            }
            Object key = entry.getKey();
            int mark = sink.mark();
            if (key == null) {
                sink.encodeString("null");
            } else {
                Encoder keyEncoder = getKeyEncoder(key.getClass());
                sink.encodeObject(key, keyEncoder);
            }
            String encodedKey = sink.sinceMark(mark);
            sink.write(':');
            CurrentPath currentPath = sink.currentPath();
            int oldPath = currentPath.enterMapValue(encodedKey);
            sink.encodeObject(entry.getValue(), spi);
            currentPath.exit(oldPath);
        }
        sink.write('}');
    }

    private Encoder getKeyEncoder(Class<?> clazz) {
        return keyEncoderCache.computeIfAbsent(clazz, this::generateKeyEncoder);
    }

    private Encoder generateKeyEncoder(Class clazz) {
        Encoder encoder = spi.encoderOf(clazz);
        if (VALID_KEY_CLASSES.contains(clazz)) {
            return encoder;
        }
        return (sink, val) -> {
            StringEncoderSink newSink = sink.borrowAttachment(TEMP_KEY);
            if (newSink == null) {
                newSink = new StringEncoderSink();
            } else {
                newSink.reset();
            }
            encoder.encode(newSink, val);
            sink.encodeString(newSink.toString());
        };
    }
}
