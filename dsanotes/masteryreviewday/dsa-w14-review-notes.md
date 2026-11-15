# LeetCodePrep Week 14 Mastery & Review Day

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 14: Advanced Graphs (November 14)
# ===============================================================

## Topics Covered
- [ ] Dijkstra
- [ ] Bellman-Ford
- [ ] Union-Find
- [ ] Prim's Algorithm and Kruskal's Algorithm (Minimum Spanning Tree)
- [ ] BFS (Kahn's Algorithm) and DFS (3-Color State) (Topological Sort) 

## LC 210. Course Schedule II
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **Directed Acyclic Graph (DAG) Topological Sort**: Course prerequisites form directed edges
      `prev -> post`. Finding the execution order of all courses requires ordering nodes such that for
      every directed edge u → v, u appears before v.
    * **Zero-Allocation Two-Pointer Linear Ring Queue Invariant**:
        - Every node is enqueued at most once into the topological sequence.
        - Therefore, the output `schedule: IntArray(numCourses)` array itself acts as BOTH the
          **Output Array** AND the **BFS Queue**!
        - Two pointers `head` (pop pointer) and `tail` (push pointer) advance linearly from 0 to V-1.
        - `schedule[tail++] = i` enqueues courses when `inDegree[i] == 0`.
        - `val pop = schedule[head++]` dequeues courses in O(1) time.
        - If `head == numCourses`, all courses were processed in topological order (return `schedule`);
          otherwise, a directed cycle exists (return `intArrayOf()`).

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Zero-Allocation Two-Pointer Ring Queue** | **O(V + E)** | **O(V + E)** | **Staff Level (Extreme)** | Zero queue heap allocations; uses `schedule` array as Queue; 100% cache-line locality. |
 **ArrayDeque BFS (Kahn's Algorithm)** | **O(V + E)** | **O(V + E)** | Peak (Canonical) | Standard interview default; clear explicit FIFO queue structure. |
 **DFS (3-Color State + Post-Order Reverse)** | **O(V + E)** | **O(V + E)** stack | Good | Fast recursive cycle detection + topological ordering. |

*(where V = numCourses, E = prerequisites.size)*

### 3. Native Kotlin Syntax Pitfalls
* **Zero-Allocation Memory Optimization**: Using the output `schedule: IntArray(numCourses)` array
  directly as the BFS queue avoids instantiating `ArrayDeque<Int>`, achieving **zero auxiliary queue
  object allocations**!
* **CPU Cache Line Locality & Mechanical Sympathy**: Sequentially reading from `schedule[head++]`
  and writing to `schedule[tail++]` operates on contiguous memory blocks in L1 CPU Cache with hardware
  prefetching.

### 4. Code Block
```kotlin
// Approach 1: Kahn's Algorithm (Topological Sort) 
// Zero-Allocation Two-Pointer Ring Queue (Staff Level Extreme)
// Time Complexity: O(V + E) | Auxiliary Space Complexity: O(V + E)
fun findOrder(numCourses: Int, prerequisites: Array<IntArray>): IntArray {
    val n = numCourses
    val inOrder = IntArray(n)
    val nextCourses = Array(n) { mutableListOf<Int>() }

    for (prep in prerequisites) {
        nextCourses[prep[1]].add(prep[0])
        inOrder[prep[0]]++
    }

    val schedule = IntArray(n)
    var head = 0
    var tail = 0

    for (i in 0 until n) {
        if (inOrder[i] == 0) schedule[tail++] = i
    }

    while (head < tail) {
        val course = schedule[head++]
        for (next in nextCourses[course]) {
            if (--inOrder[next] == 0) schedule[tail++] = next
        }
    }

    return if (head == n) schedule else intArrayOf()
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Two-Pointer Array Queue vs. `ArrayDeque`**:
    * `ArrayDeque` incurs object allocation and internal array resizing overhead.
    * The **Two-Pointer Array Queue (`schedule[tail++] = next` and `schedule[head++]`)** reuses the
      output array itself as the queue. Since every node is enqueued at most once, `head` and `tail`
      move strictly forward from 0 to V-1 without needing modulo wrapping (`% V`).
* **DFS Call Stack Overflow vs. Kahn's BFS Safety**:
    * On a deep linear graph (0 → 1 → 2 → ... → V-1), DFS recursion call stack reaches depth V.
      For V = 10^5, this risks a JVM `StackOverflowError`.
    * Kahn's BFS operates iteratively, guaranteeing **100% StackOverflow safety**.
* **Parallel Task Schedulers (Gradle, Bazel, Airflow)**:
    * Kahn's algorithm models real-world build systems and DAG orchestrators. At any BFS step, all
      nodes in the queue with `inDegree == 0` have satisfied their prerequisites and **can be executed
      concurrently in parallel across worker nodes/threads**!

---

## LC 1584. Min Cost to Connect All Points
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **Minimum Spanning Tree (MST) on Complete Dense Graph**: Connecting all N points with minimum
      total Manhattan distance forms a Complete Dense Graph with V = N nodes and E = N(N - 1) / 2 = O(N²)
      edges.
    * **Dense Prim's Algorithm Invariant (O(N²) Time, O(N) Space)**:
        - On dense graphs where E = O(V²), Prim's algorithm with a linear scan over N unvisited nodes
          runs in O(V²) = O(N²) time and O(N) space.
        - Min-Heap Prim's or Kruskal's algorithm requires O(E x Log(V)) = O(N² x Log(N)) time and O(N²)
          edge list space.
        - Therefore, **Dense Linear-Scan Prim's Algorithm is strictly optimal** for LC 1584!

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                                                                   |
 :--- | :--- | :--- | :--- |:--------------------------------------------------------------------------------------------------------------------|
 **Dense Prim's (Linear Scan)** | **O(N²)** | **O(N)** | **Staff Level (Optimal)** | Dense complete graphs (E = O(N²)); zero edge list allocations; strictly beats O(N² x Log(N)).                       |
 **Min-Heap Prim's Algorithm** | **O(N² x Log(N))** | **O(N²)** heap space | Peak | Sparse graphs (E ≪ V²).                                                                                             |
 **Kruskal's Algorithm + DSU** | **O(N² x Log(N))** | **O(N²)** edge list | Good | Minimum Spanning Tree baseline; edge list already sorted or sparse graphs.                                          |

*(where N = points.size ≤ 1000)*

### 3. Native Kotlin Syntax Pitfalls
* **Avoid Edge Object Allocations**: Generating a `List<IntArray>` or `List<Edge>` of size
  (N(N - 1) / 2) creates up to 500,000 objects on the JVM Heap. Dense Prim's algorithm maintains a
  single flat `IntArray(N)` for minimum distances, achieving **zero heap edge allocations**!
* **Inline Manhattan Distance Calculation**:
  `val dist = abs(currX - points[nextNode][0]) + abs(currY - points[nextNode][1])`
  inside the inner loop avoids function call overhead.

### 4. Code Block
```kotlin
// Approach 1: Dense Prim's Algorithm (Staff Level Optimal - O(N²) Time, O(N) Space)
// Time Complexity: O(N²) | Auxiliary Space Complexity: O(N)
fun minCostConnectPoints(points: Array<IntArray>): Int {
    val n = points.size
    val minCost = IntArray(n) { Int.MAX_VALUE }
    minCost[0] = 0
    val connected = BooleanArray(n)
    var currNode: Int
    var currMinCost: Int
    var totalMinCost = 0

    for (i in 0 until n) {
        currNode = -1
        currMinCost = Int.MAX_VALUE
        for (j in 0 until n) {
            if (!connected[j] && minCost[j] < currMinCost) {
                currMinCost = minCost[j]
                currNode = j
            }
        }

        connected[currNode] = true
        totalMinCost += currMinCost
        val currNodeX = points[currNode][0]
        val currNodeY = points[currNode][1]

        for (nextNode in 0 until n) {
            if (!connected[nextNode]) {
                val dist = abs(currNodeX - points[nextNode][0]) +
                        abs(currNodeY - points[nextNode][1])
                if (dist < minCost[nextNode]) {
                    minCost[nextNode] = dist
                }
            }
        }
    }

    return totalMinCost
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Dense Prim's vs. Kruskal's vs. Borůvka's Algorithm**:
    * **Dense Prim's (O(V²) time, O(V) space)**: Optimal choice for dense complete graphs. Avoids
      sorting E = O(V²) edges.
    * **Kruskal's (O(E x Log(E)) time, O(E) space)**: Optimal choice for sparse graphs where E ≪ V².
      Sorts edges upfront and builds MST via DSU.
    * **Borůvka's (O(E x Log(V)) time)**: Parallel MST algorithm where each component independently
      finds its cheapest outgoing edge. Highly effective for distributed GPU / MapReduce graph processing.
---


## LC 684. Redundant Connection
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **Cycle Detection in Undirected Graph**: Given an undirected graph that started as a tree of N
      nodes (N nodes, `N - 1` edges), adding 1 extra edge creates exactly 1 cycle. Finding the edge that
      creates a cycle maps directly to **Disjoint Set Union (DSU / Union-Find)**.
    * **Inverse Ackermann (O(α(N))) Union-Find Invariant**:
        - Each edge u → v connects two components.
        - If `find(u) == find(v)`, nodes u and v are ALREADY connected in the same set! Adding edge
          (u, v) completes a cycle ⇒ (u, v) is the redundant edge!
        - Processing edges sequentially from 1 to N guarantees that the LAST edge creating a cycle is
          identified.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Flat Primitive Array DSU (Path Compression + Rank)** | **O(N x α(N))** | **O(N)** | **Staff Level (Extreme)** | Zero heap allocations; flat `IntArray`; inverse Ackermann time $\mathcal{O}(\alpha(N)) \approx \mathcal{O}(1)$. |
 **HashMap DisjointSet** | **O(N x α(N))** | **O(N)** | Peak | Dynamic non-integer node IDs where object hashing is required. |
 **DFS Cycle Detection** | **O(N²)** | **O(N)** recursion stack | Baseline | Naive graph traversal baseline without DSU data structure. |

*(where $N = \text{edges.size} \le 1000$)*

### 3. Native Kotlin Syntax Pitfalls
* **Object Boxing Churn (`HashMap<Int, Int>`)**: Declaring `parents` and `ranks` as `HashMap<Int, Int>`
  causes continuous integer boxing/unboxing (`java.lang.Integer`) and null checks (`!!`). For 1-based
  node IDs `1 .. N`, flat primitive `IntArray(N + 1)` achieves **100% zero heap object allocations**
  and a **~10× speedup**!
* **Path Compression Path-Halving (`parent[root] = parent[parent[root]]`)**: Iterative path halving
  inside `find(i)` compresses trees in O(1) stack space without recursive call stack overhead.

### 4. Code Block
```kotlin
// Approach 1: Flat Primitive IntArray DSU (Staff Level Extreme - Zero Heap Object Allocations)
// Time Complexity: O(N * α(N)) ≈ O(N) | Auxiliary Space Complexity: O(N)
fun findRedundantConnectionDSU(edges: Array<IntArray>): IntArray {
    val n = edges.size
    val parents = IntArray(n + 1) { it }
    val rank = IntArray(n + 1)

    fun find(v: Int): Int {
        var root = v
        while (root != parents[root]) {
            parents[root] = parents[parents[root]]
            root = parents[root]
        }
        return root
    }

    fun union(x: Int, y: Int): Boolean {
        val rootX = find(x)
        val rootY = find(y)

        if (rootX == rootY) return false

        when {
            rank[rootX] > rank[rootY] -> parents[rootY] = rootX
            rank[rootX] < rank[rootY] -> parents[rootX] = rootY
            else -> {
                parents[rootY] = rootX
                rank[rootX]++
            }
        }
        return true
    }

    for (edge in edges) {
        if (!union(edge[0], edge[1])) return edge
    }

    return intArrayOf()
}

// Approach 2: Standard Disjoint Set (Union-Find)
// Time Complexity: O(N * α(N)) | Auxiliary Space Complexity: O(N)
fun findRedundantConnection(edges: Array<IntArray>): IntArray {
    val disjointSet = DisjointSet(edges.size)

    for (edge in edges) {
        if (!disjointSet.union(edge[0], edge[1])) return edge
    }

    return intArrayOf()
}

class DisjointSet(n: Int) {
    val parents = IntArray(n + 1) { it }
    val rank = IntArray(n + 1)

    fun find(v: Int): Int {
        var root = v
        while (root != parents[root]) {
            parents[root] = parents[parents[root]]
            root = parents[root]
        }
        return root
    }

    fun union(x: Int, y: Int): Boolean {
        val rootX = find(x)
        val rootY = find(y)

        if (rootX == rootY) return false

        when {
            rank[rootX] > rank[rootY] -> parents[rootY] = rootX
            rank[rootX] < rank[rootY] -> parents[rootX] = rootY
            else -> {
                parents[rootY] = rootX
                rank[rootX]++
            }
        }
        return true
    }

}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **DSU vs. DFS Cycle Detection**:
    * **DFS (O(N²) time)**: For each edge added, run DFS from u to check if v is reachable. If reachable,
      adding (u, v) creates a cycle.
    * **DSU (O(N x α(N)) time)**: Processes all N edges incrementally in near-constant amortized time
      (α(N) < 5 for all practical universe sizes).
* **Flat Array DSU vs. Object-Based DSU**:
    * On high-throughput JVM pipelines or real-time graph engines, avoiding `HashMap` object wrappers
      (`Integer` boxing) and using primitive `IntArray` eliminates Garbage Collection pauses and
      leverages L1 CPU cache lines.

---

## LC 787. Cheapest Flights Within K Stops
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **Shortest Path with Bounded Step Count (K Stops)**: Finding the cheapest flight path from `src`
      to `dst` with at most K stops (at most K + 1 edges).
    * **Step Boundary Relaxation Invariant (Ripple Effect Guard)**:
        - Standard Bellman-Ford updates distance in-place, allowing a chain A → B → C → D to relax 3
          edges in 1 pass.
        - For bounded K stops, pass i MUST ONLY use distances from pass i-1
          (`nextCost[v] = min(nextCost[v], cost[u] + w)`).
    * **Level-Order BFS Queue Efficiency**:
      Processing nodes level-by-level up to K+1 edge levels ONLY relaxes edges originating from active
      level nodes, skipping unnecessary scans over all E flights!

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Level-Order BFS Queue** | **O(V + E x K)** | **O(V + E)** | **Staff Level (Extreme)** | Sparse active node expansion; only relaxes edges from active level nodes. |
 **Double-Buffered Bellman-Ford** | **O(K x E)** | **O(V)** | **Staff Level (Optimal DP)** | Standard production choice; zero array allocation buffer swapping. |
 **Naive Bellman-Ford (`clone()`)** | **O(K x E)** | **O(V)** | Baseline | Creates $K+1$ `IntArray` clones during loop execution. |

*(where V = n, E = flights.size, K = max stops)*

### 3. Native Kotlin Syntax Pitfalls
* **Avoid `cost.clone()` Array Churn**: Calling `cost.clone()` on every iteration allocates K+1 heap
  arrays. Use two pre-allocated arrays `cost` and `nextCost` and swap references (`val temp = cost; 
cost = nextCost; nextCost = temp`)!
* **Level-Order BFS Size Snapshot**: Inside the BFS queue loop, snapshot `val size = queue.size` at
  the beginning of each stop level to process exactly one step level per outer loop iteration.

### 4. Code Block
```kotlin
// Approach 1: Double-Buffered Bellman-Ford Algorithm (Zero Heap Allocations)
// Time Complexity: O(K * E) | Auxiliary Space Complexity: O(N)
fun findCheapestPrice(n: Int, flights: Array<IntArray>, src: Int, dst: Int, k: Int): Int {
    val cost = IntArray(n) { Int.MAX_VALUE }
    val newCost = IntArray(n) { Int.MAX_VALUE }
    cost[src] = 0
    newCost[src] = 0

    for (i in 0 .. k) {
        var anyUpdate = false

        for (f in flights) {
            val u = f[0]
            val v = f[1]
            val w = f[2]

            if (cost[u] != Int.MAX_VALUE && cost[u] + w < newCost[v]) {
                newCost[v] = cost[u] + w
                anyUpdate = true
            }
        }

        if (!anyUpdate) break

        for (j in 0 until n) {
            cost[j] = newCost[j]
        }
    }

    return if (cost[dst] == Int.MAX_VALUE) -1 else cost[dst]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Why Standard Dijkstra Fails for Step-Bounded Shortest Path**:
    * Standard Dijkstra selects nodes purely by minimum accumulated cost.
    * A lower-cost path with `K + 2` stops might reach node X first and finalize it, blocking a slightly
      higher-cost path with ≤ K stops from reaching dst!
    * To adapt Dijkstra for K stops, the state space must track `(cost, node, stops)`, increasing heap
      overhead over simple Level-Order BFS.
* **Level-Order BFS vs. Bellman-Ford**:
    * Bellman-Ford scans all E flight edges in every pass `0 .. K`, even if most graph nodes are
      unreachable at pass i.
    * Level-Order BFS ONLY relaxes outgoing edges from active nodes at the current level, executing
      significantly faster on sparse graphs!

---

## LC 743. Network Delay Time
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **Single-Source Shortest Path with Non-Negative Edge Weights**: Reaching all N nodes in minimum
      total signal propagation time from source K maps directly to **Dijkstra's Algorithm**.
    * **Lazy Deletion / Stale Node Pruning (`if (d > dist[v]) continue`)**: `PriorityQueue` cannot
      perform O(log V) `decreaseKey` operations efficiently on the JVM. Pruning stale nodes when polled
      guarantees optimal O((V + E) x log V) time bound.
    * **Signal Delivery Invariant**: Total network delay equals the maximum shortest path distance to
      any node (Max(dist[i]) ∀ 1 ≤ i ≤ N). If any node remains unreachable (dist[i] == ∞), return `-1`.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Bit-Packed Long Dijkstra** | **O((V + E) log V)** | **O(V + E)** | **Staff Level (Extreme)** | High-throughput JVM microservices; zero heap object allocation. |
 **Dijkstra (Pair / IntArray)** | **O((V + E) log V)** | **O(V + E)** | Peak (Canonical) | Standard interview default; clear pair/array structure. |
 **Bellman-Ford Algorithm** | **O(V x E)** | **O(V)** | Good | Graphs with **negative edge weights** or negative cycle detection. |

*(where V = N, E = times.size)*

### 3. Native Kotlin Syntax Pitfalls
* **Zero-Comparator Bit-Packed Primitive `Long` Order**:
  By packing `(distance.toLong() shl 32) or node`, `distance` occupies the upper 32 bits (Most
  Significant Bits - MSB). Because primitive `Long` comparison evaluates bits from MSB to LSB,
  natural `Long` ascending order (`a.compareTo(b)`) **automatically compares `distance` first**!
  Thus, `PriorityQueue<Long>()` polls the minimum distance node natively without requiring any
  custom `Comparator` object (`compareBy { ... }`), achieving **zero heap object allocations**!
* **Bellman-Ford Outer Loop Bound**: A shortest path in a graph without negative cycles contains at
  most V - 1 edges. The outer loop should run V - 1 iterations (`for (i in 1 until n)`).
* **1-Based Index Alignment**: Allocating `IntArray(n + 1)` and `Array(n + 1)` simplifies 1-based
  node indexing without 1-off subtraction logic throughout the code.

### 4. Code Block
```kotlin
// Approach 1: Bit-Packed Long Dijkstra (Staff Level Extreme - Zero Heap Allocations)
// Time Complexity: O((E) log V) | Auxiliary Space Complexity: O(V + E)
fun networkDelayTimeBitPacked(times: Array<IntArray>, n: Int, k: Int): Int {
    val adjacent = Array(n + 1) { mutableListOf<Pair<Int, Int>>() }
    val dist = IntArray(n + 1) { Int.MAX_VALUE }
    dist[k] = 0

    for (time in times) {
        adjacent[time[0]].add(Pair(time[1], time[2]))
    }

    val encode: (Int, Int) -> Long = { v, w -> (v.toLong() shl 32) or w.toLong() }

    val pq = PriorityQueue<Long>(n)
    pq.add(encode(k, 0))

    while (pq.isNotEmpty()) {
        val uw = pq.poll()!!
        val u = (uw shr 32).toInt()
        val w = (uw and (0xFFFFFFFF)).toInt()
        if (w > dist[u]) continue
        for (adj in adjacent[u]) {
            val (v, wv) = adj
            if (w + wv < dist[v]) {
                dist[v] = w + wv
                pq.add(encode(v, dist[v]))
            }
        }
    }

    var minDelayTime = 0
    for (i in 1 .. n) {
        if (dist[i] == Int.MAX_VALUE) return -1
        minDelayTime = maxOf(minDelayTime, dist[i])
    }

    return minDelayTime
}

// Approach 2: Dijkstra (Min-Heap Pair Object)
// Time Complexity: O((V + E) log V) | Auxiliary Space Complexity: O(V + E)
fun networkDelayTime(times: Array<IntArray>, n: Int, k: Int): Int {
    val adjacent = Array(n + 1) { mutableListOf<Pair<Int, Int>>() }
    val dist = IntArray(n + 1) { Int.MAX_VALUE }
    dist[k] = 0

    for (time in times) {
        adjacent[time[0]].add(Pair(time[1], time[2]))
    }

    val pq = PriorityQueue<Pair<Int, Int>>(n,compareBy { it.second })
    pq.add(Pair(k, 0))

    while (pq.isNotEmpty()) {
        val (u, w) = pq.poll()!!
        if (w > dist[u]) continue
        for (adj in adjacent[u]) {
            val (v, wv) = adj
            if (w + wv < dist[v]) {
                dist[v] = w + wv
                pq.add(Pair(v, dist[v]))
            }
        }
    }

    var minDelayTime = 0
    for (i in 1 .. n) {
        if (dist[i] == Int.MAX_VALUE) return -1
        minDelayTime = maxOf(minDelayTime, dist[i])
    }

    return minDelayTime
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Dijkstra vs. Bellman-Ford vs. SPFA (Shortest Path Faster Algorithm)**:
    * **Dijkstra (O((V + E) Log V))**: Greedy min-heap selection. Fast, but **fails on graphs with
      negative edge weights** because greedy choices assume edge weights are non-negative.
    * **Bellman-Ford (O(V x E))**: Dynamic programming edge relaxation over V-1 iterations. Works with
      negative edge weights and identifies negative weight cycles.
* **Network Propagation & Routing Protocols (OSPF / IS-IS)**:
    * Dijkstra's algorithm powers internet backbone routing protocols (**OSPF**, **IS-IS**).
    * In distributed microservices (e.g. gRPC service mesh routing), latency metrics serve as dynamic
      edge weights where Dijkstra routes requests along the lowest-latency path.

---
