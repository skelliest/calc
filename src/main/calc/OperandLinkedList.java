package calc;

// Represents a linked list of an operand, followed by an optional list of n operators
public class OperandLinkedList {
    Expression expression;
    NaryLinkedList nary;

    public Expression toExpression() {
        Operation rootOperation = null;
        OperandLinkedList next = this;

        while (next.nary != NaryLinkedList.EMPTY) {
            NaryLinkedList operator = next.nary;

            Operation operation = new Operation(operator.symbol);

            // Precedence
            if (rootOperation == null) {
                // Our first pass through
                operation.a = this.expression;
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

            operation.b = operator.nary.expression;
            next = operator.nary;
        }

        return rootOperation;
    }
    
    @Override
    public String toString() {
        return expression.toString();
    }

}