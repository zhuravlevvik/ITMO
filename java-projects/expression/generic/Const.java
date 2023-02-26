package expression.generic;

public class Const<T> implements Expression<T> {
    private final Number cnst;
    private final Calculator<T> calculator;

    public Const(Number cnst, Calculator<T> calculator) {
        this.cnst = cnst;
        this.calculator = calculator;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return calculator.cast(cnst);
    }
}
