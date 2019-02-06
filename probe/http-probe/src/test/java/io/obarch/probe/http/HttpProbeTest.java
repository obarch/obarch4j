package io.obarch.probe.http;

import io.obarch.OB;
import io.obarch.buf.DirectEventBuffer;
import io.obarch.codec.QJSON;
import io.obarch.codec.any.Any;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class HttpProbeTest {

    private HttpProbe httpProbe;

    @Before
    public void setup() throws IOException {
        OB.SPI.__reset();
        DirectEventBuffer eventBuffer = new DirectEventBuffer();
        OB.SPI.initialize(spi -> {
            spi.registerEventHandler(eventBuffer);
            httpProbe = new HttpProbe("127.0.0.1", 8079, eventBuffer);
        });
        httpProbe.start();
    }

    @After
    public void teardown() {
        httpProbe.stop();
    }

    @Test
    public void list_events() throws Exception {
        OB.log("hello", "a", "b");
        Thread.sleep(1000);
        Any resp = QJSON.parse(Http.get("http://127.0.0.1:8079/events"));
        Assert.assertEquals(1, resp.size());
        Assert.assertEquals("b", resp.get(0, "props", "a"));
    }
}
