package io.obarch.codec.reflection;

import io.obarch.codec.encode.CurrentPath;
import io.obarch.codec.encode.EncodeProperties;
import io.obarch.codec.encode.QJsonEncodeException;
import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;
import io.obarch.codec.spi.QJsonSpi;
import io.obarch.codec.spi.StructDescriptor;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class StructEncoder implements Encoder {

    private final Provider spi;
    private final List<StructDescriptor.Prop> props;

    private StructEncoder(Encoder.Provider spi, List<StructDescriptor.Prop> props) {
        this.spi = spi;
        this.props = props;
    }

    public static StructEncoder create(
            Class clazz, QJsonSpi spi,
            BiConsumer<QJsonSpi, StructDescriptor> customizeStruct) {
        StructDescriptor structDescriptor = StructDescriptor.create(clazz, spi, customizeStruct);
        if (Modifier.isPrivate(structDescriptor.clazz.getModifiers())) {
            return new StructEncoder(spi, Collections.emptyList());
        }
        List<StructDescriptor.Prop> props = EncodeProperties.$(structDescriptor);
        return new StructEncoder(spi, props);
    }

    @Override
    public void encode(EncoderSink sink, Object val) {
        try {
            _encode(sink, val);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new QJsonEncodeException(e);
        }
    }

    public void _encode(EncoderSink sink, Object val) throws Exception {
        sink.write('{');
        CurrentPath currentPath = sink.currentPath();
        boolean isFirst = true;
        for (StructDescriptor.Prop prop : props) {
            Object propVal = getProp(prop, val);
            boolean shouldEncode = prop.shouldEncode == null || prop.shouldEncode.test(propVal);
            if (!shouldEncode) {
                continue;
            }
            if (isFirst) {
                isFirst = false;
            } else {
                sink.write(',');
            }
            sink.encodeString(prop.name);
            sink.write(':');
            int oldPath = currentPath.enterStructField(prop.name);
            encodeProp(sink, prop, propVal);
            currentPath.exit(oldPath);
        }
        sink.write('}');
    }

    private void encodeProp(EncoderSink sink, StructDescriptor.Prop prop, Object propVal) {
        if (prop.encoder == null) {
            sink.encodeValue(propVal, spi);
        } else {
            sink.encodeValue(propVal, prop.encoder);
        }
    }

    private Object getProp(StructDescriptor.Prop prop, Object val) throws Exception {
        if (prop.field != null) {
            return prop.field.get(val);
        }
        return prop.method.invoke(val);
    }
}
