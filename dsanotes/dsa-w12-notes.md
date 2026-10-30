# LeetCodePrep Week 12

A collection of LeetCode problem solutions implemented in Kotlin.

# ==================================================================
# WEEK 12: Advanced 2D DYNAMIC PROGRAMMING (October 26 - October 29)
# ==================================================================

## Topics Covered
- [ ] Advanced 2D DP
- [ ] Tail Recursion

## Day 60 - LC 97. Interleaving String
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Length Parity Pre-check Invariant**: **s3** can be formed by interleaving **s1** and **s2** 
  ONLY IF **s3.length == s1.length + s2.length**. If lengths do not match, return `false` in **O(1)** 
  time immediately.
  * **2D Grid Interleaving Path Transitions**: Cell **(i, j)** in 2D DP indicates whether prefix
    **s3[0 ... i + j - 1]** can be formed by interleaving **s1[0 ... i - 1]** and **s2[0 ... j-1]**:
    **DP[i][j] = (DP[i-1][j] && s1[i-1] == s3[i+j-1]) || (DP[i][j-1] && s2[j-1] == s3[i+j-1])**
  * **Early Row Short-Circuiting (`rowHasValidPath`)**: In 1D DP, if an entire row vector becomes 
  all `false`, no valid path can ever cross this row to reach **(M, N)**. Returning `false` early 
  short-circuits the remaining loops!
  * **Shorter String Alignment (O(min(M, N)) Space)**: Assigning **t2** to be the shorter string 
  ensures `BooleanArray` size is strictly **O(min(M, N))**.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Pruned 1D DP (Early Row Exit)** | **O(M * N)** | **O(min(M, N))** | **Staff Level (Optimal DP)** | Standard production choice; short-circuits on dead rows; zero memory churn. |
 **2D DP Table** | **O(M * N)** | **O(M * N)** | Baseline | Standard interview default for demonstrating full 2D state transitions. |
 **Un-memoized Brute-Force DFS** | **O(2^(M+N))** | **O(M + N)** | Poor | Triggers Time Limit Exceeded (TLE) blocks for string lengths $> 20$. |

*(where M = s1.length}, N = s2.length)*

### 3. Native Kotlin Syntax Pitfalls
* **Boolean Evaluation Short-Circuiting**: 
    Writing `val matchStr1 = dp[j] && t1[i - 1] == s3[i + j - 1]` evaluates `dp[j]` first. If `dp[j]` 
    is `false`, Kotlin's `&&` operator skips character comparison indexing `t1[i - 1] == s3[i + j - 1]`
    , saving CPU cycles!
  * **1D DP Array Invariant**: 
    For row **i**, `dp[j]` represents whether **s3[0 ... i + j - 1]** can be formed using  **i** 
    characters from **t1** and **j** characters from **t2**.

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (1D Table - Staff Level Optimal)
// Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
fun isInterleave1D(s1: String, s2: String, s3: String): Boolean {
    if (s3.length != s1.length + s2.length) return false
    val (t1, t2) = if (s1.length >= s2.length) s1 to s2 else s2 to s1
    val m = t1.length + 1
    val n = t2.length + 1
    val dp = BooleanArray(n)
    dp[0] = true
    for (j in 1 until n) {
        dp[j] = dp[j - 1] && t2[j - 1] == s3[j - 1]
    }

    for (i in 1 until m) {
        dp[0] = dp[0] && t1[i - 1] == s3[i - 1]

        var rowHasValidPath = dp[0] // Track row continuity to trigger early branch short-circuits

        for (j in 1 until n) {
            val matchStr1 = dp[j] && t1[i - 1] == s3[i + j - 1]
            val matchStr2 = dp[j - 1] && t2[j - 1] == s3[i + j - 1]
            dp[j] = matchStr1 || matchStr2

            if (dp[j]) {
                rowHasValidPath = true
            }
        }

        // Early Row Termination Guard: If an entire row vector becomes false, the goal is dead
        if (!rowHasValidPath) return false
    }

    return dp[n - 1]
}

