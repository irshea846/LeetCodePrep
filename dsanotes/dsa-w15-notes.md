# LeetCodePrep Week 15

A collection of LeetCode problem solutions implemented in Kotlin.

# ==================================================================
# WEEK 15: Graphs + DP Synthesis (November 16 - November 20)
# ==================================================================

## Topics Covered
- [ ] Kahn's 2D Grid DAG Topological Sort
- [ ] Dijkstra + DP State Accumulation
- [ ] Multi-Source BFS vs. Two-Pass 2D Dynamic Programming.
- [ ] Tree/Graph State DFS DP
- [ ] State-Space Shortest Path

## Day 78 - LC 847. Shortest Path Visiting All Nodes
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Shortest Path Visiting All Nodes (State-Space BFS)**: N ≤ 12 nodes, find the shortest path that 
  visits every node at least once.
  * **Bitmask State Representation**:
    - State is defined by `(currNode, visitedMask)`.
    - `visitedMask` is a bitmask of length N where the i-th bit is 1 if node i has been visited.
    - Target state: `visitedMask == (1 shl N) - 1` (all N bits set).
  * **Multi-Source BFS Initialization**:
    - Start BFS from ALL N nodes simultaneously with `dist = 0` and initial mask `1 shl i`.
    - Level-order BFS guarantees that the first state reaching `targetMask` is the global shortest path!

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Bit-Packed 1D Queue BFS** | **O(N² x 2ᴺ)** | **O(N x 2ᴺ)** | **Staff Level (Extreme)** | Zero heap object allocations; flat primitive `IntArray` queue & visited array. |
 **Standard Queue BFS (`ArrayDeque<Triple>`)** | **O(N² x 2ᴺ)** | **O(N x 2ᴺ)** | Peak (Canonical) | Standard interview default; explicit `Triple(node, mask, steps)` state queue. |

*(where N = graph.size ≤ 12)*

### 3. Native Kotlin Syntax Pitfalls
* **Triple Object Allocation Churn**: Enqueuing `Triple(currNode, mask, steps)` instantiates up to
`12 x 2^12 = 49,152` objects on the heap. Bit-packing `(node shl 12) or mask` into a single primitive 
`Int` eliminates 100% of heap object allocations during BFS!
* **1D Visited Array Alignment**: `val visited = BooleanArray(N shl 12)` flatly indexes state 
`(node shl 12) or mask`, enabling O(1) primitive array lookup.

