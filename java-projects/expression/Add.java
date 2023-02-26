package expression;

import expression.exceptions.CalculatingException;

import java.math.BigDecimal;

public class Add extends AbstractOperation {
    public Add(final OperationElement arg1, final OperationElement arg2) {
        super(arg1, arg2, "+");
    }

    @Override
    public BigDecimal calculate(BigDecimal x, BigDecimal y) {
        return x.add(y);
    }

    @Override
    public int calculate(int x, int y) {
        return x + y;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String toMiniString() {
        return convertToMiniString(arg1.getType() == 0, arg2.getType() == 0);
    }
}
