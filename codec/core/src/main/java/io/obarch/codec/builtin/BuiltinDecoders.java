package io.obarch.codec.builtin;

import io.obarch.codec.any.Any;
import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;
import io.obarch.codec.spi.QJsonSpi;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.time.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface BuiltinDecoders {

    static Map<Type, Decoder> $() {
        return new HashMap<Type, Decoder>() {{
            put(boolean.class, source -> source.decodeBoolean());
            put(Boolean.class, DecoderSource::decodeBoolean);
            put(byte.class, source -> (byte) source.decodeInt());
            put(Byte.class, source -> (byte) source.decodeInt());
            put(short.class, source -> (short) source.decodeInt());
            put(Short.class, source -> (short) source.decodeInt());
            put(int.class, source -> source.decodeInt());
            put(Integer.class, DecoderSource::decodeInt);
            put(long.class, source -> source.decodeLong());
            put(Long.class, DecoderSource::decodeLong);
            put(float.class, source -> (float) source.decodeDouble());
            put(Float.class, source -> (float) source.decodeDouble());
            put(double.class, source -> source.decodeDouble());
            put(Double.class, DecoderSource::decodeDouble);
            put(String.class, DecoderSource::decodeString);
            put(byte[].class, DecoderSource::decodeBytes);
            put(Byte[].class, source -> {
                byte[] bytes = source.decodeBytes();
                Byte[] boxed = new Byte[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    boxed[i] = bytes[i];
                }
                return boxed;
            });
            put(Date.class, source -> new Date(source.decodeLong()));
            put(Year.class, source -> Year.of(source.decodeInt()));
        }};
    }

    static Decoder create(QJsonSpi spi, Class clazz, Map<TypeVariable, Type> typeArgs) {
        if (Any.class.equals(clazz)) {
            return new AnyDecoder(spi.decoderOf(Object.class));
        }
        boolean isJavaUtil = isJavaBuiltin(clazz);
        if (Collection.class.isAssignableFrom(clazz) && isJavaUtil) {
            return CollectionDecoder.create(spi, clazz, typeArgs);
        }
        if (Map.class.isAssignableFrom(clazz) && isJavaUtil) {
            return MapDecoder.create(spi, clazz, typeArgs);
        }
        return null;
    }

    static boolean isJavaBuiltin(Class clazz) {
        if (clazz == null) {
            return false;
        }
        if (Object.class.equals(clazz)) {
            return false;
        }
        if (clazz.getName().startsWith("java.util.") || clazz.getName().startsWith("java.lang.")) {
            return true;
        }
        return isJavaBuiltin(clazz.getSuperclass());
    }
}
