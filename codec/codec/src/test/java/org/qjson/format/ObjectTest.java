package org.qjson.format;

import org.qjson.QJSON;
import io.obarch.livedoc.TestData;
import org.junit.Test;

import static io.obarch.livedoc.TestInMarkdown.stripQuote;
import static io.obarch.livedoc.TestInMarkdown.myTestData;

public class ObjectTest {

    @Test
    public void decode_encode() {
        QJSON qjson = new QJSON();
        TestData testData = myTestData();
        testData.assertTrue(encoded -> {
            encoded = stripQuote(encoded);
            Object decoded = qjson.decode(Object.class, encoded);
            String encodedBack = qjson.encode(decoded);
            return encoded.equals(encodedBack);
        });
    }
}
