package io.obarch.codec.datetime;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

import java.time.LocalDate;

class LocalDateEncoder implements Encoder {
    @Override
    public void encode(EncoderSink sink, Object val) {
        LocalDate localDate = (LocalDate) val;
        sink.write('[');
        sink.encodeInt(localDate.getYear());
        sink.write(',');
        sink.encodeInt(localDate.getMonthValue());
        sink.write(',');
        sink.encodeInt(localDate.getDayOfMonth());
        sink.write(']');
    }
}
