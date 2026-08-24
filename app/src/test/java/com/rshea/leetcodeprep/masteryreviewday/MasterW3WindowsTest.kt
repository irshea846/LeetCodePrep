package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW3WindowsTest {

    // LeetCode 220. Contains Duplicate III
    @Test
    fun testContainsNearbyAlmostDuplicate_ValidCase1() {
        val nums = intArrayOf(1, 2, 3, 1)
        val indexDiff = 3
        val valueDiff = 0
        val expectedResult = true
        val actualResult = MasterW3Windows.containsNearbyAlmostDuplicate(nums, indexDiff, valueDiff)
        assertEquals(expectedResult, actualResult, "Should return true for valid case")
    }

    @Test
    fun testContainsNearbyAlmostDuplicate_ValidCase2() {
        val nums = intArrayOf(1, 5, 9, 1, 5, 9)
        val indexDiff = 2
        val valueDiff = 3
        val expectedResult = false
        val actualResult = MasterW3Windows.containsNearbyAlmostDuplicate(nums, indexDiff, valueDiff)
        assertEquals(expectedResult, actualResult, "Should return false for valid case")
    }

    @Test
    fun testContainsNearbyAlmostDuplicate_ValidCase3() {
        val nums = intArrayOf(2, -3)
        val indexDiff = 2
        val valueDiff = 5
        val expectedResult = true
        val actualResult = MasterW3Windows.containsNearbyAlmostDuplicate(nums, indexDiff, valueDiff)
        assertEquals(expectedResult, actualResult, "Should return true for valid case")
    }

    @Test
    fun testContainsNearbyAlmostDuplicate_ValidCase4() {
        val nums = intArrayOf(10, 15, 18, 24)
        val indexDiff = 3
        val valueDiff = 3
        val expectedResult = true
        val actualResult = MasterW3Windows.containsNearbyAlmostDuplicate(nums, indexDiff, valueDiff)
        assertEquals(expectedResult, actualResult, "Should return true for valid case")
    }

    // LeetCode 259. 3Sum Smaller
    @Test
    fun testThreeSumSmaller_ValidCase1() {
        val nums = intArrayOf(-2, 0, 1, 3)
        val target = 2
        val expectedResult = 2
        val actualResult = MasterW3Windows.threeSumSmaller(nums, target)
        assertEquals(expectedResult, actualResult, "Should return 2 for valid case")
    }

    @Test
    fun testThreeSumSmaller_ValidCase2() {
        val nums = intArrayOf(3, 1, 0, -2)
        val target = 4
        val expectedResult = 3
        val actualResult = MasterW3Windows.threeSumSmaller(nums, target)
        assertEquals(expectedResult, actualResult, "Should return 3 for valid case")
    }

    @Test
    fun testThreeSumSmaller_ValidCase3() {
        val nums = intArrayOf(-1, 1, -1, -1)
        val target = -1
        val expectedResult = 1
        val actualResult = MasterW3Windows.threeSumSmaller(nums, target)
        assertEquals(expectedResult, actualResult, "Should return 1 for valid case")
    }

    @Test
    fun testThreeSumSmaller_ValidCase4() {
        val nums = intArrayOf(2, 0, 0, 2, -2)
        val target = 2
        val expectedResult = 5
        val actualResult = MasterW3Windows.threeSumSmaller(nums, target)
        assertEquals(expectedResult, actualResult, "Should return 5 for valid case")
    }

    // LeetCode 16. 3Sum Closest
    @Test
    fun testThreeSumClosest_ValidCase1() {
        val nums = intArrayOf(-1, 2, 1, -4)
        val target = 1
        val expectedResult = 2
        val actualResult = MasterW3Windows.threeSumClosest(nums, target)
        assertEquals(expectedResult, actualResult, "Should return 2 for valid case")
    }

    @Test
    fun testThreeSumClosest_ValidCase2() {
        val nums = intArrayOf(0, 0, 0)
        val target = 1
        val expectedResult = 0
        val actualResult = MasterW3Windows.threeSumClosest(nums, target)
        assertEquals(expectedResult, actualResult, "Should return 0 for valid case")
    }

    @Test
    fun testThreeSumClosest_ValidCase3() {
        val nums = intArrayOf(10, 20, 30, 40, 50, 60, 70, 80, 90)
        val target = 1
        val expectedResult = 60
        val actualResult = MasterW3Windows.threeSumClosest(nums, target)
        assertEquals(expectedResult, actualResult, "Should return 60 for valid case")
    }

    @Test
    fun testThreeSumClosest_ValidCase4() {
        val nums = intArrayOf(10, 20, 30, 40, 50, 60, 70, 80, 90)
        val target = 100
        val expectedResult = 100
        val actualResult = MasterW3Windows.threeSumClosest(nums, target)
        assertEquals(expectedResult, actualResult, "Should return 100 for valid case")
    }

    @Test
    fun testThreeSumClosest_ValidCase5() {
        val nums = intArrayOf(4, 0, 5, -5, 3, 3, 0, -4, -5)
        val target = -2
        val expectedResult = -2
        val actualResult = MasterW3Windows.threeSumClosest(nums, target)
        assertEquals(expectedResult, actualResult, "Should return -2 for valid case")
    }

    @Test
    fun testThreeSumClosest_ValidCase6() {
        val nums = intArrayOf(4, 5, -5, 3, 3, -4, -5)
        val target = -2
        val expectedResult = -4
        val actualResult = MasterW3Windows.threeSumClosest(nums, target)
        assertEquals(expectedResult, actualResult, "Should return -4 for valid case")
    }

}