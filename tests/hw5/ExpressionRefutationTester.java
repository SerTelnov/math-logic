package hw5;

import org.junit.jupiter.api.Test;
import parser.ExpressionParser;
import parser.Statements;
import parser.expressions.Expression;
import parser.expressions.ExpressionCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Created by Telnov Sergey on 18.06.2018.
 */
public class ExpressionRefutationTester {

    private ExpressionRefutation refutation = new ExpressionRefutation();
    private ExpressionParser parser = new ExpressionParser();
    private final String OK_MESSAGE = "Формула общезначима";
    private Expression[] expressions = {
            parser.parse("A"),
            parser.parse("B"),
            parser.parse("C")
    };

    private void customTest(String answer, String input) {
        assertEquals(answer, refutation.check(input), "failed '" + input + "'");
    }

    private void customTest(String input, boolean isCorrect) {
        if (isCorrect) {
            customTest(OK_MESSAGE, input);
        } else {
            assertNotSame(OK_MESSAGE, refutation.check(input));
        }
    }

    @Test
    public void test01() {
        customTest(OK_MESSAGE, "A->A");
    }

    @Test
    public void test02() {
        customTest("3\n" +
                "1 2 3\n" +
                "2 3\n" +
                "3\n" +
                "A=2", "A|!A");
    }

    @Test
    public void test03() {
        customTest("A", false);
    }

    @Test
    public void test04() {
        customTest("!!(!!A->A)", true);
    }

    @Test
    public void test05() {
        customTest("3\n" +
                "1 2 3\n" +
                "2 3\n" +
                "3\n" +
                "P=2,Q=1", "((P->Q)->P)->P");
    }

    @Test
    public void test06() {
        customTest("(P->Q->R)|(!P->Q->R)|!(Q->R)", false);
    }

    @Test
    public void test07() {
        customTest("((!((C&A)->((!B)&(C->A))))->(C->B))", false);
    }

    @Test
    public void axiomTest() {
        for (int i = 0; i != Statements.axioms.length - 2; i++) {
            customTest(OK_MESSAGE, ExpressionCreator.getCustomExpression(
                    Statements.axioms[i], expressions).toString());
        }
    }

    @Test
    public void classic10AxiomTest() {
        customTest(ExpressionCreator.getCustomExpression(
                Statements.axiom10, expressions).toString(), false);
    }
}
