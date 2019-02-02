package com.obarch;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

// Observability is the API for:
// to push event
// to let observer pull current state from resources
//
// The process of pushing event is:
// instrument/log => transaction => tx handler => filter => event => event handler
public class Observability {

    private static final Map<Qualifier, WeakReference<Resource>> resources = new ConcurrentHashMap<>();
    private static final List<Predicate<Transaction>> whilteList = new CopyOnWriteArrayList<>();
    private static final List<Predicate<Transaction>> blackList = new CopyOnWriteArrayList<>();
    private static AtomicLong seqCounter = new AtomicLong();

    private static volatile boolean isFrozen = false;
    private static final List<Consumer<Transaction>> txHandlers = new ArrayList<>();
    private static final List<Consumer<Event>> eventHandlers = new ArrayList<>();
    private static Function<Object, String> formatter = Object::toString;

    // instrumented code will call this method
    public static void log(Qualifier qualifier, String fileName, String lineNumber, String eventName, Object[] kv) {
        EventSchema schema = EventSchema.get(qualifier, fileName, lineNumber, eventName, kv);
        Transaction tx = new Transaction(schema, kv);
        push(tx);
    }

    public static void log(String eventName, Object... kv) {
        StackTraceElement frame = Thread.currentThread().getStackTrace()[2];
        String fileName = frame.getFileName();
        String lineNumber = String.valueOf(frame.getLineNumber());
        Qualifier qualifier = new Qualifier(
                "fileName", fileName,
                "lineNumber", lineNumber,
                "eventName", eventName);
        log(qualifier, fileName, lineNumber, eventName, kv);
    }

    public static void push(Transaction tx) {
        EventSchema schema = tx.schema();
        Object[] kv = tx.kv();
        String[] argValues = new String[schema.argIndices.length];
        for (int i = 0; i < schema.argIndices.length; i++) {
            int argIndex = schema.argIndices[i];
            argValues[i] = formatter.apply(kv[argIndex]);
        }
        long statValue = 0;
        if (schema.statValueIndex != -1) {
            statValue = ((Number)kv[schema.statValueIndex]).longValue();
        }
        long seq = seqCounter.incrementAndGet();
        Event event = new Event(seq, System.currentTimeMillis(), schema, argValues, statValue);
        for (Consumer<Event> eventHandler : eventHandlers) {
            eventHandler.accept(event);
        }
    }

    // register a resource to be invoked
    // normally the handler will use Observability to send one or many events back
    public static void register(Resource resource) {
        throw new RuntimeException();
    }

    public static void register(Qualifier qualifier, Resource resource) {
        throw new RuntimeException();
    }

    public static class SPI {

        public static void freeze() {
            isFrozen = true;
        }

        public static List<Qualifier> listResourceQualifiers() {
            throw new RuntimeException();
        }

        public static Resource getResource(Qualifier qualifier) {
            throw new RuntimeException();
        }

        public static void registerTransactionHandler(Consumer<Transaction> txHandler) {
            throw new RuntimeException();
        }

        public static List<String> listEventQualifiers() {
            throw new RuntimeException();
        }

        public static EventSchema getEventSchema(Qualifier qualifier) {
            throw new RuntimeException();
        }

        public static void registerFilterToWhiteList(Predicate<Transaction> filter) {
            throw new RuntimeException();
        }

        public static void registerFilterToBlackList(Predicate<Transaction> filter) {
            throw new RuntimeException();
        }

        public static void removeAllFilters() {
        }

        public static void registerEventHandler(Consumer<Event> eventHandler) {
            if (isFrozen) {
                throw new IllegalStateException("Observability.SPI has been frozen");
            }
            eventHandlers.add(eventHandler);
        }

        public static void setFormatter(Function<Object, String> newFormatter) {
            if (isFrozen) {
                throw new IllegalStateException("Observability.SPI has been frozen");
            }
            formatter = newFormatter;
        }

        public static void reset() {
            eventHandlers.clear();
            isFrozen = false;
        }
    }
}
