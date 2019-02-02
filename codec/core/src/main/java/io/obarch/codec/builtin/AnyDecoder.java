package io.obarch.codec.builtin;

import io.obarch.codec.any.Any;
import io.obarch.codec.any.AnyObject;
import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;

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
