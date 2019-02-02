package org.qjson.encode;

import org.qjson.spi.StructDescriptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

public interface EncodeProperties {

    static List<StructDescriptor.Prop> $(StructDescriptor struct) {
        Map<String, StructDescriptor.Prop> props = new HashMap<>();
        for (StructDescriptor.Prop field : struct.fields.values()) {
            if (field.name.isEmpty()) {
                field.name = field.field.getName();
            }
            props.put(field.name, field);
        }
        for (List<StructDescriptor.Prop> methods : struct.methods.values()) {
            for (StructDescriptor.Prop method : methods) {
                String propName = getterPropName(method.method);
                if (propName == null) {
                    continue;
                }
                if (method.name.isEmpty()) {
                    method.name = propName;
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

    static String getterPropName(Method method) {
        String methodName = method.getName();
        if (method.getReturnType().equals(Void.TYPE)) {
            return null;
        }
        if (method.getParameterCount() != 0) {
            return null;
        }
        if (methodName.startsWith("get")) {
            return decapitalize(methodName.substring(3));
        }
        try {
            Field field = method.getDeclaringClass().getDeclaredField(methodName);
            if (field.getType().equals(method.getReturnType())) {
                return methodName;
            }
        } catch (NoSuchFieldException e) {
            return null;
        }
        return null;
    }

    /**
     * Utility method to take a string and convert it to normal Java variable
     * name capitalization.  This normally means converting the first
     * character from upper case to lower case, but in the (unusual) special
     * case when there is more than one character and both the first and
     * second characters are upper case, we leave it alone.
     * <p>
     * Thus "FooBah" becomes "fooBah" and "X" becomes "x", but "URL" stays
     * as "URL".
     *
     * @param name The string to be decapitalized.
     * @return The decapitalized version of the string.
     */
    static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
