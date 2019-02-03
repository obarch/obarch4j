package io.obarch;

public interface LogHandler {
    void handle(int logSiteId, Object[] kv);
}
