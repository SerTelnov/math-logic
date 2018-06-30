package hw6;

import hw5.tree.Tree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Telnov Sergey on 26.06.2018.
 */
public class KripkeParser {

    public Set<Tree> parseKripke(List<String> input) {
        Set<Tree> forest = new HashSet<>();
        int countDepth = 0;
        Tree curr = null;
        int counter = 0;

        for (String str : input) {
            int currDepth = 0;
            while (currDepth < str.length() && str.charAt(currDepth) != '*') {
                currDepth++;
            }

            while (countDepth >= currDepth) {
                if (curr == null)
                    break;

                curr = curr.parent;
                countDepth--;
            }
            countDepth = currDepth;

            if (curr == null) {
                curr = new Tree(counter++);
                forest.add(curr);
            } else {
                Tree child = new Tree(curr, counter++);
                curr.addChild(child);
                curr = child;
            }

            int index = currDepth;
            while (index < str.length()) {
                while (index < str.length() &&
                        !Character.isLetterOrDigit(str.charAt(index))) {
                    index++;
                }

                int i = 0;
                while (index + i < str.length() &&
                        Character.isLetterOrDigit(str.charAt(index + i))) {
                    i++;
                }
                if (i > 0) {
                    String arg = str.substring(index, index + i);
                    curr.addArgs(arg);
                }
                index += i;
            }
        }

        return forest;
    }

    public boolean isCorrectKripke(Set<Tree> kripke) {
        for (Tree tree : kripke) {
            if (isInvalidTree(tree)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInvalidTree(Tree tree) {
        if (tree.parent != null) {
            Set<String> parentArgs = tree.parent.getForcedArgs();
            Set<String> args = tree.getForcedArgs();

            if (!args.containsAll(parentArgs) && !parentArgs.isEmpty()) {
                return true;
            }
        }

        for (Tree child : tree) {
            if (isInvalidTree(child)) {
                return true;
            }
        }
        return false;
    }
}
