package com.obarch;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class EventSchema {

    private final static Map<Qualifier, EventSchema> schemas = new ConcurrentHashMap<>();

    public String fileName;
    public String lineNumber;
    public String eventName;
    public String[] argNames;
    public int[] argIndices;
    public Level level;

    private static final Set<String> ATTRIBUTES = new HashSet<>(Arrays.asList(
            Event.STAT_VALUE,
            Event.LEVEL));
    public int statValueIndex = -1;

    static EventSchema get(Qualifier qualifier, String fileName, String lineNumber, String eventName, Object[] kv) {
        EventSchema schema = schemas.get(qualifier);
        if (schema != null) {
            return schema;
        }
        schema = new EventSchema();
        schema.fileName = fileName;
        schema.lineNumber = lineNumber;
        schema.eventName = eventName;
        List<String> argNames = new ArrayList<>();
        List<Integer> argIndices = new ArrayList<>();
        for (int i = 0; i < kv.length; i += 2) {
            String key = (String) kv[i];
            Object value = kv[i + 1];
            if (ATTRIBUTES.contains(key)) {
                switch (key) {
                    case Event.LEVEL:
                        schema.level = (Level) value;
                        continue;
                    case Event.STAT_VALUE:
                        schema.statValueIndex = i + 1;
                        continue;
                }
                continue;
            }
            argNames.add(key);
            argIndices.add(i + 1);
        }
        schema.argNames = new String[argNames.size()];
        for (int i = 0; i < schema.argNames.length; i++) {
            schema.argNames[i] = argNames.get(i);
        }
        schema.argIndices = new int[argIndices.size()];
        for (int i = 0; i < schema.argIndices.length; i++) {
            schema.argIndices[i] = argIndices.get(i);
        }
        schemas.put(qualifier, schema);
        return schema;
    }
}
