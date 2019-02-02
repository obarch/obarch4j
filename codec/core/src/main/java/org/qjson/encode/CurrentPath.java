package org.qjson.encode;

public final class CurrentPath {

    private final StringBuilder path = new StringBuilder();

    public int enterMapValue(String key) {
        int oldLength = path.length();
        path.append('[');
        path.append('\'');
        path.append(key, 1, key.length() - 1);
        path.append('\'');
        path.append(']');
        return oldLength;
    }

    public int enterListElement(int i) {
        int oldLength = path.length();
        path.append('[');
        path.append(i);
        path.append(']');
        return oldLength;
    }

    public int enterStructField(String field) {
        int oldLength = path.length();
        path.append('[');
        path.append('\'');
        path.append(field);
        path.append('\'');
        path.append(']');
        return oldLength;
    }

    public void exit(int oldLength) {
        path.setLength(oldLength);
    }

    @Override
    public String toString() {
        return path.toString();
    }
}