### 4. Code Block
```kotlin
// Approach 1: Bit-Packed Primitive Int BFS (Staff Level Extreme - 1D Array allocation)
// Time Complexity: O(N² x 2ᴺ) | Auxiliary Space Complexity: O(N x 2ᴺ)
fun shortestPathLengthBitPacking(graph: Array<IntArray>): Int {
    val n = graph.size
    if (n == 1) return 0
    val visited = BooleanArray(n shl 12)
    val targetMask = (1 shl n) - 1

    var head = 0
    var tail = 0
    val q = IntArray(n * (1 shl n))

    for (i in 0 until n) {
        val state = (i shl 12) or (1 shl i)
        visited[state] = true
        q[tail++] = state
    }

    var steps = 0
    while (head < tail) {
        val wave = tail - head
        for (i in 0 until wave) {
            val state = q[head++]
            val u = state ushr 12
            val currMask = state and 0xFFF
            for (v in graph[u]) {
                val nextMask = currMask or (1 shl v)
                if (nextMask == targetMask) return steps + 1
                val nextState = (v shl 12) or nextMask
                if (!visited[nextState]) {
                    visited[nextState] = true
                    q[tail++] = nextState
                }
            }
        }
        steps++
    }

    return steps
}

// Approach 2: Standard BFS (Staff Level Optimal - 2D Array Allocations)
// Time Complexity: O(N² x 2ᴺ) | Auxiliary Space Complexity: O(N x 2ᴺ)
fun shortestPathLengthBFS(graph: Array<IntArray>): Int {
    val n = graph.size
    val visited = Array(n) { BooleanArray(1 shl n) }
    val targetMask = (1 shl n) - 1

    val q = ArrayDeque<Triple<Int, Int, Int>>()

    for (i in 0 until n) {
        val initialMask = 1 shl i
        visited[i][initialMask] = true
        q.addLast(Triple(i, initialMask, 0))
    }

    while (q.isNotEmpty()) {
        val (currNode, currMask, steps) = q.removeFirst()
        for (adj in graph[currNode]) {
            val nextMask = currMask or (1 shl adj)
            if (nextMask == targetMask) return steps + 1
            if (!visited[adj][nextMask]) {
                visited[adj][nextMask] = true
                q.addLast(Triple(adj, nextMask, steps + 1))
            }
        }
    }

    return 0
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **State-Space BFS Invariant**:
  * Why standard shortest path algorithms (Dijkstra/BFS) on simple node graph fail: In LC 847, nodes 
  CAN be revisited!
  * By augmenting state from `node` to `(node, visitedMask)`, visiting a node with a *different subset 
  of previously visited nodes* is treated as a distinct state in a state-space graph with N x 2^N 
  vertices.
* **Multi-Source BFS vs. Single-Source BFS**:
  * If we start BFS from node 0 only, we might miss a shorter path starting from node k.
  * Enqueuing all N nodes simultaneously at step 0 explores all possible starting vertices concurrently, 
  guaranteeing global minimum path length.

---

## Day 77 - LC 337. House Robber III
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Tree Post-Order Dynamic Programming Tuple `(robThisNode, skipThisNode)`**:
    - Adjacent tree nodes cannot be robbed.
    - For each subtree root:
      1. `robThisNode`: node.val + skipLeft + skipRight (must skip children).
      2. `skipThisNode`: max(robLeft, skipLeft) + max(robRight, skipRight) 
      (can choose best option for each child).
  * **Zero Heap Allocation Bit-Packing**:
    - Packing `(robThisNode shl 32) or skipThisNode` into a primitive `Long` enables returning both 
    subproblem answers in a single 64-bit integer, achieving **zero heap object allocations** during 
    post-order tree traversal.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Bit-Packed Primitive `Long` DFS** | **O(N)** | **O(H)** call stack | **Staff Level (Extreme)** | High-throughput JVM services; zero heap array/pair object allocations. |
 **Post-Order DFS (`IntArray(2)`)** | **O(N)** | **O(H)** call stack | Peak (Canonical) | Standard interview default; returns tuple array `IntArray(2)`. |

*(where N = tree nodes, H = tree height)*

### 3. Native Kotlin Syntax Pitfalls
* **Bitwise Shift Operator Precedence**: `shl` has lower precedence than `+` or `-`, but higher than
`or`. Always parenthesize bitwise bit packing expressions: 
        `(totalRobSum shl 32) or (totalUnrobSum and 0xFFFFFFFFL)` 
to prevent logical operator evaluation bugs!
* **Unsigned Bit Shift (`ushr`)**: Use `code ushr 32` (unsigned shift right) when extracting the 
upper 32-bit `robThisNode` value to avoid negative sign extension.

### 4. Code Block
```kotlin
// Approach 1: Bit-Packed Primitive Long DFS (Staff Level Extreme - Zero Heap Allocations)
// Time Complexity: O(N) | Auxiliary Space Complexity: O(H)
fun robBitPacking(root: TreeNode?): Int {

    fun dfs(node: TreeNode?): Long {
        if (node == null) return 0L

        val leftCode = dfs(node.left)
        val rightCode = dfs(node.right)

        val totalRobSum = node.`val`.toLong() + (leftCode and 0xFFFFFFFFL) + (rightCode and 0xFFFFFFFFL)
        val totalUnrobSum = maxOf(leftCode ushr 32, leftCode and 0xFFFFFFFFL) + maxOf(rightCode ushr 32, rightCode and 0xFFFFFFFFL)

        return (totalRobSum shl 32) or (totalUnrobSum and 0xFFFFFFFFL)
    }

    val code = dfs(root)
    return maxOf((code ushr 32).toInt(), (code and 0xFFFFFFFFL).toInt())
}

