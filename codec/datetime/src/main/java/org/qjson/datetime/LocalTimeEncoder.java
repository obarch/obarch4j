package org.qjson.datetime;

import org.qjson.spi.Encoder;
import org.qjson.spi.EncoderSink;

import java.time.LocalTime;

class LocalTimeEncoder implements Encoder {
    @Override
    public void encode(EncoderSink sink, Object val) {
        LocalTime localTime = (LocalTime) val;
        sink.write('[');
        sink.encodeInt(localTime.getHour());
        sink.write(',');
        sink.encodeInt(localTime.getMinute());
        sink.write(',');
        sink.encodeInt(localTime.getSecond());
        sink.write(',');
        sink.encodeInt(localTime.getNano());
        sink.write(']');
    }
}
