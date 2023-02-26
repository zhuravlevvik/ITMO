package expression;

import java.math.BigDecimal;

public class L0 extends OperationElement {
    private final OperationElement arg;

    public L0(OperationElement arg) {
        this.arg = arg;
    }

    @Override
    public int evaluate(int x) {
        return 32 - numberOfNonZeroBits(arg.evaluate(x));
    }

    private int numberOfNonZeroBits(int n) {
        if (n == 0) {
            return 0;
        }
        return Integer.toBinaryString(n).length();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return 32 - numberOfNonZeroBits(arg.evaluate(x, y, z));
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        int arg = this.arg.evaluate(x).intValue();
        return new BigDecimal(32 - numberOfNonZeroBits(arg));
    }

    @Override
    public int getType() {
        return 6;
    }

    @Override
    public String toString() {
        return "l0(" + arg.toString() + ")";
    }

    @Override
    public int hashCode() {
        return ("l0".hashCode() * arg.hashCode());
    }

    @Override
    public String toMiniString() {
        if (arg instanceof AbstractOperation) {
            return "l0(" + arg.toMiniString() + ")";
        }
        return "l0 " + arg.toMiniString();
    }
}
