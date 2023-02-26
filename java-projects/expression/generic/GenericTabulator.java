package expression.generic;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, Calculator<? extends Number>> CALCULATORS = Map.of(
        "i" , new IntegerCalculator(),
        "d", new DoubleCalculator(),
        "bi", new BigIntegerCalculator(),
        "u", new UncheckedIntegerCalculator(),
        "l", new UncheckedLongCalculator(),
        "t", new TruncatedIntegerCalculator()
    );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        if (CALCULATORS.containsKey(mode)) {
            return createTable(x1, x2, y1, y2, z1, z2, CALCULATORS.get(mode), expression);
        } else {
            throw new AssertionError("Unsupported operation mode");
        }
    }

    private <T> Object[][][] createTable(int x1, int x2, int y1, int y2, int z1, int z2, Calculator<T> calculator, String expression) {
        ExpressionParser<T> parser = new ExpressionParser<>();
        Expression<T> expr = parser.parse(expression, calculator);
        Object[][][] table = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    table[i - x1][j - y1][k - z1] = expr.evaluate(calculator.cast(i), calculator.cast(j), calculator.cast(k));
                }
            }
        }
        return table;
    }
}
