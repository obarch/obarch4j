package io.obarch.codec.spi.encoder;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DemoDateEncoder implements Encoder {

    @Override
    public void encode(EncoderSink sink, Object val) {
        OffsetDateTime date = (OffsetDateTime) val;
        sink.encodeString(date.format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
