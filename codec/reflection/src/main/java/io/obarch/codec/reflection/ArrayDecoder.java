package io.obarch.codec.reflection;

import io.obarch.codec.encode.CurrentPath;
import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;

import java.lang.reflect.Array;

public class ArrayDecoder implements Decoder {

    private final Class elemType;
    private final Decoder elemDecoder;

    private ArrayDecoder(Class elemType, Decoder elemDecoder) {
        this.elemType = elemType;
        this.elemDecoder = elemDecoder;
    }

    public static ArrayDecoder create(Decoder.Provider spi, Class clazz) {
        Class elemType = clazz.getComponentType();
        return new ArrayDecoder(elemType, spi.decoderOf(elemType));
    }

    @Override
    public Object decode(DecoderSource source) {
        if (source.peek() != '[') {
            throw source.reportError("expect [");
        }
        source.next();
        if (source.peek() == ']') {
            source.next();
            return Array.newInstance(elemType, 0);
        }
        byte b;
        Object arr = Array.newInstance(elemType, 4);
        CurrentPath currentPath = source.currentPath();
        int i = 0;
        int length = 4;
        do {
            int oldPath = currentPath.enterListElement(i);
            Array.set(arr, i++, source.decodeValue(elemDecoder));
            currentPath.exit(oldPath);
            if (i == length) {
                arr = grow(arr, length, length * 2);
                length = length * 2;
            }
        } while ((b = source.read()) == ',');
        if (b != ']') {
            throw source.reportError("expect ]");
        }
        return shrink(arr, length, i);
    }

    private final Object grow(Object oldArr, int oldLength, int length) {
        Object newArr = Array.newInstance(elemType, length);
        for (int i = 0; i < oldLength; i++) {
            Array.set(newArr, i, Array.get(oldArr, i));
        }
        return newArr;
    }

    private final Object shrink(Object oldArr, int oldLength, int length) {
        if (oldLength == length) {
            return oldArr;
        }
        Object newArr = Array.newInstance(elemType, length);
        for (int i = 0; i < length; i++) {
            Array.set(newArr, i, Array.get(oldArr, i));
        }
        return newArr;
    }
}
