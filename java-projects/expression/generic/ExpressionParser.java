package expression.generic;

public class ExpressionParser<T> {

    public Expression<T> parse(String expression, Calculator<T> calculator)  {
        return parse(new StringSource(expression), calculator);
    }

    public Expression<T> parse(StringSource source, Calculator<T> calculator)  {
        ExpressionSourceParser parser = new ExpressionSourceParser(source, calculator);
        Expression<T> expression = parser.parse();
        if (!parser.end()) {
            throw new AssertionError("Unsupported operation at position: " + parser.getPosition());
        }
        return expression;
    }

    private class ExpressionSourceParser extends BaseParser {
        private final Calculator<T> calculator;
        public ExpressionSourceParser(StringSource stringSource, Calculator<T> calculator) {
            super(stringSource);
            this.calculator = calculator;
        }

        public Expression<T> parse() {
            return parseExpression(false);
        }

        private Expression<T> parseExpression(boolean expectedBracket) {
            skipWhiteSpace();
            Expression<T> expression = parseMinMax(parseAddSub(parseMulDiv(parseElement())));
            skipWhiteSpace();
            if (test(")", true)) {
                if (!expectedBracket) {
                    throw new AssertionError("Extra right bracket at position: " + (getPosition() - 1));
                }
            } else if (expectedBracket) {
                throw new AssertionError("Right bracket expected at position: " + getPosition());
            }
            return expression;
        }

        private Expression<T> parseElement() {
            skipWhiteSpace();
            if (end()) {
                throw new AssertionError("Argument of operation expected at position: " + (getPosition() - 1));
            }
            if (take('(')) {
                return parseExpression(true);
            } else if (take('-')) {
                if (end()) {
                    throw new AssertionError("Argument of operation expected at position: " + getPosition());
                }
                if (between('0', '9')) {
                    return parseConst(true);
                }
                return new Negate<>(parseElement(), calculator);
            } else if (between('0', '9')) {
                return parseConst(false);
            } else if (take('c')) {
                String string = "c" + takeWord();
                if (string.equals("count") && (end() || Character.isWhitespace(getCurSymbol()) || getCurSymbol() == '(')) {
                    skipWhiteSpace();
                    return new Count<>(parseElement(), calculator);
                }
                return new Variable<>(string);
            } else if (Character.isLetter(getCurSymbol())) {
                return new Variable<>(takeWord());
            }
            throw new AssertionError("Unsupported element at position : " + getPosition());
        }

        private Expression<T> parseMinMax(Expression<T> expression) {
            if (end()) {
                return expression;
            }
            skipWhiteSpace();

            if (test(')')) {
                return expression;
            } else if (test("min", true)) {
                return parseMinMax(new Min<>(expression, parseAddSub(parseMulDiv(parseElement())), calculator));
            } else if (test("max", true)) {
                return parseMinMax(new Max<>(expression, parseAddSub(parseMulDiv(parseElement())), calculator));
            }
            return parseAddSub(expression);
        }

        private Expression<T> parseAddSub(Expression<T> expression) {
            if (end()) {
                return expression;
            }
            skipWhiteSpace();
            if (test(')')) {
                return expression;
            } else if (test("+", true)) {
                return parseAddSub(new Add<>(expression, parseMulDiv(parseElement()), calculator));
            } else if (test("-", true)) {
                return parseAddSub(new Subtract<>(expression, parseMulDiv(parseElement()), calculator));
            }
            return parseMulDiv(expression);
        }

        private Expression<T> parseMulDiv(Expression<T> expression) {
            if (end()) {
                return expression;
            }
            skipWhiteSpace();
            if (test(')')) {
                return expression;
            } else if (test("*", true)) {
                return parseMulDiv(new Multiply<>(expression, parseElement(), calculator));
            } else if (test("/", true)) {
                return parseMulDiv(new Divide<>(expression, parseElement(), calculator));
            }
            return expression;
        }

        private Expression<T> parseConst(boolean withUnaryMinus) {
            int num = parseNumber(withUnaryMinus);
            if (num == 0 && withUnaryMinus) {
                return new Negate<>(new Const<>(0, calculator), calculator);
            }
            return new Const<>(num, calculator);
        }

        private void skipWhiteSpace() {
            if (end()) {
                return;
            }
            while (Character.isWhitespace(getCurSymbol())) {
                take();
                if (end()) {
                    break;
                }
            }
        }

        private int parseNumber(boolean withUnaryMinus) throws IllegalArgumentException {
            StringBuilder number = new StringBuilder();
            number.append(withUnaryMinus ? "-" : "");
            if (take('0')) {
                return 0;
            }
            while (between('0', '9')) {
                number.append(take());
                if (end()) {
                    break;
                }
            }
            try {
                return Integer.parseInt(number.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number: " + number);
            }
        }
    }
}
