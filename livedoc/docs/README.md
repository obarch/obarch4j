# Livedoc

Livedoc allow you to write JUnit test in Markdown file, to solve following problems

* You have unit test, but lack of context so that readers can not know what is actually being tested
* You have documentation, but it is frequently out of date
* Java does not have multi-line string literal
* Table driven tests requires extra effort so that you do not use this great testing method

# Setup

add this to your build.gradle 

```groovy
dependencies {
    testImplementation(
            'io.obarch:livedoc:1.0.5',
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
