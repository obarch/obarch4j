package org.qjson;

import org.junit.Assert;
import org.mdkt.compiler.InMemoryJavaCompiler;
import io.obarch.livedoc.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.obarch.livedoc.TestInMarkdown.myTestData;
import static io.obarch.livedoc.TestInMarkdown.stripQuote;

public interface TestDecode {

    static void $() {
        TestDecode.$(true);
    }

    static void $(boolean useObjectsEquals) {
        TestDecode.$(QJSON.Mode.REFLECTION, useObjectsEquals);
        TestDecode.$(QJSON.Mode.CODEGEN, useObjectsEquals);
    }

    static void $(QJSON.Mode mode, boolean useObjectsEquals) {
        TestData testData = myTestData();
        Table table = testData.table();
        boolean hasType = "type".equals(table.head.get(0));
        for (Row row : table.body) {
            List<String> sources = new ArrayList<>(testData.codes());
            String typeLiteral = hasType ? "new TypeLiteral<" + stripQuote(row.get(0)) + ">(){}" : "null";
            String source = "" +
                    "package testdata;\n" +
                    "import org.qjson.any.*;\n" +
                    "import org.qjson.*;\n" +
                    "import java.util.*;\n" +
                    "public class TestObject {\n" +
                    "   public static TypeLiteral type() {\n" +
                    "       return " + typeLiteral + ";\n" +
                    "   }\n" +
                    "   public static Object create() {\n" +
                    "       return " + stripQuote(row.get(hasType ? 1 : 0)) + ";\n" +
                    "   }\n" +
                    "}";
            sources.add(source);
            Path tempDir = CompileClasses.$(sources);
            try {
                Class testDataClass = LoadClass.$(tempDir, "testdata.TestObject");
                Object testObject = testDataClass.getMethod("create").invoke(null);
                TypeLiteral testObjectType = (TypeLiteral) testDataClass.getMethod("type").invoke(null);
                QJSON.Config config = new QJSON.Config();
                config.mode = mode;
                config.compiler = InMemoryJavaCompiler.newInstance()
                        .ignoreWarnings()
                        .useParentClassLoader(testDataClass.getClassLoader())
                        .useOptions("-classpath", System.getProperty("java.class.path") + ":" + tempDir.toString());
                QJSON qjson = new QJSON(config);
                String input = stripQuote(row.get(hasType ? 2 : 1));
                Object decoded;
                if (hasType) {
                    decoded = qjson.decode(testObjectType, input);
                } else {
                    decoded = qjson.decode(testObject.getClass(), input);
                }
                if (useObjectsEquals) {
                    Assert.assertTrue(row.get(hasType ? 1 : 0), Objects.deepEquals(testObject, decoded));
                } else {
                    Assert.assertEquals(row.get(hasType ? 1 : 0), qjson.encode(testObject), qjson.encode(decoded));
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                Rmtree.$(tempDir);
            }
        }
    }
}
