# LeetCodePrep Week 15

A collection of LeetCode problem solutions implemented in Kotlin.

# ==================================================================
# WEEK 15: Graphs + DP Synthesis (November 16 - November 20)
# ==================================================================

## Topics Covered
- [ ] 2D Grid DAG Topological Sort
- [ ] Shortest Path + DP State Accumulation
- [ ] Multi-Source BFS vs. Two-Pass 2D Dynamic Programming.
- [ ] Tree/Graph State DP
- [ ] State-Space Shortest Path

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

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Top-Down Memoized DFS (Static Direction)** | **O(M * N)** | **O(M * N)** call stack | **Staff Level (Optimal DFS)** | Standard production choice; visits each cell $(r, c)$ at most once; optimal $\mathcal{O}(M \cdot N)$ time. |
 **Out-Degree BFS Topological Sort (Kahn's)** | **O(M * N)** | **O(M * N)** queue | **Staff Level (Stack-Safe)** | 100% `StackOverflowError` safe; models level-by-level topological DAG peeling. |

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
