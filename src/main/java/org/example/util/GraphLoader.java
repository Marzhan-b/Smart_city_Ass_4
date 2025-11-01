package org.example.util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Edge;
import org.example.model.Graph;
import java.io.InputStream;
import java.util.List;

public class GraphLoader {
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
    public static DataSet loadFromResource(String resourceName) {
        try (InputStream in = GraphLoader.class.getResourceAsStream("/data/" + resourceName)) {
            if (in == null)
                throw new IllegalArgumentException("File is not found " + resourceName);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(in);

            boolean directed = root.path("directed").asBoolean(true);
            int n = root.path("n").asInt();
            Graph g = new Graph(n, directed);

            List<Edge> edges = mapper.convertValue(root.path("edges"), new TypeReference<List<Edge>>() {});
            for (Edge e : edges) {
                g.addEdge(e.getU(), e.getV(), e.getW());
            }

            int source = root.path("source").asInt(0);
            String wm = root.path("weight_model").asText("edge");

            return new DataSet(g, source, wm, resourceName);
        } catch (Exception e) {
            throw new RuntimeException("Mistakes when loaded from JSON: " + resourceName, e);
        }
    }
}
