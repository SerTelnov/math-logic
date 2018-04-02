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
public class Solver {
    private Expression a;
    private HashSet<Expression> hypothesis = new HashSet<>();
    private HashSet<Expression> usedUtterances = new HashSet<>();
    private HashMap<Expression, Pair<Expression, Expression>> mp = new HashMap<>();
    private HashSet<Expression> allUtterances;
    private StringJoiner proofTextCreator;

    public String solve(final List<String> input) {
        hypothesis.clear();
        usedUtterances.clear();
        mp.clear();
        proofTextCreator = new StringJoiner("\n");

        ExpressionParser parser = new ExpressionParser();
        List<Expression> list = parser.parseHypothesis(input.get(0));
        this.a = list.get(list.size() - 2);
        BinOperation provable = new BinOperation(a, list.get(list.size() - 1), Default.IMPLICATION);

        StringJoiner hypothesisJoiner = new StringJoiner(",", "", "|-");
        for (int i = 0; i < list.size() - 2; i++) {
            hypothesis.add(list.get(i));
            hypothesisJoiner.add(list.get(i).toString());
        }

        proofTextCreator.add(hypothesisJoiner.toString() + provable.toString());

        List<Expression> statements = new ArrayList<>(input.size() - 1);
        for (int i = 1; i != input.size(); i++) {
            statements.add(parser.parse(input.get(i)));
        }

        allUtterances = new HashSet<>(statements);

        for (Expression exp : statements) {
            if (provable.equals(processExpression(exp))) {
                break;
            }
        }

        return proofTextCreator.toString();
    }

    private void putMP(Expression expression) {
        if (expression instanceof BinOperation) {
            BinOperation binOp = (BinOperation) expression;
            if (binOp.sign.equals(Default.IMPLICATION) && !mp.containsKey(binOp.getRight())) {
                final Expression left = binOp.getLeft();
                if (allUtterances.contains(left)) {
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
            proofTextCreator
                    .add(prevExp.toString())
                    .add(currExp.toString())
                    .add(nextExp.toString());
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

            proofTextCreator
                    .add(exp1.toString())
                    .add(exp2.toString())
                    .add(exp3.toString())
                    .add(exp4.toString())
                    .add(nextExp.toString());
        } else if (mp.containsKey(currExp)) {
            Expression prevExp = mp.get(currExp).getValue();
            BinOperation exp1 = (BinOperation) ExpressionCreator.getCustomExpression(
                    Statements.axiom02,
                    a, prevExp, currExp
            );
            BinOperation exp2 = (BinOperation) exp1.getRight();
            nextExp = exp2.getRight();

            proofTextCreator
                    .add(exp1.toString())
                    .add(exp2.toString())
                    .add(nextExp.toString());
        }

        usedUtterances.add(currExp);
        putMP(currExp);

        return nextExp;
    }
}
