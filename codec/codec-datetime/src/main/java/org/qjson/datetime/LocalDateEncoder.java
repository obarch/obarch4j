package org.qjson.datetime;

import org.qjson.spi.Encoder;
import org.qjson.spi.EncoderSink;

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
