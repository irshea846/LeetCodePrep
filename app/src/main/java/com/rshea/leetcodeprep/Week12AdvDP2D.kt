package com.rshea.leetcodeprep

object Week12AdvDP2D {

    // Day 56 - LC 62. Unique Paths
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