package parser;

import parser.ExpressionParser;
import parser.expressions.Expression;

/**
 * Created by Telnov Sergey on 23.03.2018.
 */
public class Statements {
    private static ExpressionParser parser = new ExpressionParser();

    public static final Expression axiom01 = parser.parse("(a)->((b)->(a))");
    public static final Expression axiom02 = parser.parse("(a->b)->(a->b->c)->(a->c)");
    public static final Expression axiom03 = parser.parse("a->b->a&b");
    public static final Expression axiom04 = parser.parse("a&b->a");
    public static final Expression axiom05 = parser.parse("a&b->b");
    public static final Expression axiom06 = parser.parse("a->a|b");
    public static final Expression axiom07 = parser.parse("b->a|b");
    public static final Expression axiom08 = parser.parse("(a->c)->(b->c)->(a|b->c)");
    public static final Expression axiom09 = parser.parse("(a->b)->(a->!b)->!a");
    public static final Expression axiom10 = parser.parse("!!a->a");

    public static final Expression[] axioms = {axiom01, axiom02, axiom03, axiom04, axiom05,
            axiom06, axiom07, axiom08, axiom09, axiom10};
}
