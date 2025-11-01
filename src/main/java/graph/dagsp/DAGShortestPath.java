package graph.dagsp;
import graph.model.Edge;
import graph.model.Graph;
import java.util.*;

/**
 * Computes single-source shortest paths in a Directed Acyclic Graph (DAG)
 * using dynamic programming along topological order.
 * Algorithm:
 *  1. Initialize all distances as +INF except the source.
 *  2. Process vertices in topological order.
 *  3. For each edge (u -> v), relax if dist[u] + w < dist[v].
 * Time complexity: O(V + E)
 */
public class DAGShortestPath {
    /** Holds distances, parents, and source information. */
    public static class Result {
        public final long[] dist;
        public final int[] parent;
        public final int src;

        public Result(long[] dist, int[] parent, int src) {
            this.dist = dist;
            this.parent = parent;
            this.src = src;
        }
        /** Reconstructs one shortest path from src to given target. */
        public List<Integer> reconstruct(int target) {
            List<Integer> path = new ArrayList<>();
            if (dist[target] == Long.MAX_VALUE) return path; // unreachable
            for (int v = target; v != -1; v = parent[v]) path.add(v);
            Collections.reverse(path);
            return path;
        }
    }
    /**
     * Runs shortest path DP on DAG.
     *
     * @param dag DAG graph (condensation)
     * @param topo topological order
     * @param src source vertex (component index)
     * @return shortest path result
     */
    public static Result run(Graph dag, List<Integer> topo, int src) {
        int n = dag.getN();
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[src] = 0;
        // Step 1: Relax edges in topological order
        for (int u : topo) {
            if (dist[u] == Long.MAX_VALUE) continue;
            for (Edge e : dag.getAdj().get(u)) {
                long cand = dist[u] + e.getW();
                if (cand < dist[e.getV()]) {
                    dist[e.getV()] = cand;
                    parent[e.getV()] = u;
                }
            }
        }
        return new Result(dist, parent, src);
    }
}
