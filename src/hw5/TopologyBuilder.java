package hw5;

import hw5.tree.Tree;
import hw5.tree.Vertex;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Telnov Sergey on 26.06.2018.
 */
public class TopologyBuilder {

    public static String topologyToString(List<List<Vertex>> topology) {
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

    public static List<List<Vertex>> buildTopology(Set<Tree> kripke, Set<String> args) {
        Set<Vertex> vertices = new HashSet<>();
        int currAddNumber = 0;
        int currAddVertexNumber = 0;

        for (Tree tree : kripke) {
            Set<Vertex> currVertices = setVertex(tree, args);
            int maxNumber = 0;
            int maxVertexNumber = 0;

            for (Vertex v : currVertices) {
                v.vertexNumber += currAddNumber;
                if (maxNumber < v.vertexNumber) {
                    maxNumber = v.vertexNumber;
                }

                int currVertexNumber = v.incSetNumber(currAddVertexNumber);
                if (maxVertexNumber < currVertexNumber) {
                    maxVertexNumber = currVertexNumber;
                }
            }

            vertices.addAll(currVertices);
            currAddNumber = maxNumber;
            currAddVertexNumber = maxVertexNumber;
        }
        return linkVertex(vertices);
    }

    public static List<List<Vertex>> buildTopology(Tree kripke, Set<String> args) {
        return linkVertex(setVertex(kripke, args));
    }

    private static Set<Vertex> setVertex(Tree kripke, Set<String> args) {
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
        return vertices;
    }

    private static Set<Integer> setVertex(Tree tree, Map<String, Set<Integer>> args) {
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

    private static void appendVertices(Set<Vertex> vertices, final Set<Integer> fullSet, Tree kripke) {
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

    private static List<List<Vertex>> linkVertex(Set<Vertex> vertices) {
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
}
