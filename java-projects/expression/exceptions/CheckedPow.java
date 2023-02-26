package expression.exceptions;

import expression.OperationElement;
import expression.Pow;

public class CheckedPow extends Pow {
    public CheckedPow(OperationElement arg1, OperationElement arg2) {
        super(arg1, arg2);
    }

    @Override
    public int calculate(int x, int y) {
        if (y < 0) {
            throw new PowException("Integer degree of number must be non-negative");
        }
        if (x == 0 && y == 0) {
            throw new PowException("Pointless expression");
        }
        int pow = 1;
        if (x == 1) {
            return 1;
        }
        if (x == -1) {
            y %= 2;
        }
        if (x == 0 && y != 0) {
            return 0;
        }
        for (int i = 0; i < y; i++) {
            if (x > 0 && pow > 0 && x > Integer.MAX_VALUE / pow || x > 0 && pow < 0 && pow < Integer.MIN_VALUE / x
                    || x < 0 && pow > 0 && x < Integer.MIN_VALUE / pow || x < 0 && pow < 0 && x < Integer.MAX_VALUE / pow) {
                throw new OverflowException("There was an overflow in the calculations: " + x + " ** " + y);
            }
            pow *= x;
        }
        return pow;
    }
}
