# Reference

QJSON unlike JSON, it is designed to encode any object. 
Event if the object is not a data exchange object.
Internal object representation of the application logic frequently requires circular reference,
so that QJSON has a built-in support of the reference notation.

# example

```java
Object obj = new Object();
print(QJSON.stringify(new AnyList(obj, obj)));
// Output:
// [{},"\/[0]"]
```

The `\/` means this string is a object reference. 
`[0]` is the path of the object, which is the first element of the list.


```java
Object obj = new Object();
print(QJSON.stringify(new AnyMap("a", obj, "b", obj)));
// Output:
// {"a":{},"b":"\/['a']"}
```

The path here is `['a']`, which references the key `a` of the root map.

<hide>

```java
package demo;
import io.obarch.codec.QJSON;
import org.junit.Assert;
import io.obarch.codec.any.*;

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

# circular_reference

Circular reference can be encoded and decoded. Given this class:

<<< @/docs/format/ref/Node.java

Let's formal a circular chain, and encode / decode it.

```java
Node node1 = new Node();
Node node2 = new Node();
Node node3 = new Node();
node1.next = node2;
node2.next = node3;
node3.next = node2;
String encoded = QJSON.stringify(node1);
print(encoded);
Node decoded = QJSON.parse(Node.class, encoded);
Assert.assertSame(decoded.next, decoded.next.next.next);
// Output:
// {"next":{"next":{"next":"\/['next']"}}}
```

It just works.

<hide>

```java
package demo;
import io.obarch.codec.QJSON;
import org.junit.Assert;
import Node;

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

# reference_same_string

It is undesirable to represent the string as reference. 
Even if the string referenced, we still want to encode it as string.

```java
Object val = "hello";
print(QJSON.stringify(new AnyList(val, val)));
// Output:
// ["hello","hello"]
```

<hide>

```java
package demo;
import io.obarch.codec.QJSON;
import io.obarch.codec.any.*;
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

# value_object

The behavior for string generally applies to all value objects.
But QJSON does not know your value object. 
You need to use customized encoder to force the whole object serialized out.

Given `Money` class, we consider it as a value object class.
So for each reference, no matter we have encoded it or not, we want to encode a separate copy again.

<<< @/docs/format/ref/Money.java

```java
Money money = new Money();
money.amount = 13;
money.unit = "USD";
QJSON.Config cfg = new QJSON.Config();
cfg.chooseEncoder = (qjson, clazz) -> {
    if (Money.class.equals(clazz)) {
        return new ValueObjectEncoder(qjson.encoderOf(clazz));
    }
    return null;
};
QJSON qjson = new QJSON(cfg);
print(qjson.encode(new Object[]{money, money}));
// Output:
// [{"amount":"\b;;;;;;H","unit":"USD"},{"amount":"\b;;;;;;H","unit":"USD"}]
```

`ValueObjectEncoder` is defined like this:

<<< @/qjson-java-core/src/main/java/io.obarch.codec.spi/ValueObjectEncoder.java

<hide>

```java
package demo;
import io.obarch.codec.QJSON;
import ValueObjectEncoder;
import Money;

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