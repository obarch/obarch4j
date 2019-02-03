package io.obarch;

public final class Event {

    private final LogSite logSite;
    private final String[] props;
    private final long seq;
    private final long timestamp;

    public Event(long seq, long timestamp, LogSite logSite, String[] props) {
        this.seq = seq;
        this.timestamp = timestamp;
        this.logSite = logSite;
        this.props = props;
    }

    public LogSite logSite() {
        return logSite;
    }

    public String[] props() {
        return props;
    }

    public long seq() {
        return seq;
    }

    public long timestamp() {
        return timestamp;
    }
}
