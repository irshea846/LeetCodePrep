package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week13AdvDPLinearTest {

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