package parser.expressions;

/**
 * Created by Telnov Sergey on 23.03.2018.
 */
public class Statement implements Expression {
    public final String name;

    public Statement(String name) {
        this.name = name;
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
