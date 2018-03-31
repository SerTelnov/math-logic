package parser.expressions;

import java.util.HashMap;

/**
 * Created by Telnov Sergey on 31.03.2018.
 */
public class ExpressionCreator {
    private static Expression[] expressionsValues;

    public static Expression getCustomExpression(Expression axiom, Expression... expressions) {
        expressionsValues = expressions;
        return createExpression(axiom);
    }

    private static Expression getMetaVariableValue(String name) {
        switch (name) {
            case "a":
                return expressionsValues[0];
            case "b":
                return expressionsValues[1];
            default:
                return expressionsValues[2];
        }
    }

    private static Expression createExpression(Expression axiom) {
        if (axiom instanceof BinOperation) {
            BinOperation binAxiom = (BinOperation) axiom;
            return new BinOperation(
                    createExpression(binAxiom.getLeft()),
                    createExpression(binAxiom.getRight()),
                    binAxiom.sign
            );
        } else if (axiom instanceof Negate) {
            return new Negate(
                    createExpression(((Negate) axiom).getExpression())
            );
        } else if (axiom instanceof MateVariable) {
            return getMetaVariableValue(((MateVariable) axiom).name);
        } else {
            return null;
        }
    }
}
