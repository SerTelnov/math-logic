package hw5;

import hw5.tree.Tree;
import hw5.tree.TreeFactory;
import hw5.tree.Vertex;
import parser.ExpressionParser;
import parser.expressions.*;

import java.util.*;

import static hw5.KripkeChecker.isIncorrectExpressionInKripke;
import static hw5.TopologyBuilder.buildTopology;
import static hw5.TopologyBuilder.topologyToString;

/**
 * Created by Telnov Sergey on 18.06.2018.
 */
public class ExpressionRefutation {

    private TreeFactory treeFactory = new TreeFactory(6);
    private ExpressionParser parser = new ExpressionParser();

    public String check(String input) {
        treeFactory.clean();
        Expression expr = parser.parse(input);

        Set<String> args = new HashSet<>();
        expr.setVariables(args);
        List<String> argsList = Collections.unmodifiableList(new ArrayList<>(args));

        while (treeFactory.hasNextTree()) {
            Tree tree = treeFactory.nextTree();
            final int count = (int) Math.pow(2, args.size() * treeFactory.getBracketSequenceLen());
            for (int mask = 0; mask != count; mask++) {
                TreeFactory.setArgs(tree, mask, argsList);
                if (isIncorrectExpressionInKripke(expr, tree)) {
                    List<List<Vertex>> topology = buildTopology(tree, args);
                    return topologyToString(topology);
                }
            }
        }
        return "Формула общезначима";
    }
}
