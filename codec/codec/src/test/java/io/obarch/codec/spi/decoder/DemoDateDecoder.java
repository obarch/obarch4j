package io.obarch.codec.spi.decoder;

import io.obarch.codec.spi.Decoder;
import io.obarch.codec.spi.DecoderSource;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DemoDateDecoder implements Decoder {

    @Override
    public Object decode(DecoderSource source) {
        String text = source.decodeString();
        return OffsetDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
    }
}
