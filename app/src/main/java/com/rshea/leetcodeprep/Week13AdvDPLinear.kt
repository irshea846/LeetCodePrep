package com.rshea.leetcodeprep

import kotlin.math.pow
object Week13AdvDPLinear {

    // Day 65 - LC 188. Best Time to Buy and Sell Stock IV
    // Approach 1: Bottom-Up Dynamic Programming (2 1D Tables + Fast-Path Guard - Staff Level)
    // Time Complexity: O(min(k, n/2) * n) | Auxiliary Space Complexity: O(min(k, n/2))
    fun maxProfitStaff(k: Int, prices: IntArray): Int {
        val n = prices.size
        if (n <= 1 || k <= 0) return 0

        // Unlimited transactions fast-path: if k >= n / 2, greedy sum positive gains (LC 122)
        if (k >= n / 2) {
            var maxProfit = 0
            for (i in 1 until n) {
                if (prices[i] > prices[i - 1]) {
                    maxProfit += prices[i] - prices[i - 1]
                }
            }
            return maxProfit
        }

        val buy = IntArray(k + 1) { Int.MIN_VALUE / 2 } // Overflow-safe initial min value
        val sell = IntArray(k + 1)

        for (price in prices) {
            for (j in 1..k) {
                buy[j] = maxOf(buy[j], sell[j - 1] - price)
                sell[j] = maxOf(sell[j], buy[j] + price)
            }
        }

        return sell[k]
    }

    // Approach 2: Bottom-Up Dynamic Programming (2 1D Array)
    // Time Complexity: O(N x K) | Auxiliary Space Complexity: O(K)
    fun maxProfit(k: Int, prices: IntArray): Int {
        val buy = IntArray(k + 1) { Int.MIN_VALUE }
        val sell = IntArray(k + 1)

        for (price in prices) {
            for (j in 1 .. k) {
                buy[j] = maxOf(buy[j], sell[j - 1] - price)
                sell[j] = maxOf(sell[j], buy[j] + price)
            }
        }
        return maxOf(buy[buy.lastIndex], sell[sell.lastIndex])
    }
    // Code Analysis 1: maxProfit(k: Int, prices: IntArray): Int (Bottom-Up 2 1D Table)
    //    1. Unlimited Transactions Fast-Path Branch (k ≥ N / 2)
    //
    //    • The Optimization:
    //      ◦ If k ≥ N / 2, transaction capacity $k$ is greater than or equal to the maximum possible
    //        profitable transactions in an $N$-day price sequence.
    //      ◦ This collapses the problem into LC 122. Best Time to Buy and Sell Stock II (Unlimited
    //        Transactions)!
    //      ◦ We can greedily sum every positive price gain (prices[i] - prices[i-1] > 0) in O(N)
    //        time and O(1) space without running the inner loop over $k$ or allocating 2 * (k+1)
    //        arrays!
    //      ◦ if (k >= n / 2) greedy exit saves significant memory and time when k is large!
    //
    //    2. 32-Bit Integer Overflow Guard (Int.MIN_VALUE):
    //    • Notice: sell[j - 1] - price.
    //    • If sell[j - 1] = 0 (initial state) and price = 10, sell[j - 1] - price = -10 >
    //      Int.MIN_VALUE.
    //    • But if sell[j - 1] was ever set or subtracted from a large value, or if initialized to
    //      Int.MIN_VALUE / 2, initializing buy array to -10^9 or Int.MIN_VALUE / 2 avoids 32-bit
    //      Integer Underflow!
    //    • In maxProfit, initializing buy with Int.MIN_VALUE works safely because sell[j - 1] -
    //      price evaluates 0 - price, which sets buy[j] = -price on the first day!
    //
    //    3. Result Return Simplification:
    //    • sell[k] is guaranteed to be $\ge buy[k]$ because selling after holding a stock yields
    //      non-negative profit.
    //    • Returning sell[minOf(k, n / 2)] directly avoids evaluating maxOf(buy.lastIndex, sell.lastIndex)!

    // Day 64 - LC 312. Burst Balloons
    // Approach 1: Bottom-Up Dynamic Programming + Zero-Filtering Pruning (Staff Level Extreme)
    // Time Complexity: O(N'^3) where N' <= N non-zeros | Auxiliary Space Complexity: O(N'^2)
    fun maxCoinsZeroPruned(nums: IntArray): Int {
        // Zero Pruning: Filter out zeros (0 coins earned, zero impact on adjacent product calculations)
        val nonZeros = nums.filter { it > 0 }.toIntArray()
        val n = nonZeros.size
        val numsEx = IntArray(n + 2) { i -> if (i == 0 || i == n + 1) 1 else nonZeros[i - 1] }
        val dp = Array(n + 2) { IntArray(n + 2) }

        for (i in n - 1 downTo 0) {
            for (j in i + 2..n + 1) {
                val outerCoins = numsEx[i] * numsEx[j]
                for (k in i + 1 until j) {
                    dp[i][j] = maxOf(dp[i][j], dp[i][k] + dp[k][j] + (outerCoins * numsEx[k]))
                }
            }
        }

        return dp[0][n + 1]
    }

