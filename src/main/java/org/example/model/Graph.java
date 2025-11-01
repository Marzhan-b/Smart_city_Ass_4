package org.example.model;
import java.util.*;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(u, v, w));
        if (!directed) {
            adj.get(v).add(new Edge(v, u, w));
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
    public int getEdgesCount() {
        int count = 0;
        for (List<Edge> edges : adj) count += edges.size();
        return count;
    }
    public Graph transpose() {
        Graph t = new Graph(n, directed);
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                t.addEdge(e.getV(), e.getU(), e.getW());
            }
        }
        return t;
    }

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
