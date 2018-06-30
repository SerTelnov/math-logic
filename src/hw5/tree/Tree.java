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
    }

    public Tree(final int number, Set<String> forcedArgs) {
        this(null, number, forcedArgs);
    }

    public Tree(Tree parent, final int number, Set<String> forcedArgs) {
        this.parent = parent;
        this.number = number;
        this.forcedArgs = forcedArgs;
        this.children = new ArrayList<>();
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

    public List<String> asList() {
        List<String> list = new ArrayList<>();
        setList(list, 0);
        return list;
    }

    private void setList(List<String> list, final int depth) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i != depth; i++) {
            builder.append(' ');
        }
        builder.append('*');
        forcedArgs.forEach(it -> builder.append(' ').append(it));

        list.add(builder.toString());

        for (Tree child : this) {
            child.setList(list, depth + 1);
        }
    }

    public Set<String> getAllForcedArgs() {
        Set<String> set = new HashSet<>();
        setArgs(set);
        return set;
    }

    private void setArgs(Set<String> set) {
        set.addAll(this.forcedArgs);
        for (Tree child : this) {
            child.setArgs(set);
        }
    }

}
