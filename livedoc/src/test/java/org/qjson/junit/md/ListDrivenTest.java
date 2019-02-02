package org.qjson.junit.md;

import org.junit.Test;

import static org.qjson.junit.md.TestInMarkdown.myTestData;

public class ListDrivenTest {

    @Test
    public void these_are_integers() {
        myTestData().assertTrue(input -> {
            Integer.valueOf(input);
            return true;
        });
    }

    @Test
    public void these_are_not_integers() {
        myTestData().assertFalse(input -> {
            try{
                Integer.valueOf(input);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        });
    }
}
