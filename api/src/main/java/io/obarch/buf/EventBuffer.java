package io.obarch.buf;

import io.obarch.Event;
import io.obarch.EventHandler;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface EventBuffer extends EventHandler {

    // execute returns null if end reached
    List<Event> execute(Predicate<Event> predicate, long from, int limit);
}
