package io.obarch;

import io.obarch.codec.spi.Encoder;
import io.obarch.codec.spi.EncoderSink;

public class EventPropsEncoder implements Encoder {
    @Override
    public void encode(EncoderSink sink, Object val) {
        String[] props = (String[]) val;
        sink.write('{');
        for (int i = 0; i < props.length; i+=2) {
            if (i > 0) {
                sink.write(',');
            }
            sink.encodeString(props[i]);
            sink.write(':');
            sink.write(props[i+1]);
        }
        sink.write('}');
    }
}
