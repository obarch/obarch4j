package org.qjson.spi.encoder;

import org.qjson.spi.Encoder;
import org.qjson.spi.EncoderSink;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DemoDateEncoder implements Encoder {

    @Override
    public void encode(EncoderSink sink, Object val) {
        OffsetDateTime date = (OffsetDateTime) val;
        sink.encodeString(date.format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
