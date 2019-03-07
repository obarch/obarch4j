package io.obarch.codec.reflection;

import io.obarch.codec.encode.CurrentPath;
import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

import java.lang.reflect.Array;

public class ArrayEncoder implements Encoder {

    private final Provider spi;

    public ArrayEncoder(Encoder.Provider spi) {
        this.spi = spi;
    }

    @Override
    public void encode(EncoderSink sink, Object val) {
        sink.write('[');
        int length = Array.getLength(val);
        CurrentPath currentPath = sink.currentPath();
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sink.write(',');
            }
            Object elem = Array.get(val, i);
            int oldPath = currentPath.enterListElement(i);
            sink.encodeValue(elem, spi);
            currentPath.exit(oldPath);
        }
        sink.write(']');
    }
}
