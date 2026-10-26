package com.rshea.leetcodeprep.masteryreviewday

import kotlin.math.abs

object MasterW11DP2D {

    // LC 1143. Longest Common Subsequence
    // Approach 1: Bottom-Up Dynamic Programming (Single 1D Table - Staff Level Optimal)
    // Time Complexity: O(N * M) | Auxiliary Space Complexity: O(min(N, M))
    fun longestCommonSubsequence1D(text1: String, text2: String): Int {
        val (s1, s2) = if (text1.length < text2.length) text1 to text2 else text2 to text1
        val n = s1.length
        val dp = IntArray(n + 1)

        for (c2 in s2) {
            var diag = 0
            for (j in 1..n) {
                val temp = dp[j]
                if (c2 == s1[j - 1]) {
                    dp[j] = diag + 1
                } else {
                    dp[j] = maxOf(dp[j], dp[j - 1])
                }
                diag = temp
            }
        }

        return dp[n]
    }

    // Approach 2: Bottom-Up Dynamic Programming (2D Table)
    // Time Complexity: O(N * M) | Auxiliary Space Complexity: O(N * M)
    fun longestCommonSubsequence2D(text1: String, text2: String): Int {
        val dp = Array(text1.length + 1) { IntArray(text2.length + 1) }

        for (i in 1 until dp.size) {
            for (j in 1 until dp[i].size) {
                if (text1[i - 1] == text2 [j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1
                } else {
                    dp[i][j] = maxOf(dp[i - 1][j], dp[i][j - 1])
                }
            }
        }
        return dp[dp.lastIndex][dp[0].lastIndex]
    }

    // LC 518. Coin Change II
    // Approach 1: Bottom-Up Dynamic Programming (1D Table)
    // Time Complexity: O(N * amount) | Auxiliary Space Complexity: O(amount)
    fun change1D(amount: Int, coins: IntArray): Int {
        val dp = IntArray(amount + 1)
        dp[0] = 1

        for (coin in coins) {
            for (j in coin .. amount) {
                dp[j] += dp[j - coin]
            }
        }
        return dp[amount]
    }

    // Approach 2: Bottom-Up Memoization Dynamic Programming (2D Table)
    // Time Complexity: O(N * amount) | Auxiliary Space Complexity: O(N * amount)
    fun changeMemoization(amount: Int, coins: IntArray): Int {
        val dp = Array(coins.size + 1) { IntArray(amount + 1) }
        for (i in dp.indices) dp[i][0] = 1

        for (i in 1 until dp.size) {
            for (j in 1 until dp[i].size) {
                if (j >= coins[i - 1]) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]]
                } else {
                    dp[i][j] = dp[i - 1][j]
                }
            }
        }
        return dp[coins.size][amount]
    }


    // LC 494. Target Sum
    // Approach 1: Bitwise Parallelism / Polynomial Shift Optimization (Staff Level)
    // Time Complexity: O(N * targetPos * K / 64) = O(N * targetPos / 2)
    // Auxiliary Space Complexity: O(targetPos / 2)
    fun findTargetSumWaysBitset(nums: IntArray, target: Int): Int {
        val totalSum = nums.sum()
        if (kotlin.math.abs(target) > totalSum || (totalSum + target) % 2 != 0) return 0
        val targetPos = (totalSum + target) / 2

        val k = 32 // 32 bits per counter slot (supports counts up to 4,294,967,295)
        var poly = 1.toBigInteger() // Slot 0 initialized to 1 way

        for (num in nums) {
            // Polynomial addition: poly = poly + (poly << (num * K))
            poly = poly.add(poly.shiftLeft(num * k))
        }

        // Extract 32-bit counter value at slot targetPos
        return poly.shiftRight(targetPos * k).and(0xFFFFFFFFL.toBigInteger()).toInt()
    }

    // Approach 2: 0/1 knapsack Bottom-Up Dynamic Programming (1D Table)
    // Time Complexity: O(N * S) where N = nums.size, S = (sum + target) / 2 | Auxiliary Space Complexity: O(S)
    fun findTargetSumWays01Knapsack1D(nums: IntArray, target: Int): Int {
        val totalSum = nums.sum()
        if (abs(target) > totalSum || 0 != (totalSum + target) % 2) return 0
        val newTarget = (totalSum + target) / 2
        val dp = IntArray(newTarget + 1)
        dp[0] = 1

        for (num in nums) {
            for (j in newTarget downTo num) {
                dp[j] += dp[j - num]
            }
        }
        return dp[newTarget]
    }

    // Approach 3: Top-Down Memoized DFS (Negative Index Offset)
    // Time Complexity: O(N * totalSum) | Auxiliary Space Complexity: O(N * 2 * totalSum + 1)
    fun findTargetSumWaysMemoization(nums: IntArray, target: Int): Int {
        val totalSum = nums.sum()
        if (abs(target) > totalSum) return 0

        val memo = Array(nums.size) { IntArray(2 * totalSum + 1) { -1 } }

        fun findTargetSumWaysEx(i: Int, sum: Int): Int {
            if (i == nums.size) {
                return if (sum == target) 1 else 0
            }

            val j = sum + totalSum
            if (memo[i][j] != -1) {
                return memo[i][j]   // Since the subproblem is resolved, no need to recompute again
            }

            val inclusion = findTargetSumWaysEx(i + 1, sum + nums[i]) // Include the current item
            val exclusion = findTargetSumWaysEx(i + 1, sum - nums[i]) // Exclude the current item

            memo[i][j] = inclusion + exclusion
            return memo[i][j]
        }

        return findTargetSumWaysEx(0, 0)
    }

    // Approach 4: 0/1 Knapsack (Bottom-Up Recursion + Memoization (Un-memoized Baseline)
    // Time Complexity: O(2^N) where N = nums.size | Auxiliary Space Complexity: O(N)
    fun findTargetSumWaysRecursion(nums: IntArray, target: Int): Int {
        var ways = 0

        fun findTargetSumWaysEx(i: Int, sum: Int) {
            if (i == nums.size) {
                if (sum == target) ways++
                return
            }
            findTargetSumWaysEx(i + 1, sum + nums[i]) // Include the current item
            findTargetSumWaysEx(i + 1, sum - nums[i]) // Exclude the current item
        }

        findTargetSumWaysEx(0, 0)
        return ways
    }


    // LC 416. Partition Equal Subset Sum
    // Approach 1: Bitset / BigInteger Shift Optimization (Staff Level - 64x Speedup)
    // Time Complexity: O(N * target / 64) | Auxiliary Space Complexity: O(target / 64)
    fun canPartition(nums: IntArray): Boolean {
        val totalSum = nums.sum()
        if (1 == (totalSum and 1)) return false
        val target = totalSum shr 1

        var bitset = 1.toBigInteger()


        for (num in nums) {
            bitset = bitset or (bitset shl num)
            if (bitset.testBit(target)) return true
        }

        return bitset.testBit(target)
    }

    // Approach 2: Bottom-Up Dynamic Programming (1D Table Pruned)
    // Time Complexity: O(N * target) | Auxiliary Space Complexity: O(target)
    fun canPartition1D(nums: IntArray): Boolean {
        val totalSum = nums.sum()
        if (0 != totalSum % 2) return false
        val target = totalSum / 2
        val dp = BooleanArray(target + 1)
        dp[0] = true

        for (num in nums) {
            for (j in target downTo num) {
                dp[j] = dp[j] || dp[j - num]
            }
            if (dp[target]) return true
        }
        return dp[dp.lastIndex]
    }

    // Approach 3: Bottom-Up Dynamic Programming (2D Table)
    // Time Complexity: O(N * target) | Auxiliary Space Complexity: O(N * target)
    fun canPartition2D(nums: IntArray): Boolean {
        val totalSum = nums.sum()
        if (0 != totalSum % 2) return false
        val target = totalSum / 2
        val dp = Array(nums.size + 1) { BooleanArray(target + 1) }
        for (i in 0 .. nums.size) dp[i][0] = true

        for (i in 1 until dp.size) {
            for (j in 1 until dp[i].size) {
                if (j >= nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]]
                } else {
                    dp[i][j] = dp[i - 1][j]
                }
            }
            if (dp[i][target]) return true
        }
        return dp[dp.lastIndex][target]
    }

}