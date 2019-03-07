package io.obarch.codec.datetime;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;

import java.time.LocalDateTime;

class LocalDateTimeDecoder implements Decoder {

    private final Decoder arrDecoder;

    public LocalDateTimeDecoder(Decoder.Provider spi) {
        this.arrDecoder = spi.decoderOf(int[].class);
    }

    @Override
    public Object decode(DecoderSource source) {
        int[] arr = (int[]) source.decodeValue(arrDecoder);
        return LocalDateTime.of(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
    }
}
