package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week11DP2DTest {

    // Day 54 - LC 518. Coin Change II
    @Test
    fun changeTest() {
        val coins1 = intArrayOf(1, 2, 5)
        val coins2 = intArrayOf(2)
        val coins3 = intArrayOf(10)

        assertEquals(4, Week11DP2D.change(5, coins1))
        assertEquals(0, Week11DP2D.change(3, coins2))
        assertEquals(1, Week11DP2D.change(10, coins3))
    }

    // Day 53 - LC 494. Target Sum
    @Test
    fun findTargetSumWaysTest() {
        val nums1 = intArrayOf(1, 1, 1, 1, 1)
        val nums2 = intArrayOf(1)
        val nums3 = intArrayOf(1, 0)

        assertEquals(5, Week11DP2D.findTargetSumWays(nums1, 3))
        assertEquals(1, Week11DP2D.findTargetSumWays(nums2, 1))
        assertEquals(2, Week11DP2D.findTargetSumWays(nums3, 1))

        assertEquals(5, Week11DP2D.findTargetSumWaysMemoization(nums1, 3))
        assertEquals(1, Week11DP2D.findTargetSumWaysMemoization(nums2, 1))
        assertEquals(2, Week11DP2D.findTargetSumWaysMemoization(nums3, 1))

        assertEquals(5, Week11DP2D.findTargetSumWaysRecursion(nums1, 3))
        assertEquals(1, Week11DP2D.findTargetSumWaysRecursion(nums2, 1))
        assertEquals(2, Week11DP2D.findTargetSumWaysRecursion(nums3, 1))
    }

    // Day 52 - LC 416. Partition Equal Subset Sum
    @Test
   fun canPartitionTest() {
        val nums1 = intArrayOf(1, 5, 11, 5)
        val nums2 = intArrayOf(1, 2, 3, 5)
        val nums3 = intArrayOf(3, 3, 3, 4, 5)
        val nums4 = intArrayOf(1, 2, 5)

        assertEquals(true, Week11DP2D.canPartition(nums1))
        assertEquals(false, Week11DP2D.canPartition(nums2))
        assertEquals(true, Week11DP2D.canPartition(nums3))
        assertEquals(false, Week11DP2D.canPartition(nums4))

        assertEquals(true, Week11DP2D.canPartition2D(nums1))
        assertEquals(false, Week11DP2D.canPartition2D(nums2))
        assertEquals(true, Week11DP2D.canPartition2D(nums3))
        assertEquals(false, Week11DP2D.canPartition2D(nums4))

        assertEquals(true, Week11DP2D.canPartitionBitset(nums1))
        assertEquals(false, Week11DP2D.canPartitionBitset(nums2))
        assertEquals(true, Week11DP2D.canPartitionBitset(nums3))
        assertEquals(false, Week11DP2D.canPartitionBitset(nums4))

    }

}