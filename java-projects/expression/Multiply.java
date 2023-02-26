package expression;

import java.math.BigDecimal;

public class Multiply extends AbstractOperation {
    public Multiply(final OperationElement arg1, final OperationElement arg2) {
        super(arg1, arg2, "*");
    }

    @Override
    public BigDecimal calculate(BigDecimal x, BigDecimal y) {
        return x.multiply(y);
    }

    @Override
    public int calculate(int x, int y) {
        return x * y;
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String toMiniString() {
        return convertToMiniString(arg1.getType() < 3, arg2.getType() < 4);
    }
}
