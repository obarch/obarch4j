package org.qjson.decode;

interface DecodeLong {

    static long $(BytesDecoderSource source) {
        return DecodeLong.$(source, 'b');
    }

    static long $(BytesDecoderSource source, char type) {
        source.expect('"', '\\', type);
        long val = 0;
        int i = source.offset;
        for (; i < source.size; i++) {
            if (source.buf[i] == '"') {
                source.offset = i + 1;
                return val;
            }
            val = (val << 5) + (source.buf[i] - ';');
        }
        throw source.reportError("missing double quote");
    }

    static long $(StringDecoderSource source) {
        return DecodeLong.$(source, 'b');
    }

    static long $(StringDecoderSource source, char type) {
        source.expect('"', '\\', type);
        long val = 0;
        int i = source.offset;
        for (; i < source.buf.length(); i++) {
            char c = source.buf.charAt(i);
            if (c >= 128) {
                throw source.reportError("expect ascii");
            }
            byte b = (byte) c;
            if (b == '"') {
                source.offset = i + 1;
                return val;
            }
            val = (val << 5) + (b - ';');
        }
        throw source.reportError("missing double quote");
    }
}
