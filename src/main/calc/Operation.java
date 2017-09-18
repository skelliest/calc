package calc;

import java.math.BigDecimal;
import java.math.MathContext;

public class Operation implements Expression, Comparable<Operation> {
    Expression a, b;

    private String symbol;
    protected int precedent;

    public Operation(String symbol) {
        this.symbol = symbol;

        // TODO move to subclass
        if ("*".equals(symbol) || "/".equals(symbol)) {
            precedent = 2;
        } else {
            precedent = 1;
        }
    }

    @Override
    public int compareTo(Operation o) {
        return precedent - o.precedent;
    }

    @Override
    public String toString() {
        return "\t" + symbol + "\t\n" + a + "\t" + b;
    }

    @Override
    public BigDecimal evaluate() {
        BigDecimal aVal = a.evaluate();
        BigDecimal bVal = b.evaluate();

        // TODO move to subclass
        if ("*".equals(symbol)) {
            return aVal.multiply(bVal, MathContext.DECIMAL128);
        } else if ("/".equals(symbol)) {
            // TODO NaN
            return aVal.divide(bVal, MathContext.DECIMAL128);
        } else if ("+".equals(symbol)) {
            return aVal.add(bVal, MathContext.DECIMAL128);
        } else if ("-".equals(symbol)) {
            return aVal.subtract(bVal, MathContext.DECIMAL128);
        } else {
            // TODO Use enum so can't happen
            return BigDecimal.ZERO;
        }
    }
}