// Approach 2: Top-Down DFS (Staff Level Optimal - Peak Canonical - IntArray(2))
// Time Complexity: O(N) | Auxiliary Space Complexity: O(H)
fun robDFS(root: TreeNode?): Int {

    fun dfs(node: TreeNode?): IntArray {
        if (node == null) return intArrayOf(0, 0)

        val left = dfs(node.left)
        val right = dfs(node.right)

        return intArrayOf(node.`val` + left[1] + right[1], maxOf(left[0], left[1]) + maxOf(right[0], right[1]))
    }

    val (robSum, unrobSum) = dfs(root)
    return maxOf(robSum, unrobSum)
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **House Robber Progression (LC 198 → LC 213 → LC 337)**:
  * **LC 198 (1D Array)**: Linear DP with scalar state variables `(prev1, prev2)` in O(N) time, 
  O(1) space.
  * **LC 213 (Circular Array)**: First & last houses adjacent ⇒ solve two linear 
  subproblems `rob(0..n-2)` and `rob(1..n-1)` in O(N) time, O(1) space.
  * **LC 337 (Binary Tree)**: Tree state DP post-order traversal in O(N) time, O(H) call stack space.
* **Bit Packing vs. Heap Allocation**:
  * Standard tree DP returns `IntArray(2)` or `Pair(rob, unrob)` for every node, allocating N heap 
  objects.
  * Bit packing encodes `robThisNode` in upper 32 bits and `skipThisNode` in lower 32 bits of a 
  primitive `Long`, achieving **zero heap object allocations**.

---

## Day 76 - LC 542. 01 Matrix
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **2D Grid Manhattan Distance to Nearest Zero**: Finding the shortest distance from every cell 
  (r, c) to the nearest `0` in a 2D matrix.
  * **Two-Pass 2D Dynamic Programming Invariant (O(M x  N) Time, O(1) Space)**:
    - Distance to nearest `0` can be decomposed into Top-Left moves and Bottom-Right moves.
    - **Pass 1 (Top-Left to Bottom-Right Pass)**: 
        `dist[r][c] = min(dist[r][c], dist[r-1][c] + 1, dist[r][c-1] + 1)`
    - **Pass 2 (Bottom-Right to Top-Left Pass)**: 
        `dist[r][c] = \min(dist[r][c], dist[r+1][c] + 1, dist[r][c+1] + 1)`.
  * **Multi-Source BFS (O(M x  N) Time, O(M x  N) Space)**:
    - Enqueue all `0` cells simultaneously with distance 0 into a BFS queue.
    - Expand level-by-level using a primitive bit-packed queue (`(r shl 16) or c`).

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Two-Pass 2D DP** | **O(M * N)** | **O(1)** aux space | **Staff Level (Optimal Memory)** | $100\%$ `StackOverflowError` safe; zero queue allocations; flat linear 2-pass matrix scan. |
 **Multi-Source BFS (Bit-Packed)** | **O(M * N)** | **O(M * N)** | **Staff Level (Zero GC BFS)** | Standard shortest path level-order expansion; zero heap object allocations via primitive bit-packing. |

*(where $M = \text{rows}, N = \text{cols}$)*

### 3. Native Kotlin Syntax Pitfalls
* **Boundary Guard Sentinel (`INF = 10,000`)**: Initializing $1$ cells to `10_000` (or `m + n`) avoids 32-bit Integer overflow during `+ 1` distance operations (unlike `Int.MAX_VALUE`).
* **Branchless Pass Guard Conditions**: Checking `if (r > 0)` and `if (c > 0)` in Pass 1, and `if (r < m - 1)` and `if (c < n - 1)` in Pass 2 eliminates complex nested `if-else if-else` branching.

### 4. Code Block
```kotlin
// Approach 1: Two-Pass 2D Dynamic Programming (Staff Level Optimal - Clean Boundary Guards)
// Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
fun updateMatrix(mat: Array<IntArray>): Array<IntArray> {
    val m = mat.size
    val n = mat[0].size
    val INF = 10_000
    val dist = Array(m) { r -> IntArray(n) { c -> if (mat[r][c] == 0) 0 else INF } }

    // Pass 1: Top-Left to Bottom-Right (Check Top and Left neighbors)
    for (r in 0 until m) {
        for (c in 0 until n) {
            if (mat[r][c] == 0) continue
            if (r > 0) dist[r][c] = minOf(dist[r][c], dist[r - 1][c] + 1)
            if (c > 0) dist[r][c] = minOf(dist[r][c], dist[r][c - 1] + 1)
        }
    }

    // Pass 2: Bottom-Right to Top-Left (Check Bottom and Right neighbors)
    for (r in m - 1 downTo 0) {
        for (c in n - 1 downTo 0) {
            if (mat[r][c] == 0) continue
            if (r < m - 1) dist[r][c] = minOf(dist[r][c], dist[r + 1][c] + 1)
            if (c < n - 1) dist[r][c] = minOf(dist[r][c], dist[r][c + 1] + 1)
        }
    }

    return dist
}

// Approach 2: Multi-Source BFS (Bit-Packed Primitive Int Queue - Zero Heap Allocations)
// Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
fun updateMatrixMultiSourceBFS(mat: Array<IntArray>): Array<IntArray> {
    val m = mat.size
    val n = mat[0].size
    val dist = Array(m) { IntArray(n) { -1 } }
    val queue = ArrayDeque<Int>() // Bit-packed coordinate (r shl 16) or c

    // Enqueue all 0 cells simultaneously
    for (r in 0 until m) {
        for (c in 0 until n) {
            if (mat[r][c] == 0) {
                dist[r][c] = 0
                queue.addLast((r shl 16) or c)
            }
        }
    }

    val dirs = intArrayOf(0, 1, 0, -1, 0)
    while (queue.isNotEmpty()) {
        val code = queue.removeFirst()
        val r = code ushr 16
        val c = code and 0xFFFF

        for (k in 0 until 4) {
            val nr = r + dirs[k]
            val nc = c + dirs[k + 1]
            if (nr in 0 until m && nc in 0 until n && dist[nr][nc] == -1) {
                dist[nr][nc] = dist[r][c] + 1
                queue.addLast((nr shl 16) or nc)
            }
        }
    }

    return dist
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Two-Pass 2D DP vs. Multi-Source BFS**:
  * **Two-Pass 2D DP**: Executes two linear matrix scans. Requires zero queue memory allocations and 
  operates with 100\% cache locality.
  * **Multi-Source BFS**: Expands shortest distances level-by-level outward from all zeros. Best when 
  distances are small or target matrix is dynamically generated.

---

## Day 75 - LC 1976. Number of Ways to Arrive at Destination
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Shortest Path + DP Combination State Accumulation**: Finding the number of paths to node n-1 
  with minimum total travel time.
  * **State Accumulation Invariants**:
    - As Dijkstra relaxes edges u → v with weight w:
      - If `d + w < dist[v]`: Strict improvement found! Reset `dist[v] = d + w`, inherit 
      `ways[v] = ways[u]`, and enqueue `(dist[v], v)`.
      - If `d + w == dist[v]`: Alternative shortest path to v found! Accumulate 
      `ways[v] = (ways[v] + ways[u]) % MOD`. (Do NOT re-enqueue `(dist[v], v)`).
  * **Zero Heap Allocation Bit-Packing**: Packing `(distance shl 16) or node` into a primitive `Long` 
  enables `PriorityQueue<Long>()` natural ascending comparison without `Comparator` or `LongArray` 
  object allocations.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Bit-Packed Long Dijkstra + DP** | **O((V + E) log V)** | **O(V + E)** | **Staff Level (Extreme)** | High-throughput JVM microservices; zero heap object allocation. |
 **Object Dijkstra + DP (`LongArray`)** | **O((V + E) log V)** | **O(V + E)** | Peak (Canonical) | Standard interview default; explicit `LongArray` heap entries. |

*(where V = n, E = roads.size)*

### 3. Native Kotlin Syntax Pitfalls
* **Bit Packing Capacity Bounds & Overflow Protection**:
  In LC 1976, individual edge weights are up to 10^9, and cumulative shortest distances `dist[v]` 
  can reach `200 x 10^9 = 2 x 10^11`!
  If we shift distance left by 32 bits (`w shl 32`), any distance > 2^31 - 1 ≅ 2.14 x 10^9 will 
  overflow the upper 32 bits of a 64-bit `Long`!
  **Bit Allocation**: Since node v ⩽ 200, node v requires only 16 bits (`0xFFFFL`). Shifting distance 
  left by 16 bits (`w shl 16`) allocates **48 bits for cumulative distance w**, supporting path 
  lengths up to 2.8 x 10^14 without 64-bit integer overflow!
* **Avoid Re-enqueuing on Equal Distance**: When `newTime == dist[v]`, only update 
        `ways[v] = (ways[v] + ways[u]) % MOD` 
  Re-enqueuing v to PriorityQueue creates redundant graph explorations and slows execution!

### 4. Code Block
```kotlin
// Approach 1: Bit-Packed Primitive Long Dijkstra + DP (Staff Level Extreme - Zero Heap Allocations)
// Time Complexity: O((V + E) log V) | Auxiliary Space Complexity: O(V + E)
// Bit Allocation: 16 bits for node v (v <= 200 < 65,536), 48 bits for cumulative distance w (up to 2.8 * 10^14)
fun countPathsBitPacking(n: Int, roads: Array<IntArray>): Int {
    val MOD = 1_000_000_007
    val dist = LongArray(n) { Long.MAX_VALUE }
    val ways = IntArray(n)
    dist[0] = 0L
    ways[0] = 1

    val graph = Array(n) { mutableListOf<Long>() }

    fun encode(w: Long, v: Int): Long {
        return (w shl 16) or (v.toLong() and 0xFFFFL)
    }

    for (road in roads) {
        val u = road[0]
        val v = road[1]
        val w = road[2].toLong()
        graph[u].add(encode(w, v))
        graph[v].add(encode(w, u))
    }

    val pq = PriorityQueue<Long>() // Natural ascending Long order (Zero Comparator Heap)
    pq.add(encode(0L, 0))

    while (pq.isNotEmpty()) {
        val code = pq.poll()!!
        val d = code ushr 16
        val u = (code and 0xFFFFL).toInt()

        if (d > dist[u]) continue // Lazy deletion pruning

        for (edge in graph[u]) {
            val w = edge ushr 16
            val v = (edge and 0xFFFFL).toInt()
            val newTime = d + w

            if (newTime < dist[v]) {
                dist[v] = newTime
                ways[v] = ways[u]
                pq.add(encode(newTime, v))
            } else if (newTime == dist[v]) {
                ways[v] = (ways[v] + ways[u]) % MOD
            }
        }
    }

    return ways[n - 1]
}

// Approach 2: Dijkstra's Algorithm + Heap (Priority Queue) + DP
// Time Complexity: O(E x Log(V)) | Auxiliary Space Complexity: O(V + E)
fun countPaths(n: Int, roads: Array<IntArray>): Int {
    val MOD = 1_000_000_007L
    val currMinTime = LongArray(n) { Long.MAX_VALUE }
    val ways = LongArray(n)
    currMinTime[0] = 0L
    ways[0] = 1L
    val adjacent = Array(n) { mutableListOf<LongArray>() }

    for (road in roads) {
        adjacent[road[0]].add(longArrayOf(road[2].toLong(), road[1].toLong()))
        adjacent[road[1]].add(longArrayOf(road[2].toLong(), road[0].toLong()))
    }

    val pq =
        PriorityQueue<LongArray>(if (roads.isEmpty()) 1 else roads.size, compareBy { it[0] })
    pq.add(longArrayOf(0L, 0))

    while (pq.isNotEmpty()) {
        val src = pq.poll()!!
        val d = src[0]
        val u = src[1].toInt()
        if (d > currMinTime[u]) continue
        for (adj in adjacent[u]) {
            val w = adj[0]
            val v = adj[1].toInt()
            val time = d + w
            if (time < currMinTime[v]) {
                currMinTime[v] = time
                ways[v] = ways[u]
                pq.add(longArrayOf(time, v.toLong()))
            } else if (time == currMinTime[v]){
                ways[v] = (ways[v] + ways[u]) % MOD
            }
        }
    }

    return ways[n - 1].toInt()
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Dijkstra DP State Accumulation Invariant**:
  * Dijkstra guarantees that nodes are finalized in non-decreasing order of their shortest distance 
  from source 0.
  * When `dist[v]` is finalized, all shortest paths to v have already been accumulated into `ways[v]`.
  * Therefore, when v relaxes an edge to neighbor, `ways[v]` contains the complete count of shortest 
  paths to v, allowing `ways[neighbor]` to inherit or accumulate `ways[v]` correctly!
* **Zero Heap Allocation Bit Packing (`(weight shl 16) or target_node`)**:
  * For `N ⩽ 200, E ⩽ 20,000`, standard `Pair(d, v)` or `LongArray` instantiates tens of thousands 
  of objects.
  * Bit packing encodes distance in the upper 48 bits and target node in lower 16 bits into a single 
  primitive `Long`, achieving **zero heap object allocations** during graph traversal.

---

## Day 74 - LC 329. Longest Increasing Path in a Matrix
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Strictly Increasing Path Invariant (2D Grid DAG)**:
    Because paths must strictly increase (`matrix[nr][nc] > matrix[r][c]`), directed edges ONLY flow 
    from smaller values to larger values, making cycles mathematically IMPOSSIBLE!
    The matrix forms an implicit **Directed Acyclic Graph (DAG)**.
    * **Top-Down Memoization DP**:
    Each cell **(r, c)** has a well-defined longest increasing path `DP[r][c]` equal to 
    `1 + max_(nr, nc) DP[nr][nc]` over all strictly larger orthogonal neighbors.
  * **Kahn's Out-Degree Topological Sort (Peeling Sinks)**:
    Cells with `outDegree == 0` (no strictly larger neighbors) are sinks (path length 1). Peeling 
    sinks level-by-level using BFS computes the longest path length in exact BFS levels!

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                                        |
 :--- | :--- | :--- | :--- |:-----------------------------------------------------------------------------------------|
 **Top-Down Memoized DFS (Static Direction)** | **O(M * N)** | **O(M * N)** call stack | **Staff Level (Optimal DFS)** | Standard production choice; visits each cell (r, c) at most once; optimal O(M x N) time. |
 **Out-Degree BFS Topological Sort (Kahn's)** | **O(M * N)** | **O(M * N)** queue | **Staff Level (Stack-Safe)** | 100% `StackOverflowError` safe; models level-by-level topological DAG peeling.           |

*(where M = rows, N = cols)*

### 3. Native Kotlin Syntax Pitfalls
* **Avoid Nested Direction Array Object Churn**: Writing 
        `val adjacent = arrayOf(intArrayOf(0, 1), ...)` 
inside function calls instantiates `IntArray(2)` objects on every traversal. Use a static 1D 
direction vector `val DIRS = intArrayOf(0, 1, 0, -1, 0)` with `DIRS[k]` and `DIRS[k+1]` to achieve 
**zero array object allocations**.
* **Bit-Packing Grid Coordinates**: Packing `(r shl 16) or c` encodes 2D matrix coordinates into a 
primitive `Int`, storing queue entries in a flat `ArrayDeque<Int>` with zero object allocations.

### 4. Code Block
```kotlin
// Approach 1: Top-Down Memoized DFS (Staff Level Optimal - Static Direction Vector)
// Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
fun longestIncreasingPath(matrix: Array<IntArray>): Int {
    if (matrix.isEmpty() || matrix[0].isEmpty()) return 0
    val m = matrix.size
    val n = matrix[0].size
    val dp = Array(m) { IntArray(n) }
    val dirs = intArrayOf(0, 1, 0, -1, 0) // Static 1D direction vector (Zero Array Object Allocation)

    fun dfs(r: Int, c: Int): Int {
        if (dp[r][c] != 0) return dp[r][c]

        var maxLen = 1
        val currVal = matrix[r][c]

        for (k in 0 until 4) {
            val nr = r + dirs[k]
            val nc = c + dirs[k + 1]
            if (nr in 0 until m && nc in 0 until n && matrix[nr][nc] > currVal) {
                maxLen = maxOf(maxLen, 1 + dfs(nr, nc))
            }
        }

        dp[r][c] = maxLen
        return maxLen
    }

    var globalMaxLen = 0
    for (r in 0 until m) {
        for (c in 0 until n) {
            globalMaxLen = maxOf(globalMaxLen, dfs(r, c))
        }
    }

    return globalMaxLen
}

// Approach 2: Out-Degree Topological Sort BFS (Kahn's Algorithm - StackOverflow Safe)
// Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
fun longestIncreasingPathTopologicalBFS(matrix: Array<IntArray>): Int {
    if (matrix.isEmpty() || matrix[0].isEmpty()) return 0
    val m = matrix.size
    val n = matrix[0].size
    val outDegree = Array(m) { IntArray(n) }
    val dirs = intArrayOf(0, 1, 0, -1, 0)

    // Step 1: Compute out-degrees for every cell (count neighbors with strictly GREATER value)
    for (r in 0 until m) {
        for (c in 0 until n) {
            for (k in 0 until 4) {
                val nr = r + dirs[k]
                val nc = c + dirs[k + 1]
                if (nr in 0 until m && nc in 0 until n && matrix[nr][nc] > matrix[r][c]) {
                    outDegree[r][c]++
                }
            }
        }
    }

    // Step 2: Enqueue all sink cells (outDegree == 0) into BFS queue
    val queue = ArrayDeque<Int>() // Bit-packed coordinate (r shl 16) or c
    for (r in 0 until m) {
        for (c in 0 until n) {
            if (outDegree[r][c] == 0) {
                queue.addLast((r shl 16) or c)
            }
        }
    }

    // Step 3: Level-order BFS peeling
    var pathLength = 0
    while (queue.isNotEmpty()) {
        pathLength++
        val size = queue.size
        for (i in 0 until size) {
            val code = queue.removeFirst()
            val r = code ushr 16
            val c = code and 0xFFFF

            for (k in 0 until 4) {
                val nr = r + dirs[k]
                val nc = c + dirs[k + 1]
                // Decrement out-degree of smaller neighbors (matrix[nr][nc] < matrix[r][c])
                if (nr in 0 until m && nc in 0 until n && matrix[nr][nc] < matrix[r][c]) {
                    if (--outDegree[nr][nc] == 0) {
                        queue.addLast((nr shl 16) or nc)
                    }
                }
            }
        }
    }

    return pathLength
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Top-Down Memoized DFS vs. Out-Degree Topological BFS**:
  * **Top-Down DFS (O(M * N) time, O(M * N) space)**: Evaluates subproblems on-demand. Simple to 
  implement, but recursion stack depth can reach M * N call frames on long monotonic paths.
  * **Out-Degree Topological BFS (O(M * N) time, O(M * N) space)**: Peel sink nodes level-by-level 
  iteratively. **100% `StackOverflowError` safe** because it uses an explicit queue.
* **Why Matrix Strict Monotonicity Prevents Cycles**:
  * Every edge u → v requires `matrix[v] > matrix[u]`.
  * In any path v_1 → v_2 → ... → v_k, `matrix[v_1] < matrix[v_2] < ... < matrix[v_k]`.
  * Since `matrix[v_k] > matrix[v_1]`, v_k can never connect back to v_1, mathematically proving 
  that 2D grid strictly increasing paths form a **DAG**!

---
