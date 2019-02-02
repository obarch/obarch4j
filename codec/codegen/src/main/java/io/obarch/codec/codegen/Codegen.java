package io.obarch.codec.codegen;

import io.obarch.codec.PlaceholderDecoder;
import io.obarch.codec.PlaceholderEncoder;
import io.obarch.codec.codegen.gen.Gen;
import io.obarch.codec.codegen.gen.Indent;
import io.obarch.codec.codegen.gen.Line;
import io.obarch.codec.decode.QJsonDecodeException;
import io.obarch.codec.encode.QJsonEncodeException;
import io.obarch.codec.spi.*;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Codegen {

    private final Map<Class, Encoder> encoderCache = new ConcurrentHashMap<>();
    private final Map<Type, Decoder> decoderCache = new ConcurrentHashMap<>();
    private final Config cfg;
    private final QJsonSpi spi;
    private int counter;

    public static class Config {
        public InMemoryJavaCompiler compiler;
        public Function<Class, Class> chooseImpl;
        public BiConsumer<QJsonSpi, StructDescriptor> customizeStruct;
    }

    public Codegen(Config cfg, QJsonSpi spi) {
        this.cfg = cfg;
        this.spi = spi;
    }

    public Decoder generateDecoder(Type type) {
        Decoder decoder = decoderCache.get(type);
        if (decoder != null) {
            return decoder;
        }
        // placeholder to avoid infinite loop
        decoderCache.put(type, new PlaceholderDecoder(this::generateDecoder, type));
        decoder = _generateDecoder(type);
        decoderCache.put(type, decoder);
        return decoder;
    }

    private synchronized Decoder _generateDecoder(Type type) {
        Map<TypeVariable, Type> typeArgs = new HashMap<>();
        Class clazz = TypeVariables.collect(type, typeArgs);
        DecoderGenerator generator = getDecoderGenerator(clazz);
        Map<String, Object> args = generator.args(cfg, spi, clazz, typeArgs);
        String decoderClassName = "GeneratedDecoder" + (counter++);
        Gen g = new Gen();
        g.__(new Line("package gen;"));
        g.__("public class "
        ).__(decoderClassName
        ).__(" implements "
        ).__(Decoder.class.getCanonicalName()
        ).__(" {"
        ).__(new Indent(() -> {
            // fields
            generator.genFields(g, args);
            // method init
            g.__("public void init(java.util.Map<String, Object> args) {"
            ).__(new Indent(() -> {
                generator.genInit(g, args);
            })).__(new Line("}"));
            // method decode
            g.__("public Object decode("
            ).__(DecoderSource.class.getCanonicalName()
            ).__(" source) {"
            ).__(new Indent(() -> {
                g.__("try {"
                ).__(new Indent(() -> {
                    generator.genDecode(g, args, clazz);
                })).__("} catch (RuntimeException e) {"
                ).__(new Indent(() -> {
                    g.__("throw e;");
                })).__("} catch (Exception e) {"
                ).__(new Indent(() -> {
                    g.__("throw new "
                    ).__(QJsonDecodeException.class.getCanonicalName()
                    ).__("(e);");
                })).__(new Line("}"));
            })).__(new Line("}"));
            // method decodeProperties
            g.__("public void decodeProperties("
            ).__(DecoderSource.class.getCanonicalName()
            ).__(" source, Object obj) {"
            ).__(new Indent(() -> {
                g.__("try {"
                ).__(new Indent(() -> {
                    generator.genDecodeProperties(g, args, clazz);
                })).__("} catch (RuntimeException e) {"
                ).__(new Indent(() -> {
                    g.__("throw e;");
                })).__("} catch (Exception e) {"
                ).__(new Indent(() -> {
                    g.__("throw new "
                    ).__(QJsonDecodeException.class.getCanonicalName()
                    ).__("(e);");
                })).__(new Line("}"));
            })).__(new Line("}"));
        })).__(new Line("}"));
        String src = g.toString();
        try {
            printSourceCode(clazz, src);
            Class<?> decoderClass = cfg.compiler.compile("gen." + decoderClassName, src);
            Constructor<?> ctor = decoderClass.getConstructor();
            Decoder decoder = (Decoder) ctor.newInstance();
            decoderCache.put(clazz, decoder);
            generator.fillDecoder(args);
            Method initMethod = decoderClass.getMethod("init", Map.class);
            initMethod.invoke(decoder, args);
            return decoder;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new QJsonDecodeException(e);
        }
    }

    public Encoder generateEncoder(Class clazz) {
        Encoder encoder = encoderCache.get(clazz);
        if (encoder != null) {
            return encoder;
        }
        // placeholder to avoid infinite loop
        encoderCache.put(clazz, new PlaceholderEncoder(this::generateEncoder, clazz));
        encoder = _generateEncoder(clazz);
        encoderCache.put(clazz, encoder);
        return encoder;
    }

    private synchronized Encoder _generateEncoder(Class clazz) {
        EncoderGenerator generator = getEncoderGenerator(clazz);
        Map<String, Object> args = generator.args(cfg, spi, clazz);
        String encoderClassName = "GeneratedEncoder" + (counter++);
        Gen g = new Gen();
        g.__(new Line("package gen;"));
        g.__("public class "
        ).__(encoderClassName
        ).__(" implements "
        ).__(Encoder.class.getCanonicalName()
        ).__(" {"
        ).__(new Indent(() -> {
            // fields
            generator.genFields(g, args);
            // method init
            g.__("public void init(java.util.Map<String, Object> args) {"
            ).__(new Indent(() -> {
                generator.genInit(g, args);
            })).__(new Line("}"));
            // encode method
            g.__("public void encode("
            ).__(EncoderSink.class.getCanonicalName()
            ).__(" sink, Object val) {"
            ).__(new Indent(() -> {
                g.__("try {"
                ).__(new Indent(() -> {
                    generator.genEncode(g, args, clazz);
                })).__("} catch (RuntimeException e) {"
                ).__(new Indent(() -> {
                    g.__("throw e;");
                })).__("} catch (Exception e) {"
                ).__(new Indent(() -> {
                    g.__("throw new "
                    ).__(QJsonEncodeException.class.getCanonicalName()
                    ).__("(e);");
                })).__(new Line("}"));
            })).__(new Line("}"));
        })).__(new Line("}"));
        String src = g.toString();
        try {
            printSourceCode(clazz, src);
            Class<?> encoderClass = cfg.compiler.compile("gen." + encoderClassName, src);
            Constructor<?> ctor = encoderClass.getConstructor();
            Encoder encoder = (Encoder) ctor.newInstance();
            encoderCache.put(clazz, encoder);
            generator.fillEncoder(args);
            Method initMethod = encoderClass.getMethod("init", Map.class);
            initMethod.invoke(encoder, args);
            return encoder;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new QJsonEncodeException(e);
        }
    }

    private static DecoderGenerator getDecoderGenerator(Class clazz) {
        if (clazz.isArray()) {
            return new ArrayDecoderGenerator();
        }
        return new StructDecoderGenerator();
    }

    private static EncoderGenerator getEncoderGenerator(Class clazz) {
        if (clazz.isArray()) {
            return new ArrayEncoderGenerator();
        }
        return new StructEncoderGenerator();
    }

    private static void printSourceCode(Class clazz, String src) {
        if (!"true".equals(System.getenv("QJSON_DEBUG"))) {
            return;
        }
        System.err.println("=== " + clazz.getCanonicalName() + " ===");
        String lines[] = src.split("\\r?\\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            System.err.println((i + 1) + ":\t" + line);
        }
    }
}
