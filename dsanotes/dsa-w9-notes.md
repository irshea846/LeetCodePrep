# LeetCodePrep Week 9

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 9: GRAPHS (September 29 - October 3)
# ===============================================================

## Topics Covered
- [ ] DFS
- [ ] BFS
- [ ] Topological Sort
- [ ] Dijkstra’s Algorithm

## Day 43 - LC 743. Network Delay Time
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Single-Source Shortest Path with Non-Negative Edge Weights**: Reaching all **N** nodes in minimum 
  total signal propagation time maps directly to Dijkstra's Algorithm on a directed weighted graph.
  * **Lazy Deletion / Stale Node Pruning (`currentWeight > dist[curNode]`)**: PriorityQueue cannot 
  perform 𝒪(log V) `decreaseKey` operations efficiently on JVM. Pruning stale nodes when polled 
  (`currentWeight > dist[curNode]`) ensures optimal 𝒪((V+E) log V) performance.
  * **Signal Delivery Invariant**: The total network delay time equals the maximum shortest distance 
  among all 1 ... N nodes (max_{1 ≤ i ≤ N} dist[i]). If any node remains unreachable (dist[i] == ∞),
  return `-1`.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Dijkstra (Min-Heap / PriorityQueue)** | **O((V + E) log V)** | **O(V + E)** Heap + Adj List | **Staff Level (Optimal)** | Non-negative edge weights; canonical shortest path solution. |
 **Bellman-Ford Algorithm** | **O(V x E)** | **O(V)** | Dynamic / Edge-List | Graphs with **negative edge weights**; detects negative weight cycles. |
 **Floyd-Warshall Algorithm** | **O(V^3)** | **O(V^2)** | All-Pairs Matrix | Small graphs (V ≤ 500) requiring distance matrix for all node pairs. |

### 3. Native Kotlin Syntax Pitfalls
* **Heap Object Allocation (`Pair<Int, Int>`)**: Enqueuing `Pair(weight, node)` instantiates 
short-lived heap objects for every edge relaxation step. For high-throughput systems, bit-packing 
`(weight shl 32) or node` into a `Long` or using `IntArray(2)` achieves zero heap object allocations.
* **1-Based Index Alignment**: Nodes are numbered 1 ~ N. Allocating `IntArray(n + 1)` and 
`Array(n + 1)` simplifies indexing and eliminates **1**-off subtraction logic throughout the code.
* **Non-Null Assertion (`poll()!!`)**: While `pq.isNotEmpty()` guards against empty polls, using 
destructuring inside loops (`val (currDist, currNode) = pq.poll()!!`) can be cleaned using idiomatic 
Kotlin or primitive packing.

