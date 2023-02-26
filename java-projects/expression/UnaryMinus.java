package expression;

import java.math.BigDecimal;

public class UnaryMinus extends OperationElement {
    protected final OperationElement arg;

    public UnaryMinus(final OperationElement arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return "-(" + arg.toString() + ")";
    }

    @Override
    public int hashCode() {
        if (arg == null) {
            throw new NullPointerException();
        }
        return -1 * (arg.hashCode());
    }

    @Override
    public String toMiniString() {
        if (arg instanceof AbstractOperation) {
            return "-(" + arg.toMiniString() + ")";
        }
        return "- " + arg.toMiniString();
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return arg.evaluate(x).negate();
    }

    @Override
    public int getType() {
        return 6;
    }

    @Override
    public int evaluate(int x) {
        return -arg.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -arg.evaluate(x, y, z);
    }
}
