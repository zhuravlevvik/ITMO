package expression.generic;

public class IntegerCalculator implements Calculator<Integer> {

    @Override
    public Integer add(Integer a, Integer b) {
        if (a == null || b == null) {
            return null;
        }
        if (a > 0 && b > 0 && a > Integer.MAX_VALUE - b || a < 0 && b < 0 && a < Integer.MIN_VALUE - b) {
            return null;
        }
        return a + b;
    }

    @Override
    public Integer sub(Integer a, Integer b) {
        if (a == null || b == null) {
            return null;
        }
        if (a >= 0 && b < 0 && a > Integer.MAX_VALUE + b || a <= 0 && b > 0 && a < Integer.MIN_VALUE + b) {
            return null;
        }
        return a - b;
    }

    @Override
    public Integer mul(Integer a, Integer b) {
        if (a == null || b == null) {
            return null;
        }
        if (a > 0 && b > 0 && a > Integer.MAX_VALUE / b || a > 0 && b < 0 && b < Integer.MIN_VALUE / a
                || a < 0 && b > 0 && a < Integer.MIN_VALUE / b || a < 0 && b < 0 && a < Integer.MAX_VALUE / b) {
            return null;
        }
        return a * b;
    }

    @Override
    public Integer div(Integer a, Integer b) {
        if (a == null || b == null) {
            return null;
        }
        if (b == 0) {
            return null;
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            return null;
        }
        return a / b;
    }

    @Override
    public Integer negate(Integer a) {
        if (a == null) {
            return null;
        }
        if (a == Integer.MIN_VALUE) {
            return null;
        }
        return -a;
    }

    @Override
    public Integer cast(Number a) {
        if (a == null) {
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
