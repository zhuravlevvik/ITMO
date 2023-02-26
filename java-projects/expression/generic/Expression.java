package expression.generic;

public interface Expression<T> {
    T evaluate(T x, T y, T z);
}
