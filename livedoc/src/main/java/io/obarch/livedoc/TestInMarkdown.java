package io.obarch.livedoc;

import org.commonmark.node.Node;

import java.lang.reflect.Method;
import java.util.Arrays;

// entry point, import static
public interface TestInMarkdown {

    static TestData loadTestData(Class clazz) {
        Node testData = LoadTestData.$(clazz);
        return new TestData(Arrays.asList(testData));
    }

    static TestData myTestData() {
        Method method = InspectTestingMethod.$();
        return loadTestData(method.getDeclaringClass())
                .select(selectSection(method.getName()));
    }

    static FluentSelectNode selectSection(String... expectedHeadings) {
        return new FluentSelectNode().section(expectedHeadings);
    }

    static String stripQuote(String text) {
        if (text.isEmpty()) {
            return text;
        }
        if (text.charAt(0) == '`') {
            return text.substring(1, text.length() - 1).replace("\\|", "|");
        }
        return text;
    }
}
