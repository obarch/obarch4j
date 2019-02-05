package io.obarch.log4j2;

import io.obarch.OB;
import org.junit.BeforeClass;
import org.junit.Test;

public class Log4j2Test {

    @BeforeClass
    public static void setup() {
        String dir = Log4j2Test.class.getPackage().getName().replace(".", "/");
        System.setProperty("log4j.configurationFile", dir + "/log4j2.xml");
    }

    @Test
    public void log_event() {
        OB.SPI.initialize(Log4j2LogHandler::register);
        OB.log("example", "a", "b", OB.LEVEL, OB.INFO);
    }
}
