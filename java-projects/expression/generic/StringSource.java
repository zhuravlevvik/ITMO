package expression.generic;

public class StringSource implements CharSource {
    private final String string;
    private int pos;

    public StringSource(String string) {
        this.string = string;
        pos = 0;
    }

    public Character getCurSymbol() {
        return string.charAt(pos);
    }

    @Override
    public int getPosition() {
        return pos + 1;
    }

    @Override
    public char next() {
        return string.charAt(pos++);
    }

    @Override
    public boolean hasNext() {
        return pos < string.length();
    }

    public boolean hasNext(int cnt) {
        return pos + cnt - 1 < string.length();
    }

    public String getSubStr(int len) {
        return string.substring(pos, pos + len);
    }

    public void skip(int cnt) {
        pos += cnt;
    }

    @Override
    public IllegalArgumentException error(String message) {
        return new IllegalArgumentException(String.format(
                "%d: %s",
                pos, message
        ));
    }
}
