package com.rshea.leetcodeprep

import kotlin.math.*

object Week10DP {

    // Day 49 - LC 62. Unique Paths
    // Approach 1: Bottom-Up Dynamic Programming (1D Table)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(n)
    fun uniquePaths(m: Int, n: Int): Int {
        val dp = IntArray(n)
        for (i in 0 until m) {
            for (j in 0 until n) {
                if (i == 0 || j == 0) {
                    dp[j] = 1
                } else {
                    dp[j] = dp[j] + dp[j - 1]
                }
            }
        }
        return dp[n - 1]
    }

    // Approach 2: Zero-Allocation Primitive Combinatorics
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

    // Approach 3: BigInteger Combinatorics
    // Time Complexity: O(min(m, n)) | Auxiliary Space Complexity: O(1)
    fun uniquePathsCombinatorics(m: Int, n: Int): Int {
        val totalSlots = m + n - 2
        val optimalMoves = minOf(m - 1, n - 1)

        var steps = 1.toBigInteger()
        val lowerBound = totalSlots - optimalMoves + 1
        for (i in totalSlots downTo lowerBound) {
            steps = steps.times(i.toBigInteger())
        }

        for (i in optimalMoves downTo 2) {
            steps = steps.div(i.toBigInteger())
        }

        return steps.toInt()
    }


