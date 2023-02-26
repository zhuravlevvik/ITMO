package expression.generic;

public class BaseParser {
    private final StringSource source;

    public BaseParser(StringSource source) {
        this.source = source;
    }

    protected boolean test(final char expected) {
        return getCurSymbol() == expected;
    }

    protected boolean test(final String expected, boolean skip) {
        if (source.hasNext(expected.length()) && source.getSubStr(expected.length()).equals(expected)) {
            if (skip) {
                source.skip(expected.length());
            }
            return true;
        }
        return false;
    }

    protected String takeWord() {
        StringBuilder sb = new StringBuilder();
        while (!end() && Character.isLetter(getCurSymbol())) {
            sb.append(take());
            if (end()) {
                break;
            }
        }
        return sb.toString();
    }

    protected char getCurSymbol() {
        return source.getCurSymbol();
    }

    protected int getPosition() {
        return source.getPosition();
    }

    protected char take() {
        return source.next();
    }

    protected boolean take(final char expected) {
        if (end()) {
            return false;
        }
        if (test(expected)) {
            take();
            return true;
        } else {
            return false;
        }
    }

    protected IllegalArgumentException error(String message) {
        return source.error(message);
    }

    protected boolean between(final char min, final char max) {
        return min <= getCurSymbol() && getCurSymbol() <= max;
    }

    protected boolean end() {
        return !source.hasNext();
    }
}
