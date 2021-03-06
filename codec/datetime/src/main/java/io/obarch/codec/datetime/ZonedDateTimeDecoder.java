package io.obarch.codec.datetime;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

class ZonedDateTimeDecoder implements Decoder {

    private final Decoder arrDecoder;

    public ZonedDateTimeDecoder(Decoder.Provider spi) {
        this.arrDecoder = spi.decoderOf(int[].class);
    }

    @Override
    public Object decode(DecoderSource source) {
        if (source.read() != '[') {
            throw source.reportError("expect [");
        }
        int[] arr = (int[]) source.decodeValue(arrDecoder);
        LocalDateTime localDateTime = LocalDateTime.of(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(arr[7]);
        if (source.read() != ',') {
            throw source.reportError("expect ,");
        }
        ZoneId zone = ZoneId.of(source.decodeString());
        if (source.read() != ']') {
            throw source.reportError("expect ]");
        }
        return ZonedDateTime.ofStrict(localDateTime, offset, zone);
    }
}
