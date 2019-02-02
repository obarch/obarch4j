package io.obarch.codec.codegen;

import io.obarch.codec.PlaceholderDecoder;
import io.obarch.codec.codegen.gen.Indent;
import io.obarch.codec.codegen.gen.Line;
import io.obarch.codec.decode.DecodeProperties;
import io.obarch.codec.decode.QJsonDecodeException;
import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;
import io.obarch.codec.spi.QJsonSpi;
import io.obarch.codec.spi.StructDescriptor;
import io.obarch.codec.codegen.gen.Gen;

import java.lang.reflect.*;
import java.util.*;

public class StructDecoderGenerator implements DecoderGenerator {

    @Override
    public Map<String, Object> args(Codegen.Config cfg, QJsonSpi spi, Class clazz, Map<TypeVariable, Type> typeArgs) {
        StructDescriptor structDescriptor = StructDescriptor.create(clazz, spi, cfg.customizeStruct);
        List<StructDescriptor.Prop> props = DecodeProperties.$(structDescriptor, spi);
        return new HashMap<String, Object>() {{
            put("props", props);
        }};
    }

    @Override
    public void genFields(Gen g, Map<String, Object> args) {
        List<StructDescriptor.Prop> props = (List<StructDescriptor.Prop>) args.get("props");
        for (int i = 0; i < props.size(); i++) {
            g.__("private "
            ).__(Decoder.class.getCanonicalName()
            ).__(" decoder"
            ).__(i
            ).__(new Line(";"));
        }
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
            g.__("this.decoder").__(i).__(" = props.get(").__(i).__(new Line(").decoder;"));
        }
    }

    @Override
    public void genDecode(Gen g, Map<String, Object> args, Class clazz) {
        try {
            clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new QJsonDecodeException(clazz + " does not have default constructor, " +
                    "need to use config to specify decoder manually");
        }
        g.__("return new "
        ).__(clazz.getCanonicalName()
        ).__(new Line("();"));
    }

    @Override
    public void genDecodeProperties(Gen g, Map<String, Object> args, Class clazz) {
        List<StructDescriptor.Prop> props = (List<StructDescriptor.Prop>) args.get("props");
        g.__(Helper.class.getCanonicalName()).__(new Line(".expectMapHead(source);"));
        // if object is {}
        g.__(new Line("byte b = source.peek();"));
        g.__("if (b == '}') { "
        ).__(new Indent(() -> {
            g.__(new Line("source.next();"));
            g.__(new Line("return;"));
        })).__(new Line("}"));

        g.__(new Line("int oldPath;"));
        g.__(clazz.getCanonicalName()
        ).__(" struct = ("
        ).__(clazz.getCanonicalName()
        ).__(new Line(")obj;"));
        g.__("do {"
        ).__(new Indent(() -> {
            g.__(new Line("String field = source.decodeString();"));
            g.__(new Line("if (source.read() != ':') { throw source.reportError(\"expect :\"); }"));
            g.__("switch (field) {"
            ).__(new Indent(() -> {
                for (int i = 0; i < props.size(); i++) {
                    StructDescriptor.Prop prop = props.get(i);
                    g.__("case "
                    ).__(StructEncoderGenerator.asStringLiteral(prop.name)
                    ).__(": "
                    ).__(propCase(g, i, prop));
                }
                g.__(new Line("default: source.skip(); "));
            })).__(new Line("}"));
        })).__(new Line("} while ((b = source.read()) == ',');"));
        g.__(new Line("if (b != '}') { throw source.reportError(\"expect }\"); }"));
    }

    @Override
    public void fillDecoder(Map<String, Object> args) {
        List<StructDescriptor.Prop> props = (List<StructDescriptor.Prop>) args.get("props");
        for (StructDescriptor.Prop prop : props) {
            if (prop.decoder instanceof PlaceholderDecoder) {
                prop.decoder = ((PlaceholderDecoder) prop.decoder).lookup();
            }
        }
    }

    public Indent propCase(Gen g, int i, StructDescriptor.Prop prop) {
        return new Indent(() -> {
            g.__("oldPath = source.currentPath().enterStructField("
            ).__(StructEncoderGenerator.asStringLiteral(prop.name)
            ).__(new Line(");"));
            if (prop.field != null) {
                setPropertyByField(g, i, prop);
            } else {
                setPropertyBySetter(g, i, prop);
            }
            g.__(new Line("source.currentPath().exit(oldPath);"));
            g.__(new Line("break;"));
        });
    }

    private static void setPropertyBySetter(Gen g, int i, StructDescriptor.Prop prop) {
        g.__("struct."
        ).__(prop.method.getName()
        ).__("(("
        ).__(prop.method.getParameterTypes()[0].getCanonicalName()
        ).__(")source.decodeObject(decoder"
        ).__(i
        ).__(new Line("));"));
    }

    private static void setPropertyByField(Gen g, int i, StructDescriptor.Prop prop) {
        g.__("struct."
        ).__(prop.field.getName()
        ).__(" = ("
        ).__(prop.field.getType().getCanonicalName()
        ).__(")source.decodeObject(decoder"
        ).__(i
        ).__(new Line(");"));
    }

    public interface Helper {

        static void expectMapHead(DecoderSource source) {
            byte b = source.peek();
            if (b != '{') {
                throw source.reportError("expect {");
            }
            source.next();
        }
    }
}
