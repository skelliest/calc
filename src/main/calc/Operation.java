package calc;

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
    public float evaluate() {
        float aVal = a.evaluate();
        float bVal = b.evaluate();

        // TODO move to subclass
        if ("*".equals(symbol)) {
            return aVal * bVal;
        } else if ("/".equals(symbol)) {
            // TODO NaN
            return aVal / bVal;
        } else if ("+".equals(symbol)) {
            return aVal + bVal;
        } else if ("-".equals(symbol)) {
            return aVal - bVal;
        } else {
            // TODO Use enum so can't happen
            return 0;
        }
    }
}