### 4. Code Block
```kotlin
// Approach 1: PriorityQueue Dijkstra (Min-Heap Lazy Deletion)
// Time Complexity: O((V + E) log V) | Auxiliary Space Complexity: O(V + E)
fun networkDelayTime(times: Array<IntArray>, n: Int, k: Int): Int {
    val adjMap = Array(n + 1) { _ -> mutableListOf<Pair<Int, Int>>() }
    val dist = IntArray(n + 1) { Int.MAX_VALUE }
    dist[k] = 0

    for (time in times) {
        val (u, v, w) = time
        adjMap[u].add(Pair(w, v)) // Pair(weight, adjacent_node)
    }

    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })
    pq.add(Pair(0, k))

    while (pq.isNotEmpty()) {
        val (currentWeight, curNode) = pq.poll()!!
        if (currentWeight > dist[curNode]) continue // Prune stale heap entries

        for (adjacent in adjMap[curNode]) {
            val (adjWeight, adjNode) = adjacent
            val newWeight = currentWeight + adjWeight
            if (newWeight < dist[adjNode]) {
                dist[adjNode] = newWeight
                pq.add(Pair(dist[adjNode], adjNode))
            }
        }
    }

    var maxWeight = Int.MIN_VALUE
    for (i in 1..n) {
        maxWeight = max(maxWeight, dist[i])
    }

    return if (maxWeight == Int.MAX_VALUE) -1 else maxWeight
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Dijkstra vs. Bellman-Ford vs. SPFA (Shortest Path Faster Algorithm)**:
  * **Dijkstra**: Greedy selection via Min-Heap. Fast 𝒪((V+E) log V), but **fails on graphs with 
  negative edge weights** (greedy choice assumption breaks).
  * **Bellman-Ford**: Dynamic programming edge-relaxation over **V-1** iterations (O(V * E)). Works 
  with negative edge weights and detects negative weight cycles.
* **Network Propagation & Routing Protocols**:
  * Dijkstra's algorithm powers **Open Shortest Path First (OSPF)** and **IS-IS** interior gateway 
  routing protocols in computer networks.
  * In distributed microservices (e.g. gRPC service mesh routing), latency metrics are dynamically 
  weighted edges where Dijkstra routes requests along the lowest-latency network path.

---

## Day 42 - LC 207. Course Schedule
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Directed Acyclic Graph (DAG) Cycle Detection**: Dependencies represented as directed edges 
  `prev -> post` require determining whether the graph contains a directed cycle.
  * **DFS Tricolor Graph States (0/1/2)**: State 0 = Unvisited, State 1 = Visiting (currently in 
  recursion call stack), State 2 = Visited (verified cycle-free DAG component). Encountering State 1
  during traversal signals a back-edge (cycle).
  * **BFS In-Degree Topological Sort (Kahn's Algorithm)**: Processing nodes with `inDegree == 0` 
  iteratively. If the number of processed nodes equals `numCourses`, the graph is a valid DAG.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **BFS (Kahn's Algorithm)** | **O(V + E)** | **O(V + E)** queue + adj list | **Staff Level (Optimal)** | Prevent `StackOverflowError`; parallel task scheduling; models build systems (Gradle/Bazel). |
 **DFS (3-Color State)** | **O(V + E)** | **O(V + E)** stack + adj list | **Peak (Canonical)** | Standard interview choice; fast recursive cycle detection via back-edges. |

### 3. Native Kotlin Syntax Pitfalls
* **FIFO Queue vs LIFO Stack (`removeFirst()` vs `removeLast()`)**:
  * Using `dq.removeLast()` on `ArrayDeque` creates LIFO Stack behavior (DFS order).
  * For Kahn's Algorithm BFS, use **`dq.removeFirst()`** (FIFO Queue behavior) to maintain standard 
  level-by-level topological order (critical for LC 210 Course Schedule II).
* **Lambda Allocation Overhead in `forEach`**: Avoid `graphs[curr].forEach { ... }` in tight graph 
traversal loops to eliminate iterator/lambda heap allocations. Prefer `for (neighbor in graphs[curr])`.
* **Destructuring Performance (`val (post, prev) = prerequisite`)**: Destructuring arrays calls 
`component1()` and `component2()`. Direct index access `prerequisite[0]` / `prerequisite[1]` is faster.

### 4. Code Block
```kotlin
// Approach 1: DFS (3-Color State Tracking)
// Time Complexity: O(V + E) | Auxiliary Space Complexity: O(V + E)
fun canFinishDFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    val courseStates = IntArray(numCourses) // 0 = Unvisited, 1 = Visiting, 2 = Visited
    val graphs = Array(numCourses) { mutableListOf<Int>() }

    for (prerequisite in prerequisites) {
        val post = prerequisite[0]
        val prev = prerequisite[1]
        graphs[prev].add(post)
    }

    fun hasCycle(curr: Int): Boolean {
        if (courseStates[curr] == 1) return true  // Back-edge detected (cycle)
        if (courseStates[curr] == 2) return false // Already verified cycle-free

        courseStates[curr] = 1 // Mark visiting
        for (next in graphs[curr]) {
            if (hasCycle(next)) return true
        }
        courseStates[curr] = 2 // Mark visited
        return false
    }

    for (i in 0 until numCourses) {
        if (courseStates[i] == 0) {
            if (hasCycle(i)) return false
        }
    }

    return true
}
```
```kotlin
// Approach 2: BFS Kahn's Algorithm (Topological Sort)
// Time Complexity: O(V + E) | Auxiliary Space Complexity: O(V + E)
fun canFinishBFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    val inDegree = IntArray(numCourses)
    val graphs = Array(numCourses) { mutableListOf<Int>() }
    var totalCourses = 0
    val dq = ArrayDeque<Int>(numCourses)

    for (prerequisite in prerequisites) {
        val post = prerequisite[0]
        val prev = prerequisite[1]
        graphs[prev].add(post)
        inDegree[post]++
    }

    for (i in 0 until numCourses) {
        if (inDegree[i] == 0) {
            dq.addLast(i)
        }
    }

    while (dq.isNotEmpty()) {
        val course = dq.removeFirst() // Standard FIFO Queue behavior
        totalCourses++
        for (neighbor in graphs[course]) {
            if (--inDegree[neighbor] == 0) {
                dq.addLast(neighbor)
            }
        }
    }

    return totalCourses == numCourses
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **DFS Call Stack Overflow vs. Kahn's BFS Iteration**:
  * On a deep linear graph (0 → 1 → 2 → … → V-1), DFS recursion depth reaches V 
  frames. For V = 10⁵, this risks a JVM `StackOverflowError`.
  * Kahn's BFS is **100% StackOverflow safe** because it processes nodes iteratively using an explicit 
  `ArrayDeque`.
