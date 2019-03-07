package io.obarch.codec.codegen;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;
import io.obarch.codec.spi.QJsonSpi;
import io.obarch.codec.codegen.gen.Gen;
import io.obarch.codec.codegen.gen.Indent;
import io.obarch.codec.codegen.gen.Line;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class ArrayDecoderGenerator implements DecoderGenerator {

    @Override
    public Map<String, Object> args(Codegen.Config cfg, QJsonSpi spi, Class clazz, Map<TypeVariable, Type> typeArgs) {
        Decoder elemDecoder = spi.decoderOf(clazz.getComponentType());
        return new HashMap<String, Object>() {{
            put("elemDecoder", elemDecoder);
        }};
    }

    @Override
    public void genFields(Gen g, Map<String, Object> args) {
        g.__("private "
        ).__(Decoder.class.getCanonicalName()
        ).__(new Line(" elemDecoder;"));
    }

    @Override
    public void genInit(Gen g, Map<String, Object> args) {
        g.__("this.elemDecoder = ("
        ).__(Decoder.class.getCanonicalName()
        ).__(new Line(")args.get(\"elemDecoder\");"));
    }

    @Override
    public void genDecode(Gen g, Map<String, Object> args, Class clazz) {
        g.__(Helper.class.getCanonicalName()).__(new Line(".expectArrayHead(source);"));
        // if array is []
        g.__(new Line("byte b = source.peek();"));
        g.__("if (b == ']') { "
        ).__(new Indent(() -> {
            g.__(new Line("source.next();"));
            g.__("return new "
            ).__(clazz.getCanonicalName()
            ).__("{};");
        })).__(new Line("}"));
        // init arr
        if (clazz.getComponentType().isPrimitive()) {
            g.__(clazz.getCanonicalName()
            ).__(" arr = new "
            ).__(clazz.getComponentType().getCanonicalName()
            ).__(new Line("[4];"));
        } else {
            g.__(clazz.getCanonicalName()
            ).__(" arr = new "
            ).__(clazz.getCanonicalName()
            ).__(new Line("{null,null,null,null};"));
        }
        // loop to read
        g.__(new Line("int i = 0;"));
        g.__("do {"
        ).__(new Indent(() -> {
            g.__(new Line("int oldPath = source.currentPath().enterListElement(i);"));
            g.__("arr[i++] = ("
            ).__(clazz.getComponentType().getCanonicalName()
            ).__(new Line(")source.decodeValue(elemDecoder);"));
            g.__(new Line("if (i == arr.length) { arr = java.util.Arrays.copyOf(arr, arr.length * 2); }"));
            g.__(new Line("source.currentPath().exit(oldPath);"));
        })).__(new Line("} while((b = source.read()) == ',');"));
        g.__(Helper.class.getCanonicalName()).__(new Line(".expectArrayTail(source, b);"));
        g.__(new Line("return java.util.Arrays.copyOf(arr, i);"));
    }

    @Override
    public void fillDecoder(Map<String, Object> args) {

    }

    public interface Helper {

        static void expectArrayHead(DecoderSource source) {
            byte b = source.peek();
            if (b != '[') {
                throw source.reportError("expect [");
            }
            source.next();
        }

        static void expectArrayTail(DecoderSource source, byte b) {
            if (b != ']') {
                throw source.reportError("expect ]");
            }
        }
    }
}
