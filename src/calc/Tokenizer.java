package calc;
import java.io.StringReader;
import java.util.Scanner;
import java.util.regex.Pattern;

import calc.Calculator.ParseException;

public class Tokenizer {

    // operand -> ( operand
    // operand -> number n-ary
    // n-ary -> operator operand
    // n-ary -> ) n-ary
    
    private final Scanner scanner;
    private static final Pattern numberPattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
    private static final Pattern operatorPattern = Pattern.compile("[\\+\\-\\*/]");
    
    public Tokenizer(String source) {
        this(new StringReader(source));
    }
    
    public Tokenizer(Readable source) {
        scanner = new Scanner(source);

        // Delimiting on \s doesn't work: 1+1 would have token 1+1
        // Delimiting on \b doesn't work: 1+(2+2) would have token +(
        scanner.useDelimiter("");
    }
    
    public boolean hasNext() {
        scanner.skip("\\s*"); // :/

        return scanner.hasNext();
    }

    public Token nextOperandToken() throws ParseException {
        scanner.skip("\\s*");
        
        if (scanner.hasNext("[-0-9]")) { // TODO: Fix, doesn't catch malformed isolated negative sign            
            return new Token(TokenType.NUMBER, scanner.findInLine(numberPattern));
        } else if (scanner.hasNext("\\(")) {
            scanner.next(); // Pop off paren
            return new Token(TokenType.OPEN_PAREN, "(");
        } else {
            // TODO: End of file is okay here
            throw new ParseException("Malformed operand " + scanner.next());
        }
    }
    
    public Token nextNaryToken() throws ParseException {
        scanner.skip("\\s*");
        
        if (scanner.hasNext(operatorPattern)) {
            return new Token(TokenType.OPERATOR, scanner.next());
        } else if (scanner.hasNext("\\)")) {
            scanner.next(); // Pop off paren
            return new Token(TokenType.CLOSE_PAREN, ")");
        } else {
            // TODO: End of file is not okay here
            throw new ParseException("Malformed n-ary expression " + scanner.next());
        }
    }
    
    public class Token {
        TokenType type;
        String value;
        
        private Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }
    
    public enum TokenType {
        OPEN_PAREN, CLOSE_PAREN, NUMBER, OPERATOR;
    }
    
}
