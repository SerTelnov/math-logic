import org.junit.jupiter.api.Test;
import parser.ExpressionParser;
import parser.expressions.BinOperation;

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
}
