package expression;

import java.math.BigDecimal;

public class Abs extends OperationElement {
    protected OperationElement arg;

    public Abs(OperationElement arg) {
        this.arg = arg;
    }

    @Override
    public int evaluate(int x) {
        int a = arg.evaluate(x);
        if (a < 0) {
            return -a;
        }
        return a;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a = arg.evaluate(x, y, z);
        if (a < 0) {
            return -a;
        }
        return a;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return arg.evaluate(x).abs();
    }

    @Override
    public int getType() {
        return 6;
    }

    @Override
    public String toString() {
        return "abs(" + arg.toString() + ")";
    }

    @Override
    public String toMiniString() {
        if (!(arg instanceof AbstractOperation)) {
            return "abs " + arg.toMiniString();
        }
        return "abs(" + arg.toMiniString() + ")";
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
