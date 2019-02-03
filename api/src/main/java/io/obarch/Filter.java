package io.obarch;

public interface Filter {
    /**
     * Should the log become a event
     * @param logSiteId
     * @param kv
     * @return true to proceed, false to discard the log
     */
    boolean shouldLog(int logSiteId, Object[] kv);
}
