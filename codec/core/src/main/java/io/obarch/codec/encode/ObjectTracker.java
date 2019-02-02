package io.obarch.codec.encode;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

import java.util.HashMap;
import java.util.Map;

final class ObjectTracker {

    private final Map<Integer, String> tracking = new HashMap<>();
    private CurrentPath currentPath = new CurrentPath();
    private final EncoderSink sink;

    ObjectTracker(EncoderSink sink) {
        this.sink = sink;
    }

    public CurrentPath currentPath() {
        return currentPath;
    }

    public final void encodeObject(Object val, Encoder encoder) {
        if (val == null) {
            sink.encodeNull();
            return;
        }
        encode(val, encoder);
    }

    public final void encodeObject(Object val, Encoder.Provider spi) {
        if (val == null) {
            sink.encodeNull();
            return;
        }
        Encoder encoder = spi.encoderOf(val.getClass());
        encode(val, encoder);
    }

    private void encode(Object val, Encoder encoder) {
        int id = System.identityHashCode(val);
        String ref = tracking.get(id);
        if (ref == null) {
            tracking.put(id, currentPath.toString());
            encoder.encode(sink, val);
        } else {
            encoder.encodeRef(sink, val, ref);
        }
    }

    public void reset() {
        tracking.clear();
        currentPath.exit(0);
    }
}
