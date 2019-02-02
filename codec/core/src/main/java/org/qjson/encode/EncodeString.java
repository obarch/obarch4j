package org.qjson.encode;

import org.qjson.spi.EncoderSink;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

interface EncodeString {

    EncoderSink.AttachmentKey<byte[]> TEMP_KEY = EncoderSink.AttachmentKey.assign();

    static void $(BytesEncoderSink sink, String val) {
        BytesBuilder builder = sink.bytesBuilder();
        builder.append('"');
        EncodeString.body(sink, val);
        builder.append('"');
    }

    static void body(BytesEncoderSink sink, String val) {
        BytesBuilder builder = sink.bytesBuilder();
        // one char translated to 4 bytes "\/AA" in worst case
        int maxSize = 4 * val.length();
        builder.ensureCapacity(builder.length() + maxSize);
        int offset = builder.length();
        byte[] buf = builder.buf();
        ByteBuffer byteBuf = ByteBuffer.wrap(buf, offset, maxSize);
        CoderResult result = StandardCharsets.UTF_8.newEncoder().encode(
                CharBuffer.wrap(val),
                byteBuf, true);
        if (result.isError()) {
            try {
                result.throwException();
            } catch (Exception e) {
                throw sink.reportError("encode string to utf8 failed", e);
            }
        }
        int end = byteBuf.position();
        int escapePos = shouldEscape(buf, offset, end);
        if (escapePos == -1) {
            builder.setLength(end);
            return;
        }
        builder.setLength(escapePos);
        int toEscapeLen = end - escapePos;
        byte[] temp = sink.borrowAttachment(TEMP_KEY);
        if (temp == null || temp.length < toEscapeLen) {
            temp = new byte[toEscapeLen];
        }
        System.arraycopy(buf, escapePos, temp, 0, toEscapeLen);
        for (int i = 0; i < toEscapeLen; i++) {
            byte b = temp[i];
            boolean isControlCharacter = 0 <= b && b < 0x20;
            if (isControlCharacter || b == '\\' || b == '/' || b == '"') {
                builder.appendEscape(b);
            } else {
                builder.append(b);
            }
        }
        sink.setAttachment(TEMP_KEY, temp);
    }

    static int shouldEscape(byte[] buf, int offset, int end) {
        for (int i = offset; i < end; i++) {
            byte b = buf[i];
            boolean isControlCharacter = 0 <= b && b < 0x20;
            if (isControlCharacter || b == '\\' || b == '/' || b == '"') {
                return i;
            }
            // because the byte[] is converted from string
            // we can assume it is valid unicode
        }
        return -1;
    }

    static void $(StringEncoderSink sink, String val) {
        StringBuilder builder = sink.stringBuilder();
        builder.append('"');
        EncodeString.body(sink, val);
        builder.append('"');
    }

    static void body(StringEncoderSink sink, String val) {
        StringBuilder builder = sink.stringBuilder();
        for (int i = 0; i < val.length(); i++) {
            char c = val.charAt(i);
            boolean isControlCharacter = c < 0x20;
            if (isControlCharacter || c == '\\' || c == '/' || c == '"') {
                Append.escape(builder, (byte) c);
            } else {
                builder.append(c);
            }
        }
    }
}
