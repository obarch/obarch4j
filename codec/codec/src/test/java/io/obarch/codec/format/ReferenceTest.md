# ref_map_value

```java
package testdata;
import org.qjson.any.*;

public class MyTestObject {
    
    public static Object get() {
        AnyList sameVal = new AnyList();
        return new AnyMap("a", sameVal, "b", sameVal);
    }
}
```

| value | encoded |
| ---   | ---     |
| `MyTestObject.get()` | `{"a":[],"b":"\/['a']"}` |

# ref_list_element

```java
package testdata;
import org.qjson.any.*;

public class MyTestObject {
    
    public static Object get() {
        AnyMap sameVal = new AnyMap();
        return new AnyMap("a", new AnyList(sameVal), "b", new AnyList(sameVal));
    }
}
```

| value | encoded |
| ---   | ---     |
| `MyTestObject.get()` | `{"a":[{}],"b":["\/['a'][0]"]}` |

# ref_array_element

```java
package testdata;
import org.qjson.any.*;

public class MyTestObject {
    
    public static Object get() {
        AnyMap sameVal = new AnyMap();
        return new Object[]{sameVal, sameVal};
    }
}
```

| value | encoded |
| ---   | ---     |
| `MyTestObject.get()` | `[{},"\/[0]"]` |

# ref_struct_field

```java
package testdata;
import java.util.*;
public class MyStruct {
    public Object field;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyStruct that = (MyStruct) o;
        return Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }
}
```

```java
package testdata;
import org.qjson.any.*;

public class MyTestObject {
    
    public static Object get() {
        AnyMap sameVal = new AnyMap();
        MyStruct struct = new MyStruct();
        struct.field = new AnyList(sameVal, sameVal);
        return struct;
    }
}
```

| value | encoded |
| ---   | ---     |
| `MyTestObject.get()` | `{"field":[{},"\/['field'][0]"]}` |

# ref_map_itself

```java
package testdata;
import org.qjson.any.*;

public class MyTestObject {
    
    public static Object get() {
        AnyMap myself = new AnyMap();
        myself.set("a", myself);
        return myself;
    }
}
```

| value | encoded |
| ---   | ---     |
| `MyTestObject.get()` | `{"a":"\/"}` |

# ref_list_itself

```java
package testdata;
import org.qjson.any.*;

public class MyTestObject {
    
    public static Object get() {
        AnyList myself = new AnyList();
        myself.add(myself);
        return myself;
    }
}
```

| value | encoded |
| ---   | ---     |
| `MyTestObject.get()` | `["\/"]` |

# ref_struct_itself

```java
package testdata;
import java.util.*;
public class MyStruct {
    public Object field;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyStruct that = (MyStruct) o;
        return Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }
}
```

```java
package testdata;
import org.qjson.any.*;

public class MyTestObject {
    
    public static Object get() {
        MyStruct struct = new MyStruct();
        struct.field = struct;
        return struct;
    }
}
```

| value | encoded |
| ---   | ---     |
| `MyTestObject.get()` | `{"field":"\/"}` |
