package expression.generic;

public interface Calculator<T> {
    T add(T a, T b);
    T sub(T a, T b);
    T mul(T a, T b);
    T div(T a, T b);
    T negate(T a);
    T cast(Number a);
    T count(T a);
    T min(T a, T b);
    T max(T a, T b);

    default boolean checkNull(Object a, Object b) {
        return a == null || b == null;
    }
}
