package org.qjson.decode;

import org.junit.Assert;
import org.junit.Test;
import org.qjson.QJSON;
import org.qjson.reflection.ArrayDecoder;

public class MixedModeTest {

    @Test
    public void mixed_mode_decoder() throws Exception {
        QJSON.Config cfg = new QJSON.Config();
        cfg.mode = QJSON.Mode.MIXED;
        QJSON qjson = new QJSON(cfg);
        Assert.assertTrue(qjson.decoderOf(int[].class) instanceof ArrayDecoder);
        for (int i = 0; i < 10; i++) {
            Thread.sleep(i * 100);
            if (!(qjson.decoderOf(int[].class) instanceof ArrayDecoder)) {
                return;
            }
        }
        Assert.fail("mixed mode should switch implementation in background thread");
    }
}
