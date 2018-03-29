package parser.expressions;

import parser.Default;

import java.util.Objects;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Negate implements Expression {
    private Expression expression;

    public Negate(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Default.NEGATE, expression);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || !(obj instanceof Negate)) {
            return false;
        } else {
            return expression.equals(((Negate) obj).expression);
        }
    }

    @Override
    public String toString() {
        return "(!" + expression.toString() + ")";
    }

    @Override
    public String toTree() {
        return "(!" + expression.toTree() + ")";
    }
}
