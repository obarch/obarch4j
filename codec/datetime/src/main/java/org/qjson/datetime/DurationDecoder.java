package org.qjson.datetime;

import org.qjson.spi.Decoder;
import org.qjson.spi.DecoderSource;

import java.time.Duration;

class DurationDecoder implements Decoder {
    @Override
    public Object decode(DecoderSource source) {
        if (source.read() != '[') {
            throw source.reportError("expect [");
        }
        long seconds = source.decodeLong();
        if (source.read() != ',') {
            throw source.reportError("expect ,");
        }
        int nano = source.decodeInt();
        if (source.read() != ']') {
            throw source.reportError("expect ]");
        }
        return Duration.ofSeconds(seconds, nano);
    }
}
