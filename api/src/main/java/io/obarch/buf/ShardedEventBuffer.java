package io.obarch.buf;


import io.obarch.Event;

import java.util.List;
import java.util.function.Predicate;

public class ShardedEventBuffer implements EventBuffer {

    @Override
    public void handleEvent(Event event) {
    }

    @Override
    public List<Event> execute(Predicate<Event> predicate, long from, int limit) {
        return null;
    }
}
