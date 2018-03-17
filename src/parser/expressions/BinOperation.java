package parser.expressions;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class BinOperation implements Expression {
    private Expression a, b;
    private final String sign;

    public BinOperation(Expression a, Expression b, final String sign) {
        this.a = a;
        this.b = b;
        this.sign = sign;
    }

    @Override
    public String toStr() {
        return String.format("(%s,%s,%s)", sign, a.toStr(), b.toStr());
    }
}
