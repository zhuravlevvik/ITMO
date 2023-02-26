package expression;

import java.math.BigDecimal;

public abstract class OperationElement implements Expression, TripleExpression {
    public abstract int evaluate(int x);
    public abstract int evaluate(int x, int y, int z);
    public abstract BigDecimal evaluate(BigDecimal x);

    public abstract int getType();

    @Override
    public abstract String toString();

    @Override
    public abstract int hashCode();

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        return this.getClass().equals(object.getClass());
    }
}
