package io.obarch.codec.builtin;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;
import io.obarch.codec.spi.QJsonSpi;
import io.obarch.codec.spi.TypeVariables;
import io.obarch.codec.encode.CurrentPath;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

class CollectionDecoder implements Decoder {

    private final Function<DecoderSource, Object> colFactory;
    private final Decoder elemDecoder;

    public CollectionDecoder(Function<DecoderSource, Object> colFactory, Decoder elemDecoder) {
        this.colFactory = colFactory;
        this.elemDecoder = elemDecoder;
    }

    public static Decoder create(QJsonSpi spi, Class clazz, Map<TypeVariable, Type> typeArgs) {
        Function<DecoderSource, Object> colFactory = spi.factoryOf(clazz);
        TypeVariable typeParam = Collection.class.getTypeParameters()[0];
        Type elemType = TypeVariables.substitute(typeParam, typeArgs);
        Decoder elemDecoder = spi.decoderOf(elemType);
        return new CollectionDecoder(colFactory, elemDecoder);
    }

    @Override
    public Object decode(DecoderSource source) {
        return colFactory.apply(source);
    }

    @Override
    public void decodeProperties(DecoderSource source, Object obj) {
        byte b = source.peek();
        if (b != '[') {
            throw source.reportError("expect [");
        }
        source.next();
        if (source.peek() == ']') {
            source.next();
            return;
        }
        Collection col = (Collection) obj;
        int i = 0;
        do {
            CurrentPath currentPath = source.currentPath();
            int oldPath = currentPath.enterListElement(i);
            col.add(source.decodeValue(elemDecoder));
            currentPath.exit(oldPath);
            i++;
        } while ((b = source.read()) == ',');
        if (b != ']') {
            throw source.reportError("expect ]");
        }
    }
}
