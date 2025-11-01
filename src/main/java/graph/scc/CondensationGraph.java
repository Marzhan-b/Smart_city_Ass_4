package graph.scc;
import graph.model.Graph;
import graph.model.Edge;
import java.util.*;
/**
 * Builds a condensation graph (DAG) from SCC results.
 * Each strongly connected component becomes a single node.
 */
public class CondensationGraph {
    /**
     * Creates a DAG where each node represents one SCC.
     * Adds edges between SCCs if there was any connection in the original graph.
     *
     * @param g original directed graph
     * @param sccResult result from Kosaraju algorithm
     * @return condensation DAG
     */
    public static Graph build(Graph g, Kosaraju.Result sccResult) {
        int compCount = sccResult.components.size();
        Graph dag = new Graph(compCount, true);

        Set<String> addEdges = new HashSet<>();
        // Step 1: Check each edge in the original graph
        for (int u = 0; u < g.getN(); u++) {
            int compU = sccResult.component[u];
            for (Edge e : g.getAdj().get(u)) {
                int compV = sccResult.component[e.getV()];
                // Step 2: Add edges only between different SCCs
                if (compU != compV) {
                    String key = compU + "->" + compV;
                    if (!addEdges.contains(key)) {
                        dag.addEdge(compU, compV, e.getW());
                        addEdges.add(key);
                    }
                }
            }
        }
        return dag;
    }
}
