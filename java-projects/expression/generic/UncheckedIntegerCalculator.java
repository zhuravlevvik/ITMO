package expression.generic;

public class UncheckedIntegerCalculator implements Calculator<Integer> {
    @Override
    public Integer add(Integer a, Integer b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a + b;
    }

    @Override
    public Integer sub(Integer a, Integer b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a - b;
    }

    @Override
    public Integer mul(Integer a, Integer b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a * b;
    }

    @Override
    public Integer div(Integer a, Integer b) {
        if (checkNull(a, b)) {
            return null;
        }
        if (b == 0) {
            return null;
        }
        return a / b;
    }

    @Override
    public Integer negate(Integer a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return -a;
    }

    @Override
    public Integer cast(Number a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return Integer.valueOf(String.valueOf(a));
    }

    @Override
    public Integer count(Integer a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return Integer.bitCount(a);
    }

    @Override
    public Integer min(Integer a, Integer b) {
        if (checkNull(a, b)) {
            return null;
        }
        return Integer.min(a, b);
    }

    @Override
    public Integer max(Integer a, Integer b) {
        if (checkNull(a, b)) {
            return null;
        }
        return Integer.max(a, b);
    }
}