* **Parallel / Distributed Dependency Execution**:
  * Kahn's algorithm models real-world task schedulers (Gradle, Bazel, DAG orchestrators like Airflow).
  * At any BFS step, all nodes in `dq` with `inDegree == 0` have satisfied their prerequisites and 
  **can be executed concurrently in parallel across worker nodes/threads**!
* **Extension to LC 210 (Course Schedule II)**:
  * To output the valid topological ordering, record each polled course in an array 
  * (`order[index++] = course`). If `totalCourses == numCourses`, return `order`; otherwise return 
  `intArrayOf()`.

---

## Day 41 - LC 200. Number of Islands
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **2D Grid Connected Components**: Treating a 2D matrix as an implicit graph where land cells 
  (`'1'`) are nodes connected to up to 4 orthogonal neighbors (up, down, left, right).
  * **In-Place Traversal State Marking ("Sinking Islands")**: Mutating visited land cells from 
  `'1'` to `'0'` eliminates the need for an **O(M x N)** auxiliary `visited` matrix.
  * **DFS / BFS Traversal Trigger**: Iterating through every cell $(r, c)$ in the matrix; whenever 
  `'1'` is encountered, increment island count and kick off a traversal to "sink" all connected land
  cells.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **DFS (In-Place Mutate)** | **O(M x N)** | **O(M x N)** recursion stack | **Peak (Canonical)** | Standard interview implementation; fast writing; small grid sizes. |
 **BFS (Queue Iterative)** | **O(M x N)** | **O(min(M, N))** queue space | **Staff Level (Optimal)** | Guarantees no `StackOverflowError`; bounded space proportional to min matrix dimension. |
 **BFS (Bit Packing)** | **O(M x N)** | **O(min(M, N))** primitive queue | **Staff Level (Zero GC)** | Eliminates heap object allocations; maximizes CPU cache locality. |
 **Disjoint Set Union (DSU)** | **O(M x N x α(N))** | **O(M x N)** | Distributed / Dynamic | Dynamic stream of land cells; MapReduce / parallel grid chunking. |

### 3. Native Kotlin Syntax Pitfalls
* **Range Checks (`r !in 0 until rows`)**: Avoid non-idiomatic `r > grid.size - 1` or `r < 0`. 
Kotlin's `r !in 0 until rows` or `(r !in 0 until rows)` is idiomatic and clean.
* **Encapsulating Recursion Scope**: Defining `dfs` as a **nested local function** inside 
`numIslands` avoids polluting the public package/class API and captures `rows`, `cols`, and `grid` 
without passing redundant outer state parameters.
* **Bit Packing Coordinates (`(i shl 16) or j`)**: Packs two 16-bit integer coordinates into a single 
32-bit primitive `Int`. Decoded using `x = encode ushr 16` (unsigned shift right) and `y = encode and 0xFFFF` 
(masking lower 16 bits).
* **Array Index Out of Bounds Guards**: Always validate dimensions 
(`if (grid.isEmpty() || grid[0].isEmpty()) return 0`) before accessing `grid[0].size`.

