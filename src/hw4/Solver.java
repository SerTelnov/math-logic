package hw4;

import util.Reader;
import util.Writer;

import java.io.IOException;
import java.util.*;

/**
 * Created by Telnov Sergey on 14.05.2018.
 */
public class Solver {

    private Writer writer = new Writer("output.txt");

    private List<Integer>[] graph;
    private List<Integer>[] opGraph;
    private int[][] sum;
    private int[][] mul;
    private int[][] impl;
    private boolean[][] achievable;
    private boolean[][] opAchievable;
    private int n;

    private int zero;
    private int one;

    public Solver() throws IOException {
    }

    private void printMessage(String message) throws IOException {
        if (!message.isEmpty()) {
            writer.println(message);
            writer.close();
            System.exit(0);
        }
    }

    private void setElement(boolean isOpGraph, boolean[][] achievable) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i != n; i++) {
            set.add(i);
        }

        for (int i = 0; i != n; i++) {
            for (int j = 0; j != n; j++) {
                if (!achievable[j][i]) {
                    set.remove(i);
                    break;
                }
            }
        }

        Iterator<Integer> iterator = set.iterator();
        if (isOpGraph) {
            this.opAchievable = achievable;
            if (iterator.hasNext()) {
                zero = iterator.next();
            }
        } else {
            this.achievable = achievable;
            if (iterator.hasNext()) {
                one = iterator.next();
            }
        }
    }

    private boolean[][] getAchievable(boolean isOpGraph) {
        List<Integer>[] graph;
        if (isOpGraph) {
            graph = this.opGraph;
        } else {
            graph = this.graph;
        }

        boolean[][] achievable = new boolean[n][n];
        for (int i = 0; i != n; i++) {
            achievable[i] = new boolean[n];
            Queue<Integer> queue = new ArrayDeque<>();

            achievable[i][i] = true;
            queue.add(i);
            while (!queue.isEmpty()) {
                final int curr = queue.poll();
                for (final int next : graph[curr]) {
                    if (!achievable[i][next]) {
                        achievable[i][next] = true;
                        queue.add(next);
                    }
                }
            }
        }

        setElement(isOpGraph, achievable);
        return achievable;
    }

    private int getElement(Set<Integer> set, boolean[][] achievable) {
        int vertex = -1;
        for (int v : set) {
            boolean isCorrect = true;
            for (int i = 0; i != n; i++) {
                if (!achievable[v][i] && set.contains(i)) {
                    isCorrect = false;
                    break;
                }
            }
            if (isCorrect) {
                if (vertex != -1) {
                    return -1;
                } else {
                    vertex = v;
                }
            }
        }
        return vertex;
    }

    private String checkSumMul(boolean isSum) {
        int[][] values;
        char symbol;
        boolean[][] achievable;
        if (isSum) {
            values = sum;
            symbol = '+';
            achievable = getAchievable(false);
        } else {
            values = mul;
            symbol = '*';
            achievable = getAchievable(true);
        }

        for (int i = 0; i != n; i++) {
            for (int j = 0; j != n; j++) {
                if (i == j) {
                    values[i][i] = i;
                    continue;
                }
                Set<Integer> set = new HashSet<>();
                for (int k = 0; k != n; k++) {
                    if (achievable[i][k] && achievable[j][k]) {
                        set.add(k);
                    }
                }
                final int vertex = getElement(set, achievable);
                if (vertex == -1) {
                    return String.format("Операция '%c' не определена: %d%c%d", symbol, i + 1, symbol, j + 1);
                }
                values[i][j] = vertex;
            }
        }
        return "";
    }

    private String checkDistributivity() {
        for (int i = 0; i != n; i++) {
            for (int j = 0; j != n; j++) {
                for (int k = 0; k != n; k++) {
                    if (mul[i][sum[j][k]] != sum[mul[i][j]][mul[i][k]]) {
                        return String.format("Нарушается дистрибутивность: %d*(%d+%d)", i + 1, j + 1, k + 1);
                    }
                }
            }
        }
        return "";
    }

    private boolean isImpl(int a, int b) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i != n; i++) {
            int v = mul[a][i];
            if (achievable[v][b]) {
                set.add(i);
            }
        }
        int vertex = getElement(set, opAchievable);
        if (vertex == -1) {
            return false;
        } else {
            impl[a][b] = vertex;
            return true;
        }
    }

    private String checkImpl() {
        impl = new int[n][n];
        for (int i = 0; i != n; i++) {
            for (int j = 0; j != n; j++) {
                if (!isImpl(i, j)) {
                    return String.format("Операция '->' не определена: %d->%d", i + 1, j + 1);
                }
            }
        }
        return "";
    }

    private String checkBool() {
        for (int i = 0; i != n; i++) {
            if (sum[i][impl[i][zero]] != one) {
                return String.format("Не булева алгебра: %d+~%d", i + 1, i + 1);
            }
        }
        return "";
    }

    public void run() throws IOException {
        Reader reader = new Reader("input.txt");
        n = reader.nextInt();

        graph = new ArrayList[n];
        opGraph = new ArrayList[n];

        sum = new int[n][n];
        mul = new int[n][n];

        for (int i = 0; i != n; i++) {
            graph[i] = new ArrayList<>();
            opGraph[i] = new ArrayList<>();
        }

        for (int i = 0; i != n; i++) {
            String line = reader.nextLine();
            int index = 0;
            while (index < line.length()) {
                int j = index;
                while (j < line.length() && Character.isDigit(line.charAt(j))) {
                    j++;
                }

                int vertex = Integer.parseInt(line.substring(index, j)) - 1;
                graph[i].add(vertex);
                opGraph[vertex].add(i);
                index = j + 1;
            }
        }

        printMessage(checkSumMul(true));
        printMessage(checkSumMul(false));
        printMessage(checkDistributivity());
        printMessage(checkImpl());
        printMessage(checkBool());
        printMessage("Булева алгебра");
    }
}
