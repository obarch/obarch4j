package org.qjson.builtin;

import org.qjson.any.Any;
import org.qjson.any.AnyObject;
import org.qjson.spi.Decoder;
import org.qjson.spi.DecoderSource;

class AnyDecoder implements Decoder {

    private final Decoder objectDecoder;

    AnyDecoder(Decoder objectDecoder) {
        this.objectDecoder = objectDecoder;
    }

    @Override
    public Object decode(DecoderSource source) {
        Object obj = source.decodeObject(objectDecoder);
        if (obj instanceof Any) {
            return obj;
        }
        return new AnyObject(obj);
    }
}
