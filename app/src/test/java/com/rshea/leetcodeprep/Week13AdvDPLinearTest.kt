package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week13AdvDPLinearTest {

    // Day 65 -  LC 188. Best Time to Buy and Sell Stock IV
    @Test
    fun testMaxProfit() {
        val prices1 = intArrayOf(2, 4, 1)
        val prices2 = intArrayOf(3, 2, 6, 5, 0, 3)

        assertEquals(2, Week13AdvDPLinear.maxProfitStaff(2, prices1))
        assertEquals(2, Week13AdvDPLinear.maxProfit(2, prices1))
        assertEquals(7, Week13AdvDPLinear.maxProfitStaff(2, prices2))
        assertEquals(7, Week13AdvDPLinear.maxProfit(2, prices2))
    }

    // Day 64 - LC 312. Burst Balloons
    @Test
    fun testMaxCoins() {
        val nums1 = intArrayOf(3, 1, 5, 8)
        val nums2 = intArrayOf(1, 5)
        val nums3 = intArrayOf(5, 8)
        val nums4 = intArrayOf(0, 3, 0, 1, 5, 0, 8, 0) // Array with zeros

        assertEquals(167, Week13AdvDPLinear.maxCoins(nums1))
        assertEquals(10, Week13AdvDPLinear.maxCoins(nums2))
        assertEquals(48, Week13AdvDPLinear.maxCoins(nums3))
        assertEquals(167, Week13AdvDPLinear.maxCoins(nums4))

        assertEquals(167, Week13AdvDPLinear.maxCoinsZeroPruned(nums1))
        assertEquals(10, Week13AdvDPLinear.maxCoinsZeroPruned(nums2))
        assertEquals(48, Week13AdvDPLinear.maxCoinsZeroPruned(nums3))
        assertEquals(167, Week13AdvDPLinear.maxCoinsZeroPruned(nums4))

        assertEquals(167, Week13AdvDPLinear.maxCoinsMemoization(nums1))
        assertEquals(10, Week13AdvDPLinear.maxCoinsMemoization(nums2))
        assertEquals(48, Week13AdvDPLinear.maxCoinsMemoization(nums3))
        assertEquals(167, Week13AdvDPLinear.maxCoinsMemoization(nums4))
    }

    // Day 63 - LC 131. Palindrome Partitioning
    @Test
    fun testPartition() {
        var expected = listOf(
            listOf("a", "a", "b"),
            listOf("aa", "b")
        )
        assertEquals(expected, Week13AdvDPLinear.partition("aab"))
        assertEquals(expected, Week13AdvDPLinear.partitionTwoPointers("aab"))

        expected = listOf(
            listOf("a", "a", "b", "b"),
            listOf("a", "a", "bb"),
            listOf("aa", "b", "b"),
            listOf("aa", "bb")
        )
        assertEquals(expected, Week13AdvDPLinear.partition("aabb"))
        assertEquals(expected, Week13AdvDPLinear.partitionTwoPointers("aabb"))

        expected = listOf(
            listOf("a", "b", "b", "a"),
            listOf("a", "bb", "a"),
            listOf("abba")
        )
        assertEquals(expected, Week13AdvDPLinear.partition("abba"))
        assertEquals(expected, Week13AdvDPLinear.partitionTwoPointers("abba"))

    }

    // Day 62 - LC 343. Integer Break
    @Test
    fun testIntegerBreak() {
        assertEquals(1, Week13AdvDPLinear.integerBreakMath(2))
        assertEquals(36, Week13AdvDPLinear.integerBreakMath(10))
        assertEquals(1549681956, Week13AdvDPLinear.integerBreakMath(58))

        assertEquals(1, Week13AdvDPLinear.integerBreak(2))
        assertEquals(36, Week13AdvDPLinear.integerBreak(10))
        assertEquals(1549681956, Week13AdvDPLinear.integerBreak(58))

        assertEquals(1, Week13AdvDPLinear.integerBreak1D(2))
        assertEquals(36, Week13AdvDPLinear.integerBreak1D(10))
        assertEquals(1549681956, Week13AdvDPLinear.integerBreak1D(58))
    }

}