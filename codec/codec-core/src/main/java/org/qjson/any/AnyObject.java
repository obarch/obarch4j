package org.qjson.any;

public class AnyObject implements Any {

    private final Object obj;

    public AnyObject(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object get(Object... path) {
        if (path.length == 0) {
            return obj;
        }
        throw new UnsupportedOperationException("object is not indexable");
    }
}
