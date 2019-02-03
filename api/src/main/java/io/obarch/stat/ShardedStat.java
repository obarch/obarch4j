package io.obarch.stat;

import io.obarch.LogHandler;

import java.util.function.Supplier;

public class ShardedStat implements LogHandler {

    private final Supplier<LogHandler> supplier;
    private final int shardsCount;

    public ShardedStat(Supplier<LogHandler> supplier, int shardsCount) {
        this.supplier = supplier;
        this.shardsCount = shardsCount;
    }

    @Override
    public void handleLog(int logSiteId, Object[] kv) {

    }
}
