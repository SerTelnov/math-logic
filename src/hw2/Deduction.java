package hw2;

import javafx.util.Pair;
import parser.Default;
import parser.ExpressionParser;
import parser.Statements;
import parser.Util;
import parser.expressions.BinOperation;
import parser.expressions.Expression;
import parser.expressions.ExpressionCreator;

import java.util.*;

/**
 * Created by Telnov Sergey on 31.03.2018.
 */
public class Deduction {
    private Expression a;
    private Set<Expression> hypothesis = new HashSet<>();
    private Set<Expression> usedUtterances = new HashSet<>();
    private HashMap<Expression, Pair<Expression, Expression>> mp = new HashMap<>();
    private List<Expression> proofCollector;

    public String applyDeduction(final List<String> input) {
        StringJoiner proofTextJoiner = new StringJoiner("\n");
        Set<Expression> hypothesis = new HashSet<>();

        ExpressionParser parser = new ExpressionParser();
        Pair<List<Expression>, Expression> pair = parser.parseProvable(input.get(0), true);
        List<Expression> list = pair.getKey();
        list.add(pair.getValue());
        Expression a = list.get(list.size() - 2);
        Expression b = list.get(list.size() - 1);
        BinOperation provable = new BinOperation(a, list.get(list.size() - 1), Default.IMPLICATION);

        StringJoiner hypothesisJoiner = new StringJoiner(",", "", "|-");
        for (int i = 0; i < list.size() - 2; i++) {
            hypothesis.add(list.get(i));
            hypothesisJoiner.add(list.get(i)
                    .toString());
        }

        proofTextJoiner.add(hypothesisJoiner.toString() + provable.toString());

        List<Expression> statements = new ArrayList<>(input.size() - 1);
        for (int i = 1; i != input.size(); i++) {
            statements.add(parser.parse(input.get(i)));
        }

        List<Expression> proof = applyDeduction(statements, hypothesis, a, b);
        proof.forEach(it -> proofTextJoiner.add(it.toString()));

        return proofTextJoiner.toString();
    }

    public List<Expression> applyDeduction(List<Expression> expressions, Set<Expression> hypothesis,
                                           final Expression a, final Expression b) {
        this.hypothesis = hypothesis;
        proofCollector = new ArrayList<>();
        this.a = a;
        Expression provable = new BinOperation(a, b, Default.IMPLICATION);

        for (Expression exp : expressions) {
            if (provable.equals(processExpression(exp))) {
                break;
            }
        }

        usedUtterances.clear();
        mp.clear();

        return proofCollector;
    }

    private void putMP(Expression expression) {
        if (expression instanceof BinOperation) {
            BinOperation binOp = (BinOperation) expression;
            if (binOp.sign.equals(Default.IMPLICATION) && !mp.containsKey(binOp.getRight())) {
                final Expression left = binOp.getLeft();
                if (usedUtterances.contains(left)) {
                    mp.put(binOp.getRight(), new Pair<>(expression, left));
                }
            }
        }
    }

    private Expression processExpression(Expression currExp) {
        if (usedUtterances.contains(currExp))
            return null;

        Expression nextExp = null;
        if (hypothesis.contains(currExp) || Util.getAxiomNumber(currExp) > 0) {
            BinOperation prevExp = (BinOperation) ExpressionCreator.getCustomExpression(
                    Statements.axiom01,
                    currExp, a
            );
            nextExp = prevExp.getRight();

            proofCollector.add(prevExp);
            proofCollector.add(currExp);
            proofCollector.add(nextExp);
        } else if (currExp.equals(a)) {
            BinOperation exp1 = (BinOperation) ExpressionCreator.getCustomExpression(
                    Statements.axiom01,
                    a, a
            );
            BinOperation exp2 = (BinOperation) ExpressionCreator.getCustomExpression(
                    Statements.axiom02,
                    a, exp1.getRight(), a
            );
            BinOperation exp3 = (BinOperation) exp2.getRight();
            BinOperation exp4 = (BinOperation) ExpressionCreator.getCustomExpression(
                    Statements.axiom01,
                    a, exp1.getRight()
            );
            nextExp = exp3.getRight();

            proofCollector.add(exp1);
            proofCollector.add(exp2);
            proofCollector.add(exp3);
            proofCollector.add(exp4);
            proofCollector.add(nextExp);
        } else if (mp.containsKey(currExp)) {
            Expression prevExp = mp.get(currExp).getValue();
            BinOperation exp1 = (BinOperation) ExpressionCreator.getCustomExpression(
                    Statements.axiom02,
                    a, prevExp, currExp
            );
            BinOperation exp2 = (BinOperation) exp1.getRight();
            nextExp = exp2.getRight();

            proofCollector.add(exp1);
            proofCollector.add(exp2);
            proofCollector.add(nextExp);
        } else {
            for (Expression exp: usedUtterances) {
                putMP(exp);
            }
            return processExpression(currExp);
        }

        usedUtterances.add(currExp);
        putMP(currExp);

        return nextExp;
    }
}
