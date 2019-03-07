package io.obarch.codec.codegen;

import io.obarch.codec.encode.EncodeProperties;
import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.QJsonSpi;
import io.obarch.codec.spi.StructDescriptor;
import io.obarch.codec.codegen.gen.Gen;
import io.obarch.codec.codegen.gen.Line;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class StructEncoderGenerator implements EncoderGenerator {

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
        StructDescriptor structDescriptor = StructDescriptor.create(clazz, spi, cfg.customizeStruct);
        List<StructDescriptor.Prop> props = EncodeProperties.$(structDescriptor);
        return new HashMap<String, Object>() {{
            put("props", props);
            put("spi", spi);
        }};
    }

    @Override
    public void genFields(Gen g, Map<String, Object> args) {
        List<StructDescriptor.Prop> props = (List<StructDescriptor.Prop>) args.get("props");
        for (int i = 0; i < props.size(); i++) {
            g.__("private "
            ).__(Encoder.class.getCanonicalName()
            ).__(" encoder"
            ).__(i
            ).__(new Line(";"));
            g.__("private "
            ).__(Predicate.class.getCanonicalName()
            ).__(" shouldEncode"
            ).__(i
            ).__(new Line(";"));
        }
        g.__("private "
        ).__(Encoder.Provider.class.getCanonicalName()
        ).__(new Line(" spi;"));
    }

    @Override
    public void genInit(Gen g, Map<String, Object> args) {
        g.__("java.util.List<"
        ).__(StructDescriptor.Prop.class.getCanonicalName()
        ).__("> props = (java.util.List<"
        ).__(StructDescriptor.Prop.class.getCanonicalName()
        ).__(new Line(">)args.get(\"props\");"));
        List<StructDescriptor.Prop> props = (List<StructDescriptor.Prop>) args.get("props");
        for (int i = 0; i < props.size(); i++) {
            g.__("this.encoder").__(i).__(" = props.get(").__(i).__(new Line(").encoder;"));
            g.__("this.shouldEncode").__(i).__(" = props.get(").__(i).__(new Line(").shouldEncode;"));
        }
        g.__("this.spi = ("
        ).__(Encoder.Provider.class.getCanonicalName()
        ).__(new Line(")args.get(\"spi\");"));
    }

    @Override
    public void genEncode(Gen g, Map<String, Object> args, Class clazz) {
        if (ArrayEncoderGenerator.isPackageNull(clazz)) {
            ON_NULL_PACKAGE.accept(clazz);
            g.__(new Line("sink.write('{');"));
            g.__(new Line("sink.write('}');"));
            return;
        }
        if (ArrayEncoderGenerator.isPrivateClass(clazz)) {
            ON_PRIVATE_CLASS.accept(clazz);
            g.__(new Line("sink.write('{');"));
            g.__(new Line("sink.write('}');"));
            return;
        }
        List<StructDescriptor.Prop> props = (List<StructDescriptor.Prop>) args.get("props");
        // {
        g.__(new Line("sink.write('{');"));
        // cast to struct
        g.__(clazz.getCanonicalName()
        ).__(" obj = ("
        ).__(clazz.getCanonicalName()
        ).__(new Line(")val;"));
        // foreach properties
        g.__(new Line("int oldPath;"));
        g.__(new Line("boolean isFirst = true;"));
        for (int i = 0; i < props.size(); i++) {
            StructDescriptor.Prop prop = props.get(i);
            String expr;
            if (prop.field != null) {
                expr = "obj." + prop.field.getName();
            } else {
                expr = "obj." + prop.method.getName() + "()";
            }
            if (prop.shouldEncode != null) {
                g.__("if (this.shouldEncode").__(i).__(".test(").__(expr).__(new Line(")) {"));
            }
            g.__(new Line("if (isFirst) { isFirst = false; } else { sink.write(','); }"));
            g.__("sink.encodeString("
            ).__(asStringLiteral(prop.name)
            ).__(new Line(");"));
            g.__(new Line("sink.write(':');"));
            g.__("oldPath = sink.currentPath().enterStructField("
            ).__(asStringLiteral(prop.name)
            ).__(new Line(");"));
            if (prop.encoder == null) {
                g.__("sink.encodeValue(").__(expr).__(new Line(", spi);"));
            } else {
                g.__("sink.encodeValue(").__(expr).__(", this.encoder").__(i).__(new Line(");"));
            }
            g.__(new Line("sink.currentPath().exit(oldPath);"));
            if (prop.shouldEncode != null) {
                g.__(new Line("}"));
            }
        }
        // }
        g.__(new Line("sink.write('}');"));
    }

    @Override
    public void fillEncoder(Map<String, Object> args) {

    }

    static String asStringLiteral(String str) {
        return "\"" + str.replace("\\", "\\\\")
                .replace("\"", "\\\"") + "\"";
    }
}
