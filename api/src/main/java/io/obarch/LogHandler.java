package io.obarch;

public interface LogHandler {
    void handleLog(int logSiteId, Object[] kv);
}
