# Container

QJSON like JSON, only support `[]` or `{}` as container. 
Java container class will map to `[]` or `{}`.

# builtin_container

built-in container is supported out of box. The mapping relationship is like this

| java type | QJSON |
| ---   | --- |
| Iterable(Collection/Set/List) | `[]` |
| Array | `[]` |
| Map  | `{}` |

```java
print(QJSON.stringify(
  new ArrayList(){{ 
      add(true); 
  }}));
// Output:
// [true]
```

```java
print(QJSON.stringify(
  new HashSet(){{ 
      add(true);
  }}));
// Output:
// [true]
```

```java
print(QJSON.stringify(new boolean[]{true}));
// Output:
// [true]
```

```java
print(QJSON.stringify(new Object[]{true}));
// Output:
// [true]
```

```java
print(QJSON.stringify(
  new HashMap(){{ 
      put("a", true); 
  }}));
// Output:
// {"a":true}
```

If decode back without specifying type, the default mapping is 

| QJSON | java type |
| --- | --- |
| `[]` | AnyList |
| `{}` | AnyMap |

```java
Any anyList = QJSON.parse("[[true],[false]]");
Assert.assertEquals(false, anyList.get(1, 0));
Any anyMap = QJSON.parse("{\"a\":[[false]]}");
Assert.assertEquals(false, anyMap.get("a", 0, 0));
```

To decode specified type, we need to pass in the class


```java
QJSON qjson = new QJSON();
List list = qjson.decode(ArrayList.class, "[true]");
Assert.assertEquals(Arrays.asList(true), list);
```

However, due to the limit of Java generic type, we can not know the element type of the list.
We have to use this syntax to specify the accurate collection type:

```java
QJSON qjson = new QJSON();
List list = qjson.decode(new TypeLiteral<ArrayList<ArrayList>>(){}, "[[true],[false]]");
Assert.assertEquals(2, list.size());
Assert.assertEquals(Arrays.asList(true), list.get(0));
Assert.assertEquals(Arrays.asList(false), list.get(1));
```

<hide>

```java
package demo;
import QJSON;
import TypeLiteral;
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

# user_defined_container

If you container is inherited from containers in `java.util.*`, it is still considered as container automatically.
However, if your container just implements `Iterable`, it will not be encoded as `[]` automatically.

Given this class

<<< @/docs/usage/container/MyObjects.java


It will be encoded like this:

```java
print(QJSON.stringify(new MyObjects("a", "b")));
// Output:
// {"obj1":"a","obj2":"b"}
```

To encode it as `["a","b"]`, we need to register a function to choose encoder.
Same applies to decoding.

```java
QJSON.Config cfg = new QJSON.Config();
cfg.chooseEncoder = (qjson, clazz) -> {
    if (MyObjects.class.equals(clazz)) {
        return qjson.encoderOf(Iterable.class);
    }
    return null;
};
QJSON qjson = new QJSON(cfg);
print(qjson.encode(new MyObjects("a", "b")));
// Output:
// ["a","b"]
```

<hide>

```java
package demo;
import QJSON;
import org.junit.Assert;
import MyObjects;

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