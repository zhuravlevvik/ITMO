package expression.generic;

public class Max<T> extends AbstractBinaryOperation<T> {
    public Max(Expression<T> arg1, Expression<T> arg2, Calculator<T> calculator) {
        super(arg1, arg2, calculator);
    }

    @Override
    public T calculate(T a, T b) {
        return calculator.max(a, b);
    }
}
