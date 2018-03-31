package parser;

import parser.expressions.BinOperation;
import parser.expressions.Expression;
import parser.expressions.Negate;
import parser.expressions.MateVariable;

import java.util.HashMap;

/**
 * Created by Telnov Sergey on 31.03.2018.
 */
public class Util {
    private static HashMap<String, Expression> metaVariables = new HashMap<>();

    public static int getAxiomNumber(Expression exp) {
        for (int i = 0; i != Statements.axioms.length; i++) {
            metaVariables.clear();
            if (getAxiomNumber(Statements.axioms[i], exp)) {
                return i + 1;
            }
        }
        return -1;
    }

    private static boolean getAxiomNumber(Expression axiom, Expression exp) {
        if (axiom instanceof MateVariable) {
            MateVariable rule = (MateVariable) axiom;
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
                        getAxiomNumber(binAxiom.getLeft(), binExp.getLeft()) &&
                        getAxiomNumber(binAxiom.getRight(), binExp.getRight());
            }
        } else if (axiom instanceof Negate) {
            return exp instanceof Negate &&
                    getAxiomNumber(
                            ((Negate) axiom).getExpression(),
                            ((Negate) exp).getExpression());
        } else {
            return false;
        }
    }
}
