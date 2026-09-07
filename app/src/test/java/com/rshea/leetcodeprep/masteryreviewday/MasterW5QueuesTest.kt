package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW5QueuesTest {

    // LC 142. Linked List Cycle II
    @Test
    fun testDetectCycle_ValidCase1() {
        val node1 = MasterW5Queues.ListNode(3)
        val node2 = MasterW5Queues.ListNode(2)
        val node3 = MasterW5Queues.ListNode(0)
        val node4 = MasterW5Queues.ListNode(-4)
        node1.next = node2
        node2.next = node3
        node3.next = node4
        node4.next = node2
        val result = MasterW5Queues.detectCycle(node1)
        assertEquals(node2, result)
    }

    @Test
    fun testDetectCycle_ValidCase2() {
        val node1 = MasterW5Queues.ListNode(1)
        val node2 = MasterW5Queues.ListNode(2)
        node1.next = node2
        node2.next = node1
        val result = MasterW5Queues.detectCycle(node1)
        assertEquals(node1, result)
    }

    @Test
    fun testDetectCycle_ValidCase3() {
        val node1 = MasterW5Queues.ListNode(1)
        val result = MasterW5Queues.detectCycle(node1)
        assertEquals(null, result)
    }

    @Test
    fun testDetectCycle_ValidCase4() {
        val node1 = MasterW5Queues.ListNode(1)
        val node2 = MasterW5Queues.ListNode(2)
        val node3 = MasterW5Queues.ListNode(3)
        val node4 = MasterW5Queues.ListNode(4)
        val node5 = MasterW5Queues.ListNode(5)
        val node6 = MasterW5Queues.ListNode(6)
        val node7 = MasterW5Queues.ListNode(7)
        val node8 = MasterW5Queues.ListNode(8)
        val node9 = MasterW5Queues.ListNode(9)
        node1.next = node2
        node2.next = node3
        node3.next = node4
        node4.next = node5
        node5.next = node6
        node6.next = node7
        node7.next = node8
        node8.next = node9
        node9.next = node3
        val result = MasterW5Queues.detectCycle(node1)
        assertEquals(node3, result)
    }

    // LC 21. Merge Two Sorted Lists
    @Test
    fun testMergeTwoLists_ValidCase1() {
        val list1 = MasterW5Queues.ListNode(1)
        list1.next = MasterW5Queues.ListNode(2)
        list1.next!!.next = MasterW5Queues.ListNode(4)
        val list2 = MasterW5Queues.ListNode(1)
        list2.next = MasterW5Queues.ListNode(3)
        list2.next!!.next = MasterW5Queues.ListNode(4)
        val result = MasterW5Queues.mergeTwoLists(list1, list2)
        assertEquals(list1, result)
    }

    @Test
    fun testMergeTwoLists_ValidCase2() {
        val list1 = null
        val list2 = null
        val expected = null
        val result = MasterW5Queues.mergeTwoLists(list1, list2)
        assertEquals(expected, result)
    }

    @Test
    fun testMergeTwoLists_ValidCase3() {
        val list1 = null
        val list2 = MasterW5Queues.ListNode(0)
        val expected = MasterW5Queues.ListNode(0)
        val result = MasterW5Queues.mergeTwoLists(list1, list2)
        assertEquals(list2, result)
    }

    // LC 207. Course Schedule
    @Test
    fun testCanFinish_ValidCase1() {
        val numCourses = 2
        val prerequisites = arrayOf(intArrayOf(1, 0))
        val result = MasterW5Queues.canFinish(numCourses, prerequisites)
        assertEquals(true, result)
    }

    @Test
    fun testCanFinish_ValidCase2() {
        val numCourses = 2
        val prerequisites = arrayOf(intArrayOf(1, 0), intArrayOf(0, 1))
        val result = MasterW5Queues.canFinish(numCourses, prerequisites)
        assertEquals(false, result)
    }

    @Test
    fun testCanFinish_ValidCase3() {
        val numCourses = 5
        val prerequisites = arrayOf(intArrayOf(2, 3), intArrayOf(4, 1), intArrayOf(3, 0), intArrayOf(1, 2))
        val result = MasterW5Queues.canFinish(numCourses, prerequisites)
        assertEquals(true, result)
    }

    @Test
    fun testCanFinish_ValidCase4() {
        val numCourses = 20
        val prerequisites = arrayOf(intArrayOf(0, 10), intArrayOf(3, 18), intArrayOf(5, 5),
            intArrayOf(6, 11), intArrayOf(11, 14), intArrayOf(13, 1), intArrayOf(15, 1),
            intArrayOf(17,4))
        val result = MasterW5Queues.canFinish(numCourses, prerequisites)
        assertEquals(false, result)
    }

    @Test
    fun testCanFinish_ValidCase5() {
        val numCourses = 5
        val prerequisites = arrayOf(intArrayOf(1, 4), intArrayOf(2, 4), intArrayOf(3, 1),
            intArrayOf(3, 2))
        val result = MasterW5Queues.canFinish(numCourses, prerequisites)
        assertEquals(true, result)
    }

    @Test
    fun testCanFinish_ValidCase6() {
        val numCourses = 8
        val prerequisites = arrayOf(intArrayOf(1, 0), intArrayOf(2, 6), intArrayOf(1, 7),
            intArrayOf(5, 1), intArrayOf(6, 4), intArrayOf(7, 0), intArrayOf(0, 5))
        val result = MasterW5Queues.canFinish(numCourses, prerequisites)
        assertEquals(false, result)
    }

    // LC 239. Sliding Window Maximum
    @Test
    fun testMaxSlidingWindow_ValidCase1() {
        val nums = intArrayOf(1, 3, 1, 2, 0, 5)
        val k = 3
        val expected = intArrayOf(3, 3, 2, 5)
        val result = MasterW5Queues.maxSlidingWindow(nums, k)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testMaxSlidingWindow_ValidCase2() {
        val nums = intArrayOf(1)
        val k = 1
        val expected = intArrayOf(1)
        val result = MasterW5Queues.maxSlidingWindow(nums, k)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testMaxSlidingWindow_ValidCase3() {
        val nums = intArrayOf(4, -2)
        val k = 2
        val expected = intArrayOf(4)
        val result = MasterW5Queues.maxSlidingWindow(nums, k)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testMaxSlidingWindow_ValidCase4() {
        val nums = intArrayOf(3, 1, 1, 3)
        val k = 3
        val expected = intArrayOf(3, 3)
        val result = MasterW5Queues.maxSlidingWindow(nums, k)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testMaxSlidingWindow_ValidCase5() {
        val nums = intArrayOf(1, 3, 1, 2, 0, 5)
        val k = 1
        val expected = intArrayOf(1, 3, 1, 2, 0, 5)
        val result = MasterW5Queues.maxSlidingWindow(nums, k)
        assertEquals(expected.toList(), result.toList())
    }

    // LC 102. Binary Tree Level Order Traversal
    @Test
    fun testLevelOrder_ValidCase1() {
        val root = MasterW5Queues.TreeNode(3)
        val left = MasterW5Queues.TreeNode(9)
        val right = MasterW5Queues.TreeNode(20)
        right.left = MasterW5Queues.TreeNode(15)
        right.right = MasterW5Queues.TreeNode(7)
        root.left = left
        root.right = right
        val expected = listOf(listOf(3), listOf(9, 20), listOf(15, 7))
        val result = MasterW5Queues.levelOrder(root)
        assertEquals(expected, result)
    }

    @Test
    fun testLevelOrder_ValidCase2() {
        val root = MasterW5Queues.TreeNode(1)
        val expected = listOf(listOf(1))
        val result = MasterW5Queues.levelOrder(root)
        assertEquals(expected, result)
    }

    @Test
    fun testLevelOrder_ValidCase3() {
        val root = null
        val expected = emptyList<List<Int>>()
        val result = MasterW5Queues.levelOrder(root)
        assertEquals(expected, result)
    }
    
}