package hw1;

import javafx.util.Pair;
import parser.Default;
import parser.ExpressionParser;
import parser.Util;
import parser.expressions.BinOperation;
import parser.expressions.Expression;

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


    public String[] check(List<String> input) {
        ExpressionParser parser = new ExpressionParser();
        hypotheses.clear();
        allStatements.clear();
        mp.clear();
        provenAssertions.clear();

        List<Expression> listHypotheses = parser.parseAssumption(input.get(0));
        for (int i = 0; i != listHypotheses.size(); i++) {
            hypotheses.put(listHypotheses.get(i), i + 1);
        }

        final int n = input.size() - 1;
        Expression[] expressions = new Expression[n];
        messages = new String[n];

        for (int i = 0; i != n; i++) {
            expressions[i] = parser.parse(input.get(i + 1));
            checkExpression(expressions[i], i);
        }

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
                    messages[i] = getMessage(expressions[i], "Не доказано");
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
            return getMessage(exp, "M.P. " + indices.getKey() + ", " + indices.getValue());
        } else {
            return getMessage(exp, "Не доказано");
        }
    }

    private String getMessage(Expression expression, String mes) {
        return expression.toString() + " (" + mes + ")";
    }

    private void checkExpression(Expression expression, final int index) {
        if (!provenAssertions.containsKey(expression)) {
            if (mp.containsKey(expression)) {
                messages[index] = getMPMessage(expression, index);
            } else if (messages[index] == null && hypotheses.containsKey(expression)) {
                messages[index] = getMessage(expression, "Предп. " + hypotheses.get(expression));
            } else {
                final int axiomNum = Util.getAxiomNumber(expression);
                if (axiomNum > 0) {
                    messages[index] = getMessage(expression, "Сх. акс. " + axiomNum);
                }
            }
            if (messages[index] != null) {
                putStatement(expression, index);
            }
            allStatements.putIfAbsent(expression, index);
        }
    }
}
