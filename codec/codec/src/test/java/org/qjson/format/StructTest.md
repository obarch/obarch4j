# get_field_and_set_field

```java
package testdata;
import java.util.*;
public class MyClass {

    public String field;

    public MyClass init(String field) {
        this.field = field;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyClass that = (MyClass) o;
        return Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }
}
```

| value | encoded |
| ---   | ---     |
| `new MyClass().init("hello")` | `{"field":"hello"}` |

# getter_and_setter

```java
package testdata;
import java.util.*;
public class MyClass {

    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public MyClass init(String field) {
        this.field = field;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyClass that = (MyClass) o;
        return Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }
}
```

| value | encoded |
| ---   | ---     |
| `new MyClass().init("hello")` | `{"field":"hello"}` |

# fluent_getter_and_fluent_setter

```java
package testdata;
import java.util.*;
public class MyClass {

    private String field;

    public String field() {
        return field;
    }

    public void field(String field) {
        this.field = field;
    }

    public MyClass init(String field) {
        this.field = field;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyClass that = (MyClass) o;
        return Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }
}
```

| value | encoded |
| ---   | ---     |
| `new MyClass().init("hello")` | `{"field":"hello"}` |

# rename_property

```java
package testdata;
import org.qjson.spi.*;
import java.util.*;
public class MyClass {

    @QJsonProperty("\"")
    public String field;

    public MyClass init(String field) {
        this.field = field;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyClass that = (MyClass) o;
        return Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }
}
```

| value | encoded |
| ---   | ---     |
| `new MyClass().init("hello")` | `{"\\CC":"hello"}` |

# multiple_fields


```java
package testdata;
import java.util.*;
public class MyClass {

    public String field1;
    public String field2;

    public MyClass init(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyClass that = (MyClass) o;
        return Objects.equals(field1, that.field1) &&
                Objects.equals(field2, that.field2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field1, field2);
    }

}
```

| value | encoded |
| ---   | ---     |
| `new MyClass().init("a","b")` | `{"field1":"a","field2":"b"}` |

# no_field

```java
package testdata;
public class MyClass {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
```

| value | encoded |
| ---   | ---     |
| `new MyClass()` | `{}` |

# skip_unknown_field

```java
package testdata;
import java.util.*;
public class MyClass {

    public String field;

    public MyClass init(String field) {
        this.field = field;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyClass that = (MyClass) o;
        return Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }
}
```

| value | encoded |
| ---   | ---     |
| `new MyClass().init("hello")` | `{"a":"b","field":"hello"}` |