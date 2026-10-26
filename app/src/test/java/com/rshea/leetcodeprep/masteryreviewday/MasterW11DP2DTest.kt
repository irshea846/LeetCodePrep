package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW11DP2DTest {

    // LC 1143. Longest Common Subsequence
    @Test
    fun longestCommonSubsequenceTest() {
        var text1 = "abcde"
        var text2 = "ace"
        assertEquals(3, MasterW11DP2D.longestCommonSubsequence1D(text1, text2))
        assertEquals(3, MasterW11DP2D.longestCommonSubsequence2D(text1, text2))

        text1 = "abc"
        text2 = "def"
        assertEquals(0, MasterW11DP2D.longestCommonSubsequence1D(text1, text2))
        assertEquals(0, MasterW11DP2D.longestCommonSubsequence2D(text1, text2))

        text1 = "oxcpqrsvwf"
        text2 = "shmtulqrypy"
        assertEquals(2, MasterW11DP2D.longestCommonSubsequence1D(text1, text2))
        assertEquals(2, MasterW11DP2D.longestCommonSubsequence2D(text1, text2))

        text1 = "oxcpqrshmt"
        text2 = "shmtulqrypy"
        assertEquals(4, MasterW11DP2D.longestCommonSubsequence1D(text1, text2))
        assertEquals(4, MasterW11DP2D.longestCommonSubsequence2D(text1, text2))

        text1 = "abcdefghij"
        text2 = "acfeigj"
        assertEquals(5, MasterW11DP2D.longestCommonSubsequence1D(text1, text2))
        assertEquals(5, MasterW11DP2D.longestCommonSubsequence2D(text1, text2))

        text1 = "abcba"
        text2 = "abcbcba"
        assertEquals(5, MasterW11DP2D.longestCommonSubsequence1D(text1, text2))
        assertEquals(5, MasterW11DP2D.longestCommonSubsequence2D(text1, text2))
    }


    // LC 518. Coin Change II
    @Test
    fun changeTest() {
        val coins1 = intArrayOf(1, 2, 5)
        val coins2 = intArrayOf(2)
        val coins3 = intArrayOf(10)

        assertEquals(4, MasterW11DP2D.change1D(5, coins1))
        assertEquals(0, MasterW11DP2D.change1D(3, coins2))
        assertEquals(1, MasterW11DP2D.change1D(10, coins3))

        assertEquals(4, MasterW11DP2D.changeMemoization(5, coins1))
        assertEquals(0, MasterW11DP2D.changeMemoization(3, coins2))
        assertEquals(1, MasterW11DP2D.changeMemoization(10, coins3))
    }


    // LC 494. Target Sum
    @Test
    fun findTargetSumWays() {
        val nums1 = intArrayOf(1, 1, 1, 1, 1)
        val nums2 = intArrayOf(1)
        val nums3 = intArrayOf(1, 1, 2)
        val nums4 = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1)

        assertEquals(5, MasterW11DP2D.findTargetSumWaysBitset(nums1, 3))
        assertEquals(1, MasterW11DP2D.findTargetSumWaysBitset(nums2, 1))
        assertEquals(2, MasterW11DP2D.findTargetSumWaysBitset(nums3, 2))
        assertEquals(524288, MasterW11DP2D.findTargetSumWaysBitset(nums4, 1))

        assertEquals(5, MasterW11DP2D.findTargetSumWays01Knapsack1D(nums1, 3))
        assertEquals(1, MasterW11DP2D.findTargetSumWays01Knapsack1D(nums2, 1))
        assertEquals(2, MasterW11DP2D.findTargetSumWays01Knapsack1D(nums3, 2))
        assertEquals(524288, MasterW11DP2D.findTargetSumWays01Knapsack1D(nums4, 1))

        assertEquals(5, MasterW11DP2D.findTargetSumWaysMemoization(nums1, 3))
        assertEquals(1, MasterW11DP2D.findTargetSumWaysMemoization(nums2, 1))
        assertEquals(2, MasterW11DP2D.findTargetSumWaysMemoization(nums3, 2))
        assertEquals(524288, MasterW11DP2D.findTargetSumWaysMemoization(nums4, 1))

        assertEquals(5, MasterW11DP2D.findTargetSumWaysRecursion(nums1, 3))
        assertEquals(1, MasterW11DP2D.findTargetSumWaysRecursion(nums2, 1))
        assertEquals(2, MasterW11DP2D.findTargetSumWaysRecursion(nums3, 2))
        assertEquals(524288, MasterW11DP2D.findTargetSumWaysRecursion(nums4, 1))
    }

    // LC 416. Partition Equal Subset Sum
    @Test
    fun canPartitionTest() {
        val nums1 = intArrayOf(1, 5, 11, 5)
        val nums2 = intArrayOf(1, 2, 3, 5)
        val nums3 = intArrayOf(3, 3, 3, 4, 5)
        val nums4 = intArrayOf(1, 2, 5)
        val nums5 = intArrayOf(14, 9, 8, 4, 3, 2)

        assertEquals(true, MasterW11DP2D.canPartition(nums1))
        assertEquals(true, MasterW11DP2D.canPartition1D(nums1))
        assertEquals(true, MasterW11DP2D.canPartition2D(nums1))

        assertEquals(false, MasterW11DP2D.canPartition(nums2))
        assertEquals(false, MasterW11DP2D.canPartition1D(nums2))
        assertEquals(false, MasterW11DP2D.canPartition2D(nums2))

        assertEquals(true, MasterW11DP2D.canPartition(nums3))
        assertEquals(true, MasterW11DP2D.canPartition1D(nums3))
        assertEquals(true, MasterW11DP2D.canPartition2D(nums3))

        assertEquals(false, MasterW11DP2D.canPartition(nums4))
        assertEquals(false, MasterW11DP2D.canPartition1D(nums4))
        assertEquals(false, MasterW11DP2D.canPartition2D(nums4))

        assertEquals(true, MasterW11DP2D.canPartition(nums5))
        assertEquals(true, MasterW11DP2D.canPartition1D(nums5))
        assertEquals(true, MasterW11DP2D.canPartition2D(nums5))


    }
}