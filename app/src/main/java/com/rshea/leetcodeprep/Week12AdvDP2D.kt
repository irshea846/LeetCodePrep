package com.rshea.leetcodeprep

object Week12AdvDP2D {

    // Day 60 - LC 97. Interleaving String
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
    //    Code Analysis 1: isInterleave1D(s1: String, s2: String, s3: String): Boolean
    //    Staff-Level Review Findings for isInterleave1D:
    //
    //    1. Length Invariant Check:
    //      ◦ if (s3.length != s1.length + s2.length) return false O(1) time guard).
    //
    //    2. Shorter String Memory Alignment:
    //      ◦ val (t1, t2) = if (s1.length >= s2.length) s1 to s2 else s2 to s1
    //      ◦ t2 is the shorter string, so n = t2.length + 1 = min(M, N) + 1.
    //      ◦ BooleanArray(n) allocates memory strictly proportional to O(min(M, N)).
    //
    //    3. Early Row Termination Guard (rowHasValidPath):
    //      ◦ Brilliant Staff-Level Optimization! If an entire row in the 1D DP table becomes false,
    //      no valid interleaving path can ever cross this row to reach the bottom-right corner (M, N).
    //      ◦ if (!rowHasValidPath) return false immediately short-circuits the outer loop, saving
    //      thousands of iterations on invalid test strings!
    //    4. Boolean Micro-Optimization:
    //      ◦ dp[j] = (dp[j] && t1[i - 1] == s3[i + j - 1]) || (dp[j - 1] && t2[j - 1] == s3[i + j - 1])
    //      ◦ Short-circuiting dp[j] check before string character comparisons avoids character
    //      indexing overhead if dp[j] or dp[j-1] is false!

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
    //    Code Analysis 2: isInterleave2D (2D DP Table)
    //
    //    Review Findings for isInterleave2D:
    //      • Correct baseline 2D DP implementation (O(M * N) time, O(M * N) space).

    // Day 59 - LC 72. Edit Distance
    // Approach 1: Bottom-Up Dynamic Programming (1D Table)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
    fun minDistance(word1: String, word2: String): Int {
        val (s1, s2) = if (word1.length > word2.length) word1 to word2
        else word2 to word1
        if (s2.isEmpty()) return s1.length
        val m = s1.length + 1
        val n = s2.length + 1
        val dp = IntArray(n)
        for (j in 0 until n) dp[j] = j

        for (i in 1 until m) {
            dp[0] = i - 1
            var diag = i - 1
            for (j in 1 until n) {
                val temp = dp[j]
                if (s1[i - 1] == s2[j - 1]) {
                    dp[j] = diag
                } else {
                    dp[j] = 1 + minOf(dp[j], diag, dp[j - 1])
                }
                diag = temp
            }
        }

        return dp[n - 1]
    }
    //    Code Analysis 1: minDistance(word1: String, word2: String): Int (Single 1D DP Array)
    //
    //    Critical Bug in minDistance:
    //    1. dp[0] Initialization inside Row Loop:
    //      ◦ For row i, the 0th column in 2D DP corresponds to converting prefix s1[0 ... i-1]
    //      to empty string s2, which requires i deletions (dp[0] = i).
    //      ◦ In minDistance: dp[0] = i - 1 set dp[0] to i - 1 instead of i! And var diag = i - 1
    //      set top-left diagonal for (i, 1) to i - 1.
    //      ◦ Correction: At row i, the new dp[0] must be i. Before dp[0] becomes i, its previous
    //      value was i - 1. So var diag = i - 1 (the old dp[0] value from previous row i - 1), and
    //      then update dp[0] = i! Let's trace: var diag = i - 1 dp[0] = i Then for j in 1 .. n-1:
    //      val temp = dp[j] (saves old dp[j] before overwrite, which becomes top-left diagonal for
    //      next column j + 1) if (s1[i - 1] == s2[j - 1]) dp[j] = diag else dp[j] = 1 + minOf(dp[j],
    //      diag, dp[j-1]) diag = temp
    //
    //    2. String Swap Orientation:
    //      ◦ if (word1.length > word2.length) word1 to word2 else word2 to text1
    //      ◦ If we want space to be O(min(M, N)), s2 should be the SHORTER string!
    //      ◦ So val (s1, s2) = if (word1.length >= word2.length) word1 to word2 else word2 to word1.
    //      ◦ Here s1 is longer, s2 is shorter, so n = s2.length + 1 = min(M, N) + 1.
    //      ◦ Memory is strictly O(min(M, N))!

