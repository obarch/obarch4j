# Decoder

The interface of `Decoder` is defined like this:

<<< @/qjson-java-core/src/main/java/org/qjson/spi/Decoder.java

We can define our own decoder to customize the decoding behavior.

# decoder_for_type

QJSON by default decode `java.time.OffsetDateTime` from epoch milliseconds.
Let's define a decoder to decode from pretty print string instead.

<<< @/docs/spi/decoder/DemoDateDecoder.java

```java
QJSON.Config cfg = new QJSON.Config();
cfg.chooseDecoder = (qjson, type) -> {
    if (OffsetDateTime.class.equals(type)) {
        return new DemoDateDecoder();
    }
    return null;
};
QJSON qjson = new QJSON(cfg);
OffsetDateTime date = OffsetDateTime.of(LocalDateTime.of(
        2008, 8, 8, 0, 0), ZoneOffset.UTC);
Assert.assertEquals(date, qjson.decode(OffsetDateTime.class, "\"2008-08-08T00:00:00Z\""));
```


<hide>

```java
package demo;
import java.time.*;
import QJSON;
import DemoDateDecoder;
import java.util.*;
import org.junit.Assert;

public class Demo {
    
    public static void demo() {
        {{ CODE }}
    }
}
```

</hide>
