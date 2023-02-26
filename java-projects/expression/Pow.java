package expression;

import java.math.BigDecimal;

public class Pow extends AbstractOperation {
    public Pow(OperationElement arg1, OperationElement arg2) {
        super(arg1, arg2, "**");
    }

    @Override
    public BigDecimal calculate(BigDecimal x, BigDecimal y) {
        return x.pow(y.intValue());
    }

    @Override
    public int calculate(int x, int y) {
        int res = 1;
        for (int i = 1; i < y; i++) {
            res *= x;
        }
        return res;
    }

    @Override
    public String toMiniString() {
        return convertToMiniString(arg1.getType() < 5, arg2.getType() <= 5);
    }

    @Override
    public String toString() {
        return "(" + arg1.toString() + " ** " + arg2.toString() + ")";
    }

    @Override
    public int getType() {
        return 5;
    }
}