    // Approach 2: Bottom-Up Dynamic Programming (2 1D Arrays)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
    fun minDistance21D(word1: String, word2: String): Int {
        val (s1, s2) = if (word1.length > word2.length) word1 to word2
        else word2 to word1
        val m = s1.length + 1
        val n = s2.length + 1
        var prev = IntArray(n)
        var curr = IntArray(n)
        for (j in 0 until n) prev[j] = j

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
    //    Code Analysis 2: minDistance21D (Two 1D Arrays)
    //    Review Findings for minDistance21D:
    //    1. Correct Logic & Object Swap:
    //      ◦ Swaps array references val temp = prev; prev = curr; curr = temp without allocating
    //      new arrays during execution!
    //    2. Minor Optimization:
    //      ◦ Swap s1 and s2 so s2 is shorter: n = min(M, N) + 1.

    // Approach 3: Bottom-Up Dynamic Programming (2D Table)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(m * n)
    fun minDistance2D(word1: String, word2: String): Int {
        val (s1, s2) = if (word1.length > word2.length) word1 to word2
        else word2 to word1
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
    //    Code Analysis 3: minDistance2D (2D Table)
    //    Review Findings for minDistance2D:
    //      • Correct baseline 2D Edit Distance implementation (O(M * N) time, O(M * N) space).

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


    // Day 58 - LC 64. Minimum Path Sum
    // Approach 1: Bottom-Up Dynamic Programming (1D Table)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
    fun minPathSum1D(grid: Array<IntArray>): Int {
        val dp = IntArray(grid[0].size)

        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (i == 0 && j == 0) {
                    dp[j] = grid[i][j]
                } else if (i == 0) {
                    dp[j] = dp[j - 1] + grid[i][j]
                } else if (j == 0) {
                    dp[j] = dp[j] + grid[i][j]
                } else {
                    dp[j] = minOf(dp[j - 1], dp[j]) + grid[i][j]
                }
            }
        }

        return dp[dp.lastIndex]
    }
    //    Code Analysis 1: minPathSum1D(grid: Array<IntArray>): Int
    //    Review Findings for minPathSum1D:
    //    1. Inner Loop Branch Checks (if-else if-else):
    //      ◦ Inside the nested loops M * N times, there are 4 conditional branches (i == 0
    //      && j == 0, i == 0, j == 0, else).
    //      ◦ Branchless Pre-filling:
    //          ▪ Pre-fill Row 0 in the 1D DP table BEFORE entering the main loops!
    //          ▪ Row 0: dp[0] = grid[0][0]; for (j in 1 until cols) dp[j] = dp[j-1] + grid[0][j].
    //          ▪ Then, for each row $i \in 1 \dots rows-1$:
    //              ▪ dp[0] += grid[i][0] (leftmost column)
    //              ▪ for (j in 1 until cols) dp[j] = minOf(dp[j], dp[j-1]) + grid[i][j]
    //          ▪ Impact: Eliminates M * N conditional branch checks inside the hot loop!
    //
    //    2. Orientation Optimization O(min(M, N)) Space):
    //      ◦ Currently, space is O(N) (where N = grid[0].size).
    //      ◦ If grid has M = 10,000 rows and N = 5 cols, IntArray(grid[0].size) takes 5 slots.
    //      ◦ But if grid has M = 5 rows and N = 10,000 cols, IntArray(grid[0].size) takes 10,000
    //      slots!
    //      ◦ Staff Optimization: If M < N (fewer rows than columns), traverse column-by-column or
    //      transpose cell access so IntArray size is strictly O(min(M, N))!