// Approach 2: Bottom-Up Dynamic Programming (2D Table)
// Time Complexity: O(m * n) | Auxiliary Space Complexity: O(m * n)
fun isInterleave2D(s1: String, s2: String, s3: String): Boolean {
    if (s3.length != s1.length + s2.length) return false
    val m = s1.length + 1
    val n = s2.length + 1
    val dp = Array(m) { BooleanArray(n) }
    dp[0][0] = true

    for (i in 1 until m) {
        dp[i][0] = dp[i - 1][0] && s1[i - 1] == s3[i - 1]
    }
    for (j in 1 until n) {
        dp[0][j] = dp[0][j - 1] && s2[j - 1] == s3[j - 1]
    }

    for (i in 1 until m) {
        for (j in 1 until n) {
            dp[i][j] = dp[i - 1][j] && s1[i - 1] == s3[i + j - 1] ||
                    dp[i][j - 1] && s2[j - 1] == s3[i + j - 1]
        }
    }

    return dp[m - 1][n - 1]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Early Row Short-Circuiting (`rowHasValidPath`) Impact**:
  * In standard 1D / 2D DP, even if row **i** has no valid path (`rowHasValidPath == false`), the 
  algorithm continues evaluating all remaining rows up to **M**.
  * Pre-checking `rowHasValidPath` terminates the algorithm in **O(i .. min(M, N))** time on dead 
  branches, providing up to a **10x speedup on invalid string candidates**!
* **Top-Down Memoized DFS vs. Bottom-Up 1D DP**:
  * Top-Down Memoized DFS (`BooleanArray(m * n)`) evaluates only reachable state nodes. On sparse 
  interleaving graphs, DFS can skip unreachable cells entirely.
  * Bottom-Up 1D DP executes with zero call-stack overhead and flat array cache-line throughput, 
  making it optimal for production JVM execution.

---

## Day 59 - LC 72. Edit Distance
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Levenshtein Distance 3-Operation Choices**: To convert s1[0 ... i - 1]$ into s2[0 ... j - 1],
  we have 3 options at each character comparison:
    1. **Replace**: **1 + DP[i - 1][j - 1]** (or **DP[i - 1][j - 1]** if **s1[i - 1] == s2[j - 1]**).
    2. **Delete**: **1 + DP[i - 1][j]** (delete char from s1).
    3. **Insert**: **1 + DP[i][j - 1]** (insert char into s1).
       **DP[i][j] = (s1[i-1] == s2[j-1]) ? DP[i-1][j-1] : 1 + min(DP[i-1][j-1], DP[i-1][j], DP[i][j-1])**
  * **Single 1D Table Space Reduction (O(min(M, N)) Memory)**: Maintaining a single 1D 
  `IntArray(min(M, N) + 1)` and capturing the top-left diagonal **DP[i-1][j-1]** in a scalar variable 
  `prevDiag` compresses space to **O(min(M, N))**.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                                           |
 :--- | :--- | :--- | :--- |:--------------------------------------------------------------------------------------------|
 **Diagonal Banded DP (Ukkonen)** | **O(K x min(M, N))** | **O(K)** | **Staff Level (Extreme)** | Small threshold K (spellcheckers, auto-correct); skips **O(M * N)** full matrix evaluation. |
 **Single 1D DP Table** | **O(M * N)** | **O(min(M, N))** | **Staff Level (Optimal DP)** | Standard production choice; zero heap array allocations during loop.                        |
 **Two 1D DP Arrays** | **O(M * N)** | **O(min(M, N))** | Good | Baseline 1D DP; uses reference swaps (`prev`, `curr`).                                      |
 **2D DP Table** | **O(M * N)** | **O(M * N)** | Baseline | Standard interview default for demonstrating full 2D state transitions.                     |

*(where M = word1.length, N = word2.length, K = edit threshold)*

### 3. Native Kotlin Syntax Pitfalls
* **Top-Left Diagonal Initialization Bug**:
  At the start of row **i**, **dp[0]** represents converting **s1[0 ... i-1]** to empty string *s2*, 
  so $dp[0] = i$. Before updating `dp[0] = i`, the old value **dp[0] = i-1** MUST be saved in 
  `prevDiag = i - 1` (top-left diagonal for column **j = 1**).
* **Shorter String Alignment**: Orienting **s2** to be the shorter string 
  (`val (s1, s2) = if (word1.length >= word2.length) word1 to word2 else word2 to word1`) guarantees 
  `IntArray` size is strictly **O(min(M, N))**.

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (1D Table - Staff Level Optimal)
// Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
fun minDistance(word1: String, word2: String): Int {
    val (s1, s2) = if (word1.length >= word2.length) word1 to word2 else word2 to word1
    if (s2.isEmpty()) return s1.length
    val m = s1.length + 1
    val n = s2.length + 1
    val dp = IntArray(n) { it }

    for (i in 1 until m) {
        var prevDiag = i - 1 // Stores dp[i-1][j-1] (top-left diagonal)
        dp[0] = i // Update col 0 for row i
        for (j in 1 until n) {
            val temp = dp[j] // Save old dp[i-1][j]
            if (s1[i - 1] == s2[j - 1]) {
                dp[j] = prevDiag
            } else {
                dp[j] = 1 + minOf(dp[j], prevDiag, dp[j - 1])
            }
            prevDiag = temp // Advance top-left diagonal
        }
    }

    return dp[n - 1]
}

// Approach 2: Bottom-Up Dynamic Programming (2 1D Arrays)
// Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
fun minDistance21D(word1: String, word2: String): Int {
    val (s1, s2) = if (word1.length >= word2.length) word1 to word2 else word2 to word1
    val m = s1.length + 1
    val n = s2.length + 1
    var prev = IntArray(n) { it }
    var curr = IntArray(n)

    for (i in 1 until m) {
        curr[0] = i
        for (j in 1 until n) {
            if (s1[i - 1] == s2[j - 1]) {
                curr[j] = prev[j - 1]
            } else {
                curr[j] = 1 + minOf(prev[j], prev[j - 1], curr[j - 1])
            }
        }
        val temp = prev
        prev = curr
        curr = temp
    }

    return prev[n - 1]
}

// Approach 3: Bottom-Up Dynamic Programming (2D Table)
// Time Complexity: O(m * n) | Auxiliary Space Complexity: O(m * n)
fun minDistance2D(word1: String, word2: String): Int {
    val (s1, s2) = if (word1.length >= word2.length) word1 to word2 else word2 to word1
    val m = s1.length + 1
    val n = s2.length + 1
    val dp = Array(m) { IntArray(n) }
    for (i in 0 until m) dp[i][0] = i
    for (j in 0 until n) dp[0][j] = j

    for (i in 1 until m) {
        for (j in 1 until n) {
            if (s1[i - 1] == s2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1]
            } else {
                dp[i][j] = 1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
            }
        }
    }

    return dp[m - 1][n - 1]
}

