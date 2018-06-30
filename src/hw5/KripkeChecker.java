package hw5;

import hw5.tree.Tree;
import parser.Default;
import parser.expressions.BinOperation;
import parser.expressions.Expression;
import parser.expressions.Negate;
import parser.expressions.Variable;

import java.util.Set;

/**
 * Created by Telnov Sergey on 26.06.2018.
 */
public class KripkeChecker {

    public static boolean isIncorrectExpressionInKripke(Expression expression, Set<Tree> kripke) {
        for (Tree tree : kripke) {
            if (isIncorrectExpressionInKripke(expression, tree)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIncorrectExpressionInKripke(Expression expression, Tree kripke) {
        if (!checkExpression(expression, kripke)) {
            return true;
        } else {
            for (Tree child : kripke) {
                if (isIncorrectExpressionInKripke(expression, child)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkExpression(Expression exp, Tree tree) {
        if (exp instanceof BinOperation) {
            BinOperation binExp = (BinOperation) exp;
            if (binExp.sign.equals(Default.AND) || binExp.sign.equals(Default.OR)) {
                return binExp.evaluate(
                        checkExpression(binExp.getLeft(), tree),
                        checkExpression(binExp.getRight(), tree)
                );
            } else if (binExp.sign.equals(Default.IMPLICATION)) {
                return checkImpl(binExp, tree);
            }
        } else if (exp instanceof Negate) {
            return checkNegate((Negate) exp, tree);
        } else if (exp instanceof Variable) {
            return tree.isForced(((Variable) exp).name);
        }
        throw new RuntimeException("Invalid expression: " + exp.getClass().toString() + "\n");
    }

    private static boolean checkImpl(BinOperation binExp, Tree tree) {
        if (!binExp.evaluate(
                checkExpression(binExp.getLeft(), tree),
                checkExpression(binExp.getRight(), tree))) {
            return false;
        } else {
            for (Tree child : tree) {
                if (!checkImpl(binExp, child)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean checkNegate(Negate negate, Tree tree) {
        if (checkExpression(negate.expression, tree)) {
            return false;
        } else {
            for (Tree child : tree) {
                if (!checkNegate(negate, child)) {
                    return false;
                }
            }
            return true;
        }
    }
}
