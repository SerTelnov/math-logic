package hw5.tree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Telnov Sergey on 18.06.2018.
 */
public class TreeFactory {

    private StringBuilder bracketSequencesBuilder = new StringBuilder();
    private int bracketSequenceLen = 0;
    private final int bracketSequenceLenLimit;

    public TreeFactory(int limit) {
        bracketSequenceLenLimit = limit;
    }

    public void clean() {
        bracketSequencesBuilder.setLength(0);
    }

    public boolean hasNextTree() {
        return bracketSequenceLen <= bracketSequenceLenLimit;
    }

    public int getBracketSequenceLen() {
        return bracketSequenceLen;
    }

    public Tree nextTree() {
        int count = 0;

        String currSequence = nextBracketSequence();
        Tree curr = new Tree(count++);

        for (int i = 1; i < currSequence.length() - 1; i++) {
            if (currSequence.charAt(i) == '(') {
                Tree child = new Tree(curr, count++);
                curr.addChild(child);
                curr = child;
            } else if (currSequence.charAt(i) == ')') {
                if (curr.parent == null) {
                    return nextTree();
                } else {
                    curr = curr.parent;
                }
            }
        }
        return curr;
    }

    public Set<Tree> nextForest() {
        Set<Tree> forest = new HashSet<>();
        int count = 0;

        String currSequence = nextBracketSequence();
        Tree curr = new Tree(count++);

        for (int i = 1; i < currSequence.length() - 1; i++) {
            if (currSequence.charAt(i) == '(') {
                if (curr != null) {
                    Tree child = new Tree(curr, count++);
                    curr.addChild(child);
                    curr = child;
                } else {
                    curr = new Tree(count++);
                }
            } else if (currSequence.charAt(i) == ')' && curr != null) {
                if (curr.parent == null) {
                    forest.add(curr);
                    curr = null;
                } else {
                    curr = curr.parent;
                }
            }
        }
        return forest;
    }

    public static void setArgs(Tree curr, final int mask, List<String> args) {
        curr.updateArgs();

        for (int j = 0; j != args.size(); j++) {
            if ((mask & (1 << (args.size() * curr.number + j))) != 0) {
                curr.addArgs(args.get(j));
            }
        }

        for (Tree child : curr) {
            setArgs(child, mask, args);
        }
    }

    private void setBracketSequence() {
        bracketSequenceLen++;

        for (int i = 0; i != bracketSequenceLen; i++) {
            bracketSequencesBuilder.append('(');
        }

        for (int i = 0; i != bracketSequenceLen; i++) {
            bracketSequencesBuilder.append(')');
        }
    }

    private String nextBracketSequence() {
        int closeCounter = 0;
        int openCounter = 0;
        final int currLen = bracketSequencesBuilder.length();
        for (int i = currLen - 1; i >= 0; i--) {
            if (bracketSequencesBuilder.charAt(i) == '(') {
                openCounter++;
                if (closeCounter > openCounter)
                    break;
            } else {
                closeCounter++;
            }
        }

        bracketSequencesBuilder.delete(currLen - openCounter - closeCounter, currLen);
        if (bracketSequencesBuilder.length() == 0) {
            setBracketSequence();
        } else {
            bracketSequencesBuilder.append(')');

            for (int i = 0; i != openCounter; i++) {
                bracketSequencesBuilder.append('(');
            }

            for (int i = 0; i != closeCounter - 1; i++) {
                bracketSequencesBuilder.append(')');
            }
        }

        return bracketSequencesBuilder.toString();
    }
}
