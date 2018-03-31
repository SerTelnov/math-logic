package parser.expressions;

import java.util.Objects;

/**
 * Created by Telnov Sergey on 23.03.2018.
 */
public class MateVariable implements Expression {
    public final String name;
    private Expression statement;

    public MateVariable(String name) {
        this.name = name;
    }

    public MateVariable(String name, Expression exp) {
        this.name = name;
        this.statement = exp;
    }

    public Expression getStatement() {
        return statement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toTree() {
        return toString();
    }
}
