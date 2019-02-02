package io.obarch.probe.http;

import org.junit.Test;

public class HttpProbeTest {

    @Test
    public void list_resources() throws Exception {
        new HttpProbe().start();
        Thread.sleep(1000 * 1000 * 24);
    }
}
