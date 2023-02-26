package expression.generic;

public class TruncatedIntegerCalculator extends UncheckedIntegerCalculator {
    private Integer truncate(Integer a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return (a / 10) * 10;
    }

    @Override
    public Integer add(Integer a, Integer b) {
        return truncate(super.add(truncate(a), truncate(b)));
    }

    @Override
    public Integer sub(Integer a, Integer b) {
        return truncate(super.sub(truncate(a), truncate(b)));
    }

    @Override
    public Integer mul(Integer a, Integer b) {
        return truncate(super.mul(truncate(a), truncate(b)));
    }

    @Override
    public Integer div(Integer a, Integer b) {
        return truncate(super.div(truncate(a), truncate(b)));
    }

    @Override
    public Integer negate(Integer a) {
        return truncate(super.negate(truncate(a)));
    }

    @Override
    public Integer cast(Number a) {
        return truncate(super.cast(a));
    }

    @Override
    public Integer count(Integer a) {
        return truncate(super.count(truncate(a)));
    }

    @Override
    public Integer min(Integer a, Integer b) {
        return super.min(truncate(a), truncate(b));
    }

    @Override
    public Integer max(Integer a, Integer b) {
        return super.max(truncate(a), truncate(b));
    }
}
