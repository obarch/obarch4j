package io.obarch.codec.datetime;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

import java.time.*;

class ZonedDateTimeEncoder implements Encoder {
    @Override
    public void encode(EncoderSink sink, Object val) {
        ZonedDateTime zonedDateTime = (ZonedDateTime) val;
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        ZoneOffset offset = zonedDateTime.getOffset();
        String zone = zonedDateTime.getZone().getId();
        sink.write('[');
        sink.write('[');
        sink.encodeInt(localDateTime.getYear());
        sink.write(',');
        sink.encodeInt(localDateTime.getMonthValue());
        sink.write(',');
        sink.encodeInt(localDateTime.getDayOfMonth());
        sink.write(',');
        sink.encodeInt(localDateTime.getHour());
        sink.write(',');
        sink.encodeInt(localDateTime.getMinute());
        sink.write(',');
        sink.encodeInt(localDateTime.getSecond());
        sink.write(',');
        sink.encodeInt(localDateTime.getNano());
        sink.write(',');
        sink.encodeInt(offset.getTotalSeconds());
        sink.write(']');
        sink.write(',');
        sink.encodeString(zone);
        sink.write(']');
    }
}
