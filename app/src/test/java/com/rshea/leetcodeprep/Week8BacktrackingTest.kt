package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.collections.emptyList
import kotlin.test.assertEquals

class Week8BacktrackingTest {

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