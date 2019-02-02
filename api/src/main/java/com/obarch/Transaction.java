package com.obarch;

public final class Transaction {

    private final EventSchema schema;
    private final Object[] kv;

    public Transaction(EventSchema schema, Object[] kv) {
        this.schema = schema;
        this.kv = kv;
    }

    public EventSchema schema() {
        return schema;
    }

    public Object[] kv() {
        return kv;
    }
}
