package expression;

import java.math.BigDecimal;

public class Const extends OperationElement {
    private final Number c;

    public Const(int c) {
        this.c = c;
    }

    public Const(BigDecimal c) {
        this.c = c;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return (BigDecimal) c;
    }

    @Override
    public int getType() {
        return 6;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return (int) c;
    }

    @Override
    public int evaluate(int x) {
        return (int) c;
    }

    @Override
    public String toString() {
        return c.toString();
    }

    @Override
    public String toMiniString() {
        return c.toString();
    }

    @Override
    public int hashCode() {
        if (c == null) {
            throw new NullPointerException();
        }
        return c.hashCode() * 19;
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            Const obj = (Const) object;
            return this.c.equals(obj.c);
        }
        return false;
    }
}
