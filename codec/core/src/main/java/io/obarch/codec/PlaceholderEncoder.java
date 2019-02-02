package io.obarch.codec;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

public class PlaceholderEncoder implements Encoder {

    private final Encoder.Provider provider;
    private final Class clazz;

    public PlaceholderEncoder(Provider provider, Class clazz) {
        this.provider = provider;
        this.clazz = clazz;
    }

    @Override
    public void encode(EncoderSink sink, Object val) {
        lookup().encode(sink, val);
    }

    @Override
    public void encodeRef(EncoderSink sink, Object val, String ref) {
        lookup().encodeRef(sink, val, ref);
    }

    public Encoder lookup() {
        return provider.encoderOf(clazz);
    }
}
