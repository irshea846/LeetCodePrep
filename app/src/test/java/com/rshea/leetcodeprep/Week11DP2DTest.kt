package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week11DP2DTest {

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