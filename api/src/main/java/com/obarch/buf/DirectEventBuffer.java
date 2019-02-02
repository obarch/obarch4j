package com.obarch.buf;

import com.obarch.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DirectEventBuffer implements EventBuffer {

    private final Event[] events;
    private final int max;
    private int size;
    private int tail;

    public DirectEventBuffer(int max) {
        events = new Event[max];
        this.max = max;
    }

    public DirectEventBuffer() {
        this(1024 * 1024 * 16);
    }

    @Override
    public void accept(Event event) {
        events[tail % max] = event;
        tail++;
        if (tail > 2 * max) {
            tail -= max;
        }
        if (size < max) {
            size++;
        }
    }

    @Override
    public List<Event> execute(Predicate<Event> predicate, long from, int limit) {
        List<Event> filtered = new ArrayList<>();
        int fromIndex = searchIndex(from);
        if (fromIndex == -1) {
            fromIndex = tail - size;
        }
        for (int i = fromIndex; i < tail; i++) {
            Event event = events[i % max];
            if (predicate.test(event)) {
                filtered.add(event);
            }
            if (filtered.size() == limit) {
                break;
            }
        }
        return filtered;
    }

    private int searchIndex(long targetSeq) {
        int low = tail - size;
        int high = tail - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            long midVal = events[mid % max].seq();
            int cmp = Long.compare(midVal, targetSeq);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid;
        }
        return -1;
    }
}
