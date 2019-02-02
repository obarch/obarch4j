package org.qjson.decode;

import org.qjson.encode.CurrentPath;
import org.qjson.spi.Decoder;
import org.qjson.spi.DecoderSource;

import java.util.HashMap;
import java.util.Map;

final class PathTracker {

    private final Map<String, Object> tracking = new HashMap<>();
    private CurrentPath currentPath = new CurrentPath();
    private final DecoderSource source;

    PathTracker(DecoderSource source) {
        this.source = source;
    }

    public Object decodeObject(Decoder decoder, boolean track) {
        if (source.decodeNull()) {
            return null;
        }
        Object obj = source.decodeRef(decoder);
        if (obj != null) {
            return obj;
        }
        obj = decoder.decode(source);
        if (track) {
            String path = currentPath.toString();
            tracking.put(path, obj);
        }
        decoder.decodeProperties(source, obj);
        return obj;
    }

    public Object lookup(String path) {
        Object obj = tracking.get(path);
        if (obj == null) {
            throw source.reportError("referenced path not found: " + path);
        }
        return obj;
    }

    public CurrentPath currentPath() {
        return currentPath;
    }
}
