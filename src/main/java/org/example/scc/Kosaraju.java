package org.example.scc;
import org.example.model.Edge;
import org.example.model.Graph;
import java.util.*;

public class Kosaraju {
    public static class Result {
        public final int[] component;                 // component index for each vertex
        public final List<List<Integer>> components;  // list of components (each as a list of vertices)

        public Result(int[] comp, List<List<Integer>> comps) {
            this.component = comp;
            this.components = comps;
        }
    }
    // Main method to run this algorithm
    public static Result run(Graph g) {
        int n = g.getN();
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();

        // Step 1: Run first DFS to compute finish order
        // After exploring each vertex completely, we push it into the stack.
        // The vertex that finishes last will be on top.
        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                dfs1(g, v, visited, stack);
            }
        }

        // Step 2: Transpose the graph
        //we reverse all edges to prepare for the second DFS
        Graph gt = g.transpose();

        // Step 3: Second DFS on transposed graph
        Arrays.fill(visited, false);
        int[] component = new int[n];
        Arrays.fill(component, -1);
        List<List<Integer>> components = new ArrayList<>();

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                List<Integer> current = new ArrayList<>();
                dfs2(gt, v, visited, current);
                // assign component index
                int index = components.size();
                for (int u : current) {
                    component[u] = index;
                }
                components.add(current);
            }
        }
        return new Result(component, components);
    }
    private static void dfs1(Graph g, int v, boolean[] visited, Deque<Integer> stack) {
        visited[v] = true;
        for (Edge e : g.getAdj().get(v)) {
            if (!visited[e.getV()]) {
                dfs1(g, e.getV(), visited, stack);
            }
        }
        stack.push(v); // finished exploring v
    }

    /// DFS used in Step 3 (collects vertices of one component)
    private static void dfs2(Graph g, int v, boolean[] visited, List<Integer> current) {
        visited[v] = true;
        current.add(v);
        for (Edge e : g.getAdj().get(v)) {
            if (!visited[e.getV()]) {
                dfs2(g, e.getV(), visited, current);
            }
        }
    }


}
