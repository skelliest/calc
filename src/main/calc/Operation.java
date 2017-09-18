package calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Operation implements Expression, Comparable<Operation> {    
    Expression a, b;

    private Operator operator;
    private static final MathContext SIGFIG_20_CONTEXT = new MathContext(20, RoundingMode.HALF_EVEN);

    public static Operation createFromSymbol(String symbol) throws ParseException {
        if ("+".equals(symbol)) {
            return new Operation(Operator.ADD);
        } else if ("-".equals(symbol)) {
            return new Operation(Operator.SUBTRACT);
        } else if ("*".equals(symbol)) {
            return new Operation(Operator.MULTIPLY);
        } else if ("/".equals(symbol)) {
            return new Operation(Operator.DIVIDE);
        } else {
            throw new ParseException("Unknown symbol " + symbol);
        }
    }
    
    private Operation(Operator operator) {
        this.operator = operator;
    }

    @Override
    public int compareTo(Operation o) {
        return operator.precedence - o.operator.precedence;
    }

    @Override
    public String toString() {
        return "\t" + operator.symbol + "\t\n" + a + "\t" + b;
    }
    
    @Override
    public BigDecimal evaluate() {
        BigDecimal aVal = a.evaluate();
        BigDecimal bVal = b.evaluate();
        
        switch(operator) {
        case ADD:
            return aVal.add(bVal, SIGFIG_20_CONTEXT);
        case SUBTRACT:
            return aVal.subtract(bVal, SIGFIG_20_CONTEXT);
        case MULTIPLY:
            return aVal.multiply(bVal, SIGFIG_20_CONTEXT);
        case DIVIDE:
        default:
            return aVal.divide(bVal, SIGFIG_20_CONTEXT);
        }
    }
    
    private enum Operator {
        ADD("+", 1), SUBTRACT("-", 1) , MULTIPLY("*", 2) , DIVIDE("/", 2); 
        
        String symbol;
        int precedence;
        
        private Operator(String symbol, int precedence) {
            this.symbol = symbol;
            this.precedence = precedence;
        }
    }

}