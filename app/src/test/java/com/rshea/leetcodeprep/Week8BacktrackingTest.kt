package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.collections.emptyList
import kotlin.test.assertEquals

class Week8BacktrackingTest {

    // Day 37 - LC 46. Permutations
    @Test
    fun testPermute_ValidCase1() {
        val nums = intArrayOf(1, 2, 3)
        val expected1 = listOf(listOf(1, 2, 3), listOf(1, 3, 2), listOf(2, 1, 3), listOf(2, 3, 1), listOf(3, 1, 2), listOf(3, 2, 1))
        val actual1 = Week8Backtracking.permute(nums)
        assertEquals(expected1, actual1)
        val expected2 = listOf(listOf(1, 2, 3), listOf(1, 3, 2), listOf(2, 1, 3), listOf(2, 3, 1), listOf(3, 1, 2), listOf(3, 2, 1))
        val actual2 = Week8Backtracking.permuteDQ(nums)
        assertEquals(expected2, actual2)
        val expected3 = listOf(listOf(1, 2, 3), listOf(1, 3, 2), listOf(2, 1, 3), listOf(2, 3, 1), listOf(3, 2, 1), listOf(3, 1, 2))
        val actual3 = Week8Backtracking.permuteInPlace(nums)
        assertEquals(expected3, actual3)
    }

    @Test
    fun testPermute_ValidCase2() {
        val nums = intArrayOf(0, 1)
        val expected = listOf(listOf(0, 1), listOf(1, 0))
        val actual1 = Week8Backtracking.permute(nums)
        assertEquals(expected, actual1)
        val actual2 = Week8Backtracking.permuteDQ(nums)
        assertEquals(expected, actual2)
        val actual3 = Week8Backtracking.permuteInPlace(nums)
        assertEquals(expected, actual3)
    }

    @Test
    fun testPermute_ValidCase3() {
        val nums = intArrayOf(1)
        val expected = listOf(listOf(1))
        val actual1 = Week8Backtracking.permute(nums)
        assertEquals(expected, actual1)
        val actual2 = Week8Backtracking.permuteDQ(nums)
        assertEquals(expected, actual2)
        val actual3 = Week8Backtracking.permuteInPlace(nums)
        assertEquals(expected, actual3)
    }

    // Day 36 - LC 78. Subsets
    @Test
    fun testSubsets_ValidCase1() {
        val nums = intArrayOf(1, 2, 3)
        val expected = listOf(emptyList(), listOf(1), listOf(1,2), listOf(1,2,3), listOf(1,3), listOf(2), listOf(2,3), listOf(3))
        val actual1 = Week8Backtracking.subsets(nums)
        assertEquals(expected, actual1)
        val actual2 = Week8Backtracking.subsetsOriginal(nums)
        assertEquals(expected, actual2)
    }
}