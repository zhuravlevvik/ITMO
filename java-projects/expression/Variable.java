package expression;

import java.math.BigDecimal;
import java.util.Set;

public class Variable extends OperationElement {
    private String var;
    private static final Set<String> VAR_NAMES = Set.of("x", "y", "z");

    public Variable(String s) {
        var = s;
        if (!VAR_NAMES.contains(var)) {
            throw new IllegalArgumentException("Bad variable name");
        }
    }

    @Override
    public int evaluate(int x) {
            return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (var) {
            case "x": return x;
            case "y": return y;
            case "z": return z;
            default: new AssertionError("Bad variable name");
        }
        return -1;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return x;
    }

    @Override
    public int getType() {
        return 6;
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public String toMiniString() {
        return this.toString();
    }

    @Override
    public int hashCode() {
        if (var == null) {
            throw new NullPointerException();
        }
        return var.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            Variable variable = (Variable) object;
            return this.var.equals(variable.var);
        }
        return false;
    }
}
