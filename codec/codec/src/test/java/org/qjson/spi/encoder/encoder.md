# Encoder

The interface of `Encoder` is defined like this:

<<< @/qjson-java-core/src/main/java/org/qjson/spi/Encoder.java

We can define our own encoder to customize the encoding behavior.

# encoder_for_type

QJSON by default encode `java.time.OffsetDateTime` as epoch milliseconds.
Let's define a encoder to pretty print the date time instead.

<<< @/docs/spi/encoder/DemoDateEncoder.java

```java
QJSON.Config cfg = new QJSON.Config();
cfg.chooseEncoder = (qjson, clazz) -> {
    if (OffsetDateTime.class.equals(clazz)) {
        return new DemoDateEncoder();
    }
    return null;
};
QJSON qjson = new QJSON(cfg);
OffsetDateTime date = OffsetDateTime.of(LocalDateTime.of(
        2008, 8, 8, 0, 0), ZoneOffset.UTC);
print(qjson.encode(date));
// Output:
// "2008-08-08T00:00:00Z"
```

<hide>

```java
package demo;
import java.time.*;
import org.qjson.QJSON;
import org.qjson.spi.encoder.DemoDateEncoder;
import java.util.*;
import org.junit.Assert;

public class Demo {
    
    public static void demo() {
        {{ CODE }}
    }
    
    private static void print(Object obj) {
        System.out.println(obj);
    }
}
```

</hide>

# value_object_encoder

If object referenced twice, the default behavior is to write the second reference as `\/[ref_path]`.
To encode out the object fully, we need to override the method `encodeRef`.
However, there is a common implementation to be reused:

<<< @/qjson-java-core/src/main/java/org/qjson/spi/ValueObjectEncoder.java

See how to customize [ref](/format/ref/ref.html)