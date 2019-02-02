# Struct

User defined java class can be encoded decoded automatically.

# public_fields

Given this class, all the public fields can be get/set directly

<<< @/docs/usage/struct/public_fields/UserPost.java

```java
UserPost post = new UserPost();
post.title = "experience report";
post.content = "that is awsome";
QJSON qjson = new QJSON();
String encoded = qjson.encode(post);
print(encoded);
post = qjson.decode(UserPost.class, encoded);
print(post.title);
// Output:
// {"content":"that is awsome","title":"experience report"}
// experience report
```

<hide>

```java
package demo;
import org.qjson.QJSON;
import org.junit.Assert;
import org.qjson.usage.struct.public_fields.UserPost;

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

# getter_setter

If getter or setter present, will get/set through it.

<<< @/docs/usage/struct/getter_setter/UserPost.java

```java
UserPost post = new UserPost();
post.setTitle("experience report");
post.setContent("that is awsome");
QJSON qjson = new QJSON();
String encoded = qjson.encode(post);
print(encoded);
post = qjson.decode(UserPost.class, encoded);
print(post.getTitle());
// Output:
// {"content":"that is awsome","title":"experience report"}
// experience report
```

<hide>

```java
package demo;
import org.qjson.QJSON;
import org.junit.Assert;
import org.qjson.usage.struct.getter_setter.UserPost;

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

# adhoc_rename

To ad-hoc rename some property, we can use `@QJsonProperty`.

<<< @/docs/usage/struct/adhoc_rename/UserPost.java

```java
UserPost post = new UserPost();
post.title = "experience report";
post.content = "that is awsome";
QJSON qjson = new QJSON();
String encoded = qjson.encode(post);
print(encoded);
post = qjson.decode(UserPost.class, encoded);
print(post.title);
// Output:
// {"TITLE":"experience report","content":"that is awsome"}
// experience report
```

<hide>

```java
package demo;
import org.qjson.QJSON;
import org.junit.Assert;
import org.qjson.usage.struct.adhoc_rename.UserPost;

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

# batch_rename

If you do not want to annotate `@QJsonProperty` one by one.
You can use config to pass in a `customizeStruct` function.

<<< @/docs/usage/struct/public_fields/UserPost.java

```java
UserPost post = new UserPost();
post.title = "experience report";
post.content = "that is awsome";
QJSON.Config cfg = new QJSON.Config();
cfg.customizeStruct = (spi, structDescriptor) -> {
    if (!UserPost.class.equals(structDescriptor.clazz)) {
        return;
    }
    for (StructDescriptor.Prop field : structDescriptor.fields.values()) {
        field.name = field.field.getName().toUpperCase();
    }
};
QJSON qjson = new QJSON(cfg);
String encoded = qjson.encode(post);
print(encoded);
post = qjson.decode(UserPost.class, encoded);
print(post.title);
// Output:
// {"CONTENT":"that is awsome","TITLE":"experience report"}
// experience report
```

<hide>

```java
package demo;
import org.qjson.QJSON;
import org.junit.Assert;
import org.qjson.spi.StructDescriptor;
import org.qjson.usage.struct.public_fields.UserPost;

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

# adhoc_choose_encoder

`@QJsonProperty` can choose the encoder for the marked field. 
It will not affect other fields.

<<< @/docs/usage/struct/adhoc_choose_encoder/UserPost.java

<<< @/docs/spi/encoder/DemoDateEncoder.java

```java
UserPost post = new UserPost();
post.title = "experience report";
post.content = "that is awsome";
OffsetDateTime date = OffsetDateTime.of(LocalDateTime.of(
        2008, 8, 8, 0, 0), ZoneOffset.UTC);
post.publishedAt = date;
print(QJSON.stringify(post));
// Output:
// {"content":"that is awsome","publishedAt":"2008-08-08T00:00:00Z","title":"experience report"}
```

<hide>

```java
package demo;
import org.qjson.QJSON;
import org.junit.Assert;
import java.time.*;
import org.qjson.usage.struct.adhoc_choose_encoder.UserPost;

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
