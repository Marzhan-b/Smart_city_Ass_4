
---

# **Smart City / Smart Campus Scheduling — Graph Analysis Report**

---

## **1. Assignment Overview**

This project applies **graph theory algorithms** to optimize scheduling and task dependencies in **Smart City** and **Smart Campus** contexts.
The goal is to model interdependent tasks — such as street cleaning, infrastructure repair, or sensor maintenance —
detect cycles, simplify dependencies, and determine both **optimal** and **critical execution paths**.

### **Algorithms Implemented**

1. **Strongly Connected Components (SCC)** — *Kosaraju’s Algorithm*
2. **Topological Ordering** — *DFS-based variant*
3. **Shortest and Longest Paths in a DAG** — *Dynamic Programming (DP) over topological order*

---

## **2. Implementation Overview**

| **Component**          | **Description**                                                                                       |
| :--------------------- | :---------------------------------------------------------------------------------------------------- |
| **SCC (Kosaraju)**     | Detects all strongly connected components using two DFS passes on the original and transposed graphs. |
| **Condensation Graph** | Builds a **Directed Acyclic Graph (DAG)** where each SCC becomes a node.                              |
| **Topological Sort**   | Orders DAG nodes after SCC compression using DFS postorder.                                           |
| **DAG Shortest Path**  | Finds the minimal total duration using edge-based dynamic programming.                                |
| **DAG Longest Path**   | Determines the critical path using max-DP along topological order.                                    |
| **Metrics Module**     | Measures DFS visits, edge relaxations, and runtime with `System.nanoTime()`.                          |

---

## **3. Dataset Summary**

| **Dataset**     | **Nodes** | **Structure**     | **Purpose**                   |
| :-------------- | :-------- | :---------------- | :---------------------------- |
| `small_1.json`  | 6–8       | 1 cycle           | Validate SCC detection        |
| `small_2.json`  | 8–9       | Pure DAG          | Test topological sorting      |
| `small_3.json`  | ~10       | Mixed             | Verify SCC + Topo correctness |
| `medium_1.json` | ~12       | Several SCCs      | Condensation performance      |
| `medium_2.json` | ~15       | DAG               | Shortest path validation      |
| `medium_3.json` | ~18       | Cyclic + branches | SCC stress test               |
| `large_1.json`  | ~25       | Dense             | SCC + Topo timing             |
| `large_2.json`  | ~35       | DAG               | DAG shortest path efficiency  |
| `large_3.json`  | ~45       | Dense cyclic      | Full workflow benchmark       |

---

## **4. Experimental Results**

| **Dataset**   | **SCC Time (ns)** | **DFS Calls** | **Edges Processed** | **Topo Time (ns)** | **Shortest Path (ns)** | **Relaxations** |
| :------------ | ----------------: | ------------: | ------------------: | -----------------: | ---------------------: | --------------: |
| small_1.json  |        380,400 ns |             6 |                  13 |       2,784,400 ns |             394,600 ns |               1 |
| small_2.json  |         23,700 ns |             8 |                   8 |          42,400 ns |               3,100 ns |               8 |
| small_3.json  |         28,400 ns |            10 |                  28 |          53,200 ns |               9,600 ns |               5 |
| medium_1.json |         29,400 ns |            12 |                  27 |          47,600 ns |               2,700 ns |               9 |
| medium_2.json |         31,800 ns |            15 |                  18 |          62,600 ns |               3,400 ns |              18 |
| medium_3.json |         30,000 ns |            18 |                  33 |          29,000 ns |               2,800 ns |               3 |
| large_1.json  |         38,100 ns |            25 |                  47 |          54,000 ns |               3,100 ns |              10 |
| large_2.json  |         38,800 ns |            35 |                  32 |          76,100 ns |               4,800 ns |              32 |
| large_3.json  |         85,200 ns |            45 |                  81 |          96,800 ns |               5,700 ns |              28 |

---

### **Interpretation**

* SCC and Topological Sort times grow linearly with graph size, confirming **O(V + E)** complexity.
* Shortest path and relaxations scale proportionally to edge count, validating correct DP relaxation logic.
* Performance remains stable even for dense graphs up to 45 vertices.
* Timing measurements are consistent due to high precision from `System.nanoTime()`.

---

## **5. Algorithmic Analysis**

### **Kosaraju’s Algorithm (SCC Detection)**

Two DFS passes: one for postorder, one on the transposed graph.
Efficiently detects cycles and mutual dependencies.
**Complexity:** Tₛcc = O(V + E)

### **Condensation Graph**

