package parser.expressions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Telnov Sergey on 31.03.2018.
 */
public class ExpressionCreator {
    private static Map<String, Expression> expressionsValues;

    public static Expression getCustomExpression(Expression expr, Map<String, Expression> values) {
        expressionsValues = values;
        return createExpression(expr);
    }

    public static Expression getCustomExpression(Expression expr, Expression... expressions) {
        Map<String, Expression> keys = new HashMap<>();
        char key = 'a';

        for (int i = 0; i != expressions.length; i++) {
            keys.put(Character.toString((char) (key + i)), expressions[i]);
        }
        return getCustomExpression(expr, keys);
    }

    private static Expression getMetaVariableValue(String name) {
        if (!expressionsValues.containsKey(name)) {
            int a = 10;
        }
        return expressionsValues.get(name);
    }

    private static Expression createExpression(Expression expr) {
        if (expr instanceof BinOperation) {
            BinOperation binAxiom = (BinOperation) expr;
            return new BinOperation(
                    createExpression(binAxiom.getLeft()),
                    createExpression(binAxiom.getRight()),
                    binAxiom.sign
            );
        } else if (expr instanceof Negate) {
            return new Negate(
                    createExpression(((Negate) expr).getExpression())
            );
        } else if (expr instanceof MateVariable) {
            return getMetaVariableValue(((MateVariable) expr).name);
        } else {
            return null;
        }
    }
}
