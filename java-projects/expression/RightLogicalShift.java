package expression;

import java.math.BigDecimal;

public class RightLogicalShift extends AbstractOperation {

    public RightLogicalShift(OperationElement arg1, OperationElement arg2) {
        super(arg1, arg2, ">>>");
    }

    @Override
    public BigDecimal calculate(BigDecimal x, BigDecimal y) {
        return new BigDecimal(x.intValue() >>> y.intValue());
    }

    @Override
    public int calculate(int x, int y) {
        return x >>> y;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String toMiniString() {
        return convertToMiniString(false, arg2.getType() == 0);
    }
}
