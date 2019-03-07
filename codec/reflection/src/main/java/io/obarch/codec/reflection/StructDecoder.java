package io.obarch.codec.reflection;

import io.obarch.codec.decode.DecodeProperties;
import io.obarch.codec.decode.QJsonDecodeException;
import io.obarch.codec.encode.CurrentPath;
import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;
import io.obarch.codec.spi.QJsonSpi;
import io.obarch.codec.spi.StructDescriptor;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class StructDecoder implements Decoder {

    private final Constructor ctor;
    private final Map<String, StructDescriptor.Prop> props;

    public StructDecoder(Constructor ctor, Map<String, StructDescriptor.Prop> props) {
        this.ctor = ctor;
        this.props = props;
    }

    public static StructDecoder create(
            Class clazz, QJsonSpi spi,
            BiConsumer<QJsonSpi, StructDescriptor> customizeStruct) {
        StructDescriptor structDescriptor = StructDescriptor.create(clazz, spi, customizeStruct);
        List<StructDescriptor.Prop> props = DecodeProperties.$(structDescriptor, spi);
        Map<String, StructDescriptor.Prop> propMap = new HashMap<>();
        for (StructDescriptor.Prop prop : props) {
            propMap.put(prop.name, prop);
        }
        try {
            Constructor ctor = clazz.getConstructor();
            return new StructDecoder(ctor, propMap);
        } catch (NoSuchMethodException e) {
            throw new QJsonDecodeException(clazz + " does not have default constructor, " +
                    "need to use config to specify decoder manually");
        }
    }

    @Override
    public Object decode(DecoderSource source) {
        try {
            return ctor.newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new QJsonDecodeException(e);
        }
    }

    @Override
    public void decodeProperties(DecoderSource source, Object obj) {
        try {
            _decodeProperties(source, obj);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new QJsonDecodeException(e);
        }
    }

    public void _decodeProperties(DecoderSource source, Object obj) throws Exception {
        if (source.read() != '{') {
            throw source.reportError("expect {");
        }
        if (source.peek() == '}') {
            source.next();
            return;
        }
        CurrentPath currentPath = source.currentPath();
        byte b;
        do {
            String propName = source.decodeString();
            if (source.read() != ':') {
                throw source.reportError("expect :");
            }
            StructDescriptor.Prop prop = props.get(propName);
            if (prop == null) {
                source.skip();
                continue;
            }
            int oldPath = currentPath.enterStructField(prop.name);
            Object propVal = source.decodeValue(prop.decoder);
            currentPath.exit(oldPath);
            setProp(obj, prop, propVal);
        } while ((b = source.read()) == ',');
        if (b != '}') {
            throw source.reportError("expect }");
        }
    }

    private void setProp(Object obj, StructDescriptor.Prop prop, Object propVal) throws Exception {
        if (prop.field != null) {
            prop.field.set(obj, propVal);
            return;
        }
        prop.method.invoke(obj, propVal);
    }
}
