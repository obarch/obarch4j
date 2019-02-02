package org.qjson.builtin;

import org.qjson.spi.Encoder;
import org.qjson.spi.ValueObjectEncoder;

import java.util.HashMap;
import java.util.Map;

public interface BuiltinEncoders {

    static Map<Class, Encoder> $() {
        MapEncoder.init();
        return new HashMap<Class, Encoder>() {{
            put(boolean.class, new ValueObjectEncoder((sink, val) -> sink.encodeBoolean((Boolean) val)));
            put(Boolean.class, new ValueObjectEncoder((sink, val) -> sink.encodeBoolean((Boolean) val)));
            put(byte.class, new ValueObjectEncoder((sink, val) -> sink.encodeInt((Byte) val)));
            put(Byte.class, new ValueObjectEncoder((sink, val) -> sink.encodeInt((Byte) val)));
            put(short.class, new ValueObjectEncoder((sink, val) -> sink.encodeInt((Short) val)));
            put(Short.class, new ValueObjectEncoder((sink, val) -> sink.encodeInt((Short) val)));
            put(int.class, new ValueObjectEncoder((sink, val) -> sink.encodeInt((Integer) val)));
            put(Integer.class, new ValueObjectEncoder((sink, val) -> sink.encodeInt((Integer) val)));
            put(long.class, new ValueObjectEncoder((sink, val) -> sink.encodeLong((Long) val)));
            put(Long.class, new ValueObjectEncoder((sink, val) -> sink.encodeLong((Long) val)));
            put(char.class, new ValueObjectEncoder(
                    (sink, val) -> sink.encodeString(new String(new char[]{(Character) val}))));
            put(Character.class, new ValueObjectEncoder(
                    (sink, val) -> sink.encodeString(new String(new char[]{(Character) val}))));
            put(String.class, new ValueObjectEncoder((sink, val) -> sink.encodeString((String) val)));
            put(float.class, new ValueObjectEncoder((sink, val) -> sink.encodeDouble((Float) val)));
            put(Float.class, new ValueObjectEncoder((sink, val) -> sink.encodeDouble((Float) val)));
            put(double.class, new ValueObjectEncoder((sink, val) -> sink.encodeDouble((Double) val)));
            put(Double.class, new ValueObjectEncoder((sink, val) -> sink.encodeDouble((Double) val)));
            put(byte[].class, new ValueObjectEncoder((sink, val) -> sink.encodeBytes((byte[]) val)));
            put(Byte[].class, new ValueObjectEncoder((sink, val) -> {
                Byte[] boxed = (Byte[]) val;
                byte[] bytes = new byte[boxed.length];
                for (int i = 0; i < boxed.length; i++) {
                    bytes[i] = boxed[i];
                }
                sink.encodeBytes(bytes);
            }));
            put(Class.class, new ValueObjectEncoder(
                    (sink, val) -> sink.encodeString(((Class) val).getCanonicalName())));
        }};
    }

    static Encoder create(Encoder.Provider spi, Class clazz) {
        boolean isJavaUtil = BuiltinDecoders.isJavaBuiltin(clazz);
        if (Map.class.isAssignableFrom(clazz) && isJavaUtil) {
            return new MapEncoder(spi);
        }
        if (Iterable.class.isAssignableFrom(clazz) && isJavaUtil) {
            return new IterableEncoder(spi);
        }
        return null;
    }
}
