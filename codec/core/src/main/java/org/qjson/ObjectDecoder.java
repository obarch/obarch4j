package org.qjson;

import org.qjson.spi.Decoder;
import org.qjson.spi.DecoderSource;

class ObjectDecoder implements Decoder {

    private Decoder listDecoder;
    private Decoder mapDecoder;

    public void init(Decoder listDecoder, Decoder mapDecoder) {
        this.listDecoder = listDecoder;
        this.mapDecoder = mapDecoder;
    }

    @Override
    public Object decode(DecoderSource source) {
        byte b = source.peek();
        switch (b) {
            case '[':
                return source.decodeObject(listDecoder);
            case '{':
                return source.decodeObject(mapDecoder);
            case 't':
            case 'f':
                return source.decodeBoolean();
            case 'n':
                if (source.decodeNull()) {
                    return null;
                }
                throw source.reportError("expect null");
            case '"':
                return source.decodeStringOrNumber();
            default:
                throw source.reportError("unexpected token");
        }
    }
}
