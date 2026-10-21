package com.rshea.leetcodeprep

object Week11DP2D {

    // Day 54 - LC 518. Coin Change II
    // Approach 1: Bottom-Up Dynamic Programming (1D Table)
    // Time Complexity: O(N * amount) where N = coins.size | Auxiliary Space Complexity: O(amount)
    fun change(amount: Int, coins: IntArray): Int {
        val dp = IntArray(amount + 1)
        dp[0] = 1
        for (coin in coins) {
            for (j in coin .. amount) {
                dp[j] += dp[j - coin]
            }
        }

        return dp[dp.lastIndex]
    }
    //    Detailed Staff-Tier Review of change (Coin Change II):
    //
    //    1. Unbounded Knapsack Combination vs Permutation Ordering (The Crucial Difference!)
    //
    //      • Compare Coin Change II (LC 518) vs Combination Sum IV (LC 377):
    //          ◦ LC 518 (Coin Change II - Unique Combinations):
    //              ▪ Outer loop = for (coin in coins)
    //              ▪ Inner loop = for (j in coin .. amount)
    //              ▪ Why outer loop over coins? Because processing coin c_1 for all amounts, then
    //              c_2, ensures coins are processed in a fixed order (e.g., 1+2+2 is counted once,
    //              but 2+1+2 is NOT counted as a separate permutation). This counts Combinations
    //              (Order does NOT matter)!
    //
    //          ◦ LC 377 (Combination Sum IV - Unique Permutations):
    //              ▪ Outer loop = for (j in 1 .. amount)
    //              ▪ Inner loop = for (coin in coins)
    //              ▪ Reversing loop order counts Permutations (Order DOES matter)!
    //
    //    2. Performance & Micro-Optimizations
    //
    //       1. Sorting coins.sort() or filtering coin > amount:
    //          ◦ if (amount == 0) return 1 (instant $\mathcal{O}(1)$ exit for $amount = 0$).
    //          ◦ Sorting coins.sort() allows skipping coins larger than amount (if (coin > amount) continue).
    //          ◦ If coins are sorted, when coin > amount, we can break early from the outer loop!
    //
    //       2. Loop Primitive Indexing (dp[j] vs dp.lastIndex):
    //          ◦ dp[dp.lastIndex] retrieves dp[amount] by evaluating property getter lastIndex.
    //          ◦ Returning dp[amount] directly is clearer, faster, and avoids property lookup overhead.
    //
    //       3. Bit-Packing / Primitive Array SIMD or 2D Space Optimization:
    //          ◦ dp[j] += dp[j - coin]
    //          ◦ Time Complexity: O(N * amount) where N = coins.size.
    //          ◦ Space Complexity:  O(amount) (IntArray(amount + 1)).


    // Day 53 - LC 494. Target Sum
    // Approach 1: Bottom-Up Dynamic Programming (1D Table)
    // Time Complexity: O(N * S) where N = nums.size, S = (sum + target) / 2 | Auxiliary Space Complexity: O(S)
    fun findTargetSumWays(nums: IntArray, target: Int): Int {
        val totalSum = nums.sum()
        if (target + totalSum < 0 ||  0 != (totalSum + target) % 2) return 0
        val sumOfPos = (totalSum + target) / 2
        val dp = IntArray(sumOfPos + 1)
        dp[0] = 1

        for (num in nums) {
            for (j in dp.size - 1 downTo num) {
                dp[j] = dp[j] + dp[j - num]
            }
        }

        return dp[sumOfPos]
    }

    // Approach 2: Top-Down Memoized DFS (Negative Index Offset)
    // Time Complexity: O(N * totalSum) | Auxiliary Space Complexity: O(N * totalSum)
    fun findTargetSumWaysMemoization(nums: IntArray, target: Int): Int {
        val totalSum = nums.sum()
        if (kotlin.math.abs(target) > totalSum) return 0

        // Memo array size: N x (2 * totalSum + 1)
        val memo = Array(nums.size) { IntArray(2 * totalSum + 1) { -1 } }

        fun dfs(idx: Int, currentSum: Int): Int {
            if (idx == nums.size) {
                return if (currentSum == target) 1 else 0
            }

            // Offset negative sum to array index [0 .. 2 * totalSum]
            val memoIdx = currentSum + totalSum
            if (memo[idx][memoIdx] != -1) {
                return memo[idx][memoIdx]
            }

            val add = dfs(idx + 1, currentSum + nums[idx])
            val subtract = dfs(idx + 1, currentSum - nums[idx])

            memo[idx][memoIdx] = add + subtract
            return memo[idx][memoIdx]
        }

        return dfs(0, 0)
    }

    // Approach 3: Recursion + Memoization (Un-memoized Baseline)
    // Time Complexity: O(2^N) where N = nums.size | Auxiliary Space Complexity: O(N)
    fun findTargetSumWaysRecursion(nums: IntArray, target: Int): Int {

        fun findTargetSumWaysRecursionEx(nums: IntArray, target: Int, curIdx:Int, curSum: Int): Int {
            if (curIdx == nums.size) {
                return if (target == curSum) 1 else 0
            }

            return findTargetSumWaysRecursionEx(nums, target, curIdx + 1, curSum + (-1) * nums[curIdx]) +
                    findTargetSumWaysRecursionEx(nums, target, curIdx + 1, curSum + nums[curIdx])
        }

        return findTargetSumWaysRecursionEx(nums, target, 0, 0)
    }

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