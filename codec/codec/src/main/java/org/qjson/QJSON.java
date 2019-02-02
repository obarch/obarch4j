package org.qjson;

import org.mdkt.compiler.InMemoryJavaCompiler;
import org.qjson.any.Any;
import org.qjson.any.AnyList;
import org.qjson.any.AnyMap;
import org.qjson.builtin.BuiltinDecoders;
import org.qjson.builtin.BuiltinEncoders;
import org.qjson.codegen.Codegen;
import org.qjson.datetime.DateTimeDecoders;
import org.qjson.datetime.DateTimeEncoders;
import org.qjson.decode.BytesDecoderSource;
import org.qjson.decode.QJsonDecodeException;
import org.qjson.decode.StringDecoderSource;
import org.qjson.encode.BytesBuilder;
import org.qjson.encode.BytesEncoderSink;
import org.qjson.encode.StringEncoderSink;
import org.qjson.reflection.Reflection;
import org.qjson.spi.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Function;

public class QJSON implements QJsonSpi {

    public static QJSON $ = new QJSON();

    public enum Mode {
        REFLECTION,
        CODEGEN,
        MIXED
    }

    public static class Config extends Codegen.Config {
        public BiFunction<QJsonSpi, Type, Decoder> chooseDecoder;
        public BiFunction<QJsonSpi, Class, Encoder> chooseEncoder;
        public Mode mode = Mode.REFLECTION;
        public Executor executor;
    }

    private final static Map<Class, Encoder> builtinEncoders = new HashMap<Class, Encoder>() {{
        putAll(BuiltinEncoders.$());
        putAll(DateTimeEncoders.$());
    }};

    private final static Map<Type, Decoder> builtinDecoders = new HashMap<Type, Decoder>() {{
        putAll(BuiltinDecoders.$());
        putAll(DateTimeDecoders.$());
    }};

    private final Map<Class, Encoder> encoderCache = new ConcurrentHashMap<>();
    private final Map<Type, Decoder> decoderCache = new ConcurrentHashMap<>();
    private final Config cfg;
    private final Codegen codegen;
    private final Reflection reflection;
    private final QJsonSpi spi = new QJsonSpi() {
        @Override
        public Function<DecoderSource, Object> factoryOf(Class clazz) {
            return QJSON.this.factoryOf(clazz);
        }

        @Override
        public Decoder decoderOf(Type type) {
            return QJSON.this.generateDecoder(type);
        }

        @Override
        public Encoder encoderOf(Class clazz) {
            return QJSON.this.generateEncoder(clazz);
        }
    };

    public QJSON(Config cfg) {
        if (cfg.compiler == null) {
            cfg.compiler = InMemoryJavaCompiler.newInstance().ignoreWarnings();
        }
        Map<Class, Class> implMap = new HashMap<Class, Class>() {{
            put(Map.class, AnyMap.class);
            put(Iterable.class, AnyList.class);
            put(Collection.class, AnyList.class);
            put(List.class, AnyList.class);
            put(Set.class, HashSet.class);
        }};
        Function<Class, Class> defaultChooseImpl = clazz -> {
            if (clazz.isPrimitive() || clazz.isArray()) {
                return clazz;
            }
            if (!(Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface())) {
                return clazz;
            }
            Class impl = implMap.get(clazz);
            if (impl == null) {
                throw new QJsonDecodeException("can not determine implementation class to decode: " + clazz);
            }
            return impl;
        };
        if (cfg.chooseEncoder == null) {
            cfg.chooseEncoder = (qjson, clazz) -> null;
        }
        if (cfg.chooseDecoder == null) {
            cfg.chooseDecoder = (qjson, clazz) -> null;
        }
        if (cfg.chooseImpl == null) {
            cfg.chooseImpl = defaultChooseImpl;
        } else {
            Function<Class, Class> userChooseImpl = cfg.chooseImpl;
            cfg.chooseImpl = clazz -> {
                Class impl = userChooseImpl.apply(clazz);
                if (impl != null) {
                    return impl;
                }
                return defaultChooseImpl.apply(clazz);
            };
        }
        if (cfg.mode == Mode.MIXED && cfg.executor == null) {
            cfg.executor = Executors.newFixedThreadPool(1);
        }
        this.cfg = cfg;
        codegen = new Codegen(cfg, this);
        reflection = new Reflection(this, cfg.customizeStruct);
    }

    public QJSON() {
        this(new Config());
    }

    @Override
    public Function<DecoderSource, Object> factoryOf(Class clazz) {
        clazz = cfg.chooseImpl.apply(clazz);
        if (clazz.equals(AnyMap.class)) {
            return source -> new AnyMap();
        }
        if (clazz.equals(HashMap.class)) {
            return source -> new HashMap();
        }
        if (clazz.equals(AnyList.class)) {
            return source -> new AnyList();
        }
        if (clazz.equals(ArrayList.class)) {
            return source -> new ArrayList();
        }
        try {
            Constructor ctor = clazz.getConstructor();
            return source -> {
                try {
                    return (Map) ctor.newInstance();
                } catch (Exception e) {
                    throw source.reportError("create map failed", e);
                }
            };

        } catch (NoSuchMethodException e) {
            throw new QJsonDecodeException("no default constructor for: " + clazz, e);
        }
    }

