package graph.model;
/**
 * Represents a directed weighted edge in a graph.
 * Each edge connects vertex u → v with a weight w.
 */
public class Edge {
    private int u; //from
    private int v; //to
    private int w; //weight

    /** Default constructor (required for JSON parsing). */
    public Edge() {}
    /** Creates a new edge (u -> v) with given weight. */
    public Edge(int u, int v, int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    public int getU() {
        return u;
    }
    public int getV() {
        return v;
    }
    public int getW() {
        return w;
    }

    @Override
    public String toString() {
        return u + " -> " + v + " (" + w + ")";
    }
}
