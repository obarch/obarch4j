package io.obarch.codec.usage.inf;

public class My {

    public interface Inf {
        String getField();
    }

    private static class PrivateClass implements Inf {

        String field;

        @Override
        public String getField() {
            return field;
        }
    }

    public static class PublicClass implements Inf {

        public String field;

        @Override
        public String getField() {
            return field;
        }
    }

    public static Object newObject() {
        PrivateClass obj = new PrivateClass();
        obj.field = "hello";
        return obj;
    }
}
