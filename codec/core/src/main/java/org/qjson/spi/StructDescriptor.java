package org.qjson.spi;

import java.beans.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class StructDescriptor {

    public final Class clazz;
    public final Map<String, Prop> fields = new HashMap<>();
    public final Map<String, List<Prop>> methods = new HashMap<>();
    // sort the properties in order to encode
    public Function<List<Prop>, List<Prop>> sortProperties;

    private StructDescriptor(Class clazz) {
        this.clazz = clazz;
        for (Field field : clazz.getFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (Object.class.equals(field.getDeclaringClass())) {
                continue;
            }
            Prop member = new Prop(field);
            if (Modifier.isTransient(field.getModifiers()) && member.getAnnotation(QJsonProperty.class) == null) {
                member.setAnnotation(QJsonProperty.class, new QJsonProperty.Ignore());
            }
            fields.put(field.getName(), member);
        }
        for (Method method : clazz.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            if (Object.class.equals(method.getDeclaringClass())) {
                continue;
            }
            Prop member = new Prop(method);
            if (member.getAnnotation(Transient.class) != null && member.getAnnotation(QJsonProperty.class) == null) {
                Transient transientAnnotation = member.getAnnotation(Transient.class);
                if (transientAnnotation.value()) {
                    member.setAnnotation(QJsonProperty.class, new QJsonProperty.Ignore());
                }
            }
            methods.computeIfAbsent(method.getName(), k -> new ArrayList<>()).add(member);
        }
    }

    public static StructDescriptor create(Class clazz) {
        return create(clazz, null, null);
    }

    public static StructDescriptor create(
            Class clazz, QJsonSpi spi,
            BiConsumer<QJsonSpi, StructDescriptor> customizeStruct) {
        StructDescriptor struct = new StructDescriptor(clazz);
        if (customizeStruct != null) {
            customizeStruct.accept(spi, struct);
        }
        for (Prop value : struct.fields.values()) {
            moveProperty(value);
        }
        for (List<Prop> values : struct.methods.values()) {
            for (Prop value : values) {
                moveProperty(value);
            }
        }
        return struct;
    }

    private static void moveProperty(Prop prop) {
        QJsonProperty annotation = prop.getAnnotation(QJsonProperty.class);
        if (annotation == null) {
            if (prop.name == null) {
                prop.name = "";
            }
            if (prop.ignore == null) {
                prop.ignore = false;
            }
            return;
        }
        if (prop.ignore == null) {
            prop.ignore = annotation.ignore();
        }
        if (prop.name == null) {
            prop.name = annotation.value();
        }
        if (prop.encoder == null) {
            prop.encoder = newObject(annotation.encoder());
        }
        if (prop.decoder == null) {
            prop.decoder = newObject(annotation.decoder());
        }
        if (prop.shouldEncode == null) {
            prop.shouldEncode = newObject(annotation.shouldEncode());
        }
    }

    private static <T> T newObject(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        if (clazz.isInterface()) {
            return null;
        }
        try {
            return clazz.getConstructor().newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("failed to instantiate: " + clazz, e);
        }
    }

    // property can be customized by @QJsonProperty
    // or can be modified directly via StructDescriptor
    public static class Prop {

        public final Field field;
        public final Method method;
        private final Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        public Encoder encoder;
        public Decoder decoder;
        public Predicate<Object> shouldEncode;
        public String name;
        public Boolean ignore;

        public Prop(Field field) {
            this.field = field;
            this.method = null;
        }

        public Prop(Method method) {
            this.method = method;
            this.field = null;
        }

        public <T extends Annotation> T getAnnotation(Class<T> clazz) {
            Object annotation = annotations.get(clazz);
            if (annotation != null) {
                return (T) annotation;
            }
            if (field != null) {
                return field.getAnnotation(clazz);
            }
            return method.getAnnotation(clazz);
        }

        public <T extends Annotation, A extends T> void setAnnotation(Class<T> clazz, A annotation) {
            annotations.put(clazz, annotation);
        }
    }
}
