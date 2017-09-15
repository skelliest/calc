import java.util.Scanner;
import java.util.regex.Pattern;

public class Calculator {
    
    // Grammar
    // start -> expression
    // expression -> item operator expression
    // expression -> item
    // item -> ( expression )
    // item -> number
    

    public static void main(String args[]) {
        String input = "2+(3+4)/2";
        
        Scanner scanner = new Scanner(input);
        
        // Delimiting on \s doesn't work: 1+1 would have token 1+1
        // Delimiting on \b doesn't work: 1+(2+2) would have token +(
        scanner.useDelimiter("");

        Nary nary = nextNary(scanner);
        Expression expression = nary.toExpression();
        
        System.out.println(expression.evaluate());
    }
    
    static Pattern numberPattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
    static Pattern operatorPattern = Pattern.compile("[\\+\\-\\*/]");
    
    static float nextNumber(Scanner scanner) {
        String toParse = scanner.findInLine(numberPattern);
        
        System.out.println(toParse);
        return Float.valueOf(toParse);
    }
    
    static Nary nextNary(Scanner scanner) {
        // Get next token
        scanner.skip("\\s*");
        
        // If next token is a digit/neg sign, pop it and
        if (scanner.hasNext(numberPattern)) {
            // Get next digit, store it in exp
            float f = nextNumber(scanner);
            Expression exp = new Number(f);

            // Call nextNarymore(exp), store it in narymore
            Narymore narymore = nextNarymore(exp, scanner);
            // Return the nary formed from exp and narymore
            
            Nary nary = new Nary();
            nary.expression = exp;
            nary.narymore = narymore;
            return nary;
            
        }
        // If next token is an open paren,
        if (scanner.hasNext("\\(")) {
            scanner.next(); // Pop off paren
            
            // Grab next nary. The next token should be a closed paren, then next narymore
            Nary nextNary = nextNary(scanner);
            
            scanner.skip("\\s*");
            
            if (!scanner.hasNext("\\)")) {
                // If not, throw parseexception
                throw new Error();
            }
            
            scanner.next(); // Pop off closed paren

            // Call nextNarymore(exp), store it in narymore
            Narymore narymore = nextNarymore(nextNary, scanner);
            // Return the nary formed from exp and narymore
            
            
            Nary nary = new Nary();
            nary.expression = nextNary;
            nary.narymore = narymore;
            return nary;
        }
        
        // Else throw parseexception
        throw new Error();
    }
    
    static Narymore nextNarymore(Expression exp, Scanner scanner) {
        // Peek at next token
        scanner.skip("\\s*");
        
        // If next token is an operator, pop it and
        if (scanner.hasNext(operatorPattern)) {
            // Make a new operator whose first argument is exp
            String operation = scanner.next();
            
            // Scan for the next nary object, store it in tree
            Nary nary = nextNary(scanner);
            
            OperatorOld operator = new OperatorOld();
            operator.symbol = operation;
            operator.nary = nary;
            
            return operator;
        } else {
            // If next token isn't an operator, the previous expression is the complete nary
            // return a null narymore
            
            return Narymore.EMPTY;
        }
    }
    
    static class Nary implements Expression {
        Expression expression;
        Narymore narymore;
        
        Expression toExpression() {
            Operation rootOperation = null;
            Nary next = this;
            
            while (next.narymore != Narymore.EMPTY) {
                OperatorOld operator = (OperatorOld)next.narymore;
                
                Operation operation = new Operation(operator.symbol);
                
                // Precedence
                if (rootOperation == null) {
                    // Our first pass through
                    operation.a = this.expression;
                    rootOperation = operation;
                } else if (operation.compareTo(rootOperation) > 0) {
                    // The new operation has higher precedence
                    operation.a = rootOperation.b;
                    // Don't change the root expression                    
                } else {
                    // The old operation has higher precedence, or no old operation
                    operation.a = rootOperation;
                    rootOperation = operation;
                }
                
                operation.b = operator.nary.expression;
                next = operator.nary;
            }
            
            return rootOperation;
        }

        // TODO: Remove, Nary isn't an expression
        @Override
        public float evaluate() {
            return toExpression().evaluate();
        }
        
    }
    
    static class Narymore {
        static Narymore EMPTY = new Narymore() {
            @Override
            public String toString() {
                return "[EMPTY Narymore]";
            }
        };
    }
    
    
    static interface Expression {
        float evaluate();
    }
    
    static class Number implements Expression {
        float value;
        
        Number(float value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return Float.toString(value);
        }

        @Override
        public float evaluate() {
            return value;
        }
    }
    
    static class OperatorOld extends Narymore {
        String symbol;
        Nary nary;
        
        @Override
        public String toString() {
            return symbol;
        }
    }
    
    static class Operation implements Expression, Comparable<Operation> {
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
            return "\t" + symbol + "\t\n" +
                    a + "\t" + b;
        }

        @Override
        public float evaluate() {
            float aVal = a.evaluate();
            float bVal = b.evaluate();
            
            // TODO move to subclass
            if ("*".equals(symbol)) {
                return aVal * bVal;
            } else if ("/".equals(symbol)){
                // TODO NaN
                return aVal / bVal;
            } else if ("+".equals(symbol)){
                return aVal + bVal;
            } else if ("-".equals(symbol)){
                return aVal - bVal;
            } else {
                // TODO Use enum so can't happen
                return 0;
            }
        }
    }
    
    class Group implements Expression {
        Expression inner;

        @Override
        public float evaluate() {
            return inner.evaluate();
        }
    }
}
