# LeetCodePrep Week 14

A collection of LeetCode problem solutions implemented in Kotlin.

# ==================================================================
# WEEK 14: Advanced Graphs  (November 9 - November 14)
# ==================================================================

## Topics Covered
- [ ] Dijkstra
- [ ] Bellman-Ford
- [ ] Union-Find
- [ ] Minimum Spanning Trees

## Day 68 - LC 743. Network Delay Time
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
* **Heap Object Allocation (`Pair<Int, Int>`)**: Enqueuing `Pair(weight, node)` instantiates 
short-lived heap objects for every edge relaxation step. Packing `(weight shl 32) or node` into a 
primitive `Long` achieves **zero heap object allocations** during Dijkstra traversal.
* **Bellman-Ford Outer Loop Bound**: A shortest path in a graph without negative cycles contains at 
most V - 1 edges. The outer loop should run V - 1 iterations (`for (i in 1 until n)`).
* **1-Based Index Alignment**: Allocating `IntArray(n + 1)` and `Array(n + 1)` simplifies 1-based 
node indexing without 1-off subtraction logic throughout the code.

### 4. Code Block
```kotlin
// Approach 1: Bit-Packed Long Dijkstra (Staff Level Extreme - Zero Heap Allocations)
// Time Complexity: O((V + E) log V) | Auxiliary Space Complexity: O(V + E)
fun networkDelayTimeBitPacking(times: Array<IntArray>, n: Int, k: Int): Int {
    val dist = IntArray(n + 1) { Int.MAX_VALUE }
    val graph = Array(n + 1) { mutableListOf<Long>() }

    fun encode(d: Int, v: Int): Long {
        return (d.toLong() shl 32) or (v.toLong() and 0xFFFFFFFFL)
    }

    for (time in times) {
        val u = time[0]
        val v = time[1]
        val w = time[2]
        graph[u].add(encode(w, v))
    }

    dist[k] = 0
    val pq = PriorityQueue<Long>() // Natural ascending Long order (Zero Comparator Heap)
    pq.add(encode(0, k))

    while (pq.isNotEmpty()) {
        val code = pq.poll()!!
        val currDist = (code ushr 32).toInt()
        val currNode = (code and 0xFFFFFFFFL).toInt()

        if (currDist > dist[currNode]) continue // Lazy deletion pruning

        for (edge in graph[currNode]) {
            val adjWeight = (edge ushr 32).toInt()
            val adjNode = (edge and 0xFFFFFFFFL).toInt()
            val newDist = currDist + adjWeight
            if (newDist < dist[adjNode]) {
                dist[adjNode] = newDist
                pq.add(encode(newDist, adjNode))
            }
        }
    }

    var maxDistance = Int.MIN_VALUE
    for (i in 1..n) {
        maxDistance = maxOf(maxDistance, dist[i])
    }

    return if (maxDistance == Int.MAX_VALUE) -1 else maxDistance
}

// Approach 2: Dijkstra (Min-Heap Pair Object)
// Time Complexity: O((V + E) log V) | Auxiliary Space Complexity: O(V + E)
fun networkDelayTimeDijkstra(times: Array<IntArray>, n: Int, k: Int): Int {
    val adj = Array(n + 1) { mutableListOf<Pair<Int, Int>>() }
    val dist = IntArray(n + 1) { Int.MAX_VALUE }
    dist[k] = 0

    for (time in times) {
        val v = time[0]
        val u = time[1]
        val w = time[2]
        adj[v].add(Pair(w, u))
    }

    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })
    pq.add(Pair(0, k))

    while (pq.isNotEmpty()) {
        val (d, v) = pq.poll()!!
        if (d > dist[v]) continue
        for (adjacent in adj[v]) {
            val (w, u) = adjacent
            if (w + d < dist[u]) {
                dist[u] = w + d
                pq.add(Pair(dist[u], u))
            }
        }
    }

    var minDist = 0
    for (i in 1..n) {
        if (dist[i] == Int.MAX_VALUE) return -1
        minDist = maxOf(minDist, dist[i])
    }

    return minDist
}

// Approach 3: Bellman-Ford Algorithm
// Time Complexity: O(V * E) | Auxiliary Space Complexity: O(V)
fun networkDelayTimeBellmanFord(times: Array<IntArray>, n: Int, k: Int): Int {
    val dp = IntArray(n + 1) { Int.MAX_VALUE }
    dp[k] = 0

    for (i in 1 until n) { // Run V - 1 relaxation passes
        var anyUpdate = false
        for (time in times) {
            val v = time[0]
            val u = time[1]
            val w = time[2]
            if (dp[v] != Int.MAX_VALUE && dp[v] + w < dp[u]) {
                dp[u] = dp[v] + w
                anyUpdate = true
            }
        }

        if (!anyUpdate) break
    }

    var minDist = 0
    for (i in 1..n) {
        if (dp[i] == Int.MAX_VALUE) return -1
        minDist = maxOf(minDist, dp[i])
    }

    return minDist
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