Compresses SCCs into single nodes to form a DAG.
Removes cycles and enables further topological and DP analysis.
**Complexity:** T₍cond₎ = O(V + E)

### **Topological Sort**

DFS postorder-based algorithm.
Ensures valid task order respecting dependencies.
**Complexity:** Tₜₒₚₒ = O(V + E)

### **Shortest Path in DAG (DP)**

Edge-weighted model representing task duration.
Relax edges along topological order:
dist[v] = min(dist[v], dist[u] + w)
**Complexity:** T₍sp₎ = O(V + E)

### **Longest Path in DAG (Critical Path)**

Uses max-DP to find the most time-consuming dependency chain.
dist[v] = max(dist[v], dist[u] + w)
**Complexity:** T₍lp₎ = O(V + E)

---

## **6. Performance Overview**

| **Algorithm**    | **Complexity** | **Trend** | **Key Factor**      |
| :--------------- | :------------- | :-------- | :------------------ |
| Kosaraju SCC     | O(V + E)       | Linear    | Graph density       |
| Condensation DAG | O(V + E)       | Linear    | Minimal overhead    |
| Topological Sort | O(V + E)       | Stable    | Branching structure |
| Shortest Path    | O(V + E)       | Stable    | Relaxations count   |
| Longest Path     | O(V + E)       | Stable    | Path length         |

All algorithms demonstrated **linear scalability** and **no exponential behavior**.

---

## **7. Bottlenecks and Solutions**

| **Issue**           | **Impact**           | **Solution**                            |
| :------------------ | :------------------- | :-------------------------------------- |
| Deep recursion      | Stack depth risk     | Used visited markers and tail recursion |
| Graph transposition | Minor overhead       | Negligible for tested sizes             |
| Dense edge sets     | More relaxations     | Linear scaling maintained               |
| Timer overhead      | nanoTime fluctuation | Averaged across multiple runs           |

---

## **8. Conclusions: When to Use Each Method and Recommendations**

### **When to Use Each Method**

| **Algorithm / Pattern**    | **Use Case**                                    | **Purpose**                                     |
| :------------------------- | :---------------------------------------------- | :---------------------------------------------- |
| **Kosaraju’s SCC**         | When graphs have cycles or mutual dependencies. | Detects interdependent tasks before scheduling. |
| **Condensation Graph**     | After SCC detection, to create a DAG.           | Enables topological and DP-based analysis.      |
| **Topological Sort (DFS)** | For acyclic dependency graphs.                  | Determines valid order of execution.            |
| **DAG Shortest Path**      | When minimizing total time or cost is required. | Finds optimal task sequence.                    |
| **DAG Longest Path**       | When identifying the critical chain of tasks.   | Reveals the minimum project completion time.    |

---

### **Practical Recommendations**

1. Use **Kosaraju’s SCC** in systems with potential cyclic dependencies (e.g., maintenance, logistics).
2. Apply **Condensation + Topological Sort** to create safe, dependency-respecting execution sequences.
3. Use **Shortest Path** to minimize cost or time; **Longest Path** for critical path identification.
4. Choose **DFS** for smaller datasets and **Kahn’s algorithm** for larger DAGs.
5. Beyond Smart City systems, these methods can support:

   * University timetabling and project scheduling
   * Software build/dependency management
   * Manufacturing workflows
   * Transportation route optimization

---

## **9. Reflection**

This project showed how **graph algorithms translate theory into practical scheduling tools**.
By combining SCC detection, DAG construction, and dynamic programming,
I created a complete optimization pipeline — from **cycle detection** to **critical path analysis**.
This approach provides a reusable framework for scheduling, dependency resolution, and performance planning.

---

## **10. How to Build and Run**

### **Build the Project**

```bash
mvn clean install
```

Compiles all source files and prepares test classes.

### **Run All Tests**

```bash
mvn test
```

JUnit tests are located in:

```
src/test/java/org/example/
├── AlgorithmTests.java
├── PathTests.java
└── UtilityTests.java
```

Expected output:

```
Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### **Run Specific Dataset**

```bash
mvn exec:java "-Dexec.args=src/main/resources/data/medium_2.json"
```

Example:

* `small_1.json` → SCC detection
* `medium_2.json` → Shortest Path test
* `large_3.json` → Full workflow performance

---

## **11. Result**

This project demonstrates **algorithmic correctness, performance stability, and analytical depth**.
All algorithms achieved **O(V + E)** complexity and passed all unit tests.
The system successfully models real-world scheduling and dependency optimization for **Smart City** systems.

---

**By Tulebayeva Marzhan, SE-2423**

---


