import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAP {

    private final Digraph digraph;

    // software cache of recently computed length() and ancestor() queries
    private final HashMap<HashSet<Integer>, Integer[]> cache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        // corner case
        if (G == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        // make a defensive copy of G by calling the copy constructor
        // to make the data type SAP immutable
        digraph = new Digraph(G);

        cache = new HashMap<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        sap(v, w);
        HashSet<Integer> key = new HashSet<>();
        key.add(v);
        key.add(w);
        Integer[] value = cache.get(key);
        return value[0];
    }

    // a common ancestor of v and w that participates in an shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        sap(v, w);
        HashSet<Integer> key = new HashSet<>();
        key.add(v);
        key.add(w);
        Integer[] value = cache.get(key);
        return value[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return sap(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return sap(v, w)[1];
    }

    // helper methods
    private void sap(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        // check cache
        if (cache.containsKey(new Integer[] { v, w }))
            return;

        // bfs from v
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);

        // bfs from w
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);

        int distance = Integer.MAX_VALUE;
        int ancestor = -2;
        // loop through each vertex and find the minimal ancestor length one
        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (vPath.hasPathTo(vertex) && vPath.distTo(vertex) < distance && wPath.hasPathTo(vertex)
                    && wPath.distTo(vertex) < distance) {
                int sum = vPath.distTo(vertex) + wPath.distTo(vertex);
                if (distance > sum) {
                    distance = sum;
                    ancestor = vertex;
                }
            }
        }

        if (distance == Integer.MAX_VALUE) {
            // which means no such path
            distance = -1;
            ancestor = -1;
        }
        HashSet<Integer> key = new HashSet<>();
        key.add(v);
        key.add(w);
        Integer[] value = new Integer[] { distance, ancestor };
        cache.put(key, value);
    }

    private int[] sap(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);

        // bfs from v
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);

        // bfs from w
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);

        int distance = Integer.MAX_VALUE;
        int ancestor = -2;
        // loop through each vertex and find the minimal ancestor length one
        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (vPath.hasPathTo(vertex) && vPath.distTo(vertex) < distance && wPath.hasPathTo(vertex)
                    && wPath.distTo(vertex) < distance) {
                int sum = vPath.distTo(vertex) + wPath.distTo(vertex);
                if (distance > sum) {
                    distance = sum;
                    ancestor = vertex;
                }
            }
        }

        if (distance != Integer.MAX_VALUE) {
            return new int[] { distance, ancestor };
        } else {
            return new int[] { -1, -1 };
        }
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = digraph.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = digraph.V();
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("C:\\Users\\rchen\\eclipse-workspace\\WordNet\\test\\digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        Bag<Integer> v = new Bag<>();
        v.add(3);
        v.add(9);
        v.add(7);

        Bag<Integer> w = new Bag<>();
        w.add(11);
        w.add(12);
        w.add(2);

        System.out.println("length: 3 -- 11: " + sap.length(3, 11));
        System.out.println("ancestor: 3 -- 11: " + sap.ancestor(3, 11));
        System.out.println("length: 3 -- 12: " + sap.length(3, 12));
        System.out.println("ancestor: 3 -- 12: " + sap.ancestor(3, 12));
        System.out.println("length: 9 -- 11: " + sap.length(9, 11));
        System.out.println("ancestor: 9 -- 11: " + sap.ancestor(9, 11));
        System.out.println("length: 9 -- 12: " + sap.length(9, 12));
        System.out.println("ancestor: 9 -- 12: " + sap.ancestor(9, 12));

        System.out.println("Length: " + sap.length(v, w));
        System.out.println("Ancestor: " + sap.ancestor(v, w));
    }
}
