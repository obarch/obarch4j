package org.qjson.codegen;

import org.qjson.codegen.gen.Gen;
import org.qjson.spi.QJsonSpi;

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
