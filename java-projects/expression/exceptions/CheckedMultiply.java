package expression.exceptions;

import expression.Multiply;
import expression.OperationElement;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(OperationElement arg1, OperationElement arg2) {
        super(arg1, arg2);
    }

    @Override
    public int calculate(int x, int y) {
        if (x > 0 && y > 0 && x > Integer.MAX_VALUE / y || x > 0 && y < 0 && y < Integer.MIN_VALUE / x
            || x < 0 && y > 0 && x < Integer.MIN_VALUE / y || x < 0 && y < 0 && x < Integer.MAX_VALUE / y) {
            throw new OverflowException("There was an overflow in the calculations: " + x + " * " + y);
        }
        return x * y;
    }
}