package io.obarch.codec.datetime;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;

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
