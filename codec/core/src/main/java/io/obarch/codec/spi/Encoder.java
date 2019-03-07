package io.obarch.codec.spi;

// Encoder should not be used directly
// always use `sink.encodeValue` to call encoder
public interface Encoder {

    interface Provider {
        Encoder encoderOf(Class clazz);
    }

    // if null: encodeNull will be called
    // if object already encoded: encodeRef will be called
    // otherwise: encode will be called
    void encode(EncoderSink sink, Object val);

    // override encodeRef to force encode out the whole object again
    // default behavior is to write out "\/path" as reference
    default void encodeRef(EncoderSink sink, Object val, String ref) {
        sink.encodeRef(ref);
    }
}
