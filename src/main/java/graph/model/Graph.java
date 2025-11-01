package graph.model;
import java.util.*;
/**
 * Represents a graph data structure (directed or undirected).
 *
 * - Stores adjacency lists for each vertex.
 * - Supports adding edges, counting edges, and transposing the graph.
 * - Used for SCC, Topological Sort, and DAG Path algorithms.
 */

public class Graph {
    private final int n; //number of vertices
    private final boolean directed; // true if graph is directed
    private final List<List<Edge>> adj; // adjacency list
    /** Constructs an empty graph with n vertices. */
    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }
    /** Adds a directed edge (u -> v) with given weight. */
    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(u, v, w));
        if (!directed) {
            adj.get(v).add(new Edge(v, u, w)); // add reverse for undirected graphs
        }
    }

    public int getN() {
        return n;
    }
    public boolean isDirected() {
        return directed;
    }
    public List<List<Edge>> getAdj() {
        return adj;
    }
    /** Returns total number of edges. */
    public int getEdgesCount() {
        int count = 0;
        for (List<Edge> edges : adj) count += edges.size();
        return count;
    }
    /**
     * Creates a transposed version of the graph (reverse all edges).
     * Used in Kosaraju's algorithm for SCC.
     */
    public Graph transpose() {
        Graph t = new Graph(n, directed);
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                t.addEdge(e.getV(), e.getU(), e.getW());
            }
        }
        return t;
    }
    /** Returns a string representation of adjacency list. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int u = 0; u < n; u++) {
            sb.append(u).append(": ");
            for (Edge e : adj.get(u)) {
                sb.append(e.getV()).append("(").append(e.getW()).append(") ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
