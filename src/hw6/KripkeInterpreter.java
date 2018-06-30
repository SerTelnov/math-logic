package hw6;

import hw5.KripkeChecker;
import hw5.tree.Tree;
import parser.ExpressionParser;
import parser.expressions.Expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static hw5.TopologyBuilder.buildTopology;
import static hw5.TopologyBuilder.topologyToString;

/**
 * Created by Telnov Sergey on 26.06.2018.
 */
public class KripkeInterpreter {

    private KripkeParser kripkeParser = new KripkeParser();
    private ExpressionParser expressionParser = new ExpressionParser();

    public String check(String exprStr, List<String> kripkeStr) {
        Set<Tree> forest = kripkeParser.parseKripke(kripkeStr);
        Expression expression = expressionParser.parse(exprStr);

        if (!kripkeParser.isCorrectKripke(forest)) {
            return "Не модель Крипке";
        }

        if (KripkeChecker.isIncorrectExpressionInKripke(expression, forest)) {
            Set<String> args = new HashSet<>();
            expression.setVariables(args);
            forest.forEach(it -> args.addAll(it.getAllForcedArgs()));

            return topologyToString(buildTopology(forest, args));
        }
        return "Не опровергает формулу";
    }
}
