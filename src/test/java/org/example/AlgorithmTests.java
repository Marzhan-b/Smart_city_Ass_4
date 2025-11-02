package org.example;
import graph.model.Graph;
import graph.scc.Kosaraju;
import graph.scc.CondensationGraph;
import graph.topo.TopologicalSort;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Tests for core graph algorithms:
 * - SCC detection (Kosaraju)
 * - Condensation graph
 * - DFS-based Topological Sort
 */
public class AlgorithmTests {
    // --- Kosaraju tests ---
    @Test
    void testStronglyConnectedPair() {
        Graph g = new Graph(2, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);

        var result = Kosaraju.run(g);
        assertEquals(1, result.components.size());
        assertTrue(result.components.get(0).containsAll(List.of(0, 1)));
    }
    @Test
    void testDisconnectedVertices() {
        Graph g = new Graph(3, true);
        var result = Kosaraju.run(g);
        assertEquals(3, result.components.size());
    }
    // --- Condensation Graph test ---
    @Test
    void testCondensationGraph() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 2, 1);
        var scc = Kosaraju.run(g);
        var dag = CondensationGraph.build(g, scc);
        assertEquals(2, dag.getN());
    }
    // --- Topological Sort tests ---
    @Test
    void testLinearTopoOrder() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 1);

        var order = TopologicalSort.sort(g);
        assertEquals(List.of(0, 1, 2, 3), order);
    }
    @Test
    void testBranchingTopoOrder() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);

        var order = TopologicalSort.sort(g);
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(2));
    }
}
