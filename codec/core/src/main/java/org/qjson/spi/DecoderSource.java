package org.qjson.spi;

import org.qjson.decode.QJsonDecodeException;
import org.qjson.encode.CurrentPath;

public interface DecoderSource {

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

        public static int count() {
            return count;
        }
    }

    boolean decodeNull();

    boolean decodeBoolean();

    int decodeInt();

    long decodeLong();

    double decodeDouble();

    String decodeString();

    Object decodeStringOrNumber();

    byte[] decodeBytes();

    Object decodeRef(Decoder decoder);

    Object decodeObject(Decoder decoder);

    Object decodeObject(Decoder decoder, boolean track);

    CurrentPath currentPath();

    default <T> T borrowAttachment(AttachmentKey<T> key) {
        T attachment = getAttachment(key);
        setAttachment(key, null);
        return attachment;
    }

    <T> T getAttachment(AttachmentKey<T> key);

    <T> void setAttachment(AttachmentKey<T> key, T attachment);

    int mark();

    String sinceMark(int mark);

    byte read();

    byte peek();

    void next();

    void skip();

    QJsonDecodeException reportError(String errMsg);

    QJsonDecodeException reportError(String errMsg, Exception cause);
}
