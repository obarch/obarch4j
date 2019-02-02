package org.qjson;

import org.qjson.spi.Decoder;
import org.qjson.spi.DecoderSource;

import java.lang.reflect.Type;

public class PlaceholderDecoder implements Decoder {

    private final Decoder.Provider provider;
    private final Type type;

    public PlaceholderDecoder(Provider provider, Type type) {
        this.provider = provider;
        this.type = type;
    }

    @Override
    public Object decode(DecoderSource source) {
        return lookup().decode(source);
    }

    @Override
    public void decodeProperties(DecoderSource source, Object obj) {
        lookup().decodeProperties(source, obj);
    }

    public Decoder lookup() {
        return provider.decoderOf(type);
    }
}