    // Approach 2: Bottom-Up Dynamic Programming (2D Table)
    // Time Complexity: O(N^3) | Auxiliary Space Complexity: O(N^2)
    fun maxCoins(nums: IntArray): Int {
        val n = nums.size
        val numsEx = IntArray(n + 2) { i -> if (i == 0 || i == n + 1) 1 else nums[i - 1] }
        val dp = Array(n + 2) { IntArray(n + 2) }

        for (i in n - 1 downTo 0) {
            for (j in i + 2..n + 1) {
                val outerCoins = numsEx[i] * numsEx[j]
                for (k in i + 1 until j) {
                    dp[i][j] = maxOf(dp[i][j], dp[i][k] + dp[k][j] + (outerCoins * numsEx[k]))
                }
            }
        }

        return dp[0][n + 1]
    }

    // Approach 3: Top-Down Memoized DFS (Interval Divide & Conquer)
    // Time Complexity: O(N^3) | Auxiliary Space Complexity: O(N^2)
    fun maxCoinsMemoization(nums: IntArray): Int {
        val n = nums.size
        val numsEx = IntArray(n + 2) { i -> if (i == 0 || i == n + 1) 1 else nums[i - 1] }
        val memo = Array(n + 2) { IntArray(n + 2) }

        fun solve(i: Int, j: Int): Int {
            if (i + 1 >= j) return 0
            if (memo[i][j] != 0) return memo[i][j]

            var maxResult = 0
            val outerCoins = numsEx[i] * numsEx[j]
            for (k in i + 1 until j) {
                val coins = solve(i, k) + solve(k, j) + (outerCoins * numsEx[k])
                maxResult = maxOf(maxResult, coins)
            }

            memo[i][j] = maxResult
            return maxResult
        }

        return solve(0, n + 1)
    }

    // Day 63 - LC 131. Palindrome Partitioning
    // Approach 1: Backtracking (DFS)
    // Time Complexity: O(N × 2ⁿ) | Auxiliary Space Complexity: O(N²)
    fun partition(s: String): List<List<String>> {
        val list = mutableListOf<List<String>>()
        val n = s.length
        val isPalindrome = Array(n) { BooleanArray(n) }

        for (i in n - 1 downTo 0) {
            for (j in i until n) {
                isPalindrome[i][j] = s[i] == s[j] && (j - i <= 2 || isPalindrome[i + 1][j - 1])
            }
        }

        fun backtrack(i: Int, sublist: MutableList<String>) {
            if (i == n) {
                list.add(ArrayList(sublist))
                return
            }

            for (j in i until n) {
                if (isPalindrome[i][j]) {
                    sublist.add(s.substring(i, j + 1))
                    backtrack(j + 1, sublist)
                    sublist.removeAt(sublist.lastIndex)
                }
            }
        }

        backtrack(0, mutableListOf())
        return list
    }
    // Code Analysis 1: partition(s: String): List<List<String>> (Backtracking + 2D DP)
    //    1. 2D DP Palindrome Matrix Pre-computation (O(N²) Memoization)
    //      * The Highlight:
    //          ◦ You pre-computed the 2D DP table isPalindrome[i][j] in O(N²) time!
    //          ◦ Recurrence Relation:
    //              isPalindrome[i][j] = (s[i] == s[j]) && (j - i ≤ 2 || isPalindrome[i + 1][j - 1])
    //          ◦ Why this is great: Without 2D DP pre-computation, verifying if s[ i … j ] is a
    //          palindrome inside the backtracking loop takes O(N) per candidate branch, increasing
    //          total time from O(N × 2ⁿ) to O(N² × 2ⁿ).
    //          ◦ Pre-computing the 2D DP matrix reduces palindrome validity checks during
    //          backtracking to O(1)!
    //    2. Memory Allocation & Garbage Collection Optimizations (Staff Level)
    //      * Array(n) { BooleanArray(n) } Heap Churn:
    //          ◦ Array(n) { BooleanArray(n) } allocates N+1 separate heap objects.
    //          ◦ For N <= 16, we can use a Flat 1D BooleanArray(n * n) or a Bitmask/BitSet!
    //      * On-The-Fly Dynamic Backtracking with Pre-computed Palindrome Expansion (Zero O(N²)
    //      Pre-computation Allocation):
    //          ◦ Alternatively, we can check palindromes on-the-fly during backtracking using
    //          expanding two-pointers or memoization, OR pre-compute isPalindrome table using flat
    //          1D boolean array.
    //          ◦ Even cleaner: In Kotlin, pre-populating
    //              val isPalindrome = Array(n) { BooleanArray(n) }
    //          and pre-allocating path as ArrayList<String>(n) avoids sublist re-allocations in
    //          recursive calls.
    //      * s.substring(i, j + 1) Object Churn:
    //          ◦ s.substring(i, j + 1) creates a String heap object every time a palindrome is
    //          explored.
    //          ◦ Since we must output List<String>, s.substring is necessary for the final output
    //          lists, but pre-allocating the single mutable stack path (ArrayList<String>(n)) across
    //          recursive frames avoids object allocations per level.


