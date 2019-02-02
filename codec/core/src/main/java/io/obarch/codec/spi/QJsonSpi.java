package io.obarch.codec.spi;

import java.util.function.Function;

public interface QJsonSpi extends Decoder.Provider, Encoder.Provider {
    Function<DecoderSource, Object> factoryOf(Class clazz);
}
