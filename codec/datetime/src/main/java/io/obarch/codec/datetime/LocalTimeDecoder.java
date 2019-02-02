package io.obarch.codec.datetime;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;

import java.time.LocalTime;

class LocalTimeDecoder implements Decoder {

    private final Decoder arrDecoder;

    public LocalTimeDecoder(Decoder.Provider spi) {
        this.arrDecoder = spi.decoderOf(int[].class);
    }

    @Override
    public Object decode(DecoderSource source) {
        int[] arr = (int[]) source.decodeObject(arrDecoder);
        return LocalTime.of(arr[0], arr[1], arr[2], arr[3]);
    }
}
