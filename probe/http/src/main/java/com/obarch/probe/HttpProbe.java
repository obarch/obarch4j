package com.obarch.probe;

import fi.iki.elonen.NanoHTTPD;

public class HttpProbe extends NanoHTTPD {

    public HttpProbe() {
        this("0.0.0.0", 8079);
    }

    public HttpProbe(String hostname, int port) {
        super(hostname, port);
    }
}
