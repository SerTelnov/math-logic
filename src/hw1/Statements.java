package hw1;

import parser.ExpressionParser;
import parser.expressions.Expression;

/**
 * Created by Telnov Sergey on 23.03.2018.
 */
public class Statements {
    private static ExpressionParser parser = new ExpressionParser();

    public static final Expression rule01 = parser.parse("(a)->((b)->(a))");
    public static final Expression rule02 = parser.parse("(a->b)->(a->b->c)->(a->c)");
    public static final Expression rule03 = parser.parse("a->b->a&b");
    public static final Expression rule04 = parser.parse("a&b->a");
    public static final Expression rule05 = parser.parse("a&b->b");
    public static final Expression rule06 = parser.parse("a->a|b");
    public static final Expression rule07 = parser.parse("b->a|b");
    public static final Expression rule08 = parser.parse("(a->c)->(b->c)->(a|b->c)");
    public static final Expression rule09 = parser.parse("(a->b)->(a->!b)->!a");
    public static final Expression rule10 = parser.parse("!!a->a");

    public static final Expression[] rules = {rule01, rule02, rule03, rule04, rule05,
            rule06, rule07, rule08, rule09, rule10};
}
