package expression;

import java.math.BigDecimal;

public class Subtract extends AbstractOperation {
    public Subtract(final OperationElement arg1, final OperationElement arg2) {
        super(arg1, arg2, "-");
    }

    @Override
    public BigDecimal calculate(BigDecimal x, BigDecimal y) {
        return x.subtract(y);
    }

    @Override
    public int calculate(int x, int y) {
        return x - y;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String toMiniString() {
        return convertToMiniString(arg1.getType() == 0, arg2.getType() < 3);
    }
}
