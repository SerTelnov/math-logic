package hw5;

import hw5.tree.Tree;
import hw5.tree.TreeFactory;
import hw5.tree.Vertex;
import parser.Default;
import parser.ExpressionParser;
import parser.expressions.*;

import java.util.*;
import java.util.stream.Collectors;

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

    private boolean isIncorrectExpressionInKripke(Expression expression, Tree kripke) {
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

    private boolean checkExpression(Expression exp, Tree tree) {
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

    private boolean checkImpl(BinOperation binExp, Tree tree) {
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

    private boolean checkNegate(Negate negate, Tree tree) {
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

    private List<List<Vertex>> buildTopology(Tree kripke, Set<String> args) {
        Map<String, Set<Integer>> verticesStr = new HashMap<>();
        Map<Set<Integer>, Set<String>> verticesSet = new HashMap<>();
        Set<String> zeroArgs = new HashSet<>();

        args.forEach(it -> verticesStr.put(it, new HashSet<>()));
        Set<Integer> numberVertex = setVertex(kripke, verticesStr);

        verticesStr.forEach((arg, set) -> zeroArgs.remove(arg));

        verticesStr.forEach((str, vertices) -> {
            verticesSet.putIfAbsent(vertices, new HashSet<>());
            verticesSet.get(vertices).add(str);
        });

        Set<Vertex> vertices = verticesSet
                .entrySet()
                .stream()
                .map(it -> new Vertex(it.getKey(), it.getValue()))
                .collect(Collectors.toSet());

        vertices.add(new Vertex(new HashSet<>(), zeroArgs));
        vertices.add(new Vertex(numberVertex));

        appendVertices(vertices, numberVertex, kripke);
        return linkVertex(vertices);
    }

    private Set<Integer> setVertex(Tree tree, Map<String, Set<Integer>> args) {
        Set<String> forcedArgs = tree.getForcedArgs();
        Set<Integer> vertices = new HashSet<>();
        vertices.add(tree.number);
        forcedArgs.forEach(s -> args.get(s).add(tree.number));

        for (Tree child : tree) {
            Set<Integer> childVertices = setVertex(child, args);
            forcedArgs.forEach(s -> args.get(s).addAll(childVertices));
            vertices.addAll(childVertices);
        }

        return vertices;
    }

    private void appendVertices(Set<Vertex> vertices, final Set<Integer> fullSet, Tree kripke) {
        int startSize = vertices.size();
        Set<Vertex> newVertices = new HashSet<>();
        for (int opNumber = 0; opNumber != 3; opNumber++) {
            for (Vertex a : vertices) {
                for (Vertex b : vertices) {
                    Vertex v;
                    if (opNumber == 0) {
                        v = Vertex.cross(a, b);
                    } else if (opNumber == 1) {
                        v = Vertex.combine(a, b);
                    } else {
                        v = Vertex.impl(a, b, fullSet, kripke);
                    }

                    if (!vertices.contains(v)) {
                        newVertices.add(v);
                    }
                }
            }
            vertices.addAll(newVertices);
            newVertices.clear();
        }

        if (startSize < vertices.size()) {
            appendVertices(vertices, fullSet, kripke);
        }
    }

    private List<List<Vertex>> linkVertex(Set<Vertex> vertices) {
        List<Vertex> list = new ArrayList<>(vertices);
        List<List<Vertex>> graph = new ArrayList<>();

        for (int i = 0; i != vertices.size(); i++) {
            list.get(i).vertexNumber = i + 1;
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i != graph.size(); i++) {
            Vertex curr = list.get(i);
            for (int j = 0; j != list.size(); j++) {
                if (curr.containsAll(list.get(j))) {
                    graph.get(j).add(curr);
                }
            }
        }
        return graph;
    }

    private String topologyToString(List<List<Vertex>> topology) {
        Map<String, Integer> keys = new HashMap<>();

        StringBuilder builder = new StringBuilder();
        builder
                .append(topology.size())
                .append('\n');

        for (List<Vertex> list : topology) {
            for (Vertex v : list) {
                builder.append(v.vertexNumber).append(' ');
                if (!v.args.isEmpty()) {
                    for (String arg: v.args) {
                        keys.put(arg, v.vertexNumber);
                    }
                }
            }
            if (!list.isEmpty()) {
                builder.setCharAt(builder.length() - 1, '\n');
            }
        }

        return builder.append(
                keys
                        .entrySet()
                        .stream()
                        .map(it -> String.format("%s=%d", it.getKey(), it.getValue()))
                        .collect(Collectors.joining(","))).toString();
    }
}
