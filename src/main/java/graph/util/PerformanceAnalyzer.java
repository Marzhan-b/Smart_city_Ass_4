package graph.util;
import org.apache.commons.csv.*;
import graph.model.Graph;
import graph.scc.Kosaraju;
import graph.scc.CondensationGraph;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;
import graph.dagsp.DAGLongestPath;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
/**
 * PerformanceAnalyzer
 * -------------------
 * Runs SCC, Condensation, Topological Sort, and DAG Shortest/Longest Path algorithms
 * on all datasets stored under /data, and exports timing + metrics to results.csv.
 * CSV Columns:
 * dataset_name, scc_duration_ns, dfs_calls, edges_processed,
 * topo_duration_ns, stack_pushes, stack_pops, shortest_path_ns, edge_relaxations
 */
public class PerformanceAnalyzer {
    public static void main(String[] args) {
        List<String> datasets = List.of(
                "small_1.json", "small_2.json", "small_3.json",
                "medium_1.json", "medium_2.json", "medium_3.json",
                "large_1.json", "large_2.json", "large_3.json"
        );
        Path outputPath = Path.of("results.csv");
        try (var writer = Files.newBufferedWriter(outputPath);
             var csv = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "dataset_name", "scc_duration_ns", "dfs_calls", "edges_processed",
                     "topo_duration_ns", "stack_pushes", "stack_pops",
                     "shortest_path_ns", "edge_relaxations"))) {

            Metrics metrics = new Metrics();
            for (String fileName : datasets) {
                System.out.println("⚙  Processing: " + fileName);
                var ds = GraphLoader.loadFromResource(fileName);
                Graph g = ds.graph;

                // === SCC (Kosaraju) ===
                long sccStart = System.nanoTime();
                var sccResult = Kosaraju.run(g);
                long sccEnd = System.nanoTime();
                long sccTime = sccEnd - sccStart;
                metrics.add("dfs_calls", g.getN());
                metrics.add("edges_processed", g.getEdgesCount());

                // === Condensation + Topological Sort ===
                long topoStart = System.nanoTime();
                var dag = CondensationGraph.build(g, sccResult);
                var topo = TopologicalSort.sort(dag);
                long topoEnd = System.nanoTime();
                long topoTime = topoEnd - topoStart;
                metrics.add("stack_pushes", dag.getN());
                metrics.add("stack_pops", topo.size());

                // === Shortest Path ===
                long spStart = System.nanoTime();
                var sp = DAGShortestPath.run(dag, topo, sccResult.component[ds.source]);
                long spEnd = System.nanoTime();
                long spTime = spEnd - spStart;
                metrics.add("edge_relaxations", dag.getEdgesCount());

                // === Longest Path (critical path) ===
                long lpStart = System.nanoTime();
                DAGLongestPath.run(dag, topo, sccResult.component[ds.source]);
                long lpEnd = System.nanoTime();
                long lpTime = lpEnd - lpStart;

                // === Write to CSV ===
                csv.printRecord(
                        fileName,
                        sccTime,
                        metrics.get("dfs_calls"),
                        metrics.get("edges_processed"),
                        topoTime,
                        metrics.get("stack_pushes"),
                        metrics.get("stack_pops"),
                        spTime,
                        metrics.get("edge_relaxations")
                );

                System.out.printf(
                        " %-12s | SCC: %.3f ms | SP: %.3f ms | LP: %.3f ms%n",
                        fileName, sccTime / 1_000_000.0, spTime / 1_000_000.0, lpTime / 1_000_000.0
                );

                metrics.reset();
            }

            System.out.println("\n Performance analysis complete!");
            System.out.println("Results saved to " + outputPath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println(" Error writing CSV: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(" Benchmark failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
