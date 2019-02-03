package io.obarch;

import java.util.Objects;

public final class Qualifier {

    private final String className;
    private final int lineNumber;

    public Qualifier(String className, int lineNumber) {
        this.className = className;
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qualifier qualifier = (Qualifier) o;
        return lineNumber == qualifier.lineNumber &&
                Objects.equals(className, qualifier.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, lineNumber);
    }
}
