package io.obarch.livedoc;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.BiFunction;

public class TableDrivenTest {

    @Test
    public void add() {
        testAlgorithm((leftOperand, rightOperand) -> leftOperand + rightOperand);
    }

    @Test
    public void sub() {
        testAlgorithm((leftOperand, rightOperand) -> leftOperand - rightOperand);
    }

    private static void testAlgorithm(BiFunction<Integer, Integer, Integer> f) {
        Table table = TestInMarkdown.myTestData().table();
        for (NamedRow row : table) {
            int leftOperand = Integer.valueOf(row.get("left_operand"));
            int rightOperand = Integer.valueOf(row.get("right_operand"));
            int result = Integer.valueOf(row.get("result"));
            Assert.assertEquals(result, (int) f.apply(leftOperand, rightOperand));
        }
    }


}