    // Approach 2: Bottom-Up Dynamic Programming (In Place)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(1)
    fun minPathSumInPlace(grid: Array<IntArray>): Int {
        val m = grid.size
        val n = grid[0].size

        for (i in 0 until m) {
            for (j in 0 until n) {
                if (i == 0 && j == 0) continue
                else if (i == 0) {
                    grid[i][j] += grid[i][j - 1]
                } else if (j == 0) {
                    grid[i][j] += grid[i - 1][j]
                } else {
                    grid[i][j] += minOf(grid[i - 1][j], grid[i][j - 1])
                }
            }
        }

        return grid[m - 1][n - 1]
    }
    //    Code Analysis 2: minPathSumInPlace(grid: Array<IntArray>): Int
    //    Review Findings for minPathSumInPlace:
    //    1. Branchless In-Place Execution:
    //      ◦ Same as 1D DP, we can pre-fill Row 0 (for (j in 1 until n) grid[0][j] += grid[0][j-1])
    //      and Col 0 (for (i in 1 until m) grid[i][0] += grid[i-1][0]).
    //      ◦ Then run for (i in 1 until m) for (j in 1 until n) grid[i][j] += minOf(grid[i-1][j],
    //      grid[i][j-1]).
    //      ◦ Eliminates M * N branch checks completely!
    //
    //    2. Input Parameter Mutation Trade-off (Critical System Design Topic):
    //      ◦ Mutating grid[i][j] += ... achieves O(1) auxiliary space.
    //      ◦ Production Contract Danger: Modifying input parameter grid mutates the caller's array
    //      in-place. If the input grid is shared across coroutines, concurrent threads, or reused
    //      in later calculations, this side effect creates subtle bugs!
    //      ◦ In production / system design interviews, always state: "In-place mutation achieves
    //      O(1) space, but if input immutability is required by the contract, 1D DP with O(min(M, N))
    //      memory is strictly preferred."

    // Day 57 - LC 62. Unique Paths
    // Approach 1: Zero-Allocation Primitive Combinatorics (Extreme Staff Level)
    // Time Complexity: O(min(m, n)) | Auxiliary Space Complexity: O(1)
    fun uniquePathsMath(m: Int, n: Int): Int {
        val k = minOf(m - 1, n - 1)
        val total = m + n - 2
        var result = 1L

        for (i in 1..k) {
            result = result * (total - k + i) / i
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
    //    Code Analysis 1: uniquePathsCombinatorics(m: Int, n: Int): Int
    //    Review Findings for uniquePathsCombinatorics:
    //    1. BigInteger Heap Allocation Overhead:
    //      ◦ combo.times(i.toBigInteger()) and combo.div(j.toBigInteger()) instantiate multiple
    //      BigInteger heap objects inside the loop!
    //
    //    2. Top-Tier Optimization: Zero-Allocation Primitive Interleaved Math:
    //      ◦ By interleaving multiplication and division in a single loop
    //      (result = result * (total - k + i) / i) using 64-bit primitive Long, we compute
    //      C(N, K) with zero BigInteger object allocations on the JVM Heap!
    //      ◦ Why does interleaving work without rounding errors? Because ∏ (total - k + i) for i = 1
    //      to j is a product of j consecutive integers, which is mathematically guaranteed to be
    //      divisible by j! at every step i!

    // Approach 2: Bottom-Up Dynamic Programming (1D Table)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
    fun uniquePaths1D(m: Int, n: Int): Int {
        val min = minOf(m, n)
        val dp = IntArray(min) { 1 }

        for (i in 1 until maxOf(m, n)) {
            for (j in 1 until min) {
                dp[j] = dp[j] + dp[j - 1]
            }
        }

        return dp[min - 1]
    }
    //    Code Analysis 2: uniquePaths1D(m: Int, n: Int): Int
    //    Review Findings for uniquePaths1D:
    //    1. Branchless & Space-Optimized:
    //      ◦ Initializing IntArray(min) { 1 } avoids checking if (i == 0 || j == 0) inside inner
    //      loop M * N times!
    //      ◦ minOf(m, n) ensures space complexity is strictly O(min(m, n)) even if grid is elongated
    //      (M » N).
    //    2. Syntax Cleanup:
    //      ◦ dp[j] = dp[j] + dp[j - 1] can be written idiomatically as dp[j] += dp[j - 1].

    // Approach 3: Bottom-Up Dynamic Programming (2D Table)
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
    //    Code Analysis 3: uniquePaths2D(m: Int, n: Int): Int
    //    Review Findings for uniquePaths2D:
    //    1. Correctness & 2D Space Invariant:
    //      ◦ Pre-populating the 2D matrix with 1s (Array(m) { IntArray(n) { 1 } }) is a clean
    //      way to eliminate edge-case initializations for row 0 and col 0!
    //      ◦ Time Complexity: O(M * N).
    //      ◦ Space Complexity:O(M * N).


}