# LeetCodePrep Week 9 Mastery & Review Day

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 9: GRAPHS (October 3 - October 4)
# ===============================================================

## LC 133. Clone Graph
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Graph Deep Copying with Cycles**: Cloning a general graph (containing cycles, self-loops, and 
  undirected/directed edges) requires tracking mapping from original nodes to cloned nodes 
  (`HashMap<Node, Node>`) to prevent infinite recursion and duplicate node creation.
  * **Memoized Mapping Invariant**: Instantiating a cloned node and putting it in `map[v] = clone` 
  **before** making recursive calls or pushing to queue guarantees cycle resolution.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **BFS (Single Pass Iterative)** | **O(V + E)** | **O(V)** queue + map | **Staff Level (Optimal)** | 100% StackOverflow safe; connects cloned neighbor edges in a single pass. |
 **DFS (Memoized Recursion)** | **O(V + E)** | **O(V)** stack + map | **Peak (Canonical)** | Standard interview choice; clean recursive tree/graph traversal. |

### 3. Native Kotlin Syntax Pitfalls
* **Single-Lookup Null-Coalescing (`map[v]?.let { return it }`)**: Avoid calling `contains(v)` 
followed by `get(v)` (double hash map lookup). Using `map[v]?.let { return it }` performs a single 
𝒪(1) lookup.
* **Safe Null Check vs Force Assertions (`!!`)**: In BFS, retrieve `val clonedNode = map[currNode]!!`
once or use `map[adj]?.let { clonedNode.neighbors.add(it) }` to avoid force assertion (`!!`) warnings.
* **Escaped Keyword Identifier (`` `val` ``)**: Accessing the reserved keyword property `val` on 
`Node` requires backticks (`` `val` ``).

