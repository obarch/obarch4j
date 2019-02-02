package io.obarch.codec.codegen;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.QJsonSpi;
import io.obarch.codec.codegen.gen.Gen;
import io.obarch.codec.codegen.gen.Indent;
import io.obarch.codec.codegen.gen.Line;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

class ArrayEncoderGenerator implements EncoderGenerator {

    public static Consumer<Class> ON_PRIVATE_CLASS = clazz -> {
        if ("true".equals(System.getenv("QJSON_DEBUG"))) {
            System.err.println(clazz + " is private, need to use config to specify encoder manually");
        }
    };
    public static Consumer<Class> ON_NULL_PACKAGE = clazz -> {
        if ("true".equals(System.getenv("QJSON_DEBUG"))) {
            System.err.println(clazz + " package is null, need to use config to specify encoder manually");
        }
    };

    @Override
    public Map<String, Object> args(Codegen.Config cfg, QJsonSpi spi, Class clazz) {
        return new HashMap<String, Object>() {{
            put("spi", spi);
        }};
    }

    @Override
    public void genFields(Gen g, Map<String, Object> args) {
        g.__("private "
        ).__(Encoder.Provider.class.getCanonicalName()
        ).__(new Line(" spi;"));
    }

    @Override
    public void genInit(Gen g, Map<String, Object> args) {
        g.__("this.spi = ("
        ).__(Encoder.Provider.class.getCanonicalName()
        ).__(new Line(")args.get(\"spi\");"));
    }

    @Override
    public void genEncode(Gen g, Map<String, Object> args, Class clazz) {
        if (isPackageNull(clazz.getComponentType())) {
            ON_NULL_PACKAGE.accept(clazz.getComponentType());
            g.__(new Line("sink.write('[');"));
            g.__(new Line("sink.write(']');"));
            return;
        }
        if (isPrivateClass(clazz.getComponentType())) {
            ON_PRIVATE_CLASS.accept(clazz.getComponentType());
            g.__(new Line("sink.write('[');"));
            g.__(new Line("sink.write(']');"));
            return;
        }
        // [
        g.__(new Line("sink.write('[');"));
        // cast to array
        g.__(clazz.getCanonicalName()
        ).__(" arr = ("
        ).__(clazz.getCanonicalName()
        ).__(new Line(")val;"));
        // foreach
        g.__("for (int i = 0; i < arr.length; i++) {"
        ).__(new Indent(() -> {
                    g.__(new Line("if (i > 0) { sink.write(','); }"));
                    g.__(new Line("int oldPath = sink.currentPath().enterListElement(i);"));
                    g.__(new Line("sink.encodeObject(arr[i], spi);"));
                    g.__(new Line("sink.currentPath().exit(oldPath);"));
                })
        ).__(new Line("}"));
        // ]
        g.__(new Line("sink.write(']');"));
    }

    @Override
    public void fillEncoder(Map<String, Object> args) {

    }

    public static boolean isPrivateClass(Class clazz) {
        if (clazz.isArray()) {
            return isPrivateClass(clazz.getComponentType());
        }
        if (clazz.isPrimitive()) {
            return false;
        }
        return !Modifier.isPublic(clazz.getModifiers()) || clazz.getName().startsWith("sun.");
    }

    public static boolean isPackageNull(Class clazz) {
        if (clazz.isArray()) {
            return isPackageNull(clazz.getComponentType());
        }
        if (clazz.isPrimitive()) {
            return false;
        }
        return clazz.getPackage() == null;
    }
}
