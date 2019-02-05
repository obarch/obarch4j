package io.obarch;

public final class LogSite {

    public static final String STAT_VALUE = "STAT_VALUE";
    public static final String LEVEL = "LEVEL";
    private int logSiteId;
    private int levelIndex = -1;
    private int statValueIndex = -1;
    private final String className;
    private final String methodName;
    private final String fileName;
    private final int lineNumber;
    private final String eventName;
    // when LogSite is registered before LogSiteWatcher
    // we keep the kv here
    Object[] kv;

    public LogSite(String className, String methodName, String fileName, int lineNumber, String eventName) {
        this.className = className;
        this.methodName = methodName;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.eventName = eventName;
    }

    public int logSiteId() {
        return logSiteId;
    }

    public String className() {
        return className;
    }

    public String methodName() {
        return methodName;
    }

    public String fileName() {
        return fileName;
    }

    public int lineNumber() {
        return lineNumber;
    }

    public String eventName() {
        return eventName;
    }

    void initialize(int logSiteId, Object[] kv) {
        this.logSiteId = logSiteId;
        for (int i = 0; i < kv.length; i += 2) {
            if (LEVEL.equals(kv[i])) {
                levelIndex = i + 1;
            } else if (STAT_VALUE.equals(kv[i])) {
                statValueIndex = i + 1;
            }
        }
    }

    public Level getLevel(Object[] kv) {
        if (levelIndex == -1) {
            return Level.TRACE;
        }
        return (Level) kv[levelIndex];
    }

    public long getStatValue(Object[] kv) {
        if (statValueIndex == -1) {
            return 0;
        }
        Number obj = (Number) kv[statValueIndex];
        return obj.longValue();
    }
}
