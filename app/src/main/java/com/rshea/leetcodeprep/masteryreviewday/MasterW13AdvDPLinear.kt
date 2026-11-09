package com.rshea.leetcodeprep.masteryreviewday

import kotlin.math.pow

object MasterW13AdvDPLinear {

    // LC 131. Palindrome Partitioning
    // Approach 1: Backtracking (DFS) + 2D DP Pre-computation
    // Time Complexity: O(N × 2ⁿ) | Auxiliary Space Complexity: O(N²)
    fun partitionPrecomputeDP(s: String): List<List<String>> {
        val result = mutableListOf<List<String>>()
        val list = mutableListOf<String>()
        val n = s.length
        val dp = Array(n) { BooleanArray(n) }

        for (i in n - 1 downTo 0) {
            for (j in i until n) {
                dp[i][j] = s[i] == s[j] && (j - i <= 2 || dp[i + 1][j - 1])
            }
        }

        fun dfs(start: Int) {
            if (start == s.length) {
                result.add(ArrayList(list))
                return
            }

            for (end in start until s.length) {
                if (dp[start][end]) {
                    list.add(s.substring(start, end + 1))
                    dfs(end + 1)
                    list.removeAt(list.lastIndex)
                }
            }
        }

        dfs(0)
        return result
    }

    // Approach 2: Backtracking (DFS) + 2D DP
    // Time Complexity: O(N × 2ⁿ) | Auxiliary Space Complexity: O(N²)
    fun partitionDP(s: String): List<List<String>> {
        val result = mutableListOf<List<String>>()
        val list = mutableListOf<String>()
        val n = s.length
        val dp = Array(n) { BooleanArray(n) }

        fun dfs(start: Int) {
            if (start == s.length) {
                result.add(ArrayList(list))
                return
            }

            for (end in start until s.length) {
                dp[start][end] = s[start] == s[end] && (end - start <= 2 || dp[start + 1][end - 1])
                if (dp[start][end]) {
                    list.add(s.substring(start, end + 1))
                    dfs(end + 1)
                    list.removeAt(list.lastIndex)
                }
            }
        }

        dfs(0)
        return result
    }

    // Approach 3: Backtracking (DFS)
    // Time Complexity: O(N × 2ⁿ) | Auxiliary Space Complexity: O(N²)
    // Consider: How would you modify your approach if the string length was much larger and you
    // only needed to count the minimum number of partitions?
    fun partitionDFS(s: String): List<List<String>> {
        val result = mutableListOf<List<String>>()
        val list = mutableListOf<String>()

        tailrec fun isPalindrome(left: Int, right: Int): Boolean {
            if (right > left) {
                return if (s[left] != s[right]) false else isPalindrome(left + 1, right - 1)
            }
            return true
        }

        fun dfs(start: Int) {
            if (start == s.length) {
                result.add(ArrayList(list))
                return
            }

            for (end in start until s.length) {
                if (isPalindrome(start, end)) {
                    list.add(s.substring(start, end + 1))
                    dfs(end + 1)
                    list.removeAt(list.lastIndex)
                }
            }
        }

        dfs(0)
        return result
    }


    // LC 343. Integer Break
    // Approach 1: Mathematical Constant Pipeline (Pure Integer Math - Extreme Staff Level)
    // Time Complexity: O(Log N) | Auxiliary Space Complexity: O(1)
    // Consider: Since you utilized a greedy mathematical approach, can you explain why the number
    // 3 is more effective than 2 or 4?
    fun integerBreakMath(n: Int): Int {
        if (n == 2) return 1
        if (n == 3) return 2
        val quo = (n / 3).toDouble()
        val rem = n % 3

        return when (rem) {
            1 -> 3.0.pow(quo - 1.0).toInt() * 4
            2 -> 3.0.pow(quo).toInt() * 2
            else -> 3.0.pow(quo).toInt()
        }
    }

    // Approach 2: DP (Bottom-Up)
    // Time Complexity: O(N^2) | Auxiliary Space Complexity: O(N)
    fun integerBreakDP(n: Int): Int {
        val dp = IntArray(n + 1)
        dp[0] = 1
        dp[1] = 1

        for (i in 2 .. n) {
            for (j in 1 until i) {
                dp[i] = maxOf(dp[i], maxOf(j * (i - j), j * dp[i - j]))
            }
        }

        return dp[n]
    }

}