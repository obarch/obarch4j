package io.obarch.probe.http;

import fi.iki.elonen.NanoHTTPD;
import io.obarch.Event;
import io.obarch.buf.EventBuffer;
import io.obarch.codec.QJSON;

import java.util.List;

public class HttpProbe extends NanoHTTPD {

    private final EventBuffer eventBuffer;

    public HttpProbe(String hostname, int port, EventBuffer eventBuffer) {
        super(hostname, port);
        this.eventBuffer = eventBuffer;
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (Method.GET.equals(session.getMethod()) && session.getUri().equals("/events")) {
            List<Event> events = eventBuffer.execute(e -> true, 0, 1000);
            return newFixedLengthResponse(QJSON.stringify(events));
        }
        return super.serve(session);
    }
}
