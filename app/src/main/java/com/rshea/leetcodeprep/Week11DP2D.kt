package com.rshea.leetcodeprep

object Week11DP2D {

    // Day 52 - LC 416. Partition Equal Subset Sum
    // Approach 1: Bottom-Up Dynamic Programming (1D Table)
    // Time Complexity: O(N * S) where N = nums.size | Auxiliary Space Complexity: O(S)
    fun canPartition(nums: IntArray): Boolean {
        val sum = nums.sum()
        if (1 == sum % 2) return false
        val target = sum / 2
        val m = nums.size
        val n = target + 1
        val dp = BooleanArray(n)
        dp[0] = true

        for (i in 0 until m) {
            for (j in n - 1 downTo 1) {
                if (j >= nums[i]) {
                    dp[j] = dp[j] || dp[j - nums[i]]
                }
                if (dp[j] && j == n - 1) {
                    return true
                }
            }
        }
        return dp[n - 1]
    }
    //    Code Analysis 1: Review Findings for canPartition (1D DP):
    //
    //    1. Algorithmic Correctness & 0/1 Knapsack State Reduction:
    //    ◦ Total sum check: if (sum % 2 != 0) return false (Odd sums cannot be partitioned into two
    //    equal integer halves).
    //    ◦ Target sum: target = sum / 2.
    //    ◦ 0/1 Knapsack Invariant: Inner loop MUST iterate backwards for (j in target downTo nums[i])!
    //    ◦ Why iterate backwards?
    //      ▪ Iterating backwards ensures that dp[j - nums[i]] comes from the previous item's state
    //      (i.e. item $i$ is used at most once!).
    //      ▪ If inner loop iterated forward ($1 \dots \text{target}$), we would allow reusing nums[i]
    //      multiple times (Unbounded Knapsack / Coin Change behavior!).
    //    ◦ Time Complexity: O(N * S) where N = nums.size and S = sum / 2.
    //    ◦ Auxiliary Space Complexity: O(S) (BooleanArray(target + 1)).
    //
    //    2. Micro-Optimization: Inner Loop Boundary Pruning:
    //    ◦ Notice your inner loop condition:
    //          for (j in n - 1 downTo 1) {
    //              if (j >= nums[i]) { ... }
    //          }
    //    ◦ Instead of checking if (j >= nums[i]) inside the loop for indices j < nums[i], stop the
    //    loop early at nums[i]! for (j in target downTo nums[i])
    //    ◦ Impact: Cuts inner loop iterations significantly! When nums[i] is large (e.g. nums[i] =
    //    50), j stops at 50 instead of decrementing all the way down to 1.
    //
    //    3. Early Exit Optimization:
    //    ◦ if (dp[target]) return true
    //    ◦ The moment dp[target] becomes true, we can return true immediately without evaluating
    //    remaining outer elements!
    //
    //    4. Refactored Staff-Level 1D Implementation:
    //    ◦ Please see Approach 1: Bottom-Up Dynamic Programming (1D Table Pruned) in
    //    dsa-w11-notes.md.


    // Approach 2: Bottom-Up Dynamic Programming (2D Table)
    // Time Complexity: O(N * S) where N = nums.size | Auxiliary Space Complexity: O(N * S)
    fun canPartition2D(nums: IntArray): Boolean {
        val sum = nums.sum()
        if (1 == sum % 2) return false
        val target = sum / 2
        val m = nums.size + 1
        val n = target + 1
        val dp = Array(m) { BooleanArray(n) }

        for (i in 0 until m) dp[i][0] = true
        for (j in 1 until n) dp[0][j] = false

        for (i in 1 until m) {
            for (j in 1 until n) {

                if (j >= nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]]
                } else {
                    dp[i][j] = dp[i - 1][j]
                }

                if (dp[i][j] && j == n - 1) {
                    return true
                }
            }
        }
        return dp[m - 1][n - 1]
    }
    //    Code Analysis 2: Review Findings for 2D DP:
    //
    //    1. Memory Allocation Footprint:
    //    ◦ val dp = Array(m) { BooleanArray(n) } allocates an array of N+1 separate BooleanArray
    //    objects on the JVM Heap.
    //    ◦ For N = 200, S = 10000, this allocates 201 separate heap arrays.
    //    ◦ 2D DP is great for demonstrating step-by-step state transitions in interview discussions,
    //    but 1D DP is strictly preferred in production for zero extra array allocations and cache
    //    locality.

    // Approach 3: Bitset / BigInteger Shift Optimization (Staff Level - 64x Speedup)
    // Time Complexity: O(N * S / 64) | Auxiliary Space Complexity: O(S / 64)
    fun canPartitionBitset(nums: IntArray): Boolean {
        val sum = nums.sum()
        if (sum % 2 != 0) return false
        val target = sum / 2

        var bitset = 1.toBigInteger() // Bit 0 is set (0 sum achievable)

        for (num in nums) {
            bitset = bitset or (bitset shl num) // Bitwise shift evaluates 64 target sums in 1 CPU cycle
        }

        return bitset.testBit(target)
    }
}