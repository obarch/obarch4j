package org.qjson.junit.md;

import org.junit.Assert;
import org.junit.Test;

import static org.qjson.junit.md.TestInMarkdown.myTestData;

public class CodeBlockTest {

    @Test
    public void code_block_provide_test_data() {
        TestData testData = myTestData();
        String[] words = testData.code().split("\\r?\\n");
        for (NamedRow row : testData.table()) {
            int count = countPrefix(words, row.get("prefix"));
            Assert.assertEquals((int) Integer.valueOf(row.get("count")), count);
        }
    }

    private static int countPrefix(String[] words, String prefix) {
        int i = 0;
        for (String word : words) {
            if (word.startsWith(prefix)) {
                i++;
            }
        }
        return i;
    }
}
