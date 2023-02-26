package expression.exceptions;

import expression.Divide;
import expression.OperationElement;

public class CheckedDivide extends Divide {
    public CheckedDivide(OperationElement arg1, OperationElement arg2) {
        super(arg1, arg2);
    }

    @Override
    public int calculate(int x, int y) {
        if (y == 0) {
            throw new DivisionByZeroException("There was division by zero");
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException("There was an overflow in the calculations: " + x + " / " + y);
        }
        return x / y;
    }
}
