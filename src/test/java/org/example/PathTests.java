package org.example;
import graph.model.Graph;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;
import graph.dagsp.DAGLongestPath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import graph.dagsp.DAGShortestPath.Result;
/**
 * Tests for DAG shortest and longest path algorithms.
 * (edge-weight model, DP over topological order)
 */
public class PathTests {
    @Test
    void testShortestPathSimple() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(0, 2, 10);
        g.addEdge(2, 3, 1);

        var topo = TopologicalSort.sort(g);
        var result = DAGShortestPath.run(g, topo, 0);

        // Shortest: 0 -> 1 -> 2 -> 3 = 6
        assertEquals(6L, result.dist[3]);
    }
    @Test
    void testLongestPathSimple() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(0, 2, 10);
        g.addEdge(2, 3, 1);

        var topo = TopologicalSort.sort(g);
        var result = DAGLongestPath.run(g, topo, 0);

        // Longest: 0 -> 2 -> 3 = 10 + 1 = 11
        assertEquals(11L, result.dist[3]);
    }

    @Test
    void testDisconnectedVertex() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 5);

        var topo = TopologicalSort.sort(g);
        var shortest = DAGShortestPath.run(g, topo, 0);
        var longest = DAGLongestPath.run(g, topo, 0);

        // vertex 2 is unreachable
        assertEquals(Long.MAX_VALUE, shortest.dist[2]);
        assertEquals(Long.MIN_VALUE, longest.dist[2]);
    }

}
