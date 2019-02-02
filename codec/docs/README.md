# Get started

Use `QJSON.stringify` to encode any object into qjson format.
Use `QJSON.parse` to decode qjson back.

```java
String encoded = QJSON.stringify(your_object);
QJSON.parse(your.class, encoded); // decode as your class
QJSON.parse(encoded); // decode as list/map
```

`QJSON` constructor takes config object to customize its behavior.

```java
QJSON.Config cfg = new QJSON.Config();
QJSON qjson = new QJSON(cfg);
// qjson.encode(), the counterpart of QJSON.stringify
// qjson.decode(), the counterpart of QJSON.parse
```