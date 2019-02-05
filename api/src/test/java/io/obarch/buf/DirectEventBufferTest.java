package io.obarch.buf;

import io.obarch.Event;
import io.obarch.Level;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DirectEventBufferTest {

    @Test
    public void one_element() {
        DirectEventBuffer buf = new DirectEventBuffer();
        buf.handleEvent(event(0));
        List<Event> events = buf.execute(event -> true, 0, 1);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(0, events.get(0).seq());
    }

    @Test
    public void empty() {
        DirectEventBuffer buf = new DirectEventBuffer();
        List<Event> events = buf.execute(event -> true, 0, 1);
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void filter_by_predicate() {
        DirectEventBuffer buf = new DirectEventBuffer();
        buf.handleEvent(event(0));
        List<Event> events = buf.execute(event -> event.seq() != 0, 0, 1);
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void limit() {
        DirectEventBuffer buf = new DirectEventBuffer();
        buf.handleEvent(event(0));
        buf.handleEvent(event(1));
        List<Event> events = buf.execute(event -> true, 0, 1);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(0, events.get(0).seq());
    }

    @Test
    public void from() {
        DirectEventBuffer buf = new DirectEventBuffer();
        buf.handleEvent(event(0));
        buf.handleEvent(event(1));
        List<Event> events = buf.execute(event -> true, 1, 1);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(1, events.get(0).seq());
    }

    @Test
    public void from_not_found() {
        DirectEventBuffer buf = new DirectEventBuffer();
        buf.handleEvent(event(0));
        buf.handleEvent(event(1));
        List<Event> events = buf.execute(event -> true, -1, 1);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(0, events.get(0).seq());
    }

    @Test
    public void wrap_around() {
        DirectEventBuffer buf = new DirectEventBuffer(1);
        buf.handleEvent(event(0));
        buf.handleEvent(event(1));
        List<Event> events = buf.execute(event -> true, 0, 1);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(1, events.get(0).seq());
    }

    @Test
    public void full() {
        DirectEventBuffer buf = new DirectEventBuffer(3);
        buf.handleEvent(event(0));
        buf.handleEvent(event(1));
        buf.handleEvent(event(2));
        List<Event> events = buf.execute(event -> true, 0, 10);
        Assert.assertEquals(3, events.size());
        Assert.assertEquals(0, events.get(0).seq());
        Assert.assertEquals(1, events.get(1).seq());
        Assert.assertEquals(2, events.get(2).seq());
    }

    @Test
    public void overflow() {
        DirectEventBuffer buf = new DirectEventBuffer(2);
        buf.handleEvent(event(0));
        buf.handleEvent(event(1));
        buf.handleEvent(event(2));
        buf.handleEvent(event(3));
        buf.handleEvent(event(4));
        List<Event> events = buf.execute(event -> true, 0, 10);
        Assert.assertEquals(2, events.size());
        Assert.assertEquals(3, events.get(0).seq());
        Assert.assertEquals(4, events.get(1).seq());
    }

    @NotNull
    public Event event(int seq) {
        return new Event(seq, 0, null, Level.TRACE, 0, null);
    }
}
