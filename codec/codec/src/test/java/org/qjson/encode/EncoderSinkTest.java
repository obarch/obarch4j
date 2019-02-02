package org.qjson.encode;

import org.junit.Assert;
import org.junit.Test;

public class EncoderSinkTest {

    @Test
    public void encode_boolean_to_bytes() {
        BytesEncoderSink sink = new BytesEncoderSink();
        sink.encodeBoolean(true);
        Assert.assertEquals("true", sink.toString());
        sink = new BytesEncoderSink();
        sink.encodeBoolean(false);
        Assert.assertEquals("false", sink.toString());
    }

    @Test
    public void encode_boolean_to_string() {
        StringEncoderSink sink = new StringEncoderSink();
        sink.encodeBoolean(true);
        Assert.assertEquals("true", sink.toString());
        sink = new StringEncoderSink();
        sink.encodeBoolean(false);
        Assert.assertEquals("false", sink.toString());
    }

    @Test
    public void encode_null_to_bytes() {
        BytesEncoderSink sink = new BytesEncoderSink();
        sink.encodeNull();
        Assert.assertEquals("null", sink.toString());
    }

    @Test
    public void encode_null_to_string() {
        StringEncoderSink sink = new StringEncoderSink();
        sink.encodeNull();
        Assert.assertEquals("null", sink.toString());
    }
}
