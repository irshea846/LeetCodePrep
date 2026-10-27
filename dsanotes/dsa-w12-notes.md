# LeetCodePrep Week 12

A collection of LeetCode problem solutions implemented in Kotlin.

# ==================================================================
# WEEK 12: Advanced 2D DYNAMIC PROGRAMMING (October 26 - October 29)
# ==================================================================

## Topics Covered
- [ ] Advanced 2D DP
- [ ] Tail Recursion

## Day 58 - LC 64. Minimum Path Sum
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Grid Path Sum Overlapping Subproblems**: To reach cell (i, j), you must come from cell (i-1, j) 
  (top) or cell (i, j-1) (left). Thus, minPathSum(i, j) = grid[i][j] + min(minPathSum(i-1, j), 
  minPathSum(i, j-1)).
  * **Branchless Pre-filling Optimization**: Pre-filling row 0 and col 0 before entering the main 
  inner loop eliminates M * N inner `if-else` branch checks!
  * **Space Optimization (O(min(M, N)) Space)**: Maintaining a 1D DP table representing the active 
  row compresses memory down to O(min(M, N) without mutating the input matrix.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Branchless In-Place DP** | **O(M * N)** | **O(1)** | **Staff Level (Extreme)** | Auxiliary $O(1)$ memory; mutates input grid in-place. |
 **Branchless 1D DP** | **O(M * N)** | **O(min(M, N))** | **Staff Level (Optimal DP)** | Standard production choice; preserves input parameter immutability. |
 **2D DP Table** | **O(M * N)** | **O(M * N)** | Baseline | Standard interview default for demonstrating full 2D state transitions. |

*(where M = m, N = n)*

### 3. Native Kotlin Syntax Pitfalls
* **Branch Elimination**: Pre-filling row 0 (`for (j in 1 until cols) dp[j] = dp[j-1] + grid[0][j]`) 
and column 0 (`dp[0] += grid[i][0]`) removes all inner loop boundary checks (`if (i == 0 || j == 0)`).
* **Input Mutation Side Effects**: `grid[i][j] += minOf(...)` achieves O(1) space, but mutates caller 
parameter in-place. In multithreaded systems or shared data pipelines, prefer 1D DP with O(min(M, N))
space.

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (Branchless 1D Table - Staff Level Optimal)
// Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
fun minPathSum1D(grid: Array<IntArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0
    val rows = grid.size
    val cols = grid[0].size

    val dp = IntArray(cols)
    dp[0] = grid[0][0]
    for (j in 1 until cols) {
        dp[j] = dp[j - 1] + grid[0][j] // Branchless pre-fill row 0
    }

    for (i in 1 until rows) {
        dp[0] += grid[i][0] // Leftmost column transition
        for (j in 1 until cols) {
            dp[j] = minOf(dp[j], dp[j - 1]) + grid[i][j] // Branchless inner transition
        }
    }

    return dp[cols - 1]
}

// Approach 2: Bottom-Up Dynamic Programming (Branchless In-Place)
// Time Complexity: O(m * n) | Auxiliary Space Complexity: O(1)
fun minPathSumInPlace(grid: Array<IntArray>): Int {
    if (grid.isEmpty() || grid[0].isEmpty()) return 0
    val m = grid.size
    val n = grid[0].size

    // Branchless pre-fill row 0 and col 0
    for (j in 1 until n) grid[0][j] += grid[0][j - 1]
    for (i in 1 until m) grid[i][0] += grid[i - 1][0]

    for (i in 1 until m) {
        for (j in 1 until n) {
            grid[i][j] += minOf(grid[i - 1][j], grid[i][j - 1])
        }
    }

    return grid[m - 1][n - 1]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Branchless Execution vs. Inner Branch Checking**:
  * In the naive 1D / In-Place DP, `if (i == 0 && j == 0) ... else if (i == 0) ...` performs 4 branch 
  evaluations per cell (4 * M * N total conditional checks).
  * Pre-filling row 0 and column 0 upfront eliminates all inner branch checks, maximizing CPU instruction pipelining and cache throughput!
* **In-Place Mutation (O(1)) Space) vs. Parameter Immutability**:
  * In-place mutation saves memory by re-using `grid[i][j]`, but destroys input parameter state.
  * In production systems (e.g. gRPC services or Kotlin Coroutine worker threads), mutating shared 
  parameters creates subtle thread-safety bugs. 1D DP with O(min(M, N)) space is strictly preferred 
  when API immutability contracts must be honored.

---

## Day 57 - LC 62. Unique Paths
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Overlapping Subproblems Invariant**: To reach cell (i, j), you must come from cell (i - 1, j) (top) 
  or cell (i, j - 1) (left). Thus, uniquePaths(i, j) = uniquePaths(i - 1, j) + uniquePaths(i, j - 1).
  * **1D Space Optimization**: Since computing row i depends only on row i-1, we compress 2D memory 
  from O(M * N)$ down to O(min(M, N)) by maintaining a 1D DP table representing the active row.
  * **Combinatorics Closed-Form**: Since every path requires (m-1) Down moves and (n-1) Right moves 
  out of (m + n - 2) total moves, paths equal the combination C(m + n - 2, min(m - 1, n - 1)). This 
  reduces time to O(min(M, N)) and space to O(1).

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                            |
 :--- | :--- | :--- | :--- |:-----------------------------------------------------------------------------|
 **Zero-Allocation Primitive Math** | **O(min(M, N))** | **O(1)** | **Staff Level (Extreme)** | Unconstrained grid; zero heap allocations; O(1) constant memory.             |
 **BigInteger Combinatorics** | **O(min(M, N))** | **O(1)** | Peak | Prevents overflow on large calculations using `BigInteger` object instances. |
 **Branchless 1D DP** | **O(M * N)** | **O(min(M, N))** | **Staff Level (Optimal DP)** | Extensible to grids with obstacles (LC 63) or weights (LC 64).               |
 **Branchless 2D DP** | **O(M * N)** | **O(M * N)** | Baseline | Standard interview default for demonstrating full 2D state transitions.      |

*(where M = m, N = n)*

### 3. Native Kotlin Syntax Pitfalls
* **Interleaved Division vs. `BigInteger` Churn**: `result = result * (total - k + i) / i` using 64-bit 
primitive `Long` computes C(N, K) with **zero `BigInteger` heap object allocations**.
* **Orientation Space Optimization (`minOf`)**: Orienting the 1D DP table using `val min = minOf(m, n)` 
ensures space complexity is strictly O(min(M, N)) even if M > N.
* **Branchless DP Array Initialization (`IntArray(min) { 1 }`)**: Pre-populating the 1D DP array with 
1s eliminates M * N inner `if (i == 0 || j == 0)` conditional branch checks!

### 4. Code Block
```kotlin
// Approach 1: Zero-Allocation Primitive Combinatorics (Staff Level - Extreme)
// Time Complexity: O(min(m, n)) | Auxiliary Space Complexity: O(1)
fun uniquePathsMath(m: Int, n: Int): Int {
    val k = minOf(m - 1, n - 1)
    val total = m + n - 2
    var result = 1L

    for (i in 1..k) {
        result = result * (total - k + i) / i // Interleaved division prevents Long overflow
    }

    return result.toInt()
}

// Approach 2: BigInteger Combinatorics
// Time Complexity: O(min(m, n)) | Auxiliary Space Complexity: O(1)
fun uniquePathsCombinatorics(m: Int, n: Int): Int {
    val a = m + n - 2
    val b = maxOf(m, n) - 1
    val c = a - b

    var combo = 1.toBigInteger()
    for (i in a downTo b + 1) {
        combo = combo.times(i.toBigInteger())
    }

    for (j in c downTo 2) {
        combo = combo.div(j.toBigInteger())
    }

    return combo.toInt()
}

// Approach 3: Bottom-Up Dynamic Programming (1D Table Branchless)
// Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
fun uniquePaths1D(m: Int, n: Int): Int {
    val min = minOf(m, n)
    val dp = IntArray(min) { 1 }

    for (i in 1 until maxOf(m, n)) {
        for (j in 1 until min) {
            dp[j] += dp[j - 1]
        }
    }

    return dp[min - 1]
}

// Approach 4: Bottom-Up Dynamic Programming (2D Table Branchless)
// Time Complexity: O(m * n) | Auxiliary Space Complexity: O(m * n)
fun uniquePaths2D(m: Int, n: Int): Int {
    val dp = Array(m) { IntArray(n) { 1 } }

    for (i in 1 until m) {
        for (j in 1 until n) {
            dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
        }
    }

    return dp[m - 1][n - 1]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Dynamic Programming vs. Closed-Form Combinatoric Math**:
  * **1D Dynamic Programming (O(M * N) time, O(min(M, N)) space)**: Standard production choice when 
  grid dimensions are small/medium (M, N <= 100). Highly extensible to variations like **LC 63 (Unique 
  Paths II with Obstacles)** or **LC 64 (Minimum Path Sum with Weighted Cells)** where mathematical 
  combinatorics fails due to broken grid symmetry.
  * **Combinatorics (O(min(M, N) time, O(1) space)**: Optimal choice for unconstrained grids. Computes 
  **C(m + n - 2, min(m - 1, n - 1))** directly in O(min(M, N)) time.
* **Interleaved Division vs. `BigInteger` Heap Allocations**:
  * Factorials grow faster than exponential functions (N!). Multiplying **N ... (N-1) ...** before 
  dividing by K! causes `Long` overflow.
  * Interleaving multiplication and division (`result = result * (total - k + i) / i`) keeps 
  intermediate values small, enabling pure primitive `Long` computation with **zero `BigInteger` 
  heap object allocations**.
* **Extension to D-Dimensional Grid Navigation**:
  * In a D-dimensional grid with dimensions N_1, N_2, ..., N_D, total moves = ∑ (N_i - 1) for i = 1 
  to D.
  * The total unique paths formula generalizes to the **Multinomial Coefficient**:
    ( ∑ (N_i - 1) )! / ∏ ( (N_i - 1)! )   for i = 1 to D

---
