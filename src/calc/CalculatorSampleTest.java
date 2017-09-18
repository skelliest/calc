package calc;

import org.junit.Test;

public class CalculatorSampleTest {

    @Test
    public void test1() {
        CalculatorTest.comparisonTest("1.5-2.5", "-1.0");
    }

    @Test
    public void test2() {
        CalculatorTest.comparisonTest("1+2+3-4-5", "-3");
    }

    @Test
    public void test3() {
        CalculatorTest.comparisonTest("0 / 0 / 0 / 0", "NaN");
    }

    @Test
    public void test4() {
        CalculatorTest.comparisonTest("1 + 2 / 3 - 4", "-2.3333333333333333333");
    }

    @Test
    public void test5() {
        CalculatorTest.comparisonTest("1 + 2 / (3 - 4)", "-1");
    }

    @Test
    public void test6() {
        CalculatorTest.comparisonTest("(1+1)*(2+3/(4+5+6)*(7-8+9))/10", "0.72");
    }

    @Test
    public void test7() {
        CalculatorTest.comparisonTest("-20 - -30", "10");
    }
    
    @Test
    public void test8() {
        CalculatorTest.comparisonTest("  1.89 * 792  ", "1496.88");
    }
    
}
