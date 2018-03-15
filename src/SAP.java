import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph digraph;

    // software cache of recently computed length() and ancestor() queries
    private final HashMap<HashSet<Integer>, int[]> cache;

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
        int[] value = cache.get(key);
        return value[0];
    }

    // a common ancestor of v and w that participates in an shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        sap(v, w);
        HashSet<Integer> key = new HashSet<>();
        key.add(v);
        key.add(w);
        int[] value = cache.get(key);
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

        HashSet<Integer> key = new HashSet<>();
        key.add(v);
        key.add(w);

        // check cache
        if (cache.containsKey(key))
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

        int[] value = new int[] { distance, ancestor };
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
        int n = digraph.V();
        if (v < 0 || v >= n)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (n - 1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int n = digraph.V();
        for (int v : vertices) {
            if (v < 0 || v >= n) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (n - 1));
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
