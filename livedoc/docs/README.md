# Live Document

Write JUnit test in markdown so that

* the test can be read like documentation
* do not need to escape string
* easier to write a lot of table driven tests

# Setup

add this to your build.gradle 

```groovy
dependencies {
    testCompile(
            'io.obarch:livedoc:1.0.4',
            'junit:junit:4.12',
    )
}
sourceSets {
    test {
        resources {
            srcDirs = ['src/test/java']
        }
    }
}
```

it will copy markdown files to compiled test classes, so that we can access it at runtime.

# Examples

## Table Driven Tests

<<< @/src/test/java/io/obarch/livedoc/TableDrivenTest.md

* [TableDrivenTest.md](https://github.com/obarch/obarch4j/blob/master/livedoc/src/test/java/io/obarch/livedoc/TableDrivenTest.md)
* [TableDrivenTest.java](https://github.com/obarch/obarch4j/blob/master/livedoc/src/test/java/io/obarch/livedoc/TableDrivenTest.java)

## List Driven Tests

<<< @/src/test/java/io/obarch/livedoc/ListDrivenTest.md

* [ListDrivenTest.md](https://github.com/obarch/obarch4j/blob/master/livedoc/src/test/java/io/obarch/livedoc/ListDrivenTest.md)
* [ListDrivenTest.java](https://github.com/obarch/obarch4j/blob/master/livedoc/src/test/java/io/obarch/livedoc/ListDrivenTest.java)

## Use Code block

complex test data can be written in code block

<<< @/src/test/java/io/obarch/livedoc/CodeBlockTest.md

* [CodeBlockTest.md](https://github.com/obarch/obarch4j/blob/master/livedoc/src/test/java/io/obarch/livedoc/CodeBlockTest.md)
* [CodeBlockTest.java](https://github.com/obarch/obarch4j/blob/master/livedoc/src/test/java/io/obarch/livedoc/CodeBlockTest.java)
