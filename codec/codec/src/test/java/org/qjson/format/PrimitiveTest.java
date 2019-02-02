package org.qjson.format;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.qjson.decode.BytesDecoderSource;
import org.qjson.decode.StringDecoderSource;
import org.qjson.encode.BytesEncoderSink;
import org.qjson.encode.StringEncoderSink;

import java.util.List;

import static io.obarch.livedoc.TestInMarkdown.myTestData;
import static io.obarch.livedoc.TestInMarkdown.stripQuote;

public class PrimitiveTest {

    @Test
    public void type_int() {
        for (List<String> row : myTestData().table().body) {
            int val = Integer.valueOf(stripQuote(row.get(0)));
            BytesEncoderSink sink1 = newBytesEncoderSink();
            sink1.encodeInt(val);
            StringEncoderSink sink2 = newStringEncoderSink();
            sink2.encodeInt(val);
            Assert.assertEquals(row.get(0), stripQuote(row.get(1)), sink1.toString());
            Assert.assertEquals(row.get(0), stripQuote(row.get(1)), sink2.toString());
        }
        for (List<String> row : myTestData().table().body) {
            int expected = Integer.valueOf(stripQuote(row.get(0)));
            int actual1 = new BytesDecoderSource(stripQuote(row.get(1)).getBytes()).decodeInt();
            int actual2 = new StringDecoderSource(stripQuote(row.get(1))).decodeInt();
            Assert.assertEquals(row.get(0), expected, actual1);
            Assert.assertEquals(row.get(0), expected, actual2);
        }
    }

    @Test
    public void type_long() {
        for (List<String> row : myTestData().table().body) {
            long val = Long.valueOf(stripQuote(row.get(0)));
            BytesEncoderSink sink1 = newBytesEncoderSink();
            sink1.encodeLong(val);
            StringEncoderSink sink2 = newStringEncoderSink();
            sink2.encodeLong(val);
            Assert.assertEquals(row.get(0), stripQuote(row.get(1)), sink1.toString());
            Assert.assertEquals(row.get(0), stripQuote(row.get(1)), sink2.toString());
        }
        for (List<String> row : myTestData().table().body) {
            long expected = Long.valueOf(stripQuote(row.get(0)));
            long actual1 = (Long) new BytesDecoderSource(stripQuote(row.get(1)).getBytes()).decodeStringOrNumber();
            long actual2 = (Long) new StringDecoderSource(stripQuote(row.get(1))).decodeStringOrNumber();
            Assert.assertEquals(row.get(0), expected, actual1);
            Assert.assertEquals(row.get(0), expected, actual2);
        }
    }

    @Test
    public void type_double() {
        for (List<String> row : myTestData().table().body) {
            double val = Double.valueOf(stripQuote(row.get(0)));
            BytesEncoderSink sink1 = newBytesEncoderSink();
            sink1.encodeDouble(val);
            StringEncoderSink sink2 = newStringEncoderSink();
            sink2.encodeDouble(val);
            Assert.assertEquals(row.get(0), stripQuote(row.get(1)), sink1.toString());
            Assert.assertEquals(row.get(0), stripQuote(row.get(1)), sink2.toString());
        }
        for (List<String> row : myTestData().table().body) {
            String expected = stripQuote(row.get(0));
            String actual1 = String.valueOf(new BytesDecoderSource(stripQuote(row.get(1)).getBytes()).decodeStringOrNumber());
            String actual2 = String.valueOf(new StringDecoderSource(stripQuote(row.get(1))).decodeStringOrNumber());
            Assert.assertEquals(row.get(0), expected, actual1);
            Assert.assertEquals(row.get(0), expected, actual2);
        }
    }

    @Test
    public void type_string() {
        for (List<String> row : myTestData().table().body) {
            String input = stripQuote(row.get(0));
            if (input.startsWith("0x")) {
                char c = (char) Long.parseLong(input.substring(2), 16);
                input = new String(new char[]{c});
            }
            BytesEncoderSink sink1 = newBytesEncoderSink();
            sink1.encodeString(input);
            StringEncoderSink sink2 = newStringEncoderSink();
            sink2.encodeString(input);
            Assert.assertEquals(row.get(0), stripQuote(row.get(1)), sink1.toString());
            Assert.assertEquals(row.get(0), stripQuote(row.get(1)), sink2.toString());
        }
        for (List<String> row : myTestData().table().body) {
            String expected = stripQuote(row.get(0));
            if (expected.startsWith("0x")) {
                char c = (char) Long.parseLong(expected.substring(2), 16);
                expected = new String(new char[]{c});
            }
            String actual1 = new BytesDecoderSource(stripQuote(row.get(1)).getBytes()).decodeString();
            String actual2 = new StringDecoderSource(stripQuote(row.get(1))).decodeString();
            Assert.assertEquals(row.get(0), expected, actual1);
            Assert.assertEquals(row.get(0), expected, actual2);
        }
    }

    @Test
    public void type_bytes() {
        for (List<String> row : myTestData().table().body) {
            String input = stripQuote(row.get(0));
            input = input.substring(1, input.length() - 1);
            String[] elems = input.split(" ");
            if (input.isEmpty()) {
                elems = new String[0];
            }
            byte[] bytes = new byte[elems.length];
            for (int i = 0; i < elems.length; i++) {
                String elem = elems[i];
                bytes[i] = (byte) Long.parseLong(elem, 16);
            }
            BytesEncoderSink sink1 = newBytesEncoderSink();
            sink1.encodeBytes(bytes);
            StringEncoderSink sink2 = newStringEncoderSink();
            sink2.encodeBytes(bytes);
            Assert.assertEquals(row.get(0), stripQuote(row.get(1)), sink1.toString());
            Assert.assertEquals(row.get(0), stripQuote(row.get(1)), sink2.toString());
        }
        for (List<String> row : myTestData().table().body) {
            String input = stripQuote(row.get(0));
            input = input.substring(1, input.length() - 1);
            String[] elems = input.split(" ");
            if (input.isEmpty()) {
                elems = new String[0];
            }
            byte[] expected = new byte[elems.length];
            for (int i = 0; i < elems.length; i++) {
                String elem = elems[i];
                expected[i] = (byte) Long.parseLong(elem, 16);
            }
            byte[] actual1 = new BytesDecoderSource(stripQuote(row.get(1)).getBytes()).decodeBytes();
            byte[] actual2 = new StringDecoderSource(stripQuote(row.get(1))).decodeBytes();
            Assert.assertArrayEquals(row.get(0), expected, actual1);
            Assert.assertArrayEquals(row.get(0), expected, actual2);
        }
    }

    @NotNull
    private StringEncoderSink newStringEncoderSink() {
        return new StringEncoderSink();
    }

    @NotNull
    private BytesEncoderSink newBytesEncoderSink() {
        return new BytesEncoderSink();
    }
}
