package org.qjson.junit.md;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public interface LoadClass {

    static Class $(Path dir, String className) {
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{dir.toUri().toURL()});
            return classLoader.loadClass(className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
