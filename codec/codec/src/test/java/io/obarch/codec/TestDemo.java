package io.obarch.codec;

import org.junit.Assert;
import io.obarch.livedoc.CompileClasses;
import io.obarch.livedoc.LoadClass;
import io.obarch.livedoc.Rmtree;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static io.obarch.livedoc.TestInMarkdown.myTestData;

public interface TestDemo {

    static void $() {
        testDemos();
    }

    static void testDemos() {
        List<String> codes = myTestData().codes();
        List<String> demoCodes = codes.subList(0, codes.size() - 1);
        String template = codes.get(codes.size() - 1);
        for (String demoCode : demoCodes) {
            try {
                testDemo(template, demoCode);
            } catch (RuntimeException e) {
                throw e;
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof Error) {
                    throw (Error) e.getTargetException();
                } else {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    static void testDemo(String template, String demoCode) throws Exception {
        String expect = getExpect(demoCode);
        String source = template.replace("{{ CODE }}", demoCode);
        Path path = CompileClasses.$(Arrays.asList(source));
        try {
            Class demoClass = LoadClass.$(path, "demo.Demo");
            Method demoMethod = demoClass.getMethod("demo");
            if (expect != null) {
                PrintStream oldOut = System.out;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream newOut = new PrintStream(baos);
                System.setOut(newOut);
                demoMethod.invoke(null);
                System.setOut(oldOut);
                newOut.flush();
                Assert.assertEquals(expect, baos.toString());
            } else {
                demoMethod.invoke(null);
            }
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof Error) {
                throw (Error) e.getTargetException();
            } else {
                throw e;
            }
        } finally {
            Rmtree.$(path);
        }
    }

    static String getExpect(String demoCode) {
        String lines[] = demoCode.split("\\r?\\n");
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i];
            if (line.startsWith("// Output:")) {
                StringBuilder expect = new StringBuilder();
                for (int j = i + 1; j < lines.length; j++) {
                    expect.append(lines[j].replace("// ", ""));
                    expect.append("\n");
                }
                return expect.toString();
            }
        }
        return null;
    }
}
