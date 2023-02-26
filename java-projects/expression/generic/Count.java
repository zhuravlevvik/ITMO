package expression.generic;

public class Count<T> extends AbstractUnaryOperation<T> {
    public Count(Expression<T> arg, Calculator<T> calculator) {
        super(arg, calculator);
    }

    @Override
    T calculate(T a) {
        return calculator.count(a);
    }
}
