package expression.exceptions;

import expression.TripleExpression;

@FunctionalInterface
public interface TripleParser {
    TripleExpression parse(String expression) throws Exception;
}