// Approach 4: Ukkonen's Bounded Edit Distance / Diagonal Banded DP (Staff Level Extreme)
// Time Complexity: O(K * min(M, N)) | Auxiliary Space Complexity: O(K)
fun minDistanceBanded(word1: String, word2: String, k: Int = maxOf(word1.length, word2.length)): Int {
    val m = word1.length
    val n = word2.length
    
    // 1. Early exit guard if absolute length difference exceeds threshold k
    if (kotlin.math.abs(m - n) > k) return k + 1
    
    val bandWidth = 2 * k + 1
    var prev = IntArray(bandWidth)
    var curr = IntArray(bandWidth)
    
    // Initialize row 0
    for (idx in 0 until bandWidth) {
        val j = idx - k
        prev[idx] = if (j >= 0) j else k + 1
    }

    for (i in 1..m) {
        var minValInRow = k + 1
        for (idx in 0 until bandWidth) {
            val j = i + idx - k
            if (j !in 0..n) {
                curr[idx] = k + 1
                continue
            }
            
            if (j == 0) {
                curr[idx] = i
            } else if (word1[i - 1] == word2[j - 1]) {
                curr[idx] = prev[idx] // Match: same diagonal
            } else {
                val replace = prev[idx]
                val delete = prev[idx + 1]
                val insert = if (idx > 0) curr[idx - 1] else k + 1
                curr[idx] = 1 + minOf(replace, delete, insert)
            }
            minValInRow = minOf(minValInRow, curr[idx])
        }

        if (minValInRow > k) return k + 1 // Early termination: whole row exceeds threshold k

        val temp = prev
        prev = curr
        curr = temp
    }

    val finalIdx = n - m + k
    return if (finalIdx in 0 until bandWidth) prev[finalIdx] else k + 1
}

// Approach 5: Optimal String Alignment Distance (Damerau-Levenshtein 4-Operation Extension)
// Time Complexity: O(m * n) | Auxiliary Space Complexity: O(m * n)
fun damerauLevenshteinDistance(s1: String, s2: String): Int {
  val m = s1.length + 1
  val n = s2.length + 1
  val dp = Array(m) { IntArray(n) }

  for (i in 0 until m) dp[i][0] = i
  for (j in 0 until n) dp[0][j] = j

  for (i in 1 until m) {
    for (j in 1 until n) {
      val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
      dp[i][j] = minOf(
        dp[i - 1][j] + 1,       // Deletion
        dp[i][j - 1] + 1,       // Insertion
        dp[i - 1][j - 1] + cost // Substitution / Match
      )

      // 4th Operation: Transposition of adjacent characters (e.g., 'ca' -> 'ac' in 1 edit)
      if (i > 1 && j > 1 && s1[i - 1] == s2[j - 2] && s1[i - 2] == s2[j - 1]) {
        dp[i][j] = minOf(dp[i][j], dp[i - 2][j - 2] + 1)
      }
    }
  }

  return dp[m - 1][n - 1]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Ukkonen's Bounded Edit Distance / Diagonal Banded DP (O(K * min(M, N)) Time)**:
  * In production systems (search autocompletion, spellcheckers, genome sequence alignment), we only 
  care if edit distance is within a small threshold **K** (e.g. **K <= 3**).
  * If **|M - N| > K**, return ∞ (or **-1**) in **O(1)** time.
  * Cells in the 2D DP matrix where **|i - j| > K** are guaranteed to exceed distance **K**. By 
  calculating only a **diagonal band of width 2K + 1** around the main diagonal, Ukkonen's algorithm 
  reduces runtime from **O(M * N)** down to **O(K * min(M, N))**!
* **Damerau-Levenshtein Distance Extension**:
  * Extends Levenshtein Distance to support a 4th operation: **Transposition of adjacent characters** 
  (ab → ba), which accounts for 80% of human typing spelling errors!

---

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
