package graph.topo;
import graph.model.Edge;
import graph.model.Graph;
import java.util.*;
/**
 * Performs Topological Sort on a Directed Acyclic Graph (DAG)
 * using DFS-based approach.
 *
 * Steps:
 *  1. Visit each unvisited vertex.
 *  2. Explore all outgoing edges recursively.
 *  3. Push the vertex into stack after exploration.
 *  4. Reverse stack to obtain valid topological order.
 */
public class TopologicalSort {

    //  Runs topological sort on a DAG and returns the order of vertices.
    public static List<Integer> sort (Graph g) {
        int n= g.getN();
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        // Step 1: Perform DFS on all vertices
        for (int v=0;v<n;v++) {
            if (!visited[v]) {
                dfs(g,v,visited,stack);
            }
        }
        // Step 2: Reverse stack to get final order
        List<Integer> order= new ArrayList<>(stack);
        Collections.reverse(order);
        return order;
    }
    //Helper DFS that fills stack based on finish time.
    private static void dfs(Graph graph, int v, boolean[] visited, Deque<Integer> stack) {
        visited[v] = true;

        for (Edge e: graph.getAdj().get(v)) {
            if (!visited[e.getV()]) {
                dfs(graph,e.getV(),visited,stack);
            }
        }
        // when vertex fully explored, push it into stack
        stack.push(v);
    }
}
