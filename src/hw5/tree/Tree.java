package hw5.tree;

import java.util.*;

/**
 * Created by Telnov Sergey on 18.06.2018.
 */
public class Tree implements Iterable<Tree> {

    public final Tree parent;
    private final List<Tree> children;
    private Set<String> forcedArgs;
    public final int number;

    public Tree(final int number) {
        this(null, number);
    }

    public Tree(Tree parent, final int number) {
        this.parent = parent;
        this.children = new ArrayList<>();
        this.forcedArgs = new HashSet<>();
        this.number = number;

        if (parent != null) {
            forcedArgs.addAll(parent.forcedArgs);
        }
    }

    public void addChild(Tree child) {
        children.add(child);
    }

    public void addArgs(final String arg) {
        forcedArgs.add(arg);
    }

    public boolean isForced(String name) {
        return forcedArgs.contains(name);
    }

    public Set<String> getForcedArgs() {
        return new HashSet<>(forcedArgs);
    }

    public void updateArgs() {
        forcedArgs.clear();
        if (parent != null) {
            forcedArgs.addAll(parent.forcedArgs);
        }
    }

    @Override
    public Iterator<Tree> iterator() {
        return children.iterator();
    }

    private boolean setInt(Set<Integer> set, final Set<Integer> args) {
        boolean isContain = args.contains(this.number);
        for (Tree child : this.children) {
            if (!child.setInt(set, args)) {
                isContain = false;
            }
        }
        if (isContain) {
            set.add(this.number);
        }
        return isContain;
    }

    public Set<Integer> getInt(Set<Integer> args) {
        Set<Integer> intSet = new HashSet<>();
        setInt(intSet, args);
        return intSet;
    }
}
