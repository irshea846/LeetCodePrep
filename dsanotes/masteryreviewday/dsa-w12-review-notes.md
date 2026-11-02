# LeetCodePrep Week 12 Mastery & Review Day

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 12: 2D > 2x1D > 1D DP (October 30 - October 31)
# ===============================================================

## Topics Covered
- [ ] 2D to 2x 1D to 1D DP Space
- [ ] Zero-Allocation Primitive Math
- [ ] Branchless In-Place (O(1) Space) DP
- [ ] Diagonal Banded DP (Ukkonen)
- [ ] Damerau-Levenshtein 4-Operation Extension

## LC 97. Interleaving String
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

## LC 72. Edit Distance
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
fun minDistanceBanded(word1: String, word2: String): Int {
    val k: Int = maxOf(word1.length, word2.length)
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

