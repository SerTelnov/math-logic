package parser.expressions;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Negate implements Expression {
    private Expression expression;

    public Negate(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toStr() {
        return String.format("(!%s)", expression.toStr());
    }
}
