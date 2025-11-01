package org.example.scc;
import org.example.model.Graph;
import org.example.model.Edge;
import java.util.*;

public class CondensationGraph {
    public static Graph build(Graph g, Kosaraju.Result sccResult) {
        int compCount = sccResult.components.size();
        Graph dag = new Graph(compCount, true);

        Set<String> addEdges = new HashSet<>();

        for (int u = 0; u < g.getN(); u++) {
            int compU = sccResult.component[u];
            for (Edge e : g.getAdj().get(u)) {
                int compV = sccResult.component[e.getV()];
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
