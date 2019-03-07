package io.obarch.codec.encode;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

public class StringEncoderSink implements EncoderSink {

    private final ObjectTracker objectTracker = new ObjectTracker(this);
    private final Object[] attachments = new Object[EncoderSink.AttachmentKey.count()];
    private final StringBuilder builder;

    static {
        init(EncodeString.TEMP_KEY);
    }

    private static void init(Object ignore) {
    }

    public StringEncoderSink() {
        this(new StringBuilder());
    }

    public StringEncoderSink(StringBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void encodeNull() {
        Append.$(builder, 'n', 'u', 'l', 'l');
    }

    @Override
    public void encodeBoolean(boolean val) {
        if (val) {
            Append.$(builder, 't', 'r', 'u', 'e');
        } else {
            Append.$(builder, 'f', 'a', 'l', 's', 'e');
        }
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
    public void encodeValue(Object val, Encoder encoder) {
        objectTracker.encodeObject(val, encoder);
    }

    @Override
    public void encodeValue(Object val, Encoder.Provider spi) {
        objectTracker.encodeObject(val, spi);
    }

    @Override
    public CurrentPath currentPath() {
        return objectTracker.currentPath();
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
        builder.append("\"\\/");
        EncodeString.body(this, ref);
        builder.append('"');
    }

    @Override
    public int mark() {
        return builder.length();
    }

    @Override
    public String sinceMark(int mark) {
        return builder.substring(mark, builder.length());
    }

    @Override
    public void write(char b) {
        builder.append(b);
    }

    @Override
    public void write(String raw) {
        builder.append(raw);
    }

    @Override
    public QJsonEncodeException reportError(String errMsg) {
        throw new QJsonEncodeException(errMsg);
    }

    @Override
    public QJsonEncodeException reportError(String errMsg, Exception cause) {
        throw new QJsonEncodeException(errMsg, cause);
    }

    public StringBuilder stringBuilder() {
        return builder;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public void reset() {
        objectTracker.reset();
        builder.setLength(0);
    }
}
