package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week10DPTest {

    // Day 48 - LC 139. Word Break
    @Test
    fun testWordBreak() {
        val testCase1 = "leetcode"
        val testCase2 = "applepenapple"
        val testCase3 = "catsandog"
        val testCase4 =
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"

        val wordDict1 = listOf("leet", "code")
        val wordDict2 = listOf("apple", "pen")
        val wordDict3 = listOf("cats", "dog", "sand", "and", "cat")
        val wordDict4 = listOf(
            "a",
            "aa",
            "aaa",
            "aaaa",
            "aaaaa",
            "aaaaaa",
            "aaaaaaa",
            "aaaaaaaa",
            "aaaaaaaaa",
            "aaaaaaaaaa"
        )

        assertEquals(true, Week10DP.wordBreakOptimal(testCase1, wordDict1))
        assertEquals(true, Week10DP.wordBreakOptimal(testCase2, wordDict2))
        assertEquals(false, Week10DP.wordBreakOptimal(testCase3, wordDict3))
        assertEquals(false, Week10DP.wordBreakOptimal(testCase4, wordDict4))

        assertEquals(true, Week10DP.wordBreak(testCase1, wordDict1))
        assertEquals(true, Week10DP.wordBreak(testCase2, wordDict2))
        assertEquals(false, Week10DP.wordBreak(testCase3, wordDict3))
        assertEquals(false, Week10DP.wordBreak(testCase4, wordDict4))
    }


    // Day 47 - LC 198. House Robber
    @Test
    fun testRob() {
        val testCase1 = intArrayOf(1, 2, 3, 1)
        val testCase2 = intArrayOf(2, 7, 9, 3, 1)
        val testCase3 = intArrayOf(2, 1, 1, 2)
        val testCase4 = intArrayOf(
            183, 219, 57, 193, 94, 233, 202, 154, 65, 240,
            97, 234, 100, 249, 186, 66, 90
        )
        val testCase5 = intArrayOf(0)
        val testCase6 = intArrayOf(2, 1)

        assertEquals(4, Week10DP.robInPlace(testCase1))
        assertEquals(12, Week10DP.robInPlace(testCase2))
        assertEquals(4, Week10DP.robInPlace(testCase3))
        assertEquals(1612, Week10DP.robInPlace(testCase4))
        assertEquals(0, Week10DP.robInPlace(testCase5))
        assertEquals(2, Week10DP.robInPlace(testCase6))


        assertEquals(4, Week10DP.robTable(testCase1))
        assertEquals(12, Week10DP.robTable(testCase2))
        assertEquals(4, Week10DP.robTable(testCase3))
        assertEquals(1612, Week10DP.robTable(testCase4))
        assertEquals(0, Week10DP.robTable(testCase5))
        assertEquals(2, Week10DP.robTable(testCase6))

        assertEquals(4, Week10DP.robTailRec(testCase1))
        assertEquals(12, Week10DP.robTailRec(testCase2))
        assertEquals(4, Week10DP.robTailRec(testCase3))
        assertEquals(1612, Week10DP.robTailRec(testCase4))
        assertEquals(0, Week10DP.robTailRec(testCase5))
        assertEquals(2, Week10DP.robTailRec(testCase6))

    }

    // Day 46 - LC 1137. N-th Tribonacci Number
    @Test
    fun testTribonacciTailRec() {
        assertEquals(4, Week10DP.tribonacciTailRec(4))
        assertEquals(1389537, Week10DP.tribonacciTailRec(25))
        assertEquals(2082876103, Week10DP.tribonacciTailRec(37))
    }

    // Day 46 - LC 509. Fibonacci Number
    @Test
    fun testFibBitnet() {
        assertEquals(1, Week10DP.fibBitnet(2))
        assertEquals(2, Week10DP.fibBitnet(3))
        assertEquals(5, Week10DP.fibBitnet(5))
        assertEquals(317811, Week10DP.fibBitnet(28))
        assertEquals(514229, Week10DP.fibBitnet(29))
        assertEquals(832040, Week10DP.fibBitnet(30))
    }

    // Day 46 - LC 70. Climbing Stairs
    @Test
    fun testClimbStairsTable() {
        assertEquals(2, Week10DP.climbStairsTable(2))
        assertEquals(3, Week10DP.climbStairsTable(3))
        assertEquals(701408733, Week10DP.climbStairsTable(43))
        assertEquals(1134903170, Week10DP.climbStairsTable(44))
        assertEquals(1836311903, Week10DP.climbStairsTable(45))
    }

    @Test
    fun testClimbStairsInPlace() {
        assertEquals(2, Week10DP.climbStairsInPlace(2))
        assertEquals(3, Week10DP.climbStairsInPlace(3))
        assertEquals(701408733, Week10DP.climbStairsInPlace(43))
        assertEquals(1134903170, Week10DP.climbStairsInPlace(44))
        assertEquals(1836311903, Week10DP.climbStairsInPlace(45))
    }

    @Test
    fun testClimbStairsTailRec() {
        assertEquals(2, Week10DP.climbStairsTailRec(2))
        assertEquals(3, Week10DP.climbStairsTailRec(3))
        assertEquals(701408733, Week10DP.climbStairsTailRec(43))
        assertEquals(1134903170, Week10DP.climbStairsTailRec(44))
        assertEquals(1836311903, Week10DP.climbStairsTailRec(45))
    }

    @Test
    fun testClimbStairsBinetClosedFormFormula() {
        assertEquals(2, Week10DP.climbStairsBinetClosedFormFormula(2))
        assertEquals(3, Week10DP.climbStairsBinetClosedFormFormula(3))
        assertEquals(701408733, Week10DP.climbStairsBinetClosedFormFormula(43))
        assertEquals(1134903170, Week10DP.climbStairsBinetClosedFormFormula(44))
        assertEquals(1836311903, Week10DP.climbStairsBinetClosedFormFormula(45))
    }
}