### 4. Code Block
```kotlin
// Approach 1: DFS (Single Lookup Memoization)
// Time Complexity: O(V + E) | Auxiliary Space Complexity: O(V) stack
class Node(var `val`: Int) {
    var neighbors: ArrayList<Node?> = ArrayList<Node?>()
}

fun cloneGraphDFS(node: Node?): Node? {
    if (node == null) return null
    val map = HashMap<Node, Node>()

    fun clone(v: Node): Node {
        map[v]?.let { return it } // Single map lookup

        val clonedNode = Node(v.`val`)
        map[v] = clonedNode // Map before traversing neighbors to handle cycles

        for (adj in v.neighbors) {
            adj?.let {
                clonedNode.neighbors.add(clone(it))
            }
        }
        return clonedNode
    }
    return clone(node)
}

// Approach 2: BFS (Single Pass Edge Wiring)
// Time Complexity: O(V + E) | Auxiliary Space Complexity: O(V)
fun cloneGraphBFS(node: Node?): Node? {
    if (node == null) return null
    val map = HashMap<Node, Node>()
    val dq = ArrayDeque<Node>()

    val cloneHead = Node(node.`val`)
    map[node] = cloneHead
    dq.addLast(node)

    while (dq.isNotEmpty()) {
        val currNode = dq.removeFirst()
        val clonedNode = map[currNode]!!

        for (adj in currNode.neighbors) {
            if (adj == null) continue
            if (adj !in map) {
                map[adj] = Node(adj.`val`)
                dq.addLast(adj)
            }
            clonedNode.neighbors.add(map[adj]) // Wire cloned edge in single pass
        }
    }
    return cloneHead
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Two-Pass BFS vs. Single-Pass BFS**:
  * Two-pass BFS enqueues all nodes in pass 1 and connects edges in pass 2. Single-pass BFS connects 
  cloned neighbor edges immediately when examining neighbors, reducing hash map accesses by 50%.
* **Identity-Based Hashing (`java.util.IdentityHashMap`) vs. Standard `HashMap`**:
  * Standard `HashMap` uses `hashCode()` and `equals()`. If `Node` overrides `equals()` to check 
  only `val`, two distinct nodes with `node.val = 1` would collide!
  * **Staff Insight**: Real-world object cloning frameworks (Jackson, Kryo, Java Serialization) use 
  **`IdentityHashMap`**, which uses `System.identityHashCode()` and `==` reference equality to uniquely 
  identify instances regardless of property values.

---

## LC 743. Network Delay Time
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Single-Source Shortest Path with Non-Negative Edge Weights**: Finding the minimum time to reach 
  all **N** nodes from source **K** maps directly to **Dijkstra's Algorithm**.
  * **Lazy Deletion / Stale Node Pruning (`currDist > dist[currNode]`)**: PriorityQueue cannot perform 
  **O(log V)** `decreaseKey` operations efficiently on JVM. Pruning stale nodes when polled 
  (`currDist > dist[currNode]`) guarantees optimal **O((V + E) Log V)** time bound.
  * **Signal Delivery Invariant**: Total network delay equals the maximum shortest path distance to 
  any node (max(dist[1...N])). If any node remains unreachable (dist[i] == ∞), return `-1`.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Dijkstra (Bit-Packed Long)** | **O((V + E) log V)** | **O(V + E)** | **Staff Level (Zero GC)** | High-throughput JVM microservices; zero heap object allocation. |
 **Dijkstra (Pair / IntArray)** | **O((V + E) log V)** | **O(V + E)** | Peak (Canonical) | Standard interview default; clear pair/array structure. |
 **Bellman-Ford Algorithm** | **O(V x E)** | **O(V)** | Good | Graphs with **negative edge weights** or negative cycle detection. |

### 3. Native Kotlin Syntax Pitfalls
* **Comparator Subtraction Integer Overflow**: Direct subtraction `{ a, b -> a.first - b.first }` 
causes integer overflow when distance values span `Int.MAX_VALUE`. Always use **`compareBy { it.first }`** 
or `a.first.compareTo(b.first)`.
* **Bit Packing Capacity Bounds**: Bit packing 32-bit `Int` coordinates into 16 bits (`0xFFFF`) 
limits distance values to **65,535**. For Dijkstra, packing `(distance shl 32) or node` into a primitive 
**`Long`** supports full 32-bit distances (≤ 2 × 10⁹) and natural ascending `Long` sorting without 
a custom comparator!
* **Graph Initial Capacity Over-Allocation**: Writing `ArrayList<T>(n + 1)` for every graph list 
attempts to allocate N² initial slots on sparse graphs. Always use `mutableListOf<T>()`.

### 4. Code Block
```kotlin
// Approach 1: Bit-Packed Long Dijkstra (Staff Level - Zero Heap Allocations)
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
    val pq = PriorityQueue<Long>() // Natural ascending Long order
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

// Approach 2: PriorityQueue Dijkstra (Pair Object)
// Time Complexity: O((V + E) log V) | Auxiliary Space Complexity: O(V + E)
fun networkDelayTimePair(times: Array<IntArray>, n: Int, k: Int): Int {
    val dist = IntArray(n + 1) { Int.MAX_VALUE }
    val graph = Array(n + 1) { mutableListOf<Pair<Int, Int>>() }
    val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })

    for (time in times) {
        val u = time[0]
        val v = time[1]
        val w = time[2]
        graph[u].add(Pair(w, v))
    }

    dist[k] = 0
    pq.add(Pair(0, k))

    while (pq.isNotEmpty()) {
        val (currDist, currNode) = pq.poll()!!
        if (currDist > dist[currNode]) continue

        for (pair in graph[currNode]) {
            val (adjWeight, adjNode) = pair
            val newDist = currDist + adjWeight
            if (newDist < dist[adjNode]) {
                dist[adjNode] = newDist
                pq.add(Pair(newDist, adjNode))
            }
        }
    }

    var maxDistance = Int.MIN_VALUE
    for (i in 1..n) {
        maxDistance = maxOf(maxDistance, dist[i])
    }

    return if (maxDistance == Int.MAX_VALUE) -1 else maxDistance
}

