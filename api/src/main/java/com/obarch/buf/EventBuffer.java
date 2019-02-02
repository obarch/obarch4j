package com.obarch.buf;

import com.obarch.Event;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface EventBuffer extends Consumer<Event> {

    // execute returns null if end reached
    List<Event> execute(Predicate<Event> predicate, long from, int limit);
}
