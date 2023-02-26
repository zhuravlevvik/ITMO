package expression.exceptions;

import expression.Log;
import expression.OperationElement;

public class CheckedLog extends Log {
    public CheckedLog(OperationElement arg1, OperationElement arg2) {
        super(arg1, arg2);
    }

    @Override
    public int calculate(int x, int y) {
        if (y <= 1) {
            throw new LogarithmException("Integer logarithm base must be greater than 1");
        }
        if (x <= 0) {
            throw new LogarithmException("Logarithm argument must be greater than 0");
        }
        int log = 0, pow = 1;
        while (pow <= x) {
            if (pow > Integer.MAX_VALUE / y) {
                return log;
            }
            pow *= y;
            log++;
        }
        return log - 1;
    }
}