// Approach 3: Bellman-Ford Algorithm (O(V * E))
fun networkDelayTimeBellmanFord(times: Array<IntArray>, n: Int, k: Int): Int {
    val dist = IntArray(n + 1) { Int.MAX_VALUE }
    dist[k] = 0

    for (i in 1 until n) {
        var updated = false
        for (time in times) {
            val u = time[0]
            val v = time[1]
            val w = time[2]
            if (dist[u] != Int.MAX_VALUE && dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w
                updated = true
            }
        }
        if (!updated) break // Early convergence exit
    }

    var maxTime = 0
    for (j in 1..n) {
        if (dist[j] == Int.MAX_VALUE) return -1
        maxTime = maxOf(maxTime, dist[j])
    }

    return maxTime
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Dijkstra vs. Bellman-Ford vs. SPFA**:
  * **Dijkstra**: Greedy min-heap selection (𝒪((V + E) * log V)). Optimal for non-negative weights; 
  fails on negative edges.
  * **Bellman-Ford**: Dynamic programming relaxation over **V-1** iterations (O((V * E)). Handles 
  negative weights and identifies negative cycles.
* **Network Routing Protocols (OSPF / IS-IS)**:
  * Dijkstra's algorithm powers internet backbone routing protocols (**OSPF**, **IS-IS**).
  * In service mesh ingress/egress load balancers (Envoy / Istio), latency metrics serve as dynamic 
  edge weights where Dijkstra selects lowest-latency microservice routes.

---

## LC 207. Course Schedule
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Directed Acyclic Graph (DAG) Cycle Detection**: Course prerequisites form directed edges `prev 
  -> post`. Finding if all courses can be finished is equivalent to detecting whether the directed 
  graph contains a cycle.
  * **DFS Tricolor Graph States (0/1/2)**: 0 = Unvisited, 1 = Visiting (in current recursion call 
  stack), 2 = Visited (verified cycle-free component). Re-encountering State 1 during traversal 
  indicates a back-edge (cycle).
  * **BFS In-Degree Topological Sort (Kahn's Algorithm)**: Enqueuing courses with `inDegree == 0`. 
  Processing nodes iteratively by decrementing neighbor in-degrees; if `processedCount == numCourses`, 
  the graph is a valid DAG.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **BFS (Kahn's Algorithm)** | **O(V + E)** | **O(V + E)** queue + adj list | **Staff Level (Optimal)** | Guarantees no `StackOverflowError`; parallel task scheduling; models build systems (Gradle/Bazel). |
 **DFS (3-Color State)** | **O(V + E)** | **O(V + E)** stack + adj list | **Peak (Canonical)** | Standard interview default; fast recursive cycle detection via back-edges. |

### 3. Native Kotlin Syntax Pitfalls
* **Over-Allocating Initial Capacity**: Writing `Array(numCourses) { ArrayList<Int>(numCourses) }` 
allocates an array of size **V** for every node. For **V = 10,000**, this attempts to pre-allocate 
*10^8* integers (≈ 400 MB heap)! Always use `Array(numCourses) { mutableListOf<Int>() }`.
* **FIFO Queue vs LIFO Stack (`removeFirst()` vs `removeLast()`)**: Using `dq.removeFirst()` on 
`ArrayDeque` ensures true FIFO queue ordering (essential when outputting standard level-order 
topological sequences in LC 210).
* **Direct Index Access**: Direct index access `prerequisite[0]` / `prerequisite[1]` is faster than 
destructuring `val (post, prev) = prerequisite`.

### 4. Code Block
```kotlin
// Approach 1: DFS (3-Color State Tracking)
// Time Complexity: O(V + E) | Auxiliary Space Complexity: O(V + E)
fun canFinishDFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    val graph = Array(numCourses) { mutableListOf<Int>() }
    val triStates = IntArray(numCourses) // 0 = Unvisited, 1 = Visiting, 2 = Visited

    for (prerequisite in prerequisites) {
        val prev = prerequisite[1]
        val post = prerequisite[0]
        graph[prev].add(post)
    }

    fun hasCycle(course: Int): Boolean {
        if (triStates[course] == 1) return true  // Back-edge detected (cycle)
        if (triStates[course] == 2) return false // Already verified cycle-free

        triStates[course] = 1 // Mark visiting
        for (postCourse in graph[course]) {
            if (hasCycle(postCourse)) return true
        }
        triStates[course] = 2 // Mark visited
        return false
    }

    for (i in triStates.indices) {
        if (triStates[i] == 0) {
            if (hasCycle(i)) return false
        }
    }

    return true
}

// Approach 2: BFS Kahn's Algorithm (Topological Sort)
// Time Complexity: O(V + E) | Auxiliary Space Complexity: O(V + E)
fun canFinishBFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    var totalCourses = 0
    val inDegree = IntArray(numCourses)
    val graph = Array(numCourses) { mutableListOf<Int>() }

    for (prerequisite in prerequisites) {
        val prev = prerequisite[1]
        val post = prerequisite[0]
        graph[prev].add(post)
        inDegree[post]++
    }

    val dq = ArrayDeque<Int>(numCourses)

    for (i in inDegree.indices) {
        if (inDegree[i] == 0) {
            dq.addLast(i)
        }
    }

    while (dq.isNotEmpty()) {
        val course = dq.removeFirst() // Standard FIFO Queue behavior
        totalCourses++

        for (post in graph[course]) {
            if (--inDegree[post] == 0) {
                dq.addLast(post)
            }
        }
    }

    return numCourses == totalCourses
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **DFS Recursion Depth vs. Kahn's BFS Safety**:
  * On a deep linear graph (0 → 1 → 2 → ... → V-1), DFS recursion call stack reaches depth **V**. 
  For **V = 10⁵**, this will throw a JVM `StackOverflowError`.
  * Kahn's BFS operates iteratively using an explicit `ArrayDeque`, guaranteeing **100% StackOverflow 
  safety**.
* **Parallel Task Schedulers (Gradle, Bazel, Airflow)**:
  * Kahn's algorithm models real-world build systems and DAG orchestrators. At any step, all nodes 
  in `dq` with `inDegree == 0` have satisfied their prerequisites and **can be executed concurrently 
  in parallel across worker nodes/threads**!
* **Extension to LC 210 (Course Schedule II)**:
  * Record each polled course into an output array (`order[index++] = course`). If `totalCourses == 
  numCourses`, return `order`; otherwise return `intArrayOf()`.

---

## LC 200. Number of Islands
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Counting Connected Components in an Undirected Graph**: Canonical graph traversal problem 
  solvable via DFS, BFS, or DSU (Disjoint Set Union).
  * **In-Place Traversal State Marking ("Sinking Islands")**: Mutating visited land cells from 
  `'1'` to `'0'` eliminates the need for an **O(M * N)** auxiliary `visited` matrix.
  * **DFS / BFS Traversal Trigger**: Iterating through every cell $(r, c)$ in the matrix; whenever 
  `'1'` is encountered, increment island count and kick off a traversal to "sink" all connected cells.
  * **Bit Packing Coordinates (`(i shl 16) or j`)**: Packs two 16-bit integer coordinates into a 
  single 32-bit primitive `Int`. Decoded using `x = encode ushr 16` (unsigned shift right) and 
  `y = encode and 0xFFFF` (masking lower 16 bits).

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **BFS (Bit Packing)** | **O(M * N)** | **O(min(M, N))** | **Peak (Staff Level)** | Large grids ($N > 90,000$); zero heap object allocations & 100% `StackOverflowError` safe. |
 **DFS (In-Place)** | **O(M * N)** | **O(M * N)** | Good | Standard interview default; fast implementation for small to medium grids. |
 **DSU (Union-Find)** | **O(M * N * α(N))** | **O(M * N)** | **Peak (Staff Level)** | Dynamic streaming of land cells; MapReduce / parallel grid tile processing. |

### 3. Native Kotlin Syntax Pitfalls
* **Range Checks (`r !in grid.indices` / `r !in 0 until rows`)**: Avoid non-idiomatic `r > grid.size
- 1` or `r < 0`. Kotlin's `r !in grid.indices` (`r !in 0 until m`) and `c !in grid[0].indices` 
- (`c !in 0 until n`) are idiomatic, clean, and bounds-safe.
* **Encapsulating Recursion Scope**: Defining `dfs` as a **nested local function** inside the entry 
function avoids polluting the package/class API and captures `m`, `n`, and `grid` from the outer scope.
* **Unsigned Bit Shift (`ushr`)**: Prefer `encode ushr 16` over `shr 16` during bit decoding to 
prevent sign-extension corruption.

### 4. Code Block
```kotlin
fun numIslandsDFS(grid: Array<CharArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0
    var islands = 0
    val m = grid.size
    val n = grid[0].size

    val neighbors = arrayOf(intArrayOf(0, -1), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(1, 0))
    fun dfs(x: Int, y: Int) {
        if (x !in 0 until m || y !in 0 until n || grid[x][y] == '0') {
            return
        }
        grid[x][y] = '0'
        for (neighbor in neighbors) {
            val adjX = x + neighbor[0]
            val adjY = y + neighbor[1]
            dfs(adjX, adjY)
        }
    }

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == '1') {
                islands++
                dfs(i, j)
            }
        }
    }

    return islands
}

fun numIslandsBFS(grid: Array<CharArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0

    val neighbors = arrayOf(intArrayOf(0, -1), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(1, 0))
    var islands = 0
    val m = grid.size
    val n = grid[0].size
    val capacity = kotlin.math.min(m, n)
    val dq = ArrayDeque<Int>(capacity)

    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == '1') {
                islands++
                grid[i][j] = '0'
                dq.addLast((i shl 16) or j)

                while (dq.isNotEmpty()) {
                    val encode = dq.removeFirst()
                    val x = encode ushr 16
                    val y = encode and 0xFFFF
                    for (neighbor in neighbors) {
                        val adjX = x + neighbor[0]
                        val adjY = y + neighbor[1]
                        if (adjX !in 0 until m || adjY !in 0 until n || grid[adjX][adjY] == '0') {
                            continue
                        }
                        grid[adjX][adjY] = '0'
                        dq.addLast((adjX shl 16) or adjY)
                    }
                }
            }
        }
    }
    return islands
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Zero-Allocation Bit Packing vs. Object BFS**:
  * Standard BFS enqueues `IntArray(2)` or `Pair(x, y)` objects into the queue. On a **300 x 300** 
  grid, this instantiates tens of thousands of short-lived objects on the JVM Heap, inducing 
  Young Generation Garbage Collection (GC) pauses.
  * Bit packing encodes two 16-bit row/col coordinates into a single 32-bit primitive `Int` 
  (`(i shl 16) or j`). Using `ArrayDeque<Int>` stores primitive integers in a flat array, achieving 
  **zero heap object allocations** during BFS traversal and maximizing CPU L1/L2 cache line locality.
* **DFS Call Stack Overflow vs. BFS Bounded Queue**:
  * In a worst-case "snake grid" of size **M x N = 300 x 300 = 90,000** cells, DFS recursion stack 
  depth reaches $90,000$ call frames, causing a JVM `StackOverflowError`.
  * **BFS** uses an explicit queue. The maximum queue size is bounded by the grid's diagonal/perimeter 
  (O(min(M, N))), making BFS strictly safer for large grids.
* **In-Place Mutation Side-Effects vs. Input Read-Only Contract**:
  * Mutating `grid[r][c] = '0'` destroys the input array. In real-world multi-threaded systems or 
  shared data pipelines, destroying input parameter state without caller consent is a severe bug.
  * If input immutability is required, allocate a `visited: Array<BooleanArray>` or a flat `BitSet` 
  (M * N bits) at the cost of **O(M * N)** space.
* **Distributed Processing with Disjoint Set Union (DSU / Union-Find)**:
  * For massive satellite image processing distributed across MapReduce / Spark worker nodes, the 
  grid is partitioned into tiles. Each worker computes connected components locally, and **DSU** 
  merges component sets across tile boundaries in near-constant **O(α(V))** amortized time.

---
