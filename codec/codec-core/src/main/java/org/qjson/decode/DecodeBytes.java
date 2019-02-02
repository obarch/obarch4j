package org.qjson.decode;

import org.qjson.encode.BytesBuilder;
import org.qjson.spi.DecoderSource;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

interface DecodeBytes {

    DecoderSource.AttachmentKey<BytesBuilder> TEMP_KEY = DecoderSource.AttachmentKey.assign();

    static byte[] $(BytesDecoderSource source) {
        source.expect('"');
        int i = source.offset;
        for (; i < source.size; i++) {
            byte b = source.buf[i];
            if (b == '"') {
                byte[] decoded = new byte[i - source.offset];
                System.arraycopy(source.buf, source.offset, decoded, 0, decoded.length);
                source.offset = i + 1;
                return decoded;
            }
            if (b == '\\') {
                break;
            }
        }
        if (i >= source.size) {
            throw source.reportError("missing double quote");
        }
        int noEscapeLen = i - source.offset;
        BytesBuilder temp = borrowTemp(source, noEscapeLen + 16);
        System.arraycopy(source.buf, source.offset, temp.buf(), 0, noEscapeLen);
        temp.setLength(noEscapeLen);
        source.offset = i;
        while (true) {
            if (DecodeString.readEscaped(source, temp)) {
                break;
            }
            if (DecodeString.readRaw(source, temp)) {
                break;
            }
        }
        byte[] decoded = temp.copyOfBytes();
        source.setAttachment(TEMP_KEY, temp);
        return decoded;
    }

    static BytesBuilder borrowTemp(DecoderSource source, int capacity) {
        BytesBuilder temp = source.borrowAttachment(TEMP_KEY);
        if (temp == null) {
            temp = new BytesBuilder(new byte[capacity], 0);
        } else {
            temp.ensureCapacity(capacity);
        }
        return temp;
    }

    static byte[] $(StringDecoderSource source) {
        source.expect('"');
        int i = source.offset;
        for (; i < source.buf.length(); i++) {
            char c = source.buf.charAt(i);
            if (c == '"') {
                CharBuffer charBuffer = CharBuffer.wrap(source.buf, source.offset, i);
                byte[] decoded = StandardCharsets.UTF_8.encode(charBuffer).array();
                source.offset = i + 1;
                return decoded;
            }
            if (c == '\\') {
                break;
            }
        }
        if (i >= source.buf.length()) {
            throw source.reportError("missing double quote");
        }
        int noEscapeLen = i - source.offset;
        BytesBuilder temp = borrowTemp(source, noEscapeLen + 16);
        CharBuffer charBuffer = CharBuffer.wrap(source.buf, source.offset, i);
        ByteBuffer byteBuffer = ByteBuffer.wrap(temp.buf());
        CoderResult result = StandardCharsets.UTF_8.newEncoder().encode(charBuffer, byteBuffer, true);
        if (result.isError()) {
            try {
                result.throwException();
            } catch (Exception e) {
                throw source.reportError("encode string to utf8 failed", e);
            }
        }
        temp.setLength(byteBuffer.position());
        source.offset = i;
        while (true) {
            if (DecodeString.readEscaped(source, temp)) {
                break;
            }
            if (DecodeString.readRaw(source, temp)) {
                break;
            }
        }
        byte[] decoded = temp.copyOfBytes();
        source.setAttachment(TEMP_KEY, temp);
        return decoded;
    }
}
