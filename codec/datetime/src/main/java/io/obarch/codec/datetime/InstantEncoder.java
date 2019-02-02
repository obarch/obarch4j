package io.obarch.codec.datetime;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

import java.time.Instant;

class InstantEncoder implements Encoder {
    @Override
    public void encode(EncoderSink sink, Object val) {
        Instant instant = (Instant) val;
        sink.write('[');
        sink.encodeLong(instant.getEpochSecond());
        sink.write(',');
        sink.encodeInt(instant.getNano());
        sink.write(']');
    }
}
