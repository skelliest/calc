package calc;

public class NaryLinkedList {
    String symbol;
    OperandLinkedList nary;

    static NaryLinkedList EMPTY = new NaryLinkedList() {
        @Override
        public String toString() {
            return "[EMPTY NaryLinkedList]";
        }
    };

    @Override
    public String toString() {
        return symbol;
    }
}