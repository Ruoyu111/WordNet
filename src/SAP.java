import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    
    private final Digraph digraph;
    // record length of ancestral path for each pair of vertices
    private final int[][] lens;
    // record ancestor for each pair of vertices
    private final int[][] ancs;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        // corner case
        if (G == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        
        // make a defensive copy of G by calling the copy constructor
        // to make the data type SAP immutable
        digraph = new Digraph(G);
        
        lens = new int[digraph.V()][digraph.V()];
        ancs = new int[digraph.V()][digraph.V()];
        
        for (int i = 0; i < digraph.V(); i++) {
            for (int j = 0; j < digraph.V(); j++) {
                if (i == j) {
                    lens[i][j] = 0;
                    ancs[i][j] = i;
                } else {
                    // let -2 means initial value, not compute yet
                    lens[i][j] = -2;
                    ancs[i][j] = -2;
                }
            }
        }
    }
    
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        sap(v, w);
        return lens[v][w];
    }
    
    // a common ancestor of v and w that participates in an shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        sap(v, w);
        return ancs[v][w];
    }
    
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return sap(v, w)[0];
    }
    
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return sap(v, w)[1];
    }
    
    // helper methods
    private void sap(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        if (lens[v][w] != -2) return;
        
        // bfs from v
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        
        // bfs from w
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);
        
        int distance = Integer.MAX_VALUE;
        int ancestor = -2;
        // loop through each vertex and find the minimal ancestor length one
        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (vPath.hasPathTo(vertex) && vPath.distTo(vertex) < distance
                    && wPath.hasPathTo(vertex) && wPath.distTo(vertex) < distance) {
                int sum = vPath.distTo(vertex) + wPath.distTo(vertex);
                if (distance > sum) {
                    distance = sum;
                    ancestor = vertex;
                }
            }
        }
        
        if (distance != Integer.MAX_VALUE) {
            // update
            lens[v][w] = distance;
            lens[w][v] = distance;
            ancs[v][w] = ancestor;
            ancs[w][v] = ancestor;
        } else {
            // which means no such path
            lens[v][w] = -1;
            lens[w][v] = -1;
            ancs[v][w] = -1;
            ancs[w][v] = -1;
        }
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
            if (vPath.hasPathTo(vertex) && vPath.distTo(vertex) < distance
                    && wPath.hasPathTo(vertex) && wPath.distTo(vertex) < distance) {
                int sum = vPath.distTo(vertex) + wPath.distTo(vertex);
                if (distance > sum) {
                    distance = sum;
                    ancestor = vertex;
                }
            }
        }
        
        if (distance != Integer.MAX_VALUE) {
            return new int[] {distance, ancestor};
        } else {
            return new int[] {-1, -1};
        }
    }
    
    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = digraph.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = digraph.V();
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
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
