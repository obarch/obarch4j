package io.obarch;

import io.obarch.codec.spi.QJsonProperty;

public final class Event {

    private final LogSite logSite;
    private final Level level;
    private final long statValue;
    private final String[] props;
    private final long seq;
    private final long timestamp;

    public Event(long seq, long timestamp, LogSite logSite, Level level, long statValue, String[] props) {
        this.seq = seq;
        this.timestamp = timestamp;
        this.logSite = logSite;
        this.level = level;
        this.statValue = statValue;
        this.props = props;
    }

    public LogSite logSite() {
        return logSite;
    }

    @QJsonProperty(encoder=EventPropsEncoder.class)
    public String[] props() {
        return props;
    }

    public long seq() {
        return seq;
    }

    public long timestamp() {
        return timestamp;
    }

    public Level level() {
        return level;
    }

    public long statValue() {
        return statValue;
    }

    public int logSiteId() {
        return logSite.logSiteId();
    }
}
