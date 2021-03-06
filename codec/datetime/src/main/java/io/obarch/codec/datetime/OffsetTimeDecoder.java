package io.obarch.codec.datetime;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

class OffsetTimeDecoder implements Decoder {

    private final Decoder arrDecoder;

    public OffsetTimeDecoder(Decoder.Provider spi) {
        this.arrDecoder = spi.decoderOf(int[].class);
    }

    @Override
    public Object decode(DecoderSource source) {
        int[] arr = (int[]) source.decodeValue(arrDecoder);
        LocalTime localTime = LocalTime.of(arr[0], arr[1], arr[2], arr[3]);
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(arr[4]);
        return OffsetTime.of(localTime, offset);
    }
}
