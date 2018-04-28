package parser.expressions;

import parser.Default;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Negate implements Expression {
    public final Expression expression;

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
    public void setVariables(Set<String> variables) {
        expression.setVariables(variables);
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
