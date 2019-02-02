package org.qjson.spi;

public class ValueObjectEncoder implements Encoder {

    private final Encoder encoder;

    public ValueObjectEncoder(Encoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void encode(EncoderSink sink, Object val) {
        encoder.encode(sink, val);
    }

    @Override
    public void encodeRef(EncoderSink sink, Object val, String ref) {
        // force encode, event if same object already encoded
        encoder.encode(sink, val);
    }
}
