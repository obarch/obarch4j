package org.qjson.encode;

import org.junit.Assert;
import org.junit.Test;

public class BytesBuilderTest {

    @Test
    public void append() {
        BytesBuilder builder = new BytesBuilder();
        builder.append((byte) 'a');
        Assert.assertEquals("a", builder.toString());
    }

    @Test
    public void grow() {
        BytesBuilder builder = new BytesBuilder(0);
        builder.append((byte) 'a');
        Assert.assertEquals("a", builder.toString());
    }

    @Test
    public void setLength() {
        BytesBuilder builder = new BytesBuilder(0);
        builder.append((byte) 'a');
        builder.append((byte) 'b');
        builder.setLength(1);
        Assert.assertEquals("a", builder.toString());
    }
}
