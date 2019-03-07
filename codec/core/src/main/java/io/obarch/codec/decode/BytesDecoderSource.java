package io.obarch.codec.decode;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;
import io.obarch.codec.encode.CurrentPath;

import java.nio.charset.StandardCharsets;

public class BytesDecoderSource implements DecoderSource {

    private final PathTracker pathTracker = new PathTracker(this);
    private final Object[] attachments = new Object[DecoderSource.AttachmentKey.count()];
    final byte[] buf;
    int offset;
    final int size;

    static {
        init(DecodeString.TEMP_KEY);
        init(DecodeBytes.TEMP_KEY);
    }

    private static void init(Object ignore) {
    }

    public BytesDecoderSource(byte[] buf, int offset, int size) {
        this.buf = buf;
        this.offset = offset;
        this.size = size;
    }

    public BytesDecoderSource(String buf) {
        this(buf.getBytes(StandardCharsets.UTF_8));
    }

    public BytesDecoderSource(byte[] buf) {
        this(buf, 0, buf.length);
    }

    @Override
    public int decodeInt() {
        return (int) decodeLong();
    }

    @Override
    public long decodeLong() {
        return DecodeLong.$(this);
    }

    @Override
    public double decodeDouble() {
        long l = DecodeLong.$(this, 'f');
        return Double.longBitsToDouble(l);
    }

    @Override
    public String decodeString() {
        expect('"');
        return DecodeString.$(this, offset);
    }

    @Override
    public Object decodeStringOrNumber() {
        if (offset + 2 < size && buf[offset + 1] == '\\') {
            byte type = buf[offset + 2];
            if (type == 'b') {
                return decodeLong();
            } else if (type == 'f') {
                return decodeDouble();
            } else {
                throw reportError("expect \\b or \\f");
            }
        }
        return decodeString();
    }

    @Override
    public byte[] decodeBytes() {
        return DecodeBytes.$(this);
    }

    @Override
    public Object decodeRef(Decoder decoder) {
        if (offset + 2 < size && buf[offset] == '"' && buf[offset + 1] == '\\' && buf[offset + 2] == '/') {
            String path = DecodeString.$(this, offset + 3);
            Object ref = pathTracker.lookup(path);
            return ref;
        }
        return null;
    }

    @Override
    public Object decodeValue(Decoder decoder) {
        return pathTracker.decodeObject(decoder, true);
    }

    @Override
    public Object decodeValue(Decoder decoder, boolean track) {
        return pathTracker.decodeObject(decoder, track);
    }

    @Override
    public CurrentPath currentPath() {
        return pathTracker.currentPath();
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
    public int mark() {
        return offset;
    }

    @Override
    public String sinceMark(int mark) {
        return new String(buf, mark, offset - mark);
    }

    @Override
    public byte read() {
        byte b = peek();
        next();
        return b;
    }

    public boolean decodeBoolean() {
        if (offset + 4 > size) {
            throw reportError("expect true or false");
        }
        boolean isTrue = buf[offset] == 't' && buf[offset + 1] == 'r' && buf[offset + 2] == 'u' && buf[offset + 3] == 'e';
        if (isTrue) {
            offset += 4;
            return true;
        }
        expect('f', 'a', 'l', 's', 'e');
        return false;
    }

    @Override
    public boolean decodeNull() {
        if (offset + 4 > size) {
            return false;
        }
        boolean isNull = buf[offset] == 'n' && buf[offset + 1] == 'u' && buf[offset + 2] == 'l' && buf[offset + 3] == 'l';
        if (isNull) {
            offset += 4;
        }
        return isNull;
    }

    void expect(char b1, char b2, char b3, char b4, char b5) {
        if (offset + 5 > size) {
            throw reportError("expect 5 more bytes");
        }
        boolean expected = buf[offset] == b1
                && buf[offset + 1] == b2
                && buf[offset + 2] == b3
                && buf[offset + 3] == b4
                && buf[offset + 4] == b5;
        if (!expected) {
            throw reportError("expect " + new String(new char[]{b1, b2, b3, b4, b5}));
        }
        offset += 5;
    }

    void expect(char b1, char b2, char b3, char b4) {
        if (offset + 5 > size) {
            throw reportError("expect 5 more bytes");
        }
        boolean expected = buf[offset] == b1
                && buf[offset + 1] == b2
                && buf[offset + 2] == b3
                && buf[offset + 3] == b4;
        if (!expected) {
            throw reportError("expect " + new String(new char[]{b1, b2, b3, b4}));
        }
        offset += 4;
    }

    void expect(char b1, char b2, char b3) {
        if (offset + 3 > size) {
            throw reportError("expect 3 more bytes");
        }
        boolean expected = buf[offset] == b1 && buf[offset + 1] == b2 && buf[offset + 2] == b3;
        if (!expected) {
            throw reportError("expect " + new String(new char[]{b1, b2, b3}));
        }
        offset += 3;
    }

    void expect(char b1) {
        if (offset >= size) {
            throw reportError("expect more bytes");
        }
        boolean expected = buf[offset] == b1;
        if (!expected) {
            throw reportError("expect " + new String(new char[]{b1}));
        }
        offset++;
    }

    @Override
    public QJsonDecodeException reportError(String errMsg) {
        throw new QJsonDecodeException(errMsg);
    }

    @Override
    public QJsonDecodeException reportError(String errMsg, Exception cause) {
        throw new QJsonDecodeException(errMsg, cause);
    }

    public byte peek() {
        if (offset >= size) {
            throw reportError("expect more byte");
        }
        return buf[offset];
    }

    public void next() {
        offset++;
    }

    @Override
    public void skip() {
        Skip.$(this);
    }
}
