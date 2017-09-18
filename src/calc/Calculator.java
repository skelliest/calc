package calc;

import calc.Tokenizer.Token;
import calc.Tokenizer.TokenType;

public class Calculator {

    // Grammar
    // start -> expression
    // expression -> item operator expression
    // expression -> item
    // item -> ( expression )
    // item -> number

    public static void main(String args[]) {
        String input = "2+(3+4)/2";

        Tokenizer tokenizer = new Tokenizer(input);

        try {
            Nary nary = nextNary(tokenizer);
            Expression expression = nary.toExpression();
            System.out.println(expression.evaluate());
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    static Nary nextNary(Tokenizer tokenizer) throws ParseException {
        if (!tokenizer.hasNext()) {
            // Else throw parseexception
            // TODO: Unless original string is all whitespace
            throw new ParseException();
        }
    
        Token token = tokenizer.nextOperandToken();

        if (token.type == TokenType.NUMBER) {
            // Return the nary formed from exp and narymore
            Nary nary = new Nary();
            nary.expression = new Number(Float.valueOf(token.value)); // TODO NFE
            nary.narymore = nextNarymore(tokenizer);
            return nary;
        } else if (token.type == TokenType.OPEN_PAREN) {
            // Grab next nary. The next token should be a closed paren, then
            // next narymore
            Nary group = new Nary();
            group.expression = nextNary(tokenizer);
            group.narymore = nextNarymore(tokenizer);

            // Hmmmm, I think the grammar ensures the parentheses
            // are balance---otherwise, there'd be a parse error
            // TODO: FIX?
//                Token closeParen = tokenizer.nextOperandToken();
//
//                if (closeParen.type != TokenType.CLOSE_PAREN) {
//                    throw new ParseException("Expecting )");
//                }
            return group;
        } else {
            throw new ParseException("Expecting an expression");
        }

    }

    static Narymore nextNarymore(Tokenizer tokenizer) throws ParseException {
        if (!tokenizer.hasNext()) {
            // EOF allowed
            return Narymore.EMPTY;
        }
        Token token = tokenizer.nextNaryToken();
        
        if (token.type == TokenType.OPERATOR) {
            // Make a new operator whose first argument is exp
            OperatorOld operator = new OperatorOld();
            operator.symbol = token.value;

            // Scan for the next nary object, store it in tree
            operator.nary = nextNary(tokenizer);

            return operator;
        } else if (token.type == TokenType.CLOSE_PAREN) {
            // If next token isn't an operator, the previous expression is the
            // complete nary
            // return a null narymore
            // TODO: Fix
            return Narymore.EMPTY;
        } else {
            throw new ParseException("Expecting an operator, or unclosed parentheses");
        }
    }

    static class Nary implements Expression {
        Expression expression;
        Narymore narymore;

        public Expression toExpression() {
            Operation rootOperation = null;
            Nary next = this;

            while (next.narymore != Narymore.EMPTY) {
                OperatorOld operator = (OperatorOld) next.narymore;

                Operation operation = new Operation(operator.symbol);

                // Precedence
                if (rootOperation == null) {
                    // Our first pass through
                    operation.a = this.expression.toExpression();
                    rootOperation = operation;
                } else if (operation.compareTo(rootOperation) > 0) {
                    // The new operation has higher precedence
                    operation.a = rootOperation.b;
                    rootOperation.b = operation;
                    // Don't change the root expression
                } else {
                    // The old operation has higher precedence, or no old
                    // operation
                    operation.a = rootOperation;
                    rootOperation = operation;
                }

                operation.b = operator.nary.expression.toExpression();
                next = operator.nary;
            }

            return rootOperation;
        }

        // TODO: Remove, Nary isn't an expression
        @Override
        public float evaluate() {
            return toExpression().evaluate();
        }
        
        @Override
        public String toString() {
            return expression.toString();
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
        Expression toExpression(); // TODO fix
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

        @Override
        public Expression toExpression() {
            return this;
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

        @Override
        public Expression toExpression() {
            return this;
        }
    }
//
//    class Group implements Expression {
//        Expression inner;
//
//        @Override
//        public float evaluate() {
//            return inner.evaluate();
//        }
//    }
//    
    static public class ParseException extends Exception {
        private static final long serialVersionUID = 1L;

        public ParseException() {
            super();
        }
        
        public ParseException(String message) {
            super(message);
        }
    }
}
