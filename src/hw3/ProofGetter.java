package hw3;

import com.sun.javafx.UnmodifiableArrayList;
import parser.Default;
import parser.ExpressionParser;
import parser.expressions.BinOperation;
import parser.expressions.Expression;
import parser.expressions.ExpressionCreator;
import parser.expressions.Negate;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Telnov Sergey on 25.04.2018.
 */
public class ProofGetter {
    private final HashMap<String, List<Expression>> proofs = new HashMap<>();
    private final ExpressionParser parser = new ExpressionParser();

    public final List<Expression> CONTRAPOSITION_RULE;
    public final List<Expression> EXCLUTION_ASSUMPTION_RULE;
    public final List<Expression> EXCLUTION_RULE_OF_THIRD;

    public ProofGetter() {
        Path path = Paths.get("src/hw3/proofs");
        try {
            Files
                    .walk(path)
                    .filter(Files::isRegularFile)
                    .forEach(this::putProof);
        } catch (IOException e) {
            System.err.println("Can't walk in path: '" + path.getFileName() + "'");
        }

        CONTRAPOSITION_RULE = proofs.get("contraposition-ruleproofs");
        EXCLUTION_ASSUMPTION_RULE = proofs.get("exclution-assumption-ruleproofs");
        EXCLUTION_RULE_OF_THIRD = proofs.get("exclusion-rule-of-the-thirdproofs");
    }

    public List<Expression> createCustomProof(List<Expression> expressions, Expression... args) {
        List<Expression> list = new ArrayList<>(expressions.size());
        for (Expression exp: expressions) {
            list.add(ExpressionCreator.getCustomExpression(exp, args));
        }
        return list;
    }

    public List<Expression> getExclusionAssumption(final Expression p, final Expression a) {
        final Expression aOrNota = new BinOperation(
                p,
                new Negate(p),
                Default.OR
        );

        List<Expression> result = new ArrayList<>(createCustomProof(CONTRAPOSITION_RULE, p, aOrNota));
        result.addAll(createCustomProof(CONTRAPOSITION_RULE, new Negate(p), aOrNota));

        result.addAll(createCustomProof(EXCLUTION_RULE_OF_THIRD, p));
        result.addAll(createCustomProof(EXCLUTION_ASSUMPTION_RULE, a, p));
        return result;
    }

    private String getValueSymbol(final boolean value) {
        return value ? "Yes" : "No";
    }

    private String getOpSymbol(final String op) {
        switch (op) {
            case "|":
                return "or";
            case "&":
                return "and";
            case "->":
                return "imp";
            default:
                return "variable";
        }
    }

    public List<Expression> getBinProof(final String binOp, final boolean left, final boolean right) {
        String name = getValueSymbol(left) + getValueSymbol(right) + getOpSymbol(binOp);
        return proofs.get(name);
    }

    public List<Expression> getVariableProof(final boolean isExp) {
        return proofs.get(getValueSymbol(isExp) + "variable");
    }

    private void putProof(Path path) {
        String name = path
                .getFileName()
                .toString();

        String[] parents = path
                .getParent()
                .toString()
                .split(String.format("\\%c", File.separatorChar));

        String proofName = name + parents[parents.length - 1];

        try {
            Expression[] proof = parser.getExpressions(Files.readAllLines(path));
            proofs.put(proofName, new UnmodifiableArrayList<>(proof, proof.length));
        } catch (IOException e) {
            System.err.println("Can't read proof from file: '" + proofName + "'");
        }
    }
}
