package io.obarch.codec.builtin;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;
import io.obarch.codec.encode.CurrentPath;

class IterableEncoder implements Encoder {

    private final Encoder.Provider spi;

    IterableEncoder(Provider spi) {
        this.spi = spi;
    }

    @Override
    public void encode(EncoderSink sink, Object val) {
        if (val == null) {
            sink.encodeNull();
            return;
        }
        Iterable iterable = (Iterable) val;
        sink.write('[');
        int i = 0;
        for (Object elem : iterable) {
            if (i > 0) {
                sink.write(',');
            }
            CurrentPath currentPath = sink.currentPath();
            int oldPath = currentPath.enterListElement(i);
            sink.encodeObject(elem, spi);
            currentPath.exit(oldPath);
            i++;
        }
        sink.write(']');
    }
}
