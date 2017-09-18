package calc;

import java.math.BigDecimal;

import calc.Tokenizer.Token;
import calc.Tokenizer.TokenType;

/**
 * A four function calculator, allowing parentheses. Parsing of parentheses groups
 * is done recursively, therefore the call stack grows linearly with the number
 * of nested parentheses groups. This would cause a stack overflow of highly nested
 * expressions. Non-recursive, state-based parsing would be better.
 * 
 * Parses according to the following grammar
 * operand -> ( operand
 * operand -> number n-ary
 * n-ary -> operator operand
 * n-ary -> ) n-ary
 * 
 * (Actually, it's this, to ensure parentheses are balanced)
 * start -> operand
 * operand -> ( open-operand
 * operand -> number n-ary
 * n-ary -> operator operand
 * n-ary -> ) n-ary
 * n-ary -> [EOF]
 * open-operand -> ( open-operand
 * open-operand -> number open-n-ary
 * open-n-ary -> operator open-operand
 * open-n-ary -> ) n-ary
 */
public class Calculator {
    
    public static String calculateString(String input) throws ParseException {
        try {
            Tokenizer tokenizer = new Tokenizer(input);
            OperandLinkedList operand = nextOperandLinkedList(tokenizer, false);
            Expression expression = operand.toExpression();
            BigDecimal answer = expression.evaluate();
            return answer.toString();
        } catch(ArithmeticException e) {
            return "NaN";
        }
    }

    static OperandLinkedList nextOperandLinkedList(Tokenizer tokenizer, boolean open) throws ParseException {
        if (!tokenizer.hasNext()) {
            // Else throw parseexception
            // TODO: Unless original string is all whitespace
            throw new ParseException();
        }
    
        Token token = tokenizer.nextOperandToken();

        if (token.type == TokenType.NUMBER) {
            // Return the nary formed from exp and nary
            OperandLinkedList operand = new OperandLinkedList();
            operand.expression = Number.valueOf(token.value); // TODO NFE
            operand.nary = nextNaryLinkedList(tokenizer, open);
            return operand;
        } else if (token.type == TokenType.OPEN_PAREN) {
            // Grab next operand linked list. The next token should be a
            // closed paren, then next nary
            OperandLinkedList group = new OperandLinkedList();
            group.expression = nextOperandLinkedList(tokenizer, true).toExpression();
            group.nary = nextNaryLinkedList(tokenizer, open);

            return group;
        } else {
            throw new ParseException("Expecting an expression");
        }

    }

    static NaryLinkedList nextNaryLinkedList(Tokenizer tokenizer, boolean open) throws ParseException {
        if (!tokenizer.hasNext()) {
            if (open) {
                throw new ParseException("Unclosed parentheses");
            } else {
                return NaryLinkedList.EMPTY; // EOF allowed
            }
        }
        Token token = tokenizer.nextNaryToken();
        
        if (token.type == TokenType.OPERATOR) {
            // Make a new operator whose first argument is exp
            NaryLinkedList operator = new NaryLinkedList();
            operator.symbol = token.value;

            // Scan for the next operand object, store it in tree
            operator.nary = nextOperandLinkedList(tokenizer, open);

            return operator;
        } else if (token.type == TokenType.CLOSE_PAREN) {
            // If next token isn't an operator, the previous expression is the
            // complete nary
            return NaryLinkedList.EMPTY;
        } else {
            throw new ParseException("Expecting an operator, or unclosed parentheses");
        }
    }

}
