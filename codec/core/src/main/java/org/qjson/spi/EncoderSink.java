package org.qjson.spi;

import org.qjson.encode.CurrentPath;
import org.qjson.encode.QJsonEncodeException;

public interface EncoderSink {

    class AttachmentKey<T> {

        private static byte count;
        public final byte val;

        private AttachmentKey(byte val) {
            this.val = val;
        }

        public static <T> AttachmentKey<T> assign() {
            AttachmentKey key = new AttachmentKey(count);
            count++;
            if (count < 0) {
                throw new IllegalStateException("too many temp keys");
            }
            return key;
        }

        public static byte count() {
            return count;
        }
    }

    void encodeNull();

    void encodeBoolean(boolean val);

    void encodeInt(int val);

    void encodeLong(long val);

    void encodeDouble(double val);

    void encodeString(String val);

    void encodeBytes(byte[] val);

    void encodeObject(Object val, Encoder encoder);

    void encodeObject(Object val, Encoder.Provider spi);

    CurrentPath currentPath();

    default <T> T borrowAttachment(AttachmentKey<T> key) {
        T attachment = getAttachment(key);
        setAttachment(key, null);
        return attachment;
    }

    <T> T getAttachment(AttachmentKey<T> key);

    <T> void setAttachment(AttachmentKey<T> key, T attachment);

    void encodeRef(String ref);

    int mark();

    String sinceMark(int mark);

    void write(char b);

    QJsonEncodeException reportError(String errMsg);

    QJsonEncodeException reportError(String errMsg, Exception cause);
}
