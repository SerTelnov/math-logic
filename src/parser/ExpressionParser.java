package parser;

import javafx.util.Pair;
import parser.expressions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Telnov Sergey on 17.03.2018.
 */
public class ExpressionParser {
    private static final String regex = "(,|\\|-|\\|=)";
    private int index = 0;
    private String str;

    public Pair<List<Expression>, Expression> parseProvable(String provable, boolean collectEquals) {
        String[] values = provable.split(regex);
        return new Pair<>(
                getExpressions(values, values.length - 1, collectEquals),
                parse(values[values.length - 1])
        );
    }

    public List<Expression> parseAssumption(String assumption) {
        String[] values = assumption.split(regex);
        return getExpressions(values, values.length - 1, true);
    }

    private List<Expression> getExpressions(final String[] statements, final int n, boolean collectEquals) {
        Stream<String> stream = Arrays.stream(statements)
                .limit(n)
                .filter(it -> !it.isEmpty());
        if (!collectEquals) {
            stream = stream.distinct();
        }
        return stream
                .map(this::parse)
                .collect(Collectors.toList());
    }

    public Expression[] getExpressions(final List<String> statements) {
        Expression[] expressions = new Expression[statements.size()];
        for (int i = 0; i != statements.size(); i++) {
            expressions[i] = parse(statements.get(i));
        }
        return expressions;
    }

    public Expression parse(String input) {
        index = 0;
        str = input.replaceAll("\\s+", "") + "\0";
        return implication();
    }

    private Expression implication() {
        Expression curr = or();
        if (test(Default.IMPLICATION)) {
            curr = new BinOperation(curr, implication(), Default.IMPLICATION);
        }
        return curr;
    }

    private Expression or() {
        Expression curr = and();
        while (test(Default.OR)) {
            curr = new BinOperation(curr, and(), Default.OR);
        }
        return curr;
    }

    private Expression and() {
        Expression curr = unOperation();
        while (test(Default.AND)) {
            curr = new BinOperation(curr, unOperation(), Default.AND);
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
        boolean isVariable = !Character.isLowerCase(str.charAt(index));
        int i = index + 1;
        while (Character.isLetterOrDigit(str.charAt(i))) {
            i++;
        }

        String name = str.substring(index, i);
        Expression exp = isVariable ? new Variable(name) : new MateVariable(name);
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
