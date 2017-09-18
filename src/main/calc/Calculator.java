package calc;

import calc.Tokenizer.Token;
import calc.Tokenizer.TokenType;

/**
 * Parses according to the following grammar
 * 
 * operand -> ( operand
 * operand -> number n-ary
 * n-ary -> operator operand
 * n-ary -> ) n-ary
 */
public class Calculator {
    
    public static void main(String args[]) {
        try {
            System.out.println(calculateString("-1"));
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static String calculateString(String input) throws ParseException {
        float f = calculateFloat(input);
        
        return Float.toString(f);
    }
    
    public static float calculateFloat(String input) throws ParseException {
        Tokenizer tokenizer = new Tokenizer(input);
        OperandLinkedList operand = nextOperandLinkedList(tokenizer);
        Expression expression = operand.toExpression();
        return expression.evaluate();
    }

    static OperandLinkedList nextOperandLinkedList(Tokenizer tokenizer) throws ParseException {
        if (!tokenizer.hasNext()) {
            // Else throw parseexception
            // TODO: Unless original string is all whitespace
            throw new ParseException();
        }
    
        Token token = tokenizer.nextOperandToken();

        if (token.type == TokenType.NUMBER) {
            // Return the nary formed from exp and narymore
            OperandLinkedList operand = new OperandLinkedList();
            operand.expression = new Number(Float.valueOf(token.value)); // TODO NFE
            operand.nary = nextNaryLinkedList(tokenizer);
            return operand;
        } else if (token.type == TokenType.OPEN_PAREN) {
            // Grab next operand linked list. The next token should be a closed paren, then
            // next narymore
            OperandLinkedList group = new OperandLinkedList();
            group.expression = nextOperandLinkedList(tokenizer).toExpression();
            group.nary = nextNaryLinkedList(tokenizer);

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

    static NaryLinkedList nextNaryLinkedList(Tokenizer tokenizer) throws ParseException {
        if (!tokenizer.hasNext()) {
            // EOF allowed
            return NaryLinkedList.EMPTY;
        }
        Token token = tokenizer.nextNaryToken();
        
        if (token.type == TokenType.OPERATOR) {
            // Make a new operator whose first argument is exp
            NaryLinkedList operator = new NaryLinkedList();
            operator.symbol = token.value;

            // Scan for the next operand object, store it in tree
            operator.nary = nextOperandLinkedList(tokenizer);

            return operator;
        } else if (token.type == TokenType.CLOSE_PAREN) {
            // If next token isn't an operator, the previous expression is the
            // complete nary
            // return a null narymore
            // TODO: Fix
            return NaryLinkedList.EMPTY;
        } else {
            throw new ParseException("Expecting an operator, or unclosed parentheses");
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

}
