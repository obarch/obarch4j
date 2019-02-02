package org.qjson.datetime;

import org.qjson.spi.Decoder;
import org.qjson.spi.DecoderSource;

import java.time.YearMonth;

class YearMonthDecoder implements Decoder {

    private final Decoder arrDecoder;

    public YearMonthDecoder(Decoder.Provider spi) {
        this.arrDecoder = spi.decoderOf(int[].class);
    }

    @Override
    public Object decode(DecoderSource source) {
        int[] arr = (int[]) source.decodeObject(arrDecoder);
        return YearMonth.of(arr[0], arr[1]);
    }
}
