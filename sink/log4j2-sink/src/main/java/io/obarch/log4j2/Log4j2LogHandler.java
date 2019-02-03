package io.obarch.log4j2;

import io.obarch.LogHandler;
import io.obarch.LogSite;
import io.obarch.LogSiteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Log4j2LogHandler implements LogSiteHandler, LogHandler {

    private final List<Logger> loggers = new CopyOnWriteArrayList<>();

    @Override
    public void handleLogSite(LogSite logSite, Object[] kv) {
        loggers.add(LogManager.getLogger(logSite.className()));
    }

    @Override
    public void handleLog(int logSiteId, Object[] kv) {
        loggers.get(logSiteId).info(kv);
    }
}
