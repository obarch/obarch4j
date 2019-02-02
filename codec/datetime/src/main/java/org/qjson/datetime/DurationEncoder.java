package org.qjson.datetime;

import org.qjson.spi.Encoder;
import org.qjson.spi.EncoderSink;

import java.time.Duration;

class DurationEncoder implements Encoder {
    @Override
    public void encode(EncoderSink sink, Object val) {
        Duration duration = (Duration) val;
        sink.write('[');
        sink.encodeLong(duration.getSeconds());
        sink.write(',');
        sink.encodeInt(duration.getNano());
        sink.write(']');
    }
}