### 4. Code Block
```kotlin
// Approach 1: In-Place DFS (Nested Local Function)
// Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N) stack
fun numIslands(grid: Array<CharArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0

    val rows = grid.size
    val cols = grid[0].size
    var islands = 0

    fun dfs(r: Int, c: Int) {
        if ((r !in 0 until rows) || (c !in 0 until cols) || (grid[r][c] == '0')) {
            return
        }

        grid[r][c] = '0' // Sink visited land cell
        dfs(r + 1, c)
        dfs(r - 1, c)
        dfs(r, c + 1)
        dfs(r, c - 1)
    }

    for (r in 0 until rows) {
        for (c in 0 until cols) {
            if (grid[r][c] == '1') {
                islands++
                dfs(r, c)
            }
        }
    }

    return islands
}
```
```kotlin
// Approach 2: Queue-Based BFS (StackOverflow Safe & Bounded Space)
// Time Complexity: O(M * N) | Auxiliary Space Complexity: O(min(M, N))
fun numIslandsBFS(grid: Array<CharArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0

    val rows = grid.size
    val cols = grid[0].size
    var islands = 0
    val dirs = arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(0, -1))

    for (r in 0 until rows) {
        for (c in 0 until cols) {
            if (grid[r][c] == '1') {
                islands++
                grid[r][c] = '0' // Sink immediately upon queue push
                val queue = ArrayDeque<IntArray>()
                queue.addLast(intArrayOf(r, c))

                while (queue.isNotEmpty()) {
                    val (currR, currC) = queue.removeFirst()
                    for (dir in dirs) {
                        val nr = currR + dir[0]
                        val nc = currC + dir[1]
                        if (nr in 0 until rows && nc in 0 until cols && grid[nr][nc] == '1') {
                            grid[nr][nc] = '0' // Sink before push to prevent duplicate queuing
                            queue.addLast(intArrayOf(nr, nc))
                        }
                    }
                }
            }
        }
    }

    return islands
}
```

```kotlin
// Approach 3: Queue-Based BFS (Bit Packing Technique - Zero Heap Allocation)
// Time Complexity: O(M * N) | Auxiliary Space Complexity: O(min(M, N))
fun numIslandsBitPacking(grid: Array<CharArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0

    val neighbors = arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(0, -1))
    var islands = 0
    val m = grid.size
    val n = grid[0].size
    val capacity = min(m, n)
    val dq = ArrayDeque<Int>(capacity)

    for (i in 0 until m) {
        for (j in 0 until n) {
            if (grid[i][j] == '0') continue
            grid[i][j] = '0'
            islands++
            dq.addLast((i shl 16) or j)

            while (dq.isNotEmpty()) {
                val encode = dq.removeFirst()
                val x = encode ushr 16
                val y = encode and 0xFFFF

                for (neighbor in neighbors) {
                    val adjx = x + neighbor[0]
                    val adjy = y + neighbor[1]
                    if (adjx !in 0 until m || adjy !in 0 until n || grid[adjx][adjy] == '0') {
                        continue
                    }
                    grid[adjx][adjy] = '0'
                    dq.addLast((adjx shl 16) or adjy)
                }
            }
        }
    }
    return islands
}
```
### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Zero-Allocation Bit Packing vs. Object BFS**:
  * Standard BFS enqueues `IntArray(2)` or `Pair(x, y)` objects into the queue. On a **300 x 300** grid, 
  this instantiates tens of thousands of short-lived objects on the JVM Heap, inducing Young Generation 
  Garbage Collection (GC) pauses.
  * Bit packing encodes two 16-bit row/col coordinates into a single 32-bit primitive `Int` (`(i shl 16) or j`). 
  Using `ArrayDeque<Int>` stores primitive integers in a flat array, achieving **zero heap object 
  allocations** during BFS traversal and maximizing CPU L1/L2 cache line locality.
* **DFS Call Stack Overflow vs. BFS Bounded Queue**:
  * In a worst-case "snake grid" of size **M x N = 300 x 300 = 90,000** cells, DFS recursion stack 
  depth reaches $90,000$ call frames, causing a JVM `StackOverflowError`.
  * **BFS** uses an explicit queue. The maximum queue size is bounded by the grid's diagonal/perimeter 
  (𝒪(min(M, N))), making BFS strictly safer for large grids.
* **In-Place Mutation Side-Effects vs. Input Read-Only Contract**:
  * Mutating `grid[r][c] = '0'` destroys the input array. In real-world multi-threaded systems or 
  shared data pipelines, destroying input parameter state without caller consent is a severe bug.
  * If input immutability is required, allocate a `visited: Array<BooleanArray>` or a flat `BitSet` 
  (M × N bits) at the cost of 𝒪(M × N) space.
* **Distributed Processing with Disjoint Set Union (DSU / Union-Find)**:
  * For massive satellite image processing distributed across MapReduce / Spark worker nodes, the 
  grid is partitioned into tiles. Each worker computes connected components locally, and **DSU** 
  merges component sets across tile boundaries in near-constant 𝒪(α(V)) amortized time.

---
