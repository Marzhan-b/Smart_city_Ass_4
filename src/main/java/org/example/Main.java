package org.example;
import org.example.scc.Kosaraju;
import org.example.scc.CondensationGraph;
import org.example.util.GraphLoader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        var ds = GraphLoader.loadFromResource("large_1.json");
        System.out.println("File: " + ds.name);
        System.out.println("Vertices: " + ds.graph.getN());
        System.out.println("Edges: " + ds.graph.getEdgesCount());
        System.out.println("Source " + ds.source);
        System.out.println(ds.graph);

        // --- Run Kosaraju algorithm ---
        var result = Kosaraju.run(ds.graph);

        System.out.println("\nStrongly Connected Components:");
        for (int i = 0; i < result.components.size(); i++) {
            List<Integer> comp = result.components.get(i);
            System.out.println("SCC " + i + ": " + comp + " (size = " + comp.size() + ")");
        }

        // --- Build condensation DAG ---
        var condensation = CondensationGraph.build(ds.graph, result);
        System.out.println("\nCondensation DAG (each node = one SCC):");
        System.out.println(condensation);
    }

}
