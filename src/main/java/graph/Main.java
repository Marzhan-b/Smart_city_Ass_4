package graph;
import graph.scc.Kosaraju;
import graph.scc.CondensationGraph;
import graph.util.GraphLoader;
import java.util.List;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGLongestPath;
import graph.dagsp.DAGShortestPath;
import java.util.*;
/**
 * Main class for Assignment 4: Smart City
 *
 * Executes the following steps:
 *  1. Load a directed dependency graph from JSON (tasks.json format).
 *  2. Run Kosaraju's algorithm to detect Strongly Connected Components (SCCs).
 *  3. Build the condensation DAG (each SCC becomes a single node).
 *  4. Perform topological sorting on the DAG.
 *  5. Output the SCCs, their sizes, the condensation graph, and topological order.
 *
 * Note: The same main pipeline can later be extended for shortest and longest paths (Task 1.3).
 */
public class Main {
    /**
     * Entry point of the program.
     * Loads one dataset, runs SCC + Condensation + Topological Sort, and prints the results.
     */
    public static void main(String[] args) {
        // === Step 1: Load dataset ===
        var ds = GraphLoader.loadFromResource("large_1.json");
        System.out.println("File: " + ds.name);
        System.out.println("Vertices: " + ds.graph.getN());
        System.out.println("Edges: " + ds.graph.getEdgesCount());
        System.out.println("Source " + ds.source);
        System.out.println(ds.graph);

        //  Run Kosaraju algorithm
        var result = Kosaraju.run(ds.graph);

        System.out.println("\nStrongly Connected Components:");
        for (int i = 0; i < result.components.size(); i++) {
            List<Integer> comp = result.components.get(i);
            System.out.println("SCC " + i + ": " + comp + " (size = " + comp.size() + ")");
        }

        //  Build condensation DAG
        var condensation = CondensationGraph.build(ds.graph, result);
        System.out.println("\nCondensation DAG (each node = one SCC):");
        System.out.println(condensation);

        //  Topological Sort
        var topoOrder = TopologicalSort.sort(condensation);
        System.out.println("\nTopological Order of SCCs: " + topoOrder);

        // Derived order of original tasks
        System.out.println("\nDerived order of original tasks (based on SCC order):");
        for (int compIndex : topoOrder) {
            System.out.println("SCC " + compIndex + " → " + result.components.get(compIndex));
        }
        // DAG Shortest and Longest Paths ===
        int sourceComp = result.component[ds.source];
        System.out.println("\nSource component in DAG: " + sourceComp);

        // --- Shortest Paths ---
        long t1 = System.nanoTime();
        var sp = DAGShortestPath.run(condensation, topoOrder, sourceComp);
        long t2 = System.nanoTime();
        System.out.printf("Shortest paths computed in %.3f ms%n", (t2 - t1) / 1_000_000.0);

        System.out.println("Shortest distances from source:");
        System.out.println(Arrays.toString(sp.dist));

        // reconstruct path to last SCC in topo order
        int targetComp = topoOrder.get(topoOrder.size() - 1);
        System.out.println("Shortest path to SCC " + targetComp + ": " + sp.reconstruct(targetComp));

        // --- Longest Path (Critical Path) ---
        long t3 = System.nanoTime();
        var lp = DAGLongestPath.run(condensation, topoOrder, sourceComp);
        long t4 = System.nanoTime();
        System.out.printf("\nLongest path computed in %.3f ms%n", (t4 - t3) / 1_000_000.0);

        System.out.println("Critical path (SCC indices): " + lp.criticalPath());
        System.out.println("Critical path length: " + lp.length());


    }

}
