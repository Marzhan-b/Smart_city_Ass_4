package graph.util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graph.model.Edge;
import graph.model.Graph;
import java.io.InputStream;
import java.util.List;
/**
 * Utility class to load graph data from JSON files.
 *
 * Expected format:
 * {
 *   "directed": true,
 *   "n": 8,
 *   "edges": [{"u":0,"v":1,"w":3}, ...],
 *   "source": 0,
 *   "weight_model": "edge"
 * }
 */
public class GraphLoader {
    /** Holds a parsed dataset (graph + metadata). */
    public static class DataSet {
        public final Graph graph;
        public final int source;
        public final String weightModel;
        public final String name;

        public DataSet(Graph g, int source, String wm, String name) {
            this.graph = g;
            this.source = source;
            this.weightModel = wm;
            this.name = name;
        }
    }
    /**
     * Loads a graph from JSON resource file (located in /data folder).
     *
     * @param resourceName file name in /data/
     * @return DataSet object containing the parsed graph and metadata
     */
    public static DataSet loadFromResource(String resourceName) {
        try (InputStream in = GraphLoader.class.getResourceAsStream("/data/" + resourceName)) {
            if (in == null)
                throw new IllegalArgumentException("File not found: " + resourceName);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(in);

            boolean directed = root.path("directed").asBoolean(true);
            int n = root.path("n").asInt();
            Graph g = new Graph(n, directed);

            // Step 1: Read all edges
            List<Edge> edges = mapper.convertValue(root.path("edges"), new TypeReference<List<Edge>>() {});
            for (Edge e : edges) {
                g.addEdge(e.getU(), e.getV(), e.getW());
            }

            // Step 2: Extract source and weight model
            int source = root.path("source").asInt(0);
            String wm = root.path("weight_model").asText("edge");

            return new DataSet(g, source, wm, resourceName);
        } catch (Exception e) {
            // Updated message so testLoadNonExistingFile() passes
            throw new RuntimeException("File load error: " + resourceName, e);
        }
    }
}
