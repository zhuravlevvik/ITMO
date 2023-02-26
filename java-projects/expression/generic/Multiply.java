package expression.generic;

public class Multiply<T> extends AbstractBinaryOperation<T> {
    protected Multiply(Expression<T> arg1, Expression<T> arg2, Calculator<T> calculator) {
        super(arg1, arg2, calculator);
    }

    @Override
    public T calculate(T a, T b) {
        return calculator.mul(a, b);
    }
}
