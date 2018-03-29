package parser.expressions;

import java.util.Objects;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class BinOperation implements Expression {
    private Expression left, right;
    public final String sign;

    public BinOperation(Expression a, Expression b, final String sign) {
        this.left = a;
        this.right = b;
        this.sign = sign;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, sign, right);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || !(obj instanceof BinOperation)) {
            return false;
        } else {
            BinOperation that = (BinOperation) obj;
            return sign.equals(that.sign) && left.equals(that.left) && right.equals(that.right);
        }
    }

    @Override
    public String toString() {
        return "(" + left.toString() + sign + right.toString() + ")";
    }

    @Override
    public String toTree() {
        return "(" + sign + "," + left.toTree() + "," + right.toTree() + ")";
    }
}
