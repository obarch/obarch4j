package com.obarch;

public final class Event {

    public static final String STAT_VALUE = "STAT_VALUE";
    public static final String LEVEL = "LEVEL";
    public static final Level TRACE = Level.TRACE;
    public static final Level DEBUG = Level.DEBUG;
    public static final Level INFO = Level.INFO;
    public static final Level WARN = Level.WARN;
    public static final Level ERROR = Level.ERROR;
    public static final Level FATAL = Level.FATAL;

    private final EventSchema schema;
    private final String[] argValues;
    private final long statValue;
    private final long seq;
    private final long timestamp;

    public Event(long seq, long timestamp, EventSchema schema, String[] argValues, long statValue) {
        this.seq = seq;
        this.timestamp = timestamp;
        this.schema = schema;
        this.argValues = argValues;
        this.statValue = statValue;
    }

    public EventSchema schema() {
        return schema;
    }

    public String[] argValues() {
        return argValues;
    }

    public long statValue() {
        return statValue;
    }

    public long seq() {
        return seq;
    }

    public long timestamp() {
        return timestamp;
    }
}
