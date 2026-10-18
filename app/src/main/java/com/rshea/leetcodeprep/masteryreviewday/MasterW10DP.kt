package com.rshea.leetcodeprep.masteryreviewday

object MasterW10DP {

    // 322. Coin Change
    fun coinChange(coins: IntArray, amount: Int): Int {
        coins.sort()
        val dp = IntArray(amount + 1) { amount + 1 }
        val n = dp.size
        dp[0] = 0

        for (i in 1 until n) {
            for (coin in coins) {
                if (i < coin) break
                dp[i] = minOf(dp[i], dp[i - coin] + 1)
            }
        }
        return if (dp[dp.size - 1] == amount + 1) -1 else dp[dp.size - 1]
    }

    // 62. Unique Paths
    fun uniquePathsCombinatorics(m: Int, n: Int): Int {
        var ways = 1.toBigInteger()
        val hi = m + n - 2
        val lo = minOf(m, n) - 1

        for (i in hi downTo hi - lo + 1) {
            ways = ways.times(i.toBigInteger())
        }
        for (j in lo downTo 2) {
            ways = ways.div(j.toBigInteger())
        }

        return ways.toInt()
    }

    fun uniquePaths(m: Int, n: Int): Int {
        val dp = IntArray(n) { 1 }

        for (i in 1 until m) {
            for (j in 1 until n) {
                dp[j] += dp[j - 1]
            }
        }
        return dp[n - 1]
    }

    // 139. Word Break
    fun wordBreak(s: String, wordDict: List<String>): Boolean {
        val n = s.length
        val dp = BooleanArray(n + 1)
        dp[0] = true

        for (i in 0 .. n) {
            for (j in 0 .. i) {
                for (word in wordDict) {
                    if (i - word.length < 0) continue
                    if (dp[i] || dp[i - word.length] && word == s.substring(j, i)) {
                        dp[i] = true
                    }
                }
            }
        }
        return dp[dp.size - 1]
    }

    // 198. House Robber
    fun rob(nums: IntArray): Int {
        val n = nums.size
        if (n == 1) return nums[0]
        val dp = IntArray(n)
        dp[0] = nums[0]
        dp[1] = maxOf(nums[0], nums[1])
        for (i in 2 until n) {
            dp[i] = maxOf(dp[i - 1], dp[i - 2] + nums[i])
        }

        return dp[dp.lastIndex]
    }

    fun robSpaceO1(nums: IntArray): Int {
        val n = nums.size
        var prev0 = nums[0]
        var prev1 = nums[0]
        if (n > 1) prev1 = maxOf(nums[0], nums[1])

        for (i in 2 until n) {
            val curr = maxOf(prev1, prev0 + nums[i])
            prev0 = prev1
            prev1 = curr
        }

        return prev1
    }

    // LC 1137. N-th Tribonacci Number
    fun tribonacci(n: Int): Int {

        tailrec fun tribonacciEx(i: Int, c: Int, b: Int, a: Int): Int {
            return if (i > 2) tribonacciEx(i - 1, a + b + c, c, b) else
                if (i == 2) c else if (i == 1) b else a
        }

        return tribonacciEx(n, 1, 1, 0)
    }

    // 509. Fibonacci Number
    fun fib(n: Int): Int {
        tailrec fun fibEx(i: Int, b: Int, a: Int): Int{
            if (i == 0) return b
            if (i == 1) return a
            return fibEx(i - 1, a, a + b)
        }
        return fibEx(n, 0, 1)
    }

    // 70. Climbing Stairs
    fun climbStairs(n: Int): Int {
        val dp = IntArray(n + 1)
        dp[0] = 1
        dp[1] = 1

        for (i in 2 .. n) {
            dp[i] = dp[i - 1] + dp[i - 2]
        }

        return dp[dp.size - 1]
    }

}