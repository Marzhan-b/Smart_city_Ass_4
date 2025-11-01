package graph.dagsp;
import graph.model.Edge;
import graph.model.Graph;
import java.util.*;
/**
 * Computes the longest (critical) path in a DAG.
 *
 * Uses max-DP over topological order.
 * Algorithm:
 *  1. Initialize best[src] = 0 and others = -INF.
 *  2. For each vertex in topological order:
 *       for each edge (u -> v), do:
 *         best[v] = max(best[v], best[u] + w)
 *  3. Track parent[] to reconstruct the longest path.
 *
 * Time complexity: O(V + E)
 */
public class DAGLongestPath {
    /** Holds best distances and reconstructed path info. */
    public static class Result {
        public final long[] best;
        public final int[] parent;
        public final int src;
        public final int argmax; // vertex where path is longest

        public Result(long[] best, int[] parent, int src, int argmax) {
            this.best = best;
            this.parent = parent;
            this.src = src;
            this.argmax = argmax;
        }

        /**
         * Reconstructs the critical (longest) path from src to argmax.
         */
        public List<Integer> criticalPath() {
            List<Integer> path = new ArrayList<>();
            for (int v = argmax; v != -1; v = parent[v]) path.add(v);
            Collections.reverse(path);
            return path;
        }

        public long length() {
            return best[argmax];
        }
    }
    /** Runs longest-path DP on DAG. */
    public static Result run(Graph dag, List<Integer> topo, int src) {
        int n = dag.getN();
        long NEG_INF = Long.MIN_VALUE / 4;
        long[] best = new long[n];
        int[] parent = new int[n];
        Arrays.fill(best, NEG_INF);
        Arrays.fill(parent, -1);
        best[src] = 0;
        // Step 1: DP over topological order
        for (int u : topo) {
            if (best[u] == NEG_INF) continue;
            for (Edge e : dag.getAdj().get(u)) {
                long cand = best[u] + e.getW();
                if (cand > best[e.getV()]) {
                    best[e.getV()] = cand;
                    parent[e.getV()] = u;
                }
            }
        }
        // Step 2: find vertex with maximum path length
        int argmax = src;
        for (int v = 0; v < n; v++) {
            if (best[v] > best[argmax]) argmax = v;
        }
        return new Result(best, parent, src, argmax);
    }
}
