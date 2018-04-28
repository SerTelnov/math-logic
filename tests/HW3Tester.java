import hw1.OutputChecker;
import hw3.ProofGetter;
import hw3.ProofMaker;
import org.junit.jupiter.api.Test;
import parser.Default;
import parser.ExpressionParser;
import parser.Statements;
import parser.Util;
import parser.expressions.BinOperation;
import parser.expressions.Expression;
import parser.expressions.ExpressionCreator;
import parser.expressions.Variable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Telnov Sergey on 24.04.2018.
 */
public class HW3Tester {
    private static final ExpressionParser parser = new ExpressionParser();
    private static final ProofMaker proofMaker = new ProofMaker();
    private static final OutputChecker checker = new OutputChecker();

    private Expression[] getExpressionFromFile(final String fileName) throws IOException {
        return parser.getExpressions(
                Files.readAllLines(
                        Paths.get(fileName)));
    }

    private void assertEqualsExpression(Expression[] expected, List<Expression> actual) {
        assertEquals(expected.length, actual.size(), "different proofs length!\n");
        for (int i = 0; i != expected.length; i++) {
            assertEquals(expected[i].toString(), actual.get(i).toString());
        }
    }

    @Test
    public void proofGetterTest() throws IOException {
        ProofGetter getter = new ProofGetter();
        Expression[] exps = getExpressionFromFile("src/hw3/proofs/and/NoNo");
        List<Expression> getterExps = getter.getBinProof("&", false, false);

        assertEqualsExpression(exps, getterExps);
    }

    @Test
    public void proofGetterTextTest() throws IOException {
        ProofGetter getter = new ProofGetter();
        Expression[] exps = getExpressionFromFile("src/hw3/proofs/contraposition-rule");
        List<Expression> getterExps = getter.CONTRAPOSITION_RULE;

        assertEqualsExpression(exps, getterExps);

        exps = getExpressionFromFile("src/hw3/proofs/exclusion-rule-of-the-third");
        getterExps = getter.EXCLUTION_RULE_OF_THIRD;

        assertEqualsExpression(exps, getterExps);

        exps = getExpressionFromFile("src/hw3/proofs/exclution-assumption-rule");
        getterExps = getter.EXCLUTION_ASSUMPTION_RULE;

        assertEqualsExpression(exps, getterExps);
    }

    private void customIsProvedTest(String input, final boolean isProvable) {
        Expression provable = parser.parseProvable(input, false).getValue();
        String proof = proofMaker.getProof(input);
        if (!isProvable) {
            assertTrue(proof.startsWith("Высказывание ложно при "), "proof for unprovable statement");
        } else {
            StringBuilder errors = getProofErrors(proof);

            assertTrue(errors.length() == 0,
                    "input: '" + input + "' isn't proved!\nThis statements wasn't proved: \n" + errors.toString());

            assertTrue(proof.endsWith(provable.toString()), "proof didn't end with provable\n");
        }
    }

    private StringBuilder getProofErrors(String proof) {
        List<String> list = Stream
                .of(proof.split("\n"))
                .collect(Collectors.toList());
        String[] checkerStr = checker.check(list);

        StringBuilder errorsBuilder = new StringBuilder();
        for (String str : checkerStr) {
            if (str.endsWith("(Не доказано)")) {
                errorsBuilder
                        .append(str)
                        .append('\n');
            }
        }

        return errorsBuilder;
    }

    @Test
    public void test01() {
        customIsProvedTest("|=A&B", false);
    }

    @Test
    public void test02() {
        customIsProvedTest("B,W|=A->B", true);
    }

    @Test
    public void test03() {
        customIsProvedTest("|=A->A", true);
    }

    @Test
    public void test04() {
        customIsProvedTest("A|=!!A", true);
    }

    @Test
    public void test05() {
        customIsProvedTest("|=!!A->A", true);
    }

    @Test
    public void test06() {
        customIsProvedTest("|=C->A->B->A", true);
    }

    @Test
    public void test07() {
        customIsProvedTest("|=((P->(Q->R))->((Q&P)->R))", true);
    }

    @Test
    public void test08() {
        customIsProvedTest("!(A|B)|=!A&!B", true);
    }

    @Test
    public void test09() {
        customIsProvedTest("A |=!(!(!(!A)))", true);
    }

    @Test
    public void axiomsTests() {
        Map<String, Exception> errors = new HashMap<>();
        for (Expression axiom : Statements.axioms) {
            try {
                for (int i = 1; i <= 3; i++) {
                    Expression exp = Util.setExpression(i, axiom);
                    System.out.println(String.format("curr: '%s'", exp.toString()));
                    customIsProvedTest(exp.toString(), true);
                }
                System.out.println(String.format("axiom: '%s' OK", axiom.toString()));
            } catch (Exception e) {
                errors.put(String.format("axiom: '%s' FAILED\n", axiom.toString()), e);
            }
        }

        assertTrue(errors.isEmpty(),
                errors.entrySet()
                        .stream()
                        .map(it -> {
                            System.err.println(it.getKey());
                            it.getValue().printStackTrace();
                            return "";
                        })
                        .collect(Collectors.joining()));
    }

    private void customAxiomTest(final String op) {
        Map<String, Exception> errors = new HashMap<>();

        final Expression exp = new BinOperation(new Variable("A"), new Variable("B"), op);
        for (Expression axiom : Statements.axioms) {
            try {
                Expression currExp = ExpressionCreator.getCustomExpression(axiom, exp, exp, exp);
                customIsProvedTest(currExp.toString(), true);
            } catch (Exception e) {
                errors.put(String.format("axiom: '%s' FAILED\n", axiom.toString()), e);
            }
        }

        assertTrue(errors.isEmpty(),
                errors.entrySet()
                        .stream()
                        .map(it -> {
                            System.err.println(it.getKey());
                            it.getValue().printStackTrace();
                            return "";
                        })
                        .collect(Collectors.joining()));
    }

    @Test
    public void axiomOrTest() {
        customAxiomTest(Default.OR);
    }

    @Test
    public void axiomAndTest() {
        customAxiomTest(Default.AND);
    }

    @Test
    public void axiomImpTest() {
        customAxiomTest(Default.IMPLICATION);
    }
}
