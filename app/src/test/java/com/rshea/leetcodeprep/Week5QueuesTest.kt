package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week5QueuesTest {

    // Day 24 - LC 239. Sliding Window Maximum
    @Test
    fun testMaxSlidingWindow_ValidCase1() {
        val nums = intArrayOf(1, 3, 1, 2, 0, 5)
        val k = 3
        val expected = intArrayOf(3, 3, 2, 5)
        val result = Week5Queues.maxSlidingWindow(nums, k)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testMaxSlidingWindow_ValidCase2() {
        val nums = intArrayOf(1)
        val k = 1
        val expected = intArrayOf(1)
        val result = Week5Queues.maxSlidingWindow(nums, k)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testMaxSlidingWindow_ValidCase3() {
        val nums = intArrayOf(4, -2)
        val k = 2
        val expected = intArrayOf(4)
        val result = Week5Queues.maxSlidingWindow(nums, k)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testMaxSlidingWindow_ValidCase4() {
        val nums = intArrayOf(3, 1, 1, 3)
        val k = 3
        val expected = intArrayOf(3, 3)
        val result = Week5Queues.maxSlidingWindow(nums, k)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testMaxSlidingWindow_ValidCase5() {
        val nums = intArrayOf(1, 3, 1, 2, 0, 5)
        val k = 1
        val expected = intArrayOf(1, 3, 1, 2, 0, 5)
        val result = Week5Queues.maxSlidingWindow(nums, k)
        assertEquals(expected.toList(), result.toList())
    }

    // Day 23 - LC 102. Binary Tree Level Order Traversal
    @Test
    fun testLevelOrder_ValidCase1() {
        val root = Week5Queues.TreeNode(3)
        val left = Week5Queues.TreeNode(9)
        val right = Week5Queues.TreeNode(20)
        right.left = Week5Queues.TreeNode(15)
        right.right = Week5Queues.TreeNode(7)
        root.left = left
        root.right = right
        val expected = listOf(listOf(3), listOf(9, 20), listOf(15, 7))
        val result = Week5Queues.levelOrder(root)
        assertEquals(expected, result)
    }

    @Test
    fun testLevelOrder_ValidCase2() {
        val root = Week5Queues.TreeNode(1)
        val expected = listOf(listOf(1))
        val result = Week5Queues.levelOrder(root)
        assertEquals(expected, result)
    }

    @Test
    fun testLevelOrder_ValidCase3() {
        val root = null
        val expected = emptyList<List<Int>>()
        val result = Week5Queues.levelOrder(root)
        assertEquals(expected, result)
    }
}