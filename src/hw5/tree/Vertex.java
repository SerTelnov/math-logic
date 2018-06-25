package hw5.tree;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Telnov Sergey on 22.06.2018.
 */
public class Vertex {

    public final Set<String> args;
    public int vertexNumber = 0;
    private Set<Integer> set = new HashSet<>();

    public Vertex() {
        this(new HashSet<>(), new HashSet<>());
    }

    public Vertex(Set<Integer> set) {
        this(set, new HashSet<>());
    }

    public Vertex(Set<Integer> set, Set<String> args) {
        this.args = args;
        this.set = set;
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }


    public static Vertex combine(Vertex a, Vertex b) {
        Set<Integer> set = new HashSet<>(a.set);
        set.addAll(b.set);
        return new Vertex(set);
    }

    public static Vertex cross(Vertex a, Vertex b) {
        Set<Integer> set = new HashSet<>(a.set);
        for (Integer i: a.set) {
            if (!b.set.contains(i)) {
                set.remove(i);
            }
        }
        return new Vertex(set);
    }

    public static Vertex impl(Vertex a, Vertex b, Set<Integer> fullSet, Tree kripke) {
        Set<Integer> set = new HashSet<>(fullSet);
        for (Integer i : a.set) {
            if (set.contains(i)) {
                set.remove(i);
            }
        }
        set.addAll(b.set);

        return new Vertex(kripke.getInt(set));
    }

    public boolean containsAll(Vertex that) {
        return this.set.containsAll(that.set);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertex) {
            Vertex thatVertex = (Vertex) obj;
            return thatVertex.set.equals(set);
        }
        return super.equals(obj);
    }
}
