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

}
