package expression.exceptions;

import expression.OperationElement;
import expression.UnaryMinus;

public class CheckedNegate extends UnaryMinus {
    public CheckedNegate(OperationElement arg) {
        super(arg);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a = arg.evaluate(x, y, z);
        if (a == Integer.MIN_VALUE) {
            throw new OverflowException("There was an overflow in the calculations: " + " -" + a);
        }
        return -a;
    }
}
