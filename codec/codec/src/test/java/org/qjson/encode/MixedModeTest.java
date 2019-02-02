package org.qjson.encode;

import org.junit.Assert;
import org.junit.Test;
import org.qjson.QJSON;
import org.qjson.reflection.ArrayEncoder;

public class MixedModeTest {

    @Test
    public void mixed_mode_encoder() throws Exception {
        QJSON.Config cfg = new QJSON.Config();
        cfg.mode = QJSON.Mode.MIXED;
        QJSON qjson = new QJSON(cfg);
        Assert.assertTrue(qjson.encoderOf(int[].class) instanceof ArrayEncoder);
        for (int i = 0; i < 10; i++) {
            Thread.sleep(i * 100);
            if (!(qjson.encoderOf(int[].class) instanceof ArrayEncoder)) {
                return;
            }
        }
        Assert.fail("mixed mode should switch implementation in background thread");
    }
}
