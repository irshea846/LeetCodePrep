package com.rshea.leetcodeprep

object Week12AdvDP2D {

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