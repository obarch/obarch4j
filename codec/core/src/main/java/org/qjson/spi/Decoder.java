package org.qjson.spi;

import java.lang.reflect.Type;

public interface Decoder {

    interface Provider {
        Decoder decoderOf(Type type);
    }

    // create the object
    // it might be decoded from the source fully
    // or it might be a empty object
    Object decode(DecoderSource source);

    // fill the properties of object created by "decode"
    default void decodeProperties(DecoderSource source, Object obj) {
    }
}
