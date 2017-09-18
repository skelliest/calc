package calc;

import java.math.BigDecimal;

class Number implements Expression {
    BigDecimal value;

    public static Number valueOf(String string) {
        Number number = new Number();
        number.value = new BigDecimal(string);
        return number;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public BigDecimal evaluate() {
        return value;
    }
}