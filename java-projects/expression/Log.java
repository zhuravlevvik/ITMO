package expression;

import expression.exceptions.OverflowException;

import java.math.BigDecimal;

public class Log extends AbstractOperation {
    public Log(OperationElement arg1, OperationElement arg2) {
        super(arg1, arg2, "//");
    }

    @Override
    public BigDecimal calculate(BigDecimal x, BigDecimal y) {
        BigDecimal log = new BigDecimal(0);
        BigDecimal pow = new BigDecimal(1);
        while (pow.compareTo(y) < 1) {
            log.add(new BigDecimal(1));
            pow.multiply(x);
        }
        log.subtract(new BigDecimal(1));
        return log;
    }

    @Override
    public int calculate(int x, int y) {
        int log = 0, pow = 1;
        while (pow <= x) {
            if ((pow * y) / pow != y || (pow * y) / y != pow) {
                return log;
            }
            pow *= y;
            log++;
        }
        return log - 1;
    }

    @Override
    public String toMiniString() {
        return convertToMiniString(arg1.getType() < 5, arg2.getType() <= 5);
    }

    @Override
    public String toString() {
        return "(" + arg1.toString() + " // " + arg2.toString() + ")";
    }

    @Override
    public int getType() {
        return 5;
    }
}
