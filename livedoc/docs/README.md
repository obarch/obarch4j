# junit.md

Write JUnit in Markdown so that

* the test can be read like documentation
* do not need to escape string
* easier to write a lot of table driven tests

The best way to learn how to use it is by following examples

# Setup

add this to your build.gradle 

```groovy
dependencies {
    testCompile(
            'org.qjson:junit.md:1.0.3',
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

<<< @/src/test/java/org/qjson/junit/md/TableDrivenTest.md

* [TableDrivenTest.md](https://github.com/qjson/junit.md/blob/master/src/test/java/org/qjson/junit/md/TableDrivenTest.md)
* [TableDrivenTest.java](https://github.com/qjson/junit.md/blob/master/src/test/java/org/qjson/junit/md/TableDrivenTest.java)

## List Driven Tests

<<< @/src/test/java/org/qjson/junit/md/ListDrivenTest.md

* [ListDrivenTest.md](https://github.com/qjson/junit.md/blob/master/src/test/java/org/qjson/junit/md/ListDrivenTest.md)
* [ListDrivenTest.java](https://github.com/qjson/junit.md/blob/master/src/test/java/org/qjson/junit/md/ListDrivenTest.java)

## Use Code block

complex test data can be written in code block

<<< @/src/test/java/org/qjson/junit/md/CodeBlockTest.md

* [CodeBlockTest.md](https://github.com/qjson/junit.md/blob/master/src/test/java/org/qjson/junit/md/CodeBlockTest.md)
* [CodeBlockTest.java](https://github.com/qjson/junit.md/blob/master/src/test/java/org/qjson/junit/md/CodeBlockTest.java)
