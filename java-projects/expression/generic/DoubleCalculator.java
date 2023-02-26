package expression.generic;

public class DoubleCalculator implements Calculator<Double> {
    @Override
    public Double add(Double a, Double b) {
        if (a == null || b == null) {
            return null;
        }
        return a + b;
    }

    @Override
    public Double sub(Double a, Double b) {
        if (a == null || b == null) {
            return null;
        }
        return a - b;
    }

    @Override
    public Double mul(Double a, Double b) {
        if (a == null || b == null) {
            return null;
        }
        return a * b;
    }

    @Override
    public Double div(Double a, Double b) {
        if (a == null || b == null) {
            return null;
        }
        return a / b;
    }

    @Override
    public Double negate(Double a) {
        if (a == null) {
            return null;
        }
        return -a;
    }

    @Override
    public Double cast(Number a) {
        if (a == null) {
            return null;
        }
        return Double.valueOf(String.valueOf(a));
    }

    @Override
    public Double count(Double a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return cast(Long.bitCount(Double.doubleToLongBits(a)));
    }

    @Override
    public Double min(Double a, Double b) {
        if (checkNull(a, b)) {
            return null;
        }
        return Double.min(a, b);
    }

    @Override
    public Double max(Double a, Double b) {
        if (checkNull(a, b)) {
            return null;
        }
        return Double.max(a, b);
    }
}
