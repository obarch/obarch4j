package io.obarch.codec.datetime;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

import java.time.YearMonth;

class YearMonthEncoder implements Encoder {
    @Override
    public void encode(EncoderSink sink, Object val) {
        YearMonth yearMonth = (YearMonth) val;
        sink.write('[');
        sink.encodeInt(yearMonth.getYear());
        sink.write(',');
        sink.encodeInt(yearMonth.getMonthValue());
        sink.write(']');
    }
}
