package com.rshea.leetcodeprep

import kotlin.math.*

object Week10DP {

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