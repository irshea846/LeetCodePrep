package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW8BacktrackingTest {

    // LC 148. Sort List
    @Test
    fun testSortList_ValidCase1() {
        val node1 = MasterW8Backtracking.ListNode(4)
        val node2 = MasterW8Backtracking.ListNode(2)
        val node3 = MasterW8Backtracking.ListNode(1)
        val node4 = MasterW8Backtracking.ListNode(3)
        node1.next = node2; node2.next = node3; node3.next = node4
        val actualResult = MasterW8Backtracking.sortList(node1)
        var head = actualResult

        assertEquals(head, node3)
        head = head?.next
        assertEquals(head, node2)
        head = head?.next
        assertEquals(head, node4)
        head = head?.next
        assertEquals(head, node1)
        head = head?.next
        assertEquals(head, null)
    }

    @Test
    fun testSortList_ValidCase2() {
        val node1 = MasterW8Backtracking.ListNode(-1)
        val node2 = MasterW8Backtracking.ListNode(5)
        val node3 = MasterW8Backtracking.ListNode(3)
        val node4 = MasterW8Backtracking.ListNode(4)
        val node5 = MasterW8Backtracking.ListNode(0)
        node1.next = node2; node2.next = node3; node3.next = node4; node4.next = node5
        val actualResult = MasterW8Backtracking.sortList(node1)
        var head = actualResult
        assertEquals(head, node1)
        head = head?.next
        assertEquals(head, node5)
        head = head?.next
        assertEquals(head, node3)
        head = head?.next
        assertEquals(head, node4)
        head = head?.next
        assertEquals(head, node2)
        head = head?.next
        assertEquals(head, null)

    }

    // LC 47. Permutations II
    @Test
    fun testPermuteUnique_ValidCase1() {
        val nums = intArrayOf(1, 1, 2)
        val expected = listOf(listOf(1, 1, 2), listOf(1, 2, 1), listOf(2, 1, 1))
        val actual = MasterW8Backtracking.permuteUnique(nums)
        assertEquals(expected, actual)
    }

    @Test
    fun testPermuteUnique_ValidCase2() {
        val nums = intArrayOf(1, 2, 3)
        val expected = listOf(listOf(1, 2, 3), listOf(1, 3, 2), listOf(2, 1, 3), listOf(2, 3, 1), listOf(3, 1, 2), listOf(3, 2, 1))
        val actual = MasterW8Backtracking.permuteUnique(nums)
        assertEquals(expected, actual)
    }

    // LC 46. Permutations
    @Test
    fun testPermute_ValidCase1() {
        val nums = intArrayOf(1, 2, 3)
        val expected = listOf(listOf(1, 2, 3), listOf(1, 3, 2), listOf(2, 1, 3), listOf(2, 3, 1), listOf(3, 1, 2), listOf(3, 2, 1))
        val actual = MasterW8Backtracking.permute(nums)
        assertEquals(expected, actual)
    }

    @Test
    fun testPermute_ValidCase2() {
        val nums = intArrayOf(0, 1)
        val expected = listOf(listOf(0, 1), listOf(1, 0))
        val actual = MasterW8Backtracking.permute(nums)
        assertEquals(expected, actual)
    }

    @Test
    fun testPermute_ValidCase3() {
        val nums = intArrayOf(1)
        val expected = listOf(listOf(1))
        val actual = MasterW8Backtracking.permute(nums)
        assertEquals(expected, actual)
    }

    // LC 90. Subsets II
    @Test
    fun testSubsetsWithDup_ValidCase1() {
        val nums = intArrayOf(1, 2, 2)
        val expected = listOf(emptyList(), listOf(1), listOf(1, 2), listOf(1, 2, 2), listOf(2), listOf(2, 2))
        val actual = MasterW8Backtracking.subsetsWithDup(nums)
        assertEquals(expected, actual)
    }

    @Test
    fun testSubsetsWithDup_ValidCase2() {
        val nums = intArrayOf(0)
        val expected = listOf(emptyList(), listOf(0))
        val actual = MasterW8Backtracking.subsetsWithDup(nums)
        assertEquals(expected, actual)
    }

    @Test
    fun testSubsetsWithDup_ValidCase3() {
        val nums = intArrayOf(4, 4, 4, 1, 4)
        val expected = listOf(emptyList(), listOf(1), listOf(1, 4), listOf(1, 4, 4), listOf(1, 4, 4, 4), listOf(1, 4, 4, 4, 4), listOf(4), listOf(4, 4), listOf(4, 4, 4), listOf(4, 4, 4, 4))
        val actual = MasterW8Backtracking.subsetsWithDup(nums)
        assertEquals(expected, actual)
    }

    // LC 78. Subsets
    @Test
    fun testSubsets_ValidCase1() {
        val nums = intArrayOf(1, 2, 3)
        val expected = listOf(emptyList(), listOf(1), listOf(1,2), listOf(1,2,3), listOf(1,3), listOf(2), listOf(2,3), listOf(3))
        val actual = MasterW8Backtracking.subsets(nums)
        assertEquals(expected, actual)
    }

    @Test
    fun testSubsets_ValidCase2() {
        val nums = intArrayOf(0)
        val expected = listOf(emptyList(), listOf(0))
        val actual = MasterW8Backtracking.subsets(nums)
        assertEquals(expected, actual)
    }

}