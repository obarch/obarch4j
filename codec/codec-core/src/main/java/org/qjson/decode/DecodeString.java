package org.qjson.decode;

import org.qjson.encode.BytesBuilder;
import org.qjson.spi.DecoderSource;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

interface DecodeString {

    DecoderSource.AttachmentKey<BytesBuilder> TEMP_KEY = DecoderSource.AttachmentKey.assign();

    static String $(BytesDecoderSource source, int i) {
        source.offset = i;
        for (; i < source.size; i++) {
            byte b = source.buf[i];
            if (b == '"') {
                String decoded = new String(source.buf, source.offset, i - source.offset, StandardCharsets.UTF_8);
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
            if (readEscaped(source, temp)) {
                break;
            }
            if (readRaw(source, temp)) {
                break;
            }
        }
        String decoded = temp.toString();
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

    static String $(StringDecoderSource source, int i) {
        source.offset = i;
        for (; i < source.buf.length(); i++) {
            char c = source.buf.charAt(i);
            if (c == '"') {
                String decoded = source.buf.substring(source.offset, i);
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
        StringBuilder builder = new StringBuilder(source.buf.substring(source.offset, i));
        BytesBuilder temp = borrowTemp(source, 0);
        source.offset = i;
        while (true) {
            if (readEscaped(source, builder, temp)) {
                break;
            }
            if (readRaw(source, builder)) {
                break;
            }
        }
        source.setAttachment(TEMP_KEY, temp);
        return builder.toString();
    }

    static boolean readEscaped(BytesDecoderSource source, BytesBuilder builder) {
        for (int i = source.offset; i < source.size; ) {
            byte b = source.buf[i];
            if (b == '"') {
                source.offset = i + 1;
                return true;
            }
            if (b != '\\') {
                source.offset = i;
                return false;
            }
            if (i + 4 >= source.size) {
                throw source.reportError("missing double quote");
            }
            if (source.buf[i + 1] != '\\') {
                throw source.reportError("escape \\\\ is the only supported escape form");
            }
            b = (byte) (((source.buf[i + 2] - 'A') << 4) + source.buf[i + 3] - 'A');
            builder.append(b);
            i += 4;
        }
        throw source.reportError("missing double quote");
    }

    static boolean readEscaped(StringDecoderSource source, BytesBuilder builder) {
        for (int i = source.offset; i < source.buf.length(); ) {
            char c = source.buf.charAt(i);
            if (c == '"') {
                source.offset = i + 1;
                return true;
            }
            if (c != '\\') {
                source.offset = i;
                return false;
            }
            if (i + 4 >= source.buf.length()) {
                throw source.reportError("missing double quote");
            }
            if (source.buf.charAt(i + 1) != '\\') {
                throw source.reportError("escape \\\\ is the only supported escape form");
            }
            byte b = (byte) (((source.buf.charAt(i + 2) - 'A') << 4) + source.buf.charAt(i + 3) - 'A');
            builder.append(b);
            i += 4;
        }
        throw source.reportError("missing double quote");
    }

    static boolean readEscaped(StringDecoderSource source, StringBuilder builder, BytesBuilder temp) {
        temp.setLength(0);
        for (int i = source.offset; i < source.buf.length(); ) {
            char c = source.buf.charAt(i);
            if (c == '"') {
                builder.append(temp.toString());
                source.offset = i + 1;
                return true;
            }
            if (c != '\\') {
                builder.append(temp.toString());
                source.offset = i;
                return false;
            }
            if (i + 4 >= source.buf.length()) {
                throw source.reportError("missing double quote");
            }
            if (source.buf.charAt(i + 1) != '\\') {
                throw source.reportError("escape \\\\ is the only supported escape form");
            }
            byte b = (byte) (((source.buf.charAt(i + 2) - 'A') << 4) + source.buf.charAt(i + 3) - 'A');
            temp.append(b);
            i += 4;
        }
        throw source.reportError("missing double quote");
    }

    static boolean readRaw(BytesDecoderSource source, BytesBuilder builder) {
        for (int i = source.offset; i < source.size; i++) {
            byte b = source.buf[i];
            if (b == '"') {
                source.offset = i + 1;
                return true;
            }
            if (b == '\\') {
                source.offset = i;
                return false;
            }
            builder.append(b);
        }
        throw source.reportError("missing double quote");
    }

    static boolean readRaw(StringDecoderSource source, StringBuilder builder) {
        for (int i = source.offset; i < source.buf.length(); i++) {
            char c = source.buf.charAt(i);
            if (c == '"') {
                source.offset = i + 1;
                return true;
            }
            if (c == '\\') {
                source.offset = i;
                return false;
            }
            builder.append(c);
        }
        throw source.reportError("missing double quote");
    }

    static boolean readRaw(StringDecoderSource source, BytesBuilder builder) {
        int start = source.offset;
        for (int i = source.offset; i < source.buf.length(); i++) {
            char c = source.buf.charAt(i);
            if (c == '"') {
                appendUTF8(source, builder, source.buf, start, i);
                source.offset = i + 1;
                return true;
            }
            if (c == '\\') {
                appendUTF8(source, builder, source.buf, start, i);
                source.offset = i;
                return false;
            }
        }
        throw source.reportError("missing double quote");
    }

    static void appendUTF8(StringDecoderSource source, BytesBuilder builder, String buf, int offset, int end) {
        CharBuffer charBuffer = CharBuffer.wrap(buf, offset, end);
        int maxLength = 3 * (end - offset);
        builder.ensureCapacity(builder.length() + maxLength);
        ByteBuffer byteBuffer = ByteBuffer.wrap(builder.buf(), builder.length(), maxLength);
        CoderResult result = StandardCharsets.UTF_8.newEncoder().encode(charBuffer, byteBuffer, true);
        if (result.isError()) {
            try {
                result.throwException();
            } catch (Exception e) {
                throw source.reportError("encode string to utf8 failed", e);
            }
        }
        builder.setLength(byteBuffer.position());
    }
}
