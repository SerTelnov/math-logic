package parser;

import parser.expressions.BinOperation;
import parser.expressions.Expression;
import parser.expressions.Negate;
import parser.expressions.Statement;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class ExpressionParser {
    private int index = 0;
    private String str;

    public Expression parse(String input) {
        index = 0;
        str = input.replaceAll("\\s+","") + "\0";
        return implication();
    }

    private Expression implication() {
        Expression curr = or();
        while (true) {
            if (!test(Default.IMPLICATION)) {
                break;
            } else {
                curr = new BinOperation(curr, implication(), Default.IMPLICATION);
            }
        }
        return curr;
    }

    private Expression or() {
        Expression curr = and();
        while (true) {
            if (!test(Default.OR)) {
                break;
            } else {
                curr = new BinOperation(curr, and(), Default.OR);
            }
        }
        return curr;
    }

    private Expression and() {
        Expression curr = unOperation();
        while (true) {
            if (!test(Default.AND)) {
                break;
            } else {
                curr = new BinOperation(curr, unOperation(), Default.AND);
            }
        }
        return curr;
    }

    private Expression unOperation() {
        if (test(Default.OPEN_BRACKET)) {
            return bracket();
        } else if (test(Default.NEGATE)) {
            return negate();
        } else {
            return statement();
        }
    }


    private Expression bracket() {
        Expression exp = implication();
        index++;
        return exp;
    }

    private Expression negate() {
        return new Negate(unOperation());
    }

    private Expression statement() {
        int i = index;
        while (Character.isLetter(str.charAt(i)) || Character.isDigit(str.charAt(i))) {
            i++;
        }
        Expression exp = new Statement(str.substring(index, i));
        index = i;
        return exp;
    }

    private boolean test(String operation) {
        if (str.startsWith(operation, index)) {
            index += operation.length();
            return true;
        }
        return false;
    }
}
