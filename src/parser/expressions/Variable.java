package parser.expressions;

import java.util.Objects;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Variable implements Expression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Variable && ((Variable) obj).name.equals(name);
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
