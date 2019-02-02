package io.obarch.codec.codegen;

import io.obarch.codec.spi.QJsonSpi;
import io.obarch.codec.codegen.gen.Gen;

import java.util.Map;

public interface EncoderGenerator {

    default Map<String, Object> args(Codegen.Config cfg, QJsonSpi spi, Class clazz) {
        return null;
    }

    default void genFields(Gen g, Map<String, Object> args) {
    }

    default void genInit(Gen g, Map<String, Object> args) {

    }

    void genEncode(Gen g, Map<String, Object> args, Class clazz);

    void fillEncoder(Map<String, Object> args);
}