    // Day 48 - LC 139. Word Break
    // Approach 1: Bottom-Up DP + Max Word Length Pruning (Staff Level Optimal)
    // Time Complexity: O(N * L^2) where L = max word length | Auxiliary Space Complexity: O(N + M)
    fun wordBreakOptimal(s: String, wordDict: List<String>): Boolean {
        if (s.isEmpty() || wordDict.isEmpty()) return false

        val wordSet = wordDict.toHashSet()
        val maxLen = wordDict.maxOf { it.length }
        val dp = BooleanArray(s.length + 1)
        dp[0] = true

        for (i in 1..s.length) {
            // Prune search space: only check inner split j up to max word length
            for (j in (i - 1) downTo maxOf(0, i - maxLen)) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true
                    break
                }
            }
        }

        return dp[s.length]
    }

    // Approach 2: Bottom-Up DP (Baseline)
    // Time Complexity: O(N² × M) time and Auxiliary Space Complexity: O(N + M × k)
    fun wordBreak(s: String, wordDict: List<String>): Boolean {
        val n = s.length + 1
        val dp = BooleanArray(n)
        val wordSet = HashSet<String>()
        dp[0] = true

        for (word in wordDict) {
            wordSet.add(word)
        }

        for (i in 1 .. s.length) {
            for (j in 0 until i) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true
                    break
                }
            }
        }
        return dp[s.length]
    }
    //    Detailed Senior / Staff Level Review
    //
    //    1. Algorithmic Correctness & DP State Definition
    //      • State Definition: DP[i] = Boolean indicating whether the substring s[0...i-1] can be segmented into dictionary words.
    //      • Base Case: DP[0] = true (empty string is always valid).
    //      • State Transition: DP[i] = [Any split point j where 0 ≤ j < i] (DP[j] AND wordSet.contains(s[j...i-1]))
    //      • Output: Correctly returns DP[s.length].
    //
    //    2. Performance & Optimization Opportunities (Staff Level)
    //      1. HashSet Initialization Idiom:
    //          ◦ Current: val wordSet = HashSet<String>(); for (word in wordDict) { wordSet.add(word) }
    //          ◦ Idiomatic Kotlin: val wordSet = wordDict.toHashSet() or val wordSet = HashSet(wordDict).
    //      2. Substring Range Limiting via Max Word Length:
    //         ◦ Problem: In the inner loop for (j in 0 until i), j iterates from 0 up to i.
    //         ◦ This creates substrings s[j...i-1] of lengths up to i ≤ N.
    //         ◦ Performance Bottleneck: s.substring(j, i) creates a new String heap object on every
    //         inner iteration!
    //         ◦ Total String allocations ≈ 𝒪(N²) short-lived objects.
    //         ◦ Optimization 1 (Max Word Length Bound):
    //            ▪ Find the length of the longest word in the dictionary:
    //                  val maxLen = wordDict.maxOf { it.length }.
    //            ▪ Limit j to iterate only down from i-1 down to maxOf(0, i - maxLen).
    //            ▪ Impact: Reduces inner loop iterations from 𝒪(N) per outer step down to 𝒪(L)
    //            (where L ≤ 20 is max word length). This improves time complexity from 𝒪(N³)
    //            (including 𝒪(N) string slicing/hashing) down to 𝒪(N · L²)!
    //      3. Prefix Tree (Trie) / Aho-Corasick Alternative for Zero Substring Allocations:
    //         ◦ Alternative: Instead of calling s.substring(j, i) (which allocates new heap Strings),
    //         build a Trie of dictionary words.
    //         ◦ Benefit: Traversing characters in s directly on the Trie checks word matches without
    //         creating any substring objects!


    // Day 47 - LC 198. House Robber
    // Approach 1: Bottom-Up Dynamic Programming (In-Place)
    // Time Complexity: O(n) | Auxiliary Space Complexity: O(1)
    fun robInPlace(nums: IntArray): Int {
        val n = nums.size
        if (n < 2) return nums[0]

        var prev2 = nums[0]
        var prev1 = nums[1]
        for (i in 2 until n) {
            val skip = prev1
            val rub = maxOf(prev1, prev2 + nums[i])
            prev2 = max(prev1, prev2)
            prev1 = max(skip, rub)
        }

        return if (n == 2) max(prev1, prev2) else prev1
    }
    //    BUG ALERT in robInPlace:
    //    • Look at prev1 = nums[1].
    //    • For nums = intArrayOf(2, 1, 1, 2):
    //      ◦ prev2 = 2, prev1 = 1.
    //      ◦ Max profit at house index 1 should be max(nums[0], nums[1]) = max(2, 1) = 2! But prev1
    //      was set to 1 (just nums[1]), ignoring nums[0]!
    //      ◦ Loop $i=2$ (nums[2] = 1):
    //          ▪ rub = maxOf(prev1, prev2 + nums[2]) = maxOf(1, 2 + 1) = 3.
    //          ▪ prev2 = max(1, 2) = 2.
    //          ▪ prev1 = max(1, 3) = 3.
    //      ◦ Loop $i=3$ (nums[3] = 2):
    //          ▪rub = maxOf(3, 2 + 2) = 4.
    //      ◦ Result becomes wrong for certain test cases!
    //
    //    • Canonical & Idiomatic O(1) Space House Robber: At any house $i$, we have two choices:
    //      i. Rob house i: Total loot = prev2 + nums[i]
    //      ii. Skip house i: Total loot = prev1, curr = max(prev1, prev2 + nums[i])
    //
    //    If we initialize prev2 = 0 (max loot before house 0) and prev1 = 0 (max loot at current house),
    //    the loop handles ALL $n \ge 1$ cleanly without edge cases:
    //
    //    fun robInPlace(nums: IntArray): Int {
    //          var prev2 = 0
    //          var prev1 = 0
    //          for (num in nums) {
    //              val curr = maxOf(prev1, prev2 + num)
    //              prev2 = prev1
    //              prev1 = curr
    //          }
    //          return prev1
    //    }
    //    Look how elegant and clean this 6-line O(1) space implementation is! It handles n = 0, 1,
    //    2, ... seamlessly with ZERO special branching!

    // Approach 2: Bottom-Up Dynamic Programming (Table)
    // Time Complexity: O(n) | Auxiliary Space Complexity: O(n)
    fun robTable(nums: IntArray): Int {
        val dp = IntArray(nums.size)

        dp[0] = nums[0]
        if (nums.size > 1) dp[1] = maxOf(dp[0], nums[1])
        for (i in 2 until nums.size) {
            val skip = dp[i - 1]
            val rub = maxOf(dp[i - 1], dp[i - 2] + nums[i])
            dp[i] = max(skip, rub)
        }

        return dp[dp.lastIndex]
    }
    //    Review Findings:
    //
    //    1. Correctness:
    //      ◦ dp[0] = nums[0], dp[1] = maxOf(nums[0], nums[1]).
    //      ◦ dp[i] = maxOf(dp[i - 1], dp[i - 2] + nums[i]).
    //      ◦ Correctly returns dp[dp.lastIndex].
    //
    //    2. Syntax Cleanup:
    //      ◦ In the loop: val skip = dp[i - 1]; val rub = maxOf(dp[i - 1], dp[i - 2] + nums[i]);
    //      dp[i] = max(skip, rub) repeats dp[i-1] comparisons.
    //      ◦ Simply: dp[i] = maxOf(dp[i - 1], dp[i - 2] + nums[i]).

    // Approach 3: Top-Down Recursion with Memoization
    // Time Complexity: O(n) | Auxiliary Space Complexity: O(1)
    fun robTailRec(nums: IntArray): Int {
        val n = nums.size

        tailrec fun robEx(i: Int, b: Int, a: Int): Int {
            if (i == 0) return a
            if (i == 1) return robEx(0, b, max(a, b))

            return robEx(
                i - 1,
                max(b, a),
                max(a, b + nums[n - i + 1])
            )
        }

        return robEx(n - 1, nums[0], if (n < 2) nums[0] else nums[1])
    }
    //    Review Findings:
    //
    //    1. Index Alignment & State Variables:
    //
    //      ◦ Tracking nums[n - i + 1] with reverse index count makes tracing difficult.
    //      ◦ Passing prev2 and prev1 directly as tail parameters moving forward through indices 0 .. N-1:
    //          fun robTailRec(nums: IntArray): Int {
    //              tailrec fun helper(idx: Int, prev2: Int, prev1: Int): Int {
    //                  if (idx == nums.size) return prev1
    //                  return helper(idx + 1, prev1, maxOf(prev1, prev2 + nums[idx]))
    //              }
    //              return helper(0, 0, 0)
    //          }
    //
    //      ◦ Look at helper(idx + 1, prev1, maxOf(prev1, prev2 + nums[idx])):
    //          ▪ prev2 becomes the old prev1.
    //          ▪ prev1 becomes maxOf(prev1, prev2 + nums[idx]).
    //      ◦ This matches the 2-variable iterative DP state transition exactly!
    //      ◦ Pure O(N) time, O(1) space, and 100% stack-safe!

    // Day 46 - LC 1137. N-th Tribonacci Number
    // Approach 1: Top-Down Recursion with Memoization
    // Time Complexity: O(n) | Auxiliary Space Complexity: O(1)
    fun tribonacciTailRec(n: Int): Int {

        tailrec fun tribonacciEx(i: Int, c: Int, b: Int, a: Int): Int {
            return if (i > 2) tribonacciEx(i - 1, a + b + c, c, b) else
                if (i == 2) c else if (i == 1) b else a
        }

        return tribonacciEx(n, 1, 1, 0)
    }
    //    Code Style Cleanup:
    //      ◦ Using chained ternary if ... else if ... else on one line can be cleaned up using
    //      idiomatic Kotlin when expression:
    //            tailrec fun tribonacciEx(i: Int, c: Int, b: Int, a: Int): Int {
    //                return when {
    //                    i > 2 -> tribonacciEx(i - 1, a + b + c, c, b)
    //                    i == 2 -> c
    //                    i == 1 -> b
    //                    else -> a
    //                }
    //            }


    // Day 46 - LC 509. Fibonacci Number
    // Approach 1: Binet's Closed-Form Formula
    // Time Complexity: O(1) | Auxiliary Space Complexity: O(1)
    // Consider 1: If n were significantly larger, say one million, how would you calculate the result
    // modulo 10 to the 9 plus 7 in logarithmic time or change your strategy to compute the result
    // modulo m efficiently?
    // Consider 2: Since you mastered the linear approach, could you think of a way to solve this in
    // logarithmic time using matrix exponentiation?
    // Consider 3: Since you used the mathematical approach, how would you handle potential precision
    // issues if the input n was significantly larger?
    fun fibBitnet(n: Int): Int {
        val sqrt5 = sqrt(5.0)
        val phi = (1.0 + sqrt5) / 2

        return round(phi.pow(n) / sqrt5).toInt()
    }


    // Approach 2: Top-Down Recursion with Memoization
    // Time Complexity: O(n) | Auxiliary Space Complexity: O(1)
    fun fibTailRec(n: Int): Int {

        tailrec fun fibEx(times: Int, b: Int, a: Int): Int {
            if (times == 0) return b
            return fibEx(times - 1, a, a + b)
        }

        return fibEx(n, 0, 1)
    }

    // Day 46 - LC 70. Climbing Stairs
    // Approach 1: Bottom-Up Dynamic Programming (Table)
    // Time Complexity: O(n) | Auxiliary Space Complexity: O(n)
    // Consider: If n were very large, could you find a way to calculate the result in logarithmic
    // time using matrix exponentiation?
    fun climbStairsTable(n: Int): Int {
        val table = IntArray(n + 1)
        table[0] = 1
        table[1] = 1

        fun findDistinctWays(steps: Int) {
            for (i in 2 .. steps) {
                table[i] = table[i - 2] + table[i - 1]
            }
        }

        findDistinctWays(n)
        return table[n]
    }
    //    Review Findings:
    //    1. Correctness & Mathematical Invariant:
    //    ◦ Recurrence relation: DP[i] = DP[i-1] + DP[i-2] (Fibonacci sequence offset by 1 step).
    //    ◦ DP[0] = 1 (base case: 1 way to be at ground), DP[1] = 1 (1 step to step 1).
    //    ◦ Correctly calculates DP[n] in 𝒪(N) time and 𝒪(N) space.
    //    2. Syntax & Code Structure:
    //    ◦ Wrapping the for loop in an inner local function findDistinctWays(n) is unnecessary here
    //    because the function doesn't recurse—it just runs a single loop. Writing the for loop
    //    directly inside climbStairsTable simplifies the bytecode and improves readability.

    // Approach 2: Bottom-Up Dynamic Programming (In-Place)
    // Time Complexity: O(n) | Auxiliary Space Complexity: O(1)
    // Consider: Since you mastered the linear approach, could you find a way to reach the answer in
    // logarithmic time using matrix exponentiation?
    fun climbStairsInPlace(n: Int): Int {
        var secondPrevNumber = 1
        var firstPrevNumber = 1

        fun findDistinctWays(steps: Int): Int {
            for (i in 3 .. steps) {
                val sum = secondPrevNumber + firstPrevNumber
                secondPrevNumber = firstPrevNumber
                firstPrevNumber = sum
            }
            return secondPrevNumber + firstPrevNumber
        }


        return if (n == 1) firstPrevNumber else findDistinctWays(n)
    }
    //    Review Findings:
    //    1. Index Alignment & Loop Boundary:
    //    ◦ Look at line 4: for (i in 3 .. steps).
    //    ◦ Why start at i = 3?
    //    ◦ If steps = 2:
    //      ▪ The loop for (i in 3 .. 2) does not execute.
    //      ▪ findDistinctWays(2) returns secondPrevNumber + firstPrevNumber = $1 + 1 = 2$.
    //      ▪ Correct result for $n = 2$!
    //    ◦ If steps = 1:
    //      ▪ Handled by if (n == 1) firstPrevNumber else ... -> returns $1$. Correct!
    //    ◦ Simplification / Idiomatic Cleanup: Instead of i in 3..steps with special return expressions:
    //            fun climbStairsInPlace(n: Int): Int {
    //                if (n <= 1) return 1
    //                var prev2 = 1 // ways(0)
    //                var prev1 = 1 // ways(1)
    //                for (i in 2..n) {
    //                    val curr = prev1 + prev2
    //                    prev2 = prev1
    //                    prev1 = curr
    //                }
    //                return prev1
    //            }
    //      This uniform 2 * n loop eliminates inner functions, if (n == 1) ternary conditions,
    //      and index confusion!



    // Approach 3: Top-Down Recursion with Memoization
    // Time Complexity: O(n) | Auxiliary Space Complexity: O(1)
    // Consider: Since you mastered the linear approach, could you think of how to solve this in
    // logarithmic time for much larger values of n?
    fun climbStairsTailRec(n: Int): Int {

        tailrec fun findDistinctWays(steps: Int, a: Int, b:Int): Int {
            if (steps == 1) return b
            return findDistinctWays(steps - 1, b, a + b)
        }

        return findDistinctWays(n, 1, 1)
    }
    //    Review Findings:
    //    1. The tailrec Kotlin Keyword Optimization (Staff Level):
    //        ◦ How tailrec works: In Kotlin, marking a recursive function as tailrec instructs the Kotlin compiler to transform tail recursion into an iterative while loop in bytecode!
    //        ◦ Zero Stack Frames: Even though climbStairsTailRec looks recursive in Kotlin source code, the generated JVM bytecode uses zero call stack frames ($\mathcal{O}(1)$ auxiliary space)!
    //        ◦ Execution Mechanics:
    //            ▪ findDistinctWays(n, 1, 1):
    //                ▪ Step n: findDistinctWays(n-1, 1, 2)
    //                ▪ Step n-1: findDistinctWays(n-2, 2, 3)
    //                ▪ Step 1: returns b.
    //            ▪ Pure{O(N) time, O(1) space, and 100% StackOverflowError safe.


    // Approach 4: Binet's Closed-Form Formula
    // Time Complexity: O(Log N) | Auxiliary Space Complexity: O(1)
    fun climbStairsBinetClosedFormFormula(n: Int): Int {
        val rootOfFive = sqrt(5.0)
        val phi = (1.0 + rootOfFive) / 2
        val psi = (1.0 - rootOfFive) / 2

        return ((phi.pow(n + 1) - psi.pow(n + 1)) / rootOfFive).toInt()
    }

}