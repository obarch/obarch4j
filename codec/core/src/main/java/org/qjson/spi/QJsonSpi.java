package org.qjson.spi;

import java.util.function.Function;

public interface QJsonSpi extends Decoder.Provider, Encoder.Provider {
    Function<DecoderSource, Object> factoryOf(Class clazz);
}
