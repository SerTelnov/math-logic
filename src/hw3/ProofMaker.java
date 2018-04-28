package hw3;

import hw2.Deduction;
import javafx.util.Pair;
import parser.Default;
import parser.ExpressionParser;
import parser.expressions.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Telnov Sergey on 24.04.2018.
 */
public class ProofMaker {

    private final ProofGetter getter = new ProofGetter();
    private final Deduction deduction = new Deduction();
    private final ExpressionParser parser = new ExpressionParser();
    private List<String> variables;
    private Expression provable;

    public String getProof(final String input) {
        Pair<List<Expression>, Expression> pair = parser.parseProvable(input, false);
        List<Expression> hypothesis = pair.getKey();
        provable = pair.getValue();

        StringJoiner headerTextJoiner = new StringJoiner(", ", "", "|-");
        for (Expression exp : hypothesis) {
            headerTextJoiner.add(exp.toString());
            provable = new BinOperation(exp, provable, Default.IMPLICATION);
        }

        Set<String> provableVariables = new HashSet<>();
        provable.setVariables(provableVariables);
        variables = new ArrayList<>(provableVariables);

        List<Expression> proof;
        try {
            proof = createProof(new HashMap<>(), 0);
        } catch (UnprovableException e) {
            return e.getMessage();
        }

        for (int i = 0; i != hypothesis.size(); i++) {
            BinOperation binOp = (BinOperation) provable;
            proof.add(binOp.getLeft());
            proof.add(binOp.getRight());

            provable = binOp.getRight();
        }

        String header = headerTextJoiner.toString() + provable.toString();

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(header);
        for (final Expression exp : proof) {
            joiner.add(exp.toString());
        }
        return joiner.toString();
    }

    private Expression getVariable(String name, boolean isPositive) {
        return isPositive ? new Variable(name) : new Negate(new Variable(name));
    }

    private List<Expression> createProof(final Map<String, Boolean> values, int depth) throws UnprovableException {
        if (depth == variables.size()) {
            List<Expression> currProof = new ArrayList<>();
            boolean isProvable = putProofToList(provable, values, currProof);

            if (!isProvable) {
                StringJoiner errorArgsJoiner = new StringJoiner(", ");
                values.forEach((name, value) -> errorArgsJoiner
                        .add(name + '=' + (value ? 'И' : 'Л')));

                throw new UnprovableException(errorArgsJoiner.toString());
            }
            return currProof;
        } else {
            List<Expression> left = getProofList(values, depth, true);
            List<Expression> right = getProofList(values, depth, false);

            Expression currArg = getVariable(variables.get(depth), true);
            Set<Expression> args = values
                    .entrySet()
                    .stream()
                    .map(it -> getVariable(it.getKey(), it.getValue()))
                    .collect(Collectors.toSet());

            List<Expression> proof = new ArrayList<>();
            proof.addAll(deduction.applyDeduction(left, args, currArg, provable));
            proof.addAll(deduction.applyDeduction(right, args, new Negate(currArg), provable));

            proof.addAll(getter.getExclusionAssumption(currArg, provable));
            return proof;
        }
    }

    private List<Expression> getProofList(Map<String, Boolean> values, final int depth, boolean isLeft) throws UnprovableException {
        Map<String, Boolean> newValue = new HashMap<>(values);
        newValue.put(variables.get(depth), isLeft);

        return createProof(newValue, depth + 1);
    }

    private boolean putProofToList(final Expression curr, Map<String, Boolean> values, List<Expression> proof) {
        if (curr instanceof Variable) {
            return values.get(((Variable) curr).name);
        } else {
            if (curr instanceof BinOperation) {
                BinOperation binExp = (BinOperation) curr;
                final boolean isLeft = putProofToList(binExp.getLeft(), values, proof);
                final boolean isRight = putProofToList(binExp.getRight(), values, proof);

                List<Expression> currProof = getBinProof(binExp, isLeft, isRight);
                proof.addAll(currProof);

                return binExp.evaluate(isLeft, isRight);
            } else if (curr instanceof Negate) {
                Negate negExpr = (Negate) curr;
                final boolean isExp = putProofToList(negExpr.expression, values, proof);

                List<Expression> currProof = getVariableProof(negExpr.expression, isExp);
                proof.addAll(currProof);

                return !isExp;
            }
        }
        return false;
    }

    private List<Expression> getBinProof(final BinOperation binExp, boolean isLeft, boolean isRight) {
        List<Expression> proof = getter.getBinProof(binExp.sign, isLeft, isRight);
        return getter.createCustomProof(proof, binExp.getLeft(), binExp.getRight());
    }

    private List<Expression> getVariableProof(final Expression exp, boolean isExp) {
        List<Expression> proof = getter.getVariableProof(isExp);
        return getter.createCustomProof(proof, exp);
    }
}
