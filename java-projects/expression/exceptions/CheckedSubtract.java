package expression.exceptions;

import expression.OperationElement;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(OperationElement arg1, OperationElement arg2) {
        super(arg1, arg2);
    }

    @Override
    public int calculate(int x, int y) {
        if (x >= 0 && y < 0 && x - y < 0 || x <= 0 && y > 0 && x - y > 0) {
            throw new OverflowException("There was an overflow in the calculations: " + x + " - " + y);
        }
        return x - y;
    }
}
