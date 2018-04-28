package parser.expressions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class Variable implements Expression {
    public final String name;

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
    public void setVariables(Set<String> variables) {
        variables.add(name);
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
