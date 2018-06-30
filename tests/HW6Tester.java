import hw5.tree.Tree;
import hw5.tree.TreeFactory;
import hw5.tree.Vertex;
import hw6.KripkeInterpreter;
import org.junit.jupiter.api.Test;
import parser.expressions.Expression;

import java.util.*;
import java.util.stream.Collectors;

import static hw5.KripkeChecker.isIncorrectExpressionInKripke;
import static hw5.TopologyBuilder.buildTopology;
import static hw5.TopologyBuilder.topologyToString;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Telnov Sergey on 26.06.2018.
 */
public class HW6Tester {

    private final KripkeInterpreter checker = new KripkeInterpreter();

    private void customTest(String input, List<String> kripke, String answer) {
        assertEquals(answer, checker.check(input, kripke));
    }

    @Test
    public void test01() {
        List<String> list = Arrays.asList(
                "*\n",
                "\t* A");
        String input = "A|!A";
        customTest(input, list,
                "3\n" +
                        "1 2 3\n" +
                        "2 3\n" +
                        "3\n" +
                        "A=2");
    }

    @Test
    public void test02() {
        List<String> list = Arrays.asList(
                "* A,B",
                "\t* A",
                "* B"
        );
        String input = "A|B";
        customTest(input, list, "Не модель Крипке");
    }

    @Test
    public void test03() {
        List<String> list = Arrays.asList(
                "* A",
                " *",
                "  * A",
                " * A"
        );
        String input = "A";
        customTest(input, list, "Не модель Крипке");
    }

    @Test
    public void test04() {
        List<String> list = Arrays.asList(
                "    *",
                "     *",
                "      * Q, P",
                "      * Q",
                "     * A"
        );
        String input = "((P->(Q->R))|((!P)->(Q->R)))|(!(Q->R)))";
        customTest(input, list,
                "11\n" +
                        "1 2 3 4 5 6 7 8 9 10 11\n" +
                        "2 5 6 7 9 10 11\n" +
                        "3 5 7 8 9 10 11\n" +
                        "4 6 8 9 10 11\n" +
                        "5 7 9 10 11\n" +
                        "6 9 10 11\n" +
                        "7 10 11\n" +
                        "8 9 10 11\n" +
                        "9 10 11\n" +
                        "10\n" +
                        "10 11\n" +
                        "P=2,A=4,Q=5,R=1");
    }

    @Test
    public void assertFailedTest() {
        final String input = "A->B->A";
        TreeFactory factory = new TreeFactory(4);
        List<String> argsList = Arrays.asList("A", "B", "C", "D");
        while (factory.hasNextTree()) {
            Set<Tree> currForest = factory.nextForest();
            for (int i = 0; i != 4; i++) {
                final int count = (int) Math.pow(2, i * factory.getBracketSequenceLen());
                for (int mask = 0; mask != count; mask++) {
                    for (Tree tree : currForest) {
                        TreeFactory.setArgs(tree, mask, argsList.subList(0, i));
                    }

                    List<String> list = new ArrayList<>();
                    currForest.forEach(it -> list.addAll(it.asList()));

                    try {
                        checker.check(input, list);
                    } catch (Exception e) {
                        System.err.println(String.format("test: %s\n, args: %s",
                                list
                                        .stream()
                                        .collect(Collectors.joining("\n")),
                                argsList
                                        .subList(0, i)
                                        .stream()
                                        .collect(Collectors.joining(" "))));
                        throw e;
                    }
                }
            }
        }
    }
}
