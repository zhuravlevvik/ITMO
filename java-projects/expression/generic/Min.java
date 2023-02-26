package expression.generic;

public class Min<T> extends AbstractBinaryOperation<T> {
    Min(Expression<T> arg1, Expression<T> arg2, Calculator<T> calculator) {
        super(arg1, arg2, calculator);
    }

    @Override
    public T calculate(T a, T b) {
        return calculator.min(a, b);
    }
}