    public Encoder encoderOf(Class clazz) {
        Encoder encoder = encoderCache.get(clazz);
        if (encoder == null) {
            // placeholder to avoid infinite loop
            encoderCache.put(clazz, new PlaceholderEncoder(this, clazz));
            encoder = chooseOrGenerateEncoder(clazz);
            encoderCache.put(clazz, encoder);
        }
        return encoder;
    }

    private Encoder chooseOrGenerateEncoder(Class clazz) {
        Encoder encoder = cfg.chooseEncoder.apply(spi, clazz);
        if (encoder != null) {
            return encoder;
        }
        return generateEncoder(clazz);
    }

    private Encoder generateEncoder(Class clazz) {
        Encoder encoder = builtinEncoders.get(clazz);
        if (encoder != null) {
            return encoder;
        }
        encoder = BuiltinEncoders.create(this, clazz);
        if (encoder != null) {
            return encoder;
        }
        if (cfg.mode == Mode.CODEGEN) {
            return codegen.generateEncoder(clazz);
        }
        encoder = reflection.generateEncoder(clazz);
        if (cfg.mode == Mode.MIXED) {
            cfg.executor.execute(() -> encoderCache.put(clazz, codegen.generateEncoder(clazz)));
        }
        return encoder;
    }

    public Decoder decoderOf(Type type) {
        Decoder decoder = decoderCache.get(type);
        if (decoder == null) {
            // placeholder to avoid infinite loop
            decoderCache.put(type, new PlaceholderDecoder(this, type));
            decoder = chooseOrGenerateDecoder(type);
            decoderCache.put(type, decoder);
        }
        return decoder;
    }

    private Decoder chooseOrGenerateDecoder(Type type) {
        Decoder decoder = cfg.chooseDecoder.apply(spi, type);
        if (decoder != null) {
            return decoder;
        }
        return generateDecoder(type);
    }

    private Decoder generateDecoder(Type type) {
        Decoder decoder = builtinDecoders.get(type);
        if (decoder != null) {
            return decoder;
        }
        if (Object.class.equals(type)) {
            ObjectDecoder objectDecoder = new ObjectDecoder();
            decoderCache.put(Object.class, objectDecoder);
            objectDecoder.init(
                    decoderOf(cfg.chooseImpl.apply(List.class)),
                    decoderOf(cfg.chooseImpl.apply(Map.class)));
            return objectDecoder;
        }
        Map<TypeVariable, Type> typeArgs = new HashMap<>();
        Class clazz = TypeVariables.collect(type, typeArgs);
        if (clazz == null) {
            throw new QJsonDecodeException("can not cast to class: " + type);
        }
        decoder = BuiltinDecoders.create(this, clazz, typeArgs);
        if (decoder != null) {
            return decoder;
        }
        decoder = DateTimeDecoders.create(this, clazz, typeArgs);
        if (decoder != null) {
            return decoder;
        }
        if (cfg.mode == Mode.CODEGEN) {
            return codegen.generateDecoder(type);
        }
        decoder = reflection.generateDecoder(clazz, typeArgs);
        if (cfg.mode == Mode.MIXED) {
            cfg.executor.execute(() -> decoderCache.put(type, codegen.generateDecoder(type)));
        }
        return decoder;
    }

    public String encode(Object val) {
        StringEncoderSink sink = new StringEncoderSink(new StringBuilder());
        sink.encodeObject(val, this);
        return sink.toString();
    }

    public String encode(Object val, StringBuilder builder) {
        builder.setLength(0);
        StringEncoderSink sink = new StringEncoderSink(builder);
        sink.encodeObject(val, this);
        return sink.toString();
    }

    public void encode(Object val, BytesBuilder bytesBuilder) {
        BytesEncoderSink sink = new BytesEncoderSink(bytesBuilder);
        sink.encodeObject(val, this);
    }

    public <T> T decode(Class<T> clazz, String encoded) {
        return (T) decode((Type) clazz, encoded);
    }

    public <T> T decode(Class<T> clazz, byte[] encoded) {
        return decode(clazz, encoded, 0, encoded.length);
    }

    public <T> T decode(Class<T> clazz, byte[] encoded, int offset, int size) {
        return (T) decode((Type) clazz, encoded, offset, size);
    }

    public <T> T decode(TypeLiteral<T> typeLiteral, byte[] encoded) {
        return (T) decode(typeLiteral.$(), encoded, 0, encoded.length);
    }

    public <T> T decode(TypeLiteral<T> typeLiteral, byte[] encoded, int offset, int size) {
        return (T) decode(typeLiteral.$(), encoded, offset, size);
    }

    public <T> T decode(TypeLiteral<T> typeLiteral, String encoded) {
        return (T) decode(typeLiteral.$(), encoded);
    }

    private Object decode(Type type, byte[] encoded, int offset, int size) {
        Decoder decoder = decoderOf(type);
        BytesDecoderSource source = new BytesDecoderSource(encoded, offset, size);
        return source.decodeObject(decoder);
    }

    private Object decode(Type type, String encoded) {
        Decoder decoder = decoderOf(type);
        StringDecoderSource source = new StringDecoderSource(encoded);
        return source.decodeObject(decoder);
    }

    // === static api ===

    public static Any parse(String encoded) {
        return $.decode(Any.class, encoded);
    }

    public static <T> T parse(Class<T> clazz, String encoded) {
        return $.decode(clazz, encoded);
    }

    public static String stringify(Object val) {
        return $.encode(val);
    }
}
