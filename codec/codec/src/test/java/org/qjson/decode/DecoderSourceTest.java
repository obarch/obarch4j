package org.qjson.decode;

import org.junit.Assert;
import org.junit.Test;

public class DecoderSourceTest {

    @Test
    public void decode_boolean_from_bytes() {
        BytesDecoderSource source = new BytesDecoderSource("true");
        Assert.assertTrue(source.decodeBoolean());
        source = new BytesDecoderSource("false");
        Assert.assertFalse(source.decodeBoolean());
    }

    @Test
    public void decode_boolean_from_string() {
        StringDecoderSource source = new StringDecoderSource("true");
        Assert.assertTrue(source.decodeBoolean());
        source = new StringDecoderSource("false");
        Assert.assertFalse(source.decodeBoolean());
    }

    @Test
    public void decode_null_from_bytes() {
        BytesDecoderSource source = new BytesDecoderSource("\"\"");
        Assert.assertFalse(source.decodeNull());
        source = new BytesDecoderSource("null");
        Assert.assertTrue(source.decodeNull());
    }

    @Test
    public void decode_null_from_string() {
        StringDecoderSource source = new StringDecoderSource("\"\"");
        Assert.assertFalse(source.decodeNull());
        source = new StringDecoderSource("null");
        Assert.assertTrue(source.decodeNull());
    }
}
