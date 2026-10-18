package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW10DPTest {

    // 322. Coin Change
    @Test
    fun testCoinChange() {
        assertEquals(3, MasterW10DP.coinChange(intArrayOf(1, 2, 5), 11))
        assertEquals(-1, MasterW10DP.coinChange(intArrayOf(2), 3))
        assertEquals(20, MasterW10DP.coinChange(intArrayOf(186, 419, 83, 408), 6249))
    }

    // 62. Unique Paths
    @Test
    fun testUniquePaths() {
        assertEquals(28, MasterW10DP.uniquePaths(3, 7))
        assertEquals(3, MasterW10DP.uniquePaths(3, 2))
        assertEquals(48620, MasterW10DP.uniquePaths(10, 10))

        assertEquals(28, MasterW10DP.uniquePathsCombinatorics(3, 7))
        assertEquals(3, MasterW10DP.uniquePathsCombinatorics(3, 2))
        assertEquals(48620, MasterW10DP.uniquePathsCombinatorics(10, 10))
    }

    // 139. Word Break
    @Test
    fun testWordBreak() {
        assertEquals(true, MasterW10DP.wordBreak("leetcode", listOf("leet", "code")))
        assertEquals(true, MasterW10DP.wordBreak("applepenapple", listOf("apple", "pen")))
        assertEquals(false, MasterW10DP.wordBreak("catsandog", listOf("cats", "dog", "sand", "and", "cat")))
    }

    // 198. House Robber
    @Test
    fun testRob() {
        assertEquals(4, MasterW10DP.rob(intArrayOf(1, 2, 3, 1)))
        assertEquals(12, MasterW10DP.rob(intArrayOf(2, 7, 9, 3, 1)))
        assertEquals(4, MasterW10DP.rob(intArrayOf(2, 1, 1, 2)))

        assertEquals(4, MasterW10DP.robSpaceO1(intArrayOf(1, 2, 3, 1)))
        assertEquals(12, MasterW10DP.robSpaceO1(intArrayOf(2, 7, 9, 3, 1)))
        assertEquals(4, MasterW10DP.robSpaceO1(intArrayOf(2, 1, 1, 2)))
    }

    // 1137. N-th Tribonacci Number
    @Test
    fun testTribonacciTailRec() {
        assertEquals(0, MasterW10DP.tribonacci(0))
        assertEquals(1, MasterW10DP.tribonacci(1))
        assertEquals(4, MasterW10DP.tribonacci(4))
        assertEquals(1389537, MasterW10DP.tribonacci(25))
    }

    // 509. Fibonacci Number
    @Test
    fun testFib() {
        assertEquals(0, MasterW10DP.fib(0))
        assertEquals(1, MasterW10DP.fib(1))
        assertEquals(1, MasterW10DP.fib(2))
        assertEquals(2, MasterW10DP.fib(3))
        assertEquals(3, MasterW10DP.fib(4))
    }

    // 70. Climbing Stairs
    @Test
    fun testClimbStairs() {
        assertEquals(2, MasterW10DP.climbStairs(2))
        assertEquals(3, MasterW10DP.climbStairs(3))
        assertEquals(1134903170, MasterW10DP.climbStairs(44))
    }

}