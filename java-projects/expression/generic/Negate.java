package expression.generic;

public class Negate<T> extends AbstractUnaryOperation<T> {

    public Negate(Expression<T> arg, Calculator<T> calculator) {
        super(arg, calculator);
    }

    @Override
    T calculate(T a) {
        return calculator.negate(a);
    }
}
