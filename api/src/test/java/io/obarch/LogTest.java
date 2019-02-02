package io.obarch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LogTest {

    @Before
    public void setup() {
        OB.SPI.reset();
    }

    @Test
    public void no_arg_no_attribute() {
        List<Event> events = new ArrayList<>();
        OB.SPI.registerEventHandler(events::add);
        OB.log("some event");
        Assert.assertEquals(1, events.size());
        Assert.assertEquals("some event", events.get(0).schema().eventName);
    }

    @Test
    public void one_arg() {
        List<Event> events = new ArrayList<>();
        OB.SPI.registerEventHandler(events::add);
        OB.log("some event", "arg1", 100);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals("some event", events.get(0).schema().eventName);
        Assert.assertArrayEquals(new String[]{"arg1"}, events.get(0).schema().argNames);
        Assert.assertArrayEquals(new String[]{"100"}, events.get(0).argValues());
    }

    @Test
    public void two_args() {
        List<Event> events = new ArrayList<>();
        OB.SPI.registerEventHandler(events::add);
        OB.log("some event", "arg1", 100, "arg2", "hello");
        Assert.assertEquals(1, events.size());
        Assert.assertEquals("some event", events.get(0).schema().eventName);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, events.get(0).schema().argNames);
        Assert.assertArrayEquals(new String[]{"100", "hello"}, events.get(0).argValues());
    }

    @Test
    public void level_attribute() {
        List<Event> events = new ArrayList<>();
        OB.SPI.registerEventHandler(events::add);
        OB.log("some event", Event.LEVEL, Event.DEBUG);
        Assert.assertEquals(1, events.size());
        Assert.assertArrayEquals(new String[0], events.get(0).schema().argNames);
        Assert.assertEquals(Event.DEBUG, events.get(0).schema().level);
    }

    @Test
    public void stat_value() {
        List<Event> events = new ArrayList<>();
        OB.SPI.registerEventHandler(events::add);
        OB.log("some event", Event.STAT_VALUE, 100);
        Assert.assertEquals(1, events.size());
        Assert.assertArrayEquals(new String[0], events.get(0).schema().argNames);
        Assert.assertEquals(100L, events.get(0).statValue());
    }
}
