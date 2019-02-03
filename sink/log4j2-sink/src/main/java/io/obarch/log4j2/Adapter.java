package io.obarch.log4j2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Adapter {
    public static void main(String[] args) {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.add(builder.newAppender("Stdout", "CONSOLE").add(
                builder.newLayout("PatternLayout").addAttribute("pattern", "%map %m")));
        builder.add(builder.newLogger("hello", Level.DEBUG).
                add(builder.newAppenderRef("Stdout")).
                addAttribute("additivity", false));
        BuiltConfiguration conf = builder.build(true);
        Configurator.initialize(conf);
        LoggerFactory.getLogger("hello").error("hello", "a", "b");
    }
}
