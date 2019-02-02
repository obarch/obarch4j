package org.qjson.datetime;

import org.qjson.spi.Encoder;
import org.qjson.spi.EncoderSink;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

class OffsetTimeEncoder implements Encoder {
    @Override
    public void encode(EncoderSink sink, Object val) {
        OffsetTime offsetTime = (OffsetTime) val;
        LocalTime localTime = offsetTime.toLocalTime();
        ZoneOffset offset = offsetTime.getOffset();
        sink.write('[');
        sink.encodeInt(localTime.getHour());
        sink.write(',');
        sink.encodeInt(localTime.getMinute());
        sink.write(',');
        sink.encodeInt(localTime.getSecond());
        sink.write(',');
        sink.encodeInt(localTime.getNano());
        sink.write(',');
        sink.encodeInt(offset.getTotalSeconds());
        sink.write(']');
    }
}
