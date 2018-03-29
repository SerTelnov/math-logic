package hw1;

import javafx.util.Pair;
import parser.Default;
import parser.ExpressionParser;
import parser.expressions.BinOperation;
import parser.expressions.Expression;
import parser.expressions.Negate;
import parser.expressions.Statement;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Telnov Sergey on 23.03.2018.
 */
public class OutputChecker {
    private final HashMap<String, Expression> metaVariables = new HashMap<>();
    private String[] messages;
    private HashMap<Expression, Integer> hypotheses = new HashMap<>();
    private HashMap<Expression, Integer> allStatements = new HashMap<>();
//    something, that can be proved
    private HashMap<Expression, Pair<Integer, Integer>> mp = new HashMap<>();
//    used statements
    private HashMap<Expression, Integer> provenAssertions = new HashMap<>();


    public String[] check(String[] input) {
        ExpressionParser parser = new ExpressionParser();
        hypotheses.clear();
        allStatements.clear();
        mp.clear();
        provenAssertions.clear();

        List<Expression> listHypotheses = parser.parseAssumption(input[0]);
        for (int i = 0; i != listHypotheses.size(); i++) {
            hypotheses.put(listHypotheses.get(i), i + 1);
        }

        final int n = input.length - 1;
        Expression[] expressions = new Expression[n];
        messages = new String[n];

        for (int i = 0; i != n; i++) {
            expressions[i] = parser.parse(input[i + 1]);
        }
        checkExpressionsOnAxioms(expressions);

        for (int i = 0; i != n; i++) {
            if (messages[i] == null) {
                if (mp.containsKey(expressions[i])) {
                    putStatement(expressions[i], i);
                    messages[i] = getMPMessage(expressions[i], i);
                } else if (provenAssertions.containsKey(expressions[i])) {
                    messages[i] = messages[provenAssertions.get(expressions[i]) - 1];
                }
            } else {
                putMP(expressions[i], i);
            }
        }

        for (int i = 0; i != n; i++) {
            if (messages[i] == null) {
                if (mp.containsKey(expressions[i])) {
                    messages[i] = getMPMessage(expressions[i], i);
                    putStatement(expressions[i], i);
                } else {
                    putMP(expressions[i], i);
                }
            }
        }

        for (int i = 0; i != n; i++) {
            if (messages[i] == null) {
                if (mp.containsKey(expressions[i])) {
                    messages[i] = getMPMessage(expressions[i], i);
                } else if (provenAssertions.containsKey(expressions[i])) {
                    messages[i] = messages[provenAssertions.get(expressions[i]) - 1];
                } else {
                    messages[i] = getMessage(i, expressions[i], "Не доказано");
                }
            }
        }
//        for (int i = 0; i != n; i++) {
//            messages[i] = "(" + (i + 1) + ") " + messages[i];
//        }
        return messages;
    }

    private void putMP(Expression expression, final int expNumber) {
        if (expression instanceof BinOperation) {
            BinOperation binOp = (BinOperation) expression;
            if (binOp.sign.equals(Default.IMPLICATION)) {
                if (mp.containsKey(binOp.getRight())) {
                    Pair<Integer, Integer> pair = mp.get(binOp.getRight());
                    if (pair.getKey() <= expNumber + 1) {
                        return;
                    }
                }
                final Expression left = binOp.getLeft();
                if (allStatements.containsKey(left)) {
                    final int leftIndex = allStatements.get(left);
                    mp.put(binOp.getRight(), new Pair<>(expNumber + 1, leftIndex + 1));
                }
            }
        }
    }

    private void putStatement(Expression expression, final int expNumber) {
        if (provenAssertions.containsKey(expression)) {
            final int index = provenAssertions.get(expression);
            if (index < expNumber) {
                putMP(expression, expNumber);
                return;
            }
        }
        provenAssertions.put(expression, expNumber + 1);
        putMP(expression, expNumber);
    }

    private String getMPMessage(Expression exp, final int index) {
        Pair<Integer, Integer> indices = mp.get(exp);
        if (indices.getValue() < index + 1 && indices.getKey() < index + 1) {
            return getMessage(index, exp, "M.P. " + indices.getKey() + ", " + indices.getValue());
        } else {
            return getMessage(index, exp, "Не доказано");
        }
    }

    private String getMessage(final int index, Expression expression, String mes) {
        return expression.toString() + " (" + mes + ")";
    }

    private void checkExpressionsOnAxioms(Expression[] expressions) {
        for (int i = 0; i != expressions.length; i++) {
            if (!provenAssertions.containsKey(expressions[i])) {
                if (mp.containsKey(expressions[i])) {
                    messages[i] = getMPMessage(expressions[i], i);
                } else if (messages[i] == null && hypotheses.containsKey(expressions[i])) {
                    messages[i] = getMessage(i, expressions[i], "Предп. " + hypotheses.get(expressions[i]));
                } else {
                    for (int j = 0; j != Statements.rules.length; j++) {
                        metaVariables.clear();
                        if (isAxioms(Statements.rules[j], expressions[i])) {
                            messages[i] = getMessage(i, expressions[i], "Сх. акс. " + (j + 1));
                            break;
                        }
                    }
                }
                if (messages[i] != null) {
                    putStatement(expressions[i], i);
                }
                allStatements.putIfAbsent(expressions[i], i);
            }
        }
    }

    private boolean isAxioms(Expression axiom, Expression exp) {
        if (axiom instanceof Statement) {
            Statement rule = (Statement) axiom;
            if (!metaVariables.containsKey(rule.name)) {
                metaVariables.put(rule.name, exp);
                return true;
            } else {
                return metaVariables.get(rule.name).equals(exp);
            }
        } else if (axiom instanceof BinOperation) {
            if (!(exp instanceof BinOperation)) {
                return false;
            } else {
                BinOperation binAxiom = (BinOperation) axiom;
                BinOperation binExp = (BinOperation) exp;
                return binAxiom.sign.equals(binExp.sign) &&
                        isAxioms(binAxiom.getLeft(), binExp.getLeft()) &&
                        isAxioms(binAxiom.getRight(), binExp.getRight());
            }
        } else if (axiom instanceof Negate) {
            return exp instanceof Negate &&
                    isAxioms(
                            ((Negate) axiom).getExpression(),
                            ((Negate) exp).getExpression());
        } else {
            return false;
        }
    }
}
