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
