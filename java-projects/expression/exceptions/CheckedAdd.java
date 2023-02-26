package expression.exceptions;

import expression.Add;
import expression.OperationElement;

public class CheckedAdd extends Add {

    public CheckedAdd(OperationElement arg1, OperationElement arg2) {
        super(arg1, arg2);
    }

    @Override
    public int calculate(int x, int y) throws OverflowException {
        if (x > 0 && y > 0 && x > Integer.MAX_VALUE - y || x < 0 && y < 0 && x < Integer.MIN_VALUE - y) {
            throw new OverflowException("There was an overflow in the calculations: " + x + " + " + y);
        }
        return x + y;
    }
}