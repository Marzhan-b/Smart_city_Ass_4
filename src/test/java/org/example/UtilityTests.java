package org.example;
import graph.util.Metrics;
import graph.util.GraphLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Utility tests for Metrics and GraphLoader functionality.
 */
public class UtilityTests {
    // --- Metrics tests ---
    @Test
    void testMetricsIncrementAndReset() {
        Metrics m = new Metrics();
        m.inc("dfs_calls");
        m.add("dfs_calls", 2);
        assertEquals(3, m.get("dfs_calls"));
        m.reset();
        assertEquals(0, m.get("dfs_calls"));
    }
    @Test
    void testMetricsSummary() {
        Metrics m = new Metrics();
        m.add("edge_relaxations", 4);
        assertTrue(m.summary().contains("edge_relaxations=4"));
    }
    // --- GraphLoader tests ---
    @Test
    void testLoadExistingDataset() {
        var ds = GraphLoader.loadFromResource("small_1.json");
        assertNotNull(ds.graph);
        assertTrue(ds.graph.getN() > 0);
    }
    @Test
    void testLoadNonExistingFile() {
        Exception ex = assertThrows(RuntimeException.class, () ->
                GraphLoader.loadFromResource("no_such_file.json"));
        assertTrue(ex.getMessage().contains("File"));
    }
}
