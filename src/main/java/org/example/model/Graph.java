package org.example.model;

import java.util.List;

public interface Graph {
    int vertexCount();
    List<Integer> neighbors(int v);
}