    // Approach 2: Backtracking + On-the-Fly Two-Pointers (O(N) Space)
    // Time Complexity: O(N * 2^N) | Auxiliary Space Complexity: O(N) recursion stack
    fun partitionTwoPointers(s: String): List<List<String>> {
        val result = mutableListOf<List<String>>()
        val n = s.length
        val path = ArrayList<String>(n)

        fun isPalindrome(l: Int, r: Int): Boolean {
            var left = l
            var right = r
            while (left < right) {
                if (s[left++] != s[right--]) return false
            }
            return true
        }

        fun backtrack(start: Int) {
            if (start == n) {
                result.add(ArrayList(path))
                return
            }

            for (j in start until n) {
                if (isPalindrome(start, j)) {
                    path.add(s.substring(start, j + 1))
                    backtrack(j + 1)
                    path.removeAt(path.lastIndex)
                }
            }
        }

        backtrack(0)
        return result
    }


    // Day 62 - LC 343. Integer Break
    // Approach 1: Mathematical Constant Pipeline (Pure Integer Math - Extreme Staff Level)
    // Time Complexity: O(1) | Auxiliary Space Complexity: O(1)
    fun integerBreakMath(n: Int): Int {
        if (n <= 3) return n - 1

        fun pow3(exp: Int): Int {
            var res = 1
            var base = 3
            var e = exp
            while (e > 0) {
                if (e % 2 == 1) res *= base
                base *= base
                e /= 2
            }
            return res
        }

        return when (n % 3) {
            0 -> pow3(n / 3)
            1 -> pow3((n - 4) / 3) * 4
            else -> pow3(n / 3) * 2
        }
    }

    // Approach 2: Mathematical Constant Pipeline (Math.pow)
    // Time Complexity: O(1) | Auxiliary Space Complexity: O(1)
    fun integerBreak(n: Int): Int {
        if (n <= 3) return n - 1
        return when (n % 3) {
            0 -> 3.0.pow(n / 3).toInt()
            1 -> 3.0.pow((n - 4) / 3).toInt() * 4
            else -> 3.0.pow(n / 3).toInt() * 2
        }
    }
    // Code Analysis 1: integerBreak(n: Int): Int (Mathematical Constant / Modulo 3 Pipeline)
    //  Staff-Level Review Findings for integerBreak (Mathematical O(1) Approach):
    //    1. Mathematical Invariant & Euler's Constant (e ≈ 2.718):
    //      ◦ The derivative of f(x) = x^(N / x) is maximized when x = e ≈ 2.718$.
    //      ◦ Since n must be partitioned into integer factors, the integer closest to e is 3!
    //      ◦ Therefore, to maximize the product ∏ (a_i) such that ∑(a_i) = N:
    //          ▪ Break n into as many 3s as possible.
    //          ▪ Remainder Cases:
    //              ▪ Remainder 0: Multiply 3^(N / 3).
    //              ▪ Remainder 1: 3 × 1 = 3 < 4 = 2 × 2. Replace one 3 and remainder 1 with
    //              2 × 2 = 4. Product = 3^((N - 4) / 3) × 4.
    //              ▪ Remainder 2: Multiply 3^(N / 3) * 2.
    //      ◦ Base Cases: For N ≤ 3, must split into at least k ≥ 2 parts:
    //          ▪ n = 2 ⇒ 1 + 1 ⇒ 1 × 1 = 1 = (n - 1).
    //          ▪ n = 3 ⇒ 2 + 1 ⇒ 2 × 1 = 2 = (n - 1).
    //    2. Floating-Point Power Overhead vs. Fast Integer Exponentiation (O(1) or O(log N)):
    //      ◦ 3.0.pow(...) converts integer to double, invokes IEEE 754 floating-point exponentiation
    //      library pow, and casts back .toInt().
    //      ◦ Pure Integer Exponentiation / pow3: Because N ≤ 58 in LC 343, we can either:
    //          ▪ Use binary integer exponentiation: fun pow3(exp: Int): Int in pure integer
    //          arithmetic (zero floating-point conversion).
    //          ▪ Or compute bitwise exponentiation in O(1) time with integer math!



