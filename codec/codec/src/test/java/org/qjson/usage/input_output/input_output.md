# Input & Output

QJSON can decode from string or byte[] and encode to string or byte[]. 
But `InputStream` and `OutputStream` is not supported.
Internally string and byte[] are handled in its own optimal implementation.
Because string is encoded as utf-16 chars in JVM, but byte[] is UTF-8 (assumed by QJSON).

The static methods of `QJSON` support string only. To use byte[] we need a instance of QJSON
```java
QJSON qjson = new QJSON();
// use qjson.encode or qjson.decode
```

# decode_input

We use built-in `Any.class` as an example here. You can decode into your own class.

decode from string

```java
Assert.assertEquals(
        new AnyMap("a", "b"), 
        qjson.decode(Any.class, "{\"a\":\"b\"}"));
```

decode from bytes

```java
byte[] bytes = "{\"a\":\"b\"}".getBytes(StandardCharsets.UTF_8);

Assert.assertEquals(
        new AnyMap("a", "b"), 
        qjson.decode(Any.class, bytes));
```

decode from partial bytes

```java
byte[] bytes = "{\"a\":\"b\"}".getBytes(StandardCharsets.UTF_8);

Assert.assertEquals(
        "a", 
        qjson.decode(Any.class, bytes, 1, 4).get());
```

<hide>

```java
package demo;
import org.qjson.QJSON;
import org.qjson.any.*;
import org.junit.Assert;
import java.nio.charset.StandardCharsets;

public class Demo {
    
    public static void demo() {
        QJSON qjson = new QJSON();
        {{ CODE }}
    }
}
```

</hide>

# encode_output

encode to string

```java
Assert.assertEquals("true", qjson.encode(true));
```

encode to string builder

```java
StringBuilder stringBuilder = new StringBuilder();
qjson.encode(true, stringBuilder);
Assert.assertEquals("true", stringBuilder.toString());
```

encode to byte[]

```java
BytesBuilder bytesBuilder = new org.qjson.encode.BytesBuilder();
qjson.encode(true, bytesBuilder);
Assert.assertArrayEquals(new byte[]{'t','r','u','e'}, bytesBuilder.copyOfBytes());
```

<hide>

```java
package demo;
import org.qjson.QJSON;
import org.qjson.encode.BytesBuilder;
import org.qjson.any.*;
import org.junit.Assert;
import java.nio.charset.StandardCharsets;

public class Demo {
    
    public static void demo() {
        QJSON qjson = new QJSON();
        {{ CODE }}
    }
}
```

</hide>