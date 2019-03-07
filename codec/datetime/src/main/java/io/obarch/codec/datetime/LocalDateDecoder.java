package io.obarch.codec.datetime;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;

import java.time.LocalDate;

class LocalDateDecoder implements Decoder {

    private final Decoder arrDecoder;

    public LocalDateDecoder(Decoder.Provider spi) {
        arrDecoder = spi.decoderOf(int[].class);
    }

    @Override
    public Object decode(DecoderSource source) {
        int[] arr = (int[]) source.decodeValue(arrDecoder);
        return LocalDate.of(arr[0], arr[1], arr[2]);
    }
}
