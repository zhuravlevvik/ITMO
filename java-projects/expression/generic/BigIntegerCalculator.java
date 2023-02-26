package expression.generic;

import java.math.BigInteger;

public class BigIntegerCalculator implements Calculator<BigInteger> {
    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a.add(b);
    }

    @Override
    public BigInteger sub(BigInteger a, BigInteger b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a.subtract(b);
    }

    @Override
    public BigInteger mul(BigInteger a, BigInteger b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a.multiply(b);
    }

    @Override
    public BigInteger div(BigInteger a, BigInteger b) {
        if (checkNull(a, b)) {
            return null;
        }
        if (b.equals(new BigInteger("0"))) {
            return null;
        }
        return a.divide(b);
    }

    @Override
    public BigInteger negate(BigInteger a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return a.negate();
    }

    @Override
    public BigInteger cast(Number a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return new BigInteger(String.valueOf(a));
    }

    @Override
    public BigInteger count(BigInteger a) {
        if (checkNull(a, 0)) {
            return null;
        }
        return cast(a.bitCount());
    }

    @Override
    public BigInteger min(BigInteger a, BigInteger b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a.min(b);
    }

    @Override
    public BigInteger max(BigInteger a, BigInteger b) {
        if (checkNull(a, b)) {
            return null;
        }
        return a.max(b);
    }
}
