package org.qjson.encode;

import java.util.ArrayList;
import java.util.List;

final class TempHolder {

    private final List<Object> temps = new ArrayList<>();

    public <T> T borrowTemp(Class<T> clazz) {
        for (int i = 0; i < temps.size(); i++) {
            Object temp = temps.get(i);
            if (temp != null && temp.getClass().equals(clazz)) {
                temps.set(i, null);
                return (T) temp;
            }
        }
        return null;
    }

    public void releaseTemp(Object temp) {
        for (int i = 0; i < temps.size(); i++) {
            if (temps.get(i) == null) {
                temps.set(i, temp);
                return;
            }
        }
        temps.add(temp);
    }
}
