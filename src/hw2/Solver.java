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
    private Expression a, b;
    private BinOperation provable;
    private HashSet<Expression> hypothesis = new HashSet<>();
    private HashSet<Expression> utterances = new HashSet<>();
    private HashMap<Expression, Pair<Expression, Expression>> mp = new HashMap<>();
    private StringJoiner proofTextCreator;

    /**
     *
     * @param input
     * @return <tt>List</tt> with two values:
     *              First is Grammar|-a, b
     *              Second is proof's text
     */
    public List<String> solve(List<String> input) {
        hypothesis.clear();
        utterances.clear();
        mp.clear();
        proofTextCreator = new StringJoiner("\n");

        ExpressionParser parser = new ExpressionParser();
        List<Expression> list = parser.parseHypothesis(input.get(0));
        this.a = list.get(list.size() - 2);
        this.b = list.get(list.size() - 1);
        provable = new BinOperation(a, b, Default.IMPLICATION);

        for (int i = 0; i < list.size() - 2; i++) {
            hypothesis.add(list.get(i));
        }

        for (int i = 1; i != input.size(); i++) {
            Expression exp = parser.parse(input.get(i));
            if (provable.equals(processExpression(exp))) {
                break;
            }
        }

        StringJoiner joiner = new StringJoiner(",", "", "|-");
        for (int i = 0; i != list.size() - 2; i++) {
            joiner.add(list.get(i).toString());
        }

        return Arrays.asList(
            joiner.toString() + provable.toString() + "\n",
                proofTextCreator.toString()
        );
    }

    private void putMP(Expression expression) {
        if (expression instanceof BinOperation) {
            BinOperation binOp = (BinOperation) expression;
            if (binOp.sign.equals(Default.IMPLICATION) && !mp.containsKey(binOp.getRight())) {
                final Expression left = binOp.getLeft();
                if (utterances.contains(left)) {
                    mp.put(binOp.getRight(), new Pair<>(expression, left));
                }
            }
        }
    }

    private Expression processExpression(Expression currExp) {
        if (utterances.contains(currExp))
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
        } else {
            for (Expression exp : utterances) {
                putMP(exp);
            }
            processExpression(currExp);
        }

        utterances.add(currExp);
        putMP(currExp);

        return nextExp;
    }
}
