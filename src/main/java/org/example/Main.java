package org.example;

import org.example.util.GraphLoader;

public class Main {
    public static void main(String[] args) {
        var ds = GraphLoader.loadFromResource("small_1.json");
        System.out.println("File: " + ds.name);
        System.out.println("Vertices: " + ds.graph.getN());
        System.out.println("Edges: " + ds.graph.getEdgesCount());
        System.out.println("Source " + ds.source);
        System.out.println(ds.graph);
    }
}
