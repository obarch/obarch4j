package io.obarch;

public interface Filter {
    /**
     * Should the log become a event
     * @param eventSchemaId
     * @param kv
     * @return true to proceed, false to discard the log
     */
    boolean shouldLog(int eventSchemaId, Object[] kv);
}
