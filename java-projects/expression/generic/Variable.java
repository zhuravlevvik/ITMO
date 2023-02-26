package expression.generic;

import java.util.Set;

public class Variable<T> implements Expression<T> {
    private final String var;
    private static final Set<String> VAR_NAMES = Set.of("x", "y", "z");

    public Variable(String s) {
        var = s;
        if (!VAR_NAMES.contains(var)) {
            throw new IllegalArgumentException("Bad variable name");
        }
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (var) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new AssertionError("Bad variable name");
        };
    }
}