    // Approach 3: Bottom-Up Dynamic Programming (1D Table)
    // Time Complexity: O(n^2) | Auxiliary Space Complexity: O(n)
    fun integerBreak1D(n: Int): Int {
        val dp = IntArray(n + 1)

        for (i in 2 until dp.size) {
            for (j in 1 until i) {
                dp[i] = maxOf(dp[i], maxOf(j * (i - j), j * dp[i - j]))
            }
        }

        return dp[n] // dp[n] is direct and clearer than dp[dp.lastIndex]
    }
    // Code Analysis 2: integerBreak1D(n: Int): Int (Bottom-Up 1D DP)
    //    Review Findings for integerBreak1D:
    //    1. Inner Loop Pruning:
    //      ◦ In for (j in 1 until i), notice that factor j only needs to be checked up to j ≤ 3
    //      (specifically j = 2 and j = 3) because any number j ≥ 4 can be broken into 2 × (j - 2) ≥ j!
    //      ◦ Limiting j ∈ 1 ... min(i - 1, 3) cuts DP inner iterations from O(N^2) down to O(N)!
    //    2. Idiomatic Index Access:
    //      ◦ dp[n] is direct and clearer than dp[dp.lastIndex].

}


//    2 , 1 - [0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0]
//    3 , 1 - [0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0]
//    3 , 2 - [0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0]
//    4 , 1 - [0, 0, 1, 2, 3, 0, 0, 0, 0, 0, 0]
//    4 , 2 - [0, 0, 1, 2, 4, 0, 0, 0, 0, 0, 0]
//    4 , 3 - [0, 0, 1, 2, 4, 0, 0, 0, 0, 0, 0]
//    5 , 1 - [0, 0, 1, 2, 4, 4, 0, 0, 0, 0, 0]
//    5 , 2 - [0, 0, 1, 2, 4, 6, 0, 0, 0, 0, 0]
//    5 , 3 - [0, 0, 1, 2, 4, 6, 0, 0, 0, 0, 0]
//    5 , 4 - [0, 0, 1, 2, 4, 6, 0, 0, 0, 0, 0]
//    6 , 1 - [0, 0, 1, 2, 4, 6, 6, 0, 0, 0, 0]
//    6 , 2 - [0, 0, 1, 2, 4, 6, 8, 0, 0, 0, 0]
//    6 , 3 - [0, 0, 1, 2, 4, 6, 9, 0, 0, 0, 0]
//    6 , 4 - [0, 0, 1, 2, 4, 6, 9, 0, 0, 0, 0]
//    6 , 5 - [0, 0, 1, 2, 4, 6, 9, 0, 0, 0, 0]
//    7 , 1 - [0, 0, 1, 2, 4, 6, 9, 9, 0, 0, 0]
//    7 , 2 - [0, 0, 1, 2, 4, 6, 9, 12, 0, 0, 0]
//    7 , 3 - [0, 0, 1, 2, 4, 6, 9, 12, 0, 0, 0]
//    7 , 4 - [0, 0, 1, 2, 4, 6, 9, 12, 0, 0, 0]
//    7 , 5 - [0, 0, 1, 2, 4, 6, 9, 12, 0, 0, 0]
//    7 , 6 - [0, 0, 1, 2, 4, 6, 9, 12, 0, 0, 0]
//    8 , 1 - [0, 0, 1, 2, 4, 6, 9, 12, 12, 0, 0]
//    8 , 2 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 0, 0]
//    8 , 3 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 0, 0]
//    8 , 4 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 0, 0]
//    8 , 5 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 0, 0]
//    8 , 6 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 0, 0]
//    8 , 7 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 0, 0]
//    9 , 1 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 18, 0]
//    9 , 2 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 24, 0]
//    9 , 3 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 0]
//    9 , 4 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 0]
//    9 , 5 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 0]
//    9 , 6 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 0]
//    9 , 7 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 0]
//    9 , 8 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 0]
//    10 , 1 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 27]
//    10 , 2 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 36]
//    10 , 3 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 36]
//    10 , 4 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 36]
//    10 , 5 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 36]
//    10 , 6 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 36]
//    10 , 7 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 36]
//    10 , 8 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 36]
//    10 , 9 - [0, 0, 1, 2, 4, 6, 9, 12, 18, 27, 36]
