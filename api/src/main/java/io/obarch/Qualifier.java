package io.obarch;

import java.util.Arrays;

public final class Qualifier {

    private final String[] segments;

    public Qualifier(String... segments) {
        this.segments = segments;
    }

    public String[] segments() {
        return segments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qualifier qualifier = (Qualifier) o;
        return Arrays.equals(segments, qualifier.segments);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(segments);
    }
}
