import org.junit.jupiter.api.Test;
import parser.ExpressionParser;
import parser.Statements;
import parser.Util;
import parser.expressions.BinOperation;
import parser.expressions.Expression;
import parser.expressions.ExpressionCreator;
import parser.expressions.Variable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Telnov Sergey on 23.03.2018.
 */
public class ExpressionsTester {
    private ExpressionParser parser = new ExpressionParser();

    @Test
    public void equalsTest01() {
        BinOperation exp1 = (BinOperation) parser.parse("A->B");
        BinOperation exp2 = (BinOperation) parser.parse("C->D");

        assertFalse(exp1.equals(exp2));
        assertFalse(exp2.equals(exp1));

        assertFalse(exp1.equals(exp1.getLeft()));
        assertFalse(exp1.getLeft().equals(exp1));
    }

    @Test
    public void equalsTest02() {
        BinOperation exp1 = (BinOperation) parser.parse("A&B");
        BinOperation exp2 = (BinOperation) parser.parse("A|B");

        assertFalse(exp1.equals(exp2));
        assertFalse(exp2.equals(exp1));
    }

    @Test
    public void equalsTest03() {
        BinOperation exp1 = (BinOperation) parser.parse("A->B->A");
        BinOperation exp2 = (BinOperation) parser.parse("A->B");

        assertFalse(exp1.equals(exp2));
        assertFalse(exp2.equals(exp1));
    }



    @Test
    public void getAxiomNumberTest() {
        Expression[] axioms = Statements.axioms;
        for (int i = 0; i != axioms.length; i++) {
            for (int j = 1; j <= 3; j++) {
                Expression exp = Util.setExpression(j, axioms[i]);
                final int number = Util.getAxiomNumber(exp);

                assertTrue(i + 1 == number || axioms[i].equals(exp),
                        String.format("axiom%d: '%s' is number: '%d'\nnumber of variables: '%d'\n",
                                i + 1, axioms[i].toString(), number, j));
            }
        }
    }
}
