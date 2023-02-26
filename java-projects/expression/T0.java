package expression;

import java.math.BigDecimal;

public class T0 extends OperationElement {
    private final OperationElement arg;

    public T0(OperationElement arg) {
        this.arg = arg;
    }

    @Override
    public int evaluate(int x) {
        return numberOfZeroLowOrderBits(arg.evaluate(x));
    }

    private int numberOfZeroLowOrderBits(int n) {
        if (n == 0) {
            return 32;
        }
        String bin = Integer.toBinaryString(n);
        int cnt = 0;
        for (int i = bin.length() - 1; i >= 0; i--) {
            if (bin.charAt(i) == '1') {
                break;
            }
            cnt++;
        }
        return cnt;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return numberOfZeroLowOrderBits(arg.evaluate(x, y, z));
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        int arg = this.arg.evaluate(x).intValue();
        return new BigDecimal(numberOfZeroLowOrderBits(arg));
    }

    @Override
    public int getType() {
        return 6;
    }

    @Override
    public String toString() {
        return "t0(" + arg.toString() + ")";
    }

    @Override
    public int hashCode() {
        return ("t0".hashCode() * arg.hashCode());
    }

    @Override
    public String toMiniString() {
        if (arg instanceof AbstractOperation) {
            return "t0(" + arg.toMiniString() + ")";
        }
        return "t0 " + arg.toMiniString();
    }
}
