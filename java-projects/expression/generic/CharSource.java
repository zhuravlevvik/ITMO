package expression.generic;

public interface CharSource {
    char next();
    boolean hasNext();
    int getPosition();

    IllegalArgumentException error(String message);
}
