package graph.util;
import java.util.HashMap;
import java.util.Map;
/**
 * Metrics utility class
 * ---------------------
 * Collects and manages algorithmic operation counters such as:
 *  - DFS calls and edges processed (SCC)
 *  - Stack operations (Topological Sort)
 *  - Relaxations (DAG Shortest Path)
 * Also used together with System.nanoTime() for timing.
 */
public class Metrics {
    private final Map<String, Long> counters = new HashMap<>();
    /** Increase a specific counter by 1 (e.g., dfs_calls++) */
    public void inc(String name) {
        counters.put(name, counters.getOrDefault(name, 0L) + 1);
    }
    /** Add a value to a counter (e.g., relaxations += count) */
    public void add(String name, long value) {
        counters.put(name, counters.getOrDefault(name, 0L) + value);
    }
    /** Get the current value of a counter */
    public long get(String name) {
        return counters.getOrDefault(name, 0L);
    }
    /** Reset all counters to zero (use before processing the next dataset) */
    public void reset() {
        counters.clear();
    }
    /** Print metrics for debugging */
    public String summary() {
        StringBuilder sb = new StringBuilder();
        counters.forEach((key, value) -> sb.append(key).append("=").append(value).append(" "));
        return sb.toString().trim();
    }
}

