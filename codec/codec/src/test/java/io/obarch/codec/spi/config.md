# Config

There are couple of options you can configure.

| type | field | comment | usage |
| --- | --- | --- | --- |
| Function<Class, Class> | chooseImpl | choose the concrete implementation class to create decoded object | [usage](/usage/inf/inf.html) |
| BiFunction<QJsonSpi, Type, Decoder> | chooseDecoder | customize decoder for given type | [usage](/spi/decoder/decoder.html) |
| BiFunction<QJsonSpi, Class, Encoder> | chooseEncoder | customize encoder for given type | [usage](/spi/encoder/encoder.html) |
| BiConsumer<QJsonSpi, StructDescriptor> | customizeStruct | customize struct fields encoding and decoding | [usage](/usage/struct/struct.html) |
| QJSON.Mode | mode | should runtime code generation be used, or mixed with reflection mode | |
| InMemoryJavaCompiler | compiler | the compiler used for runtime code generation | |
| java.util.concurrent.Executor | executor | the background thread pool for runtime code generation in mixed mode | |
