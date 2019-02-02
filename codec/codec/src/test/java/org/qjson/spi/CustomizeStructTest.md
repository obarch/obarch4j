# customize_encoder

```java
package testdata;
import org.qjson.spi.*;
public class StringAsBoolean implements Encoder {
    public void encode(EncoderSink sink, Object val) {
        boolean b = Boolean.valueOf((String)val);
        sink.encodeBoolean(b);
    }
}
```

```java
package testdata;
import org.qjson.spi.*;
public class MyClass {

    @QJsonProperty(encoder = StringAsBoolean.class)
    public String field;

    public MyClass init(String field) {
        this.field = field;
        return this;
    }
}
```

| value | encoded |
| ---   | ---     |
| `new MyClass().init("true")` | `{"field":true}` |

# customize_should_encode

```java
package testdata;
import org.qjson.spi.*;
import java.util.function.Predicate;
public class OmitNull implements Predicate<Object> {
    public boolean test(Object val) {
        return val != null;
    }
}
```

```java
package testdata;
import org.qjson.spi.*;
public class MyClass {

    @QJsonProperty(shouldEncode = OmitNull.class)
    public String field1;
    public String field2;

    public MyClass init(String field2) {
        this.field2 = field2;
        return this;
    }
}
```

| value | encoded |
| ---   | ---     |
| `new MyClass().init("hello")` | `{"field2":"hello"}` |