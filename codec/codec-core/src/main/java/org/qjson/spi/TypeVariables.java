package org.qjson.spi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Map;

public interface TypeVariables {
    
    static Class collect(Type type, Map<TypeVariable, Type> collector) {
        if (Object.class.equals(type)) {
            return null;
        }
        if (type instanceof Class) {
            Class clazz = (Class) type;
            for (Type inf : clazz.getGenericInterfaces()) {
                TypeVariables.collect(inf, collector);
            }
            TypeVariables.collect(clazz.getGenericSuperclass(), collector);
            return clazz;
        }
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] theTypeArgs = parameterizedType.getActualTypeArguments();
        Type rawType = parameterizedType.getRawType();
        if (!(rawType instanceof Class)) {
            return null;
        }
        Class clazz = (Class) rawType;
        TypeVariable[] theTypeParams = clazz.getTypeParameters();
        if (theTypeArgs.length == theTypeParams.length) {
            for (int i = 0; i < theTypeParams.length; i++) {
                TypeVariable theTypeParam = theTypeParams[i];
                Type theTypeArg = theTypeArgs[i];
                // if the type arg is also a type param, try substitute
                collector.put(theTypeParam, collector.getOrDefault(theTypeArg, theTypeArg));
            }
        }
        for (Type inf : clazz.getGenericInterfaces()) {
            TypeVariables.collect(inf, collector);
        }
        TypeVariables.collect(clazz.getGenericSuperclass(), collector);
        return clazz;
    }

    static Type substitute(TypeVariable typeParam, Map<TypeVariable, Type> typeArgs) {
        Type sub = typeArgs.get(typeParam);
        if (sub instanceof Class || sub instanceof ParameterizedType) {
            return sub;
        }
        if (sub == null) {
            sub = typeParam;
        }
        if (sub instanceof TypeVariable) {
            return ((TypeVariable) sub).getBounds()[0];
        }
        if (sub instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) sub;
            if (wildcardType.getLowerBounds().length > 0) {
                return wildcardType.getLowerBounds()[0];
            }
            if (wildcardType.getUpperBounds().length > 0) {
                return wildcardType.getUpperBounds()[0];
            }
        }
        return Object.class;
    }
}
