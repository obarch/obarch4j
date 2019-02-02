package io.obarch.codec.datetime;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

import java.time.MonthDay;

class MonthDayEncoder implements Encoder {
    @Override
    public void encode(EncoderSink sink, Object val) {
        MonthDay monthDay = (MonthDay) val;
        sink.write('[');
        sink.encodeInt(monthDay.getMonthValue());
        sink.write(',');
        sink.encodeInt(monthDay.getDayOfMonth());
        sink.write(']');
    }
}
