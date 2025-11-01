package graph.util;

public interface Metrics {
    void incrementOperations();
    void incrementTime(long time);
}
