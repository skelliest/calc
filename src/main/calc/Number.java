package calc;

import java.math.BigDecimal;

class Number implements Expression {
    BigDecimal value;

    public static Number valueOf(String string) throws ParseException {
        try {
            Number number = new Number();
            number.value = new BigDecimal(string);
            return number;
        } catch (NumberFormatException nfe) {
            throw new ParseException("Unable to parse number " + string);
        }
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