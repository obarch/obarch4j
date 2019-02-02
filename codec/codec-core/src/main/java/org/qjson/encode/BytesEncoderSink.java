package org.qjson.encode;

import org.qjson.spi.Encoder;
import org.qjson.spi.EncoderSink;

import java.nio.charset.StandardCharsets;

public final class BytesEncoderSink implements EncoderSink {

    private final ObjectTracker objectTracker = new ObjectTracker(this);
    private final Object[] attachments = new Object[EncoderSink.AttachmentKey.count()];
    private final BytesBuilder builder;

    static {
        init(EncodeString.TEMP_KEY);
    }

    private static void init(Object ignore) {
    }

    public BytesEncoderSink(BytesBuilder builder) {
        this.builder = builder;
    }

    public BytesEncoderSink() {
        this(new BytesBuilder());
    }

    @Override
    public void encodeInt(int val) {
        EncodeLong.$(this, val);
    }

    @Override
    public void encodeLong(long val) {
        EncodeLong.$(this, val);
    }

    @Override
    public void encodeDouble(double val) {
        long l = Double.doubleToRawLongBits(val);
        EncodeLong.$(this, 'f', l);
    }

    @Override
    public void encodeString(String val) {
        EncodeString.$(this, val);
    }

    @Override
    public void encodeBytes(byte[] val) {
        EncodeBytes.$(this, val);
    }

    @Override
    public void encodeObject(Object val, Encoder encoder) {
        objectTracker.encodeObject(val, encoder);
    }

    @Override
    public void encodeObject(Object val, Encoder.Provider spi) {
        objectTracker.encodeObject(val, spi);
    }

    @Override
    public CurrentPath currentPath() {
        return null;
    }

    @Override
    public <T> T getAttachment(AttachmentKey<T> key) {
        return (T) attachments[key.val];
    }

    @Override
    public <T> void setAttachment(AttachmentKey<T> key, T attachment) {
        attachments[key.val] = attachment;
    }

    @Override
    public void encodeRef(String ref) {
        builder.append('"', '\\', '/');
        EncodeString.body(this, ref);
        builder.append('"');
    }

    @Override
    public int mark() {
        return builder.length();
    }

    @Override
    public String sinceMark(int mark) {
        return new String(builder.buf(), mark, builder.length() - mark, StandardCharsets.UTF_8);
    }

    @Override
    public void write(char b) {
        builder.append(b);
    }

    public void encodeBoolean(boolean val) {
        if (val) {
            builder.append('t', 'r', 'u', 'e');
        } else {
            builder.append('f', 'a', 'l', 's', 'e');
        }
    }

    @Override
    public void encodeNull() {
        builder.append('n', 'u', 'l', 'l');
    }

    @Override
    public QJsonEncodeException reportError(String errMsg) {
        throw new QJsonEncodeException(errMsg);
    }

    @Override
    public QJsonEncodeException reportError(String errMsg, Exception cause) {
        throw new QJsonEncodeException(errMsg, cause);
    }

    public BytesBuilder bytesBuilder() {
        return builder;
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
