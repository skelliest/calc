package calc;

class Number implements Expression {
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