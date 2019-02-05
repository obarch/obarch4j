package io.obarch;

public final class LogSite {

    private int logSiteId;
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

    void allocatedId(int logSiteId) {
        this.logSiteId = logSiteId;
    }
}
