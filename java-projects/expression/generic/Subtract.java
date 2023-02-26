package expression.generic;

public class Subtract<T> extends AbstractBinaryOperation<T> {
    public Subtract(Expression<T> arg1, Expression<T> arg2, Calculator<T> calculator) {
        super(arg1, arg2, calculator);
    }

    @Override
    public T calculate(T a, T b) {
        return calculator.sub(a, b);
    }
}
