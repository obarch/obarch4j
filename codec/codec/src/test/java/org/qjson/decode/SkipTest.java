package org.qjson.decode;

import org.junit.Test;

import static io.obarch.livedoc.TestInMarkdown.myTestData;
import static io.obarch.livedoc.TestInMarkdown.stripQuote;

public class SkipTest {

    @Test
    public void skip() {
        myTestData().assertTrue(code -> {
            BytesDecoderSource source = new BytesDecoderSource( stripQuote(code) + "true");
            source.skip();
            return source.decodeBoolean();
        });
        myTestData().assertTrue(code -> {
            StringDecoderSource source = new StringDecoderSource( stripQuote(code) + "true");
            source.skip();
            return source.decodeBoolean();
        });
    }
}
