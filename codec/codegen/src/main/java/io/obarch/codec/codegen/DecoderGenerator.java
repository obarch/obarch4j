package io.obarch.codec.codegen;

import io.obarch.codec.spi.QJsonSpi;
import io.obarch.codec.codegen.gen.Gen;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

public interface DecoderGenerator {

    default Map<String, Object> args(Codegen.Config cfg, QJsonSpi spi, Class clazz, Map<TypeVariable, Type> typeArgs) {
        return null;
    }

    default void genFields(Gen g, Map<String, Object> args) {
    }

    default void genInit(Gen g, Map<String, Object> args) {

    }

    void genDecode(Gen g, Map<String, Object> args, Class clazz);

    default void genDecodeProperties(Gen g, Map<String, Object> args, Class clazz) {
    }

    void fillDecoder(Map<String, Object> args);
}
