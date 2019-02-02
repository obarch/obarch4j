package io.obarch.codec.decode;

import io.obarch.codec.encode.EncodeProperties;
import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.StructDescriptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;

public interface DecodeProperties {

    static List<StructDescriptor.Prop> $(StructDescriptor struct, Decoder.Provider spi) {
        Map<String, StructDescriptor.Prop> props = new HashMap<>();
        for (StructDescriptor.Prop field : struct.fields.values()) {
            if (Modifier.isFinal(field.field.getModifiers())) {
                continue;
            }
            if (field.name.isEmpty()) {
                field.name = field.field.getName();
            }
            if (field.encoder == null) {
                field.decoder = spi.decoderOf(field.field.getGenericType());
            }
            props.put(field.name, field);
        }
        for (List<StructDescriptor.Prop> methods : struct.methods.values()) {
            for (StructDescriptor.Prop method : methods) {
                String propName = setterPropName(method.method);
                if (propName == null) {
                    continue;
                }
                if (method.name.isEmpty()) {
                    method.name = propName;
                }
                if (method.encoder == null) {
                    method.decoder = spi.decoderOf(method.method.getGenericParameterTypes()[0]);
                }
                props.put(method.name, method);
            }
        }
        Function<List<StructDescriptor.Prop>, List<StructDescriptor.Prop>> sortProperties = struct.sortProperties;
        if (sortProperties == null) {
            sortProperties = properties -> {
                Collections.sort(properties, Comparator.comparing(o -> o.name));
                return properties;
            };
        }
        return sortProperties.apply(new ArrayList<>(props.values()));
    }

    static String setterPropName(Method method) {
        String methodName = method.getName();
        if (method.getParameterCount() != 1) {
            return null;
        }
        if (methodName.startsWith("set")) {
            return EncodeProperties.decapitalize(methodName.substring(3));
        }
        try {
            Field field = method.getDeclaringClass().getDeclaredField(methodName);
            if (field.getType().equals(method.getParameterTypes()[0])) {
                return methodName;
            }
        } catch (NoSuchFieldException e) {
            return null;
        }
        return null;
    }
}
