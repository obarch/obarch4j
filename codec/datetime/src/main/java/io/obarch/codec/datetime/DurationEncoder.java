package io.obarch.codec.datetime;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

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
