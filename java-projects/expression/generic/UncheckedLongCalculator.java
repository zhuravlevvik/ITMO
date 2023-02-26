package expression.generic;

public class UncheckedLongCalculator implements Calculator<Long> {
    @Override
    public Long add(Long a, Long b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a + b;
    }

    @Override
    public Long sub(Long a, Long b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a - b;
    }

    @Override
    public Long mul(Long a, Long b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a * b;
    }

    @Override
    public Long div(Long a, Long b) {
        if (checkNull(a, b)) {
            return null;
        }
        if (b == 0) {
            return null;
        }
        return a / b;
    }

    @Override
    public Long negate(Long a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return -a;
    }

    @Override
    public Long cast(Number a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return Long.valueOf(String.valueOf(a));
    }

    @Override
    public Long count(Long a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return cast(Long.bitCount(a));
    }

    @Override
    public Long min(Long a, Long b) {
        if (checkNull(a, b)) {
            return null;
        }
        return Long.min(a, b);
    }

    @Override
    public Long max(Long a, Long b) {
        if (checkNull(a, b)) {
            return null;
        }
        return Long.max(a, b);
    }
}
