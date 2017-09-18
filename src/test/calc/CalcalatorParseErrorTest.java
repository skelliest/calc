package calc;

import org.junit.Test;

public class CalcalatorParseErrorTest {

    @Test(expected = ParseException.class)
    public void testUnknownOperator() throws ParseException {
        Calculator.calculateString("1 ^ 2");
    }

    @Test(expected = ParseException.class)
    public void testUnclosedParentheses() throws ParseException {
        Calculator.calculateString("1 + ( 2");
        Calculator.calculateString("1 + ( 2 + ( 3");
        Calculator.calculateString("1 + ( 2 + ( 3 + 4 )");
    }

    @Test(expected = ParseException.class)
    public void testDoubleOperator() throws ParseException {
        Calculator.calculateString("1 + + 2");
        Calculator.calculateString("1 + ( 2 - - 3 )");
    }

    // The empty string cannot be assigned a value, so should not parse
    @Test(expected = ParseException.class)
    public void testEmptyString() throws ParseException {
        Calculator.calculateString("");
        Calculator.calculateString("    ");
    }
    
}
