# Any

There are times we want to encode and decode QJSON without corresponding Java classes.
The natural choice is to use `ArrayList` and `HashMap`.
`Any` is a syntax sugar for this usage pattern to make the Java code with less type casting.

# create_and_encode

With `AnyMap` and `AnyList` we can construct a object graph with one line.
Then we can encode it as QJSON:

```java
print(QJSON.stringify(
    new AnyList(
        new AnyMap(
            "a", "b", 
            "c", "d"),
        new AnyList("e1", "e2")
    )
));
// Output:
// [{"a":"b","c":"d"},["e1","e2"]]
```

<hide>

```java
package demo;
import io.obarch.codec.QJSON;
import io.obarch.codec.any.*;
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

# decode_and_access_via_path

Decode QJSON without any type will return `Any` by default. The interface is defined like this:

<<< @/qjson-java-core/src/main/java/io.obarch.codec.any/Any.java

We can use one `get` method call to access any value in the graph via path.

```java
Any decoded = QJSON.parse("[{'a':'b','c':'d'},['e1','e2']]".replace('\'', '"'));
// 0: get the 0th element
// "c": get the key of "c"
Assert.assertEquals("d", decoded.get(0, "c"));
```

<hide>

```java
package demo;
import io.obarch.codec.QJSON;
import io.obarch.codec.any.*;
import java.util.*;
import org.junit.Assert;

public class Demo {
    
    public static void demo() {
        {{ CODE }}
    }
}
```

</hide>

