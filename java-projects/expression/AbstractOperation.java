package expression;

import java.math.BigDecimal;

abstract class AbstractOperation extends OperationElement {
    protected final OperationElement arg1;
    protected final OperationElement arg2;
    private final String operation;
    private boolean hashIsCalculated = false;
    private int hash;

    public AbstractOperation(final OperationElement arg1, final OperationElement arg2, final String operation) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.operation = operation;
    }

    public abstract BigDecimal calculate(BigDecimal x, BigDecimal y);
    public abstract int calculate(int x, int y);

    public BigDecimal evaluate(BigDecimal x) {
        return calculate(arg1.evaluate(x), arg2.evaluate(x));
    }
    public int evaluate(int x) {
        return calculate(arg1.evaluate(x), arg2.evaluate(x));
    }
    public int evaluate(int x, int y, int z) {
        return calculate(arg1.evaluate(x, y, z), arg2.evaluate(x, y, z));
    }


    @Override
    public String toString() {
        return "(" + arg1.toString() + " " + operation + " " + arg2.toString() + ")";
    }

    public abstract String toMiniString();

    protected String convertToMiniString(boolean log1, boolean log2) {
        return elementToMiniString(log1, arg1) +" " + operation + " " + elementToMiniString(log2, arg2);
    }

    private String elementToMiniString(boolean withBrackets, OperationElement arg) {
        StringBuilder sb = new StringBuilder();
        sb.append(withBrackets ? "(" + arg.toMiniString() + ")" : arg.toMiniString());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if (arg1 == null || arg2 == null) {
            throw new NullPointerException();
        }
        if (!hashIsCalculated) {
            hash = operation.hashCode() * (arg1.hashCode() + 19 * arg2.hashCode());
            hashIsCalculated = true;
        }
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            AbstractOperation operation = (AbstractOperation) object;
            return this.arg1.equals(operation.arg1) && this.arg2.equals(operation.arg2);
        }
        return false;
    }
}
