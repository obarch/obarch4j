package io.obarch.codec.reflection;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.QJsonSpi;
import io.obarch.codec.spi.StructDescriptor;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.function.BiConsumer;

public class Reflection {

    private final QJsonSpi spi;
    private BiConsumer<QJsonSpi, StructDescriptor> customizeStruct;

    public Reflection(QJsonSpi spi, BiConsumer<QJsonSpi, StructDescriptor> customizeStruct) {
        this.spi = spi;
        this.customizeStruct = customizeStruct;
    }

    public Encoder generateEncoder(Class clazz) {
        if (clazz.isArray()) {
            return new ArrayEncoder(spi);
        }
        return StructEncoder.create(clazz, spi, customizeStruct);
    }

    public Decoder generateDecoder(Class clazz, Map<TypeVariable, Type> typeArgs) {
        if (clazz.isArray()) {
            return ArrayDecoder.create(spi, clazz);
        }
        return StructDecoder.create(clazz, spi, customizeStruct);
    }
}
