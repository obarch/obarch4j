package org.qjson.spi.decoder;

import org.qjson.spi.Decoder;
import org.qjson.spi.DecoderSource;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DemoDateDecoder implements Decoder {

    @Override
    public Object decode(DecoderSource source) {
        String text = source.decodeString();
        return OffsetDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
    }
}
