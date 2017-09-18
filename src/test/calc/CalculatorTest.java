package calc;

import static org.junit.Assert.*;

import org.junit.Test;

public class CalculatorTest {

    public static void comparisonTest(String testString, String expectedOutput) {
        try {
            assertEquals(expectedOutput, Calculator.calculateString(testString));
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            fail();
        }
    }

    public static void comparisonTest(String testString, float expectedOutput) {
        try {
            assertEquals(expectedOutput, Calculator.calculateFloat(testString), 0.0001f);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            fail();
        }
    }
        
    // Verify that * and / take precedence over + and -
    @Test
    public void testOperatorPrecedence() {
        comparisonTest("5 + 4 / 2", 7);
        comparisonTest("3 * 4 - 2", 10);
        comparisonTest("1 + 2 + 3 * 4 + 5 + 6", 26);
    }

    // Verify that ( ) takes precedence over * and /
    @Test
    public void testGroupPrecedence() {
        comparisonTest("2 * (3 + 4) * 5", 70);
    }

    // Verify that whitespace doesn't matter
    @Test
    public void testWhitespace() {
        comparisonTest("2+ ( 3   * 1)", 5);
        comparisonTest("  2 +(3*1)", 5);
        comparisonTest("2+(3* 1)      ", 5);
    }
}
