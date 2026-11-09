package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW13AdvDPLinearTest {

    // LC 131. Palindrome Partitioning
    @Test
    fun testPartition() {
        assertEquals(listOf(listOf("a","a","b"), listOf("aa","b")), MasterW13AdvDPLinear.partitionDP("aab"))
        assertEquals(listOf(listOf("a","a","a","a"), listOf("a","a","aa"),
            listOf("a","aa","a"), listOf("a","aaa"), listOf("aa","a","a"),
            listOf("aa","aa"), listOf("aaa","a"), listOf("aaaa")), MasterW13AdvDPLinear.partitionDP("aaaa"))
        assertEquals(listOf(listOf("a")), MasterW13AdvDPLinear.partitionDP("a"))
        assertEquals(listOf(listOf("e","f", "e"), listOf("efe")), MasterW13AdvDPLinear.partitionDP("efe"))

        assertEquals(listOf(listOf("a","a","b"), listOf("aa","b")), MasterW13AdvDPLinear.partitionDFS("aab"))
        assertEquals(listOf(listOf("a","a","a","a"), listOf("a","a","aa"),
            listOf("a","aa","a"), listOf("a","aaa"), listOf("aa","a","a"),
            listOf("aa","aa"), listOf("aaa","a"), listOf("aaaa")), MasterW13AdvDPLinear.partitionDFS("aaaa"))
        assertEquals(listOf(listOf("a")), MasterW13AdvDPLinear.partitionDFS("a"))
        assertEquals(listOf(listOf("e","f", "e"), listOf("efe")), MasterW13AdvDPLinear.partitionDFS("efe"))

    }
    // LC 343. Integer Break
    @Test
    fun testIntegerBreak() {
        assertEquals(1, MasterW13AdvDPLinear.integerBreakMath(2))
        assertEquals(36, MasterW13AdvDPLinear.integerBreakMath(10))
        assertEquals(43046721, MasterW13AdvDPLinear.integerBreakMath(48))

        assertEquals(1, MasterW13AdvDPLinear.integerBreakDP(2))
        assertEquals(36, MasterW13AdvDPLinear.integerBreakDP(10))
        assertEquals(43046721, MasterW13AdvDPLinear.integerBreakDP(48))
    }
}