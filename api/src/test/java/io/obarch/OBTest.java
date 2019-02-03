package io.obarch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OBTest {

    @Before
    public void setup() {
        OB.SPI.__reset();
    }

    @Test
    public void no_arg_no_attribute() {
        List<Event> events = new ArrayList<>();
        OB.SPI.initialize(spi -> spi.registerEventHandler(events::add));
        OB.log("some event");
        Assert.assertEquals(1, events.size());
        Assert.assertEquals("some event", events.get(0).logSite().eventName());
    }

    @Test
    public void one_arg() {
        List<Event> events = new ArrayList<>();
        OB.SPI.initialize(spi -> spi.registerEventHandler(events::add));
        OB.log("some event", "arg1", 100);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals("some event", events.get(0).logSite().eventName());
        Assert.assertArrayEquals(new String[]{"arg1", "100"}, events.get(0).props());
    }

    @Test
    public void two_args() {
        List<Event> events = new ArrayList<>();
        OB.SPI.initialize(spi -> spi.registerEventHandler(events::add));
        OB.log("some event", "arg1", 100, "arg2", "hello");
        Assert.assertEquals(1, events.size());
        Assert.assertEquals("some event", events.get(0).logSite().eventName());
        Assert.assertArrayEquals(new String[]{"arg1", "100", "arg2", "hello"}, events.get(0).props());
    }
}
