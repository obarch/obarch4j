package io.obarch;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

// OB (observability) provides the API:
// to push event
// to let observer pull current state from resourceMap
//
// The process of pushing event is:
// instrument/log => kv => log handler => filter => event => event handler
public class OB {

    public static final String STAT_VALUE = "STAT_VALUE";
    public static final String LEVEL = "LEVEL";
    public static final Level TRACE = Level.TRACE;
    public static final Level DEBUG = Level.DEBUG;
    public static final Level INFO = Level.INFO;
    public static final Level WARN = Level.WARN;
    public static final Level ERROR = Level.ERROR;

    private static final Map<Qualifier, WeakReference<Resource>> resourceMap = new ConcurrentHashMap<>();
    private static final Map<Qualifier, LogSite> logSiteMap = new ConcurrentHashMap<>();
    private static final List<LogSite> logSites = new CopyOnWriteArrayList<>();
    private static AtomicLong seqCounter = new AtomicLong();

    private static boolean initialized = false;
    private static final List<LogSiteHandler> logSiteHandlers = new ArrayList<>();
    private static final List<LogHandler> logHandlers = new ArrayList<>();
    private static Filter filter = null;
    private static final List<EventHandler> eventHandlers = new ArrayList<>();
    private static Function<Object, String> formatter = String::valueOf;

    // register a resource to be invoked
    // normally the handler will use OB to send one or many events back
    public static void registerResource(Resource resource) {
        StackTraceElement frame = Thread.currentThread().getStackTrace()[2];
        registerResource(new Qualifier(frame.getClassName(), frame.getLineNumber()), resource);
    }

    public static void registerResource(Qualifier qualifier, Resource resource) {
        resourceMap.put(qualifier, new WeakReference<>(resource));
    }

    public static int registerLogSite(LogSite logSite, Object[] kv) {
        synchronized (logSites) {
            int logSiteId = logSites.size();
            logSite.allocatedId(logSiteId);
            if (!initialized) {
                logSite.kv = kv;
            }
            for (LogSiteHandler logSiteHandler : logSiteHandlers) {
                logSiteHandler.handleLogSite(logSite, kv);
            }
            logSites.add(logSite);
            return logSiteId;
        }
    }

    public static void log(int logSiteId, Object... kv) {
        for (LogHandler logHandler : logHandlers) {
            logHandler.handleLog(logSiteId, kv);
        }
        if (!filter.shouldLog(logSiteId, kv)) {
            return;
        }
        Event event = createEvent(logSites.get(logSiteId), kv);
        for (EventHandler eventHandler : eventHandlers) {
            eventHandler.handleEvent(event);
        }
    }

    public static void log(String eventName, Object... kv) {
        StackTraceElement frame = Thread.currentThread().getStackTrace()[2];
        Qualifier qualifier = new Qualifier(frame.getClassName(), frame.getLineNumber());
        LogSite logSite = logSiteMap.get(qualifier);
        if (logSite == null) {
            logSite = new LogSite(frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber(), eventName);
            registerLogSite(logSite, kv);
            logSiteMap.put(qualifier, logSite);
        }
        log(logSite.logSiteId(), kv);
    }

    private static Event createEvent(LogSite logSite, Object[] kv) {
        String[] props = new String[kv.length];
        for (int i = 0; i < kv.length; i += 2) {
            props[i] = (String) kv[i];
            props[i + 1] = formatter.apply(kv[i + 1]);
        }
        long seq = seqCounter.incrementAndGet();
        return new Event(seq, System.currentTimeMillis(), logSite, props);
    }

    public static class SPI {

        public static synchronized void initialize(Consumer<SPI> init) {
            if (initialized) {
                throw new IllegalStateException("OB.SPI has already been initialized");
            }
            initialized = true;
            init.accept(new SPI());
            if (filter == null) {
                if (eventHandlers.isEmpty()) {
                    filter = (logSiteId, kv) -> false;
                } else {
                    filter = (logSiteId, kv) -> true;
                }
            }
            for (LogSite logSite : logSites) {
                logSite.kv = null;
            }
        }

        private SPI() {
            // use initialize to register spi
        }

        public List<Qualifier> listResourceQualifiers() {
            throw new RuntimeException();
        }

        public Resource getResource(Qualifier qualifier) {
            throw new RuntimeException();
        }

        public void registerLogHandler(LogHandler logHandler) {
            logHandlers.add(logHandler);
        }

        public List<String> listLogSites() {
            throw new RuntimeException();
        }

        public LogSite getLogSite(Qualifier qualifier) {
            throw new RuntimeException();
        }

        public void setFilter(Filter newFilter) {
            filter = newFilter;
        }

        public void registerEventHandler(EventHandler eventHandler) {
            eventHandlers.add(eventHandler);
        }

        public void setFormatter(Function<Object, String> newFormatter) {
            formatter = newFormatter;
        }

        public LogSite getLogSite(int logSiteId) {
            return logSites.get(logSiteId);
        }

        public void registerLogSiteHandler(LogSiteHandler logSiteHandler) {
            synchronized (logSites) {
                for (LogSite logSite : logSites) {
                    logSiteHandler.handleLogSite(logSite, logSite.kv);
                }
                logSiteHandlers.add(logSiteHandler);
            }
        }

        // should only be used in test
        public static void __reset() {
            logSiteHandlers.clear();
            logSites.clear();
            eventHandlers.clear();
            initialized = false;
        }
    }
}
