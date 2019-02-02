package org.qjson.datetime;

import org.qjson.spi.Decoder;
import org.qjson.spi.DecoderSource;

import java.time.MonthDay;

class MonthDayDecoder implements Decoder {

    private final Decoder arrDecoder;

    public MonthDayDecoder(Decoder.Provider spi) {
        this.arrDecoder = spi.decoderOf(int[].class);
    }

    @Override
    public Object decode(DecoderSource source) {
        int[] arr = (int[]) source.decodeObject(arrDecoder);
        return MonthDay.of(arr[0], arr[1]);
    }
}
