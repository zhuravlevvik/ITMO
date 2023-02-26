package expression.generic;

abstract class AbstractUnaryOperation<T> implements Expression<T> {
    protected Expression<T> arg;
    protected Calculator<T> calculator;

    protected AbstractUnaryOperation(Expression<T> arg, Calculator<T> calculator) {
        this.arg = arg;
        this.calculator = calculator;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return calculate(arg.evaluate(x, y, z));
    }

    abstract T calculate(T a);
}
