package expression.exceptions;

import expression.Abs;
import expression.OperationElement;

public class CheckedAbs extends Abs {
    public CheckedAbs(OperationElement arg) {
        super(arg);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a = arg.evaluate(x, y, z);
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("There was an overflow in the calculations:  abs(" + a + ")");
        }
        if (a < 0) {
            return -a;
        }
        return a;
    }
}
