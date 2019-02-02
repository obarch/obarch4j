package org.qjson.builtin;

import org.qjson.decode.BytesDecoderSource;
import org.qjson.encode.CurrentPath;
import org.qjson.spi.Decoder;
import org.qjson.spi.DecoderSource;
import org.qjson.spi.QJsonSpi;
import org.qjson.spi.TypeVariables;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.function.Function;

class MapDecoder implements Decoder {

    private final Function<DecoderSource, Object> mapFactory;
    private final Decoder keyDecoder;
    private final Decoder valueDecoder;

    public MapDecoder(Function<DecoderSource, Object> mapFactory, Decoder keyDecoder, Decoder valueDecoder) {
        this.mapFactory = mapFactory;
        this.keyDecoder = keyDecoder;
        this.valueDecoder = valueDecoder;
    }

    static MapDecoder create(QJsonSpi spi, Class clazz, Map<TypeVariable, Type> typeArgs) {
        Function<DecoderSource, Object> mapFactory = spi.factoryOf(clazz);
        Decoder keyDecoder = getKeyDecoder(spi, typeArgs);
        Decoder valueDecoder = getValueDecoder(spi, typeArgs);
        return new MapDecoder(mapFactory, keyDecoder, valueDecoder);
    }

    private static Decoder getKeyDecoder(Decoder.Provider spi, Map<TypeVariable, Type> typeArgs) {
        TypeVariable typeParam = Map.class.getTypeParameters()[0];
        Type keyType = TypeVariables.substitute(typeParam, typeArgs);
        if (Object.class.equals(keyType)) {
            keyType = String.class;
        }
        Decoder keyDecoder = spi.decoderOf(keyType);
        if (MapEncoder.VALID_KEY_CLASSES.contains(keyType)) {
            return keyDecoder;
        }
        return source -> {
            byte[] bytes = source.decodeBytes();
            BytesDecoderSource newSource = new BytesDecoderSource(bytes, 0, bytes.length);
            return newSource.decodeObject(keyDecoder);
        };
    }

    private static Decoder getValueDecoder(Decoder.Provider spi, Map<TypeVariable, Type> typeArgs) {
        TypeVariable typeParam = Map.class.getTypeParameters()[1];
        Type valueType = TypeVariables.substitute(typeParam, typeArgs);
        Decoder valueDecoder = spi.decoderOf(valueType);
        return valueDecoder;
    }

    @Override
    public Object decode(DecoderSource source) {
        return mapFactory.apply(source);
    }

    @Override
    public void decodeProperties(DecoderSource source, Object obj) {
        byte b = source.peek();
        if (b != '{') {
            throw source.reportError("expect {");
        }
        source.next();
        // if map is {}
        b = source.peek();
        if (b == '}') {
            source.next();
            return;
        }
        Map map = (Map) obj;
        CurrentPath currentPath = source.currentPath();
        do {
            int mark = source.mark();
            Object key = source.decodeObject(keyDecoder, false);
            String encodedKey = source.sinceMark(mark);
            if (source.read() != ':') {
                throw source.reportError("expect :");
            }
            int oldPath = currentPath.enterMapValue(encodedKey);
            Object value = source.decodeObject(valueDecoder);
            currentPath.exit(oldPath);
            map.put(key, value);
        } while ((b = source.read()) == ',');
        if (b != '}') {
            throw source.reportError("expect }");
        }
    }
}
