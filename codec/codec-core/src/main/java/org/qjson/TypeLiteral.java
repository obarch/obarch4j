package org.qjson;

import org.qjson.decode.QJsonDecodeException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeLiteral<T> {
    public Type $() {
        if (TypeLiteral.class.equals(getClass())) {
            throw new QJsonDecodeException("should specify type like this: new TypeLiteral<List<String>>(){}");
        }
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type type = parameterizedType.getActualTypeArguments()[0];
        return type;
    }
}
