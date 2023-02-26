package expression.exceptions;

import expression.*;

public class ExpressionParser implements TripleParser {

    @Override
    public TripleExpression parse(String expression) throws ParsingExceptions {
        //System.err.println("{" + expression + "}");
        return parse(new StringSource(expression));
    }

    public TripleExpression parse(StringSource source) throws ParsingExceptions {
        ExpressionSourceParser parser = new ExpressionSourceParser(source);
        TripleExpression expression = parser.parse();
        if (!parser.end()) {
            throw new IllegalSymbolException("Unsupported operation at position: " + parser.getPosition());
        }
        return expression;
    }

    private static class ExpressionSourceParser extends BaseParser {
        public ExpressionSourceParser(StringSource stringSource) {
            super(stringSource);
        }

        public TripleExpression parse() throws ParsingExceptions {
            return parseExpression(false);
        }

        private TripleExpression parseExpression(boolean expectedBracket) throws ParsingExceptions {
            skipWhiteSpace();
            TripleExpression expression = parseShifts(parseAddSub(parseMulDiv(parseLogPow(parseElement()))));
            skipWhiteSpace();
            if (test(")", true)) {
                if (!expectedBracket) {
                    throw new ExtraBracketException("Extra right bracket at position: " + (getPosition() - 1));
                }
            } else if (expectedBracket) {
                throw new ExtraBracketException("Right bracket expected at position: " + getPosition());
            }
            return expression;
        }

        private OperationElement parseElement() throws ParsingExceptions {
            skipWhiteSpace();
            if (end()) {
                throw new MissingArgumentException("Argument of operation expected at position: " + (getPosition() - 1));
            }
            if (take('(')) {
                return (OperationElement) parseExpression(true);
            } else if (take('-')) {
                if (end()) {
                    throw new MissingArgumentException("Argument of operation expected at position: " + getPosition());
                }
                if (between('0', '9')) {
                    return parseConst(true);
                }
                return new CheckedNegate(parseElement());
            } else if (between('0', '9')) {
                return parseConst(false);
            } else if (take('a')) {
                String string = "a" + takeWord();
                if (string.equals("abs") && (end() || Character.isWhitespace(getCurSymbol()) || getCurSymbol() == '(')) {
                    skipWhiteSpace();
                    return new CheckedAbs(parseElement());
                }
                return new Variable(string);
            } else if (Character.isLetter(getCurSymbol())) {
                return new Variable(takeWord());
            }
            throw new IllegalSymbolException("Un1supported element at position : " + getPosition());
        }

        private OperationElement parseShifts(OperationElement element) throws ParsingExceptions {
            if (end()) {
                return element;
            }
            skipWhiteSpace();
            if (test(')')) {
                return element;
            } else if (test(">>>", true)) {
                return parseShifts(new RightLogicalShift(element, parseAddSub(parseMulDiv(parseLogPow(parseElement())))));
            } else if (test(">>", true)) {
                return parseShifts(new RightShift(element, parseAddSub(parseMulDiv(parseLogPow(parseElement())))));
            } else if (test("<<", true)) {
                return parseShifts(new LeftShift(element, parseAddSub(parseMulDiv(parseLogPow(parseElement())))));
            }
            return parseAddSub(element);
        }

        private OperationElement parseAddSub(OperationElement element) throws ParsingExceptions {
            if (end()) {
                return element;
            }
            skipWhiteSpace();
            if (test(')')) {
                return element;
            } else if (test("+", true)) {
                return parseAddSub(new CheckedAdd(element, parseMulDiv(parseLogPow(parseElement()))));
            } else if (test("-", true)) {
                return parseAddSub(new CheckedSubtract(element, parseMulDiv(parseLogPow(parseElement()))));
            }
            return parseMulDiv(element);
        }

        private OperationElement parseMulDiv(OperationElement element) throws ParsingExceptions {
            if (end()) {
                return element;
            }
            skipWhiteSpace();
            if (test(')')) {
                return element;
            } else if (!test("**", false) && test("*", true)) {
                return parseMulDiv(new CheckedMultiply(element, parseLogPow(parseElement())));
            } else if (!test("//", false) && test("/", true)) {
                return parseMulDiv(new CheckedDivide(element, parseLogPow(parseElement())));
            }
            return parseLogPow(element);
        }

        private OperationElement parseLogPow(OperationElement element) throws ParsingExceptions {
            if (end()) {
                return element;
            }
            skipWhiteSpace();
            if (test("**", true)) {
                return parseLogPow(new CheckedPow(element, parseElement()));
            } else if (test("//", true)) {
                return parseLogPow(new CheckedLog(element, parseElement()));
            }
            return element;
        }

        private OperationElement parseConst(boolean withUnaryMinus) {
            int num = parseNumber(withUnaryMinus);
            if (num == 0 && withUnaryMinus) {
                return new UnaryMinus(new Const(0));
            }
            return new Const(num);
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
