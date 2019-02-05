package io.obarch.log4j2;

import io.obarch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StructuredDataMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class Log4j2LogHandler implements LogSiteWatcher, EventHandler {

    private final static Map<io.obarch.Level, org.apache.logging.log4j.Level> levelMap = new HashMap<io.obarch.Level, org.apache.logging.log4j.Level>() {{
        put(Level.TRACE, org.apache.logging.log4j.Level.TRACE);
        put(Level.DEBUG, org.apache.logging.log4j.Level.DEBUG);
        put(Level.INFO, org.apache.logging.log4j.Level.INFO);
        put(Level.WARN, org.apache.logging.log4j.Level.WARN);
        put(Level.ERROR, org.apache.logging.log4j.Level.ERROR);
    }};
    private final List<Consumer<Event>> loggers = new CopyOnWriteArrayList<>();

    public static void register(OB.SPI spi) {
        Log4j2LogHandler handler = new Log4j2LogHandler();
        spi.registerEventHandler(handler);
        spi.registerLogSiteWatcher(handler);
    }

    @Override
    public void onLogSiteAdded(LogSite logSite, Object[] sample) {
        Logger logger = LogManager.getLogger(logSite.className());
        loggers.add(event -> {
            org.apache.logging.log4j.Level level = levelMap.get(event.level());
            StructuredDataMessage msg = new StructuredDataMessage(
                    String.valueOf(event.seq()), logSite.eventName(), "Obarch",
                    convert(event.props()));
            logger.log(level, msg);
        });
    }

    @Override
    public void handleEvent(Event event) {
        loggers.get(event.logSiteId()).accept(event);
    }

    private static Map<String, String> convert(String[] props) {
        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < props.length; i += 2) {
            data.put(props[i], props[i + 1]);
        }
        return data;
    }
}
