package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week5QueuesTest {

    // Day 25 - LC 207. Course Schedule
    @Test
    fun testCanFinish_ValidCase1() {
        val numCourses = 2
        val prerequisites = arrayOf(intArrayOf(1, 0))
        val dfsResult = Week5Queues.canFinishDFS(numCourses, prerequisites)
        val bfsResult = Week5Queues.canFinishBFS(numCourses, prerequisites)
        assertEquals(true, dfsResult)
        assertEquals(true, bfsResult)
    }

    @Test
    fun testCanFinish_ValidCase2() {
        val numCourses = 2
        val prerequisites = arrayOf(intArrayOf(1, 0), intArrayOf(0, 1))
        val dfsResult = Week5Queues.canFinishDFS(numCourses, prerequisites)
        val bfsResult = Week5Queues.canFinishBFS(numCourses, prerequisites)
        assertEquals(false, dfsResult)
        assertEquals(false, bfsResult)
    }

    @Test
    fun testCanFinish_ValidCase3() {
        val numCourses = 5
        val prerequisites = arrayOf(intArrayOf(2, 3), intArrayOf(4, 1), intArrayOf(3, 0), intArrayOf(1, 2))
        val dfsResult = Week5Queues.canFinishDFS(numCourses, prerequisites)
        val bfsResult = Week5Queues.canFinishBFS(numCourses, prerequisites)
        assertEquals(true, dfsResult)
        assertEquals(true, bfsResult)
    }

    @Test
    fun testCanFinish_ValidCase4() {
        val numCourses = 20
        val prerequisites = arrayOf(intArrayOf(0, 10), intArrayOf(3, 18), intArrayOf(5, 5),
                intArrayOf(6, 11), intArrayOf(11, 14), intArrayOf(13, 1), intArrayOf(15, 1),
                intArrayOf(17,4))
        val dfsResult = Week5Queues.canFinishDFS(numCourses, prerequisites)
        val bfsResult = Week5Queues.canFinishBFS(numCourses, prerequisites)
        assertEquals(false, dfsResult)
        assertEquals(false, bfsResult)
    }

    @Test
    fun testCanFinish_ValidCase5() {
        val numCourses = 5
        val prerequisites = arrayOf(intArrayOf(1, 4), intArrayOf(2, 4), intArrayOf(3, 1),
            intArrayOf(3, 2))
        val dfsResult = Week5Queues.canFinishDFS(numCourses, prerequisites)
        val bfsResult = Week5Queues.canFinishBFS(numCourses, prerequisites)
        assertEquals(true, dfsResult)
        assertEquals(true, bfsResult)
    }

    @Test
    fun testCanFinish_ValidCase6() {
        val numCourses = 8
        val prerequisites = arrayOf(intArrayOf(1, 0), intArrayOf(2, 6), intArrayOf(1, 7),
            intArrayOf(5, 1), intArrayOf(6, 4), intArrayOf(7, 0), intArrayOf(0, 5))
        val dfsResult = Week5Queues.canFinishDFS(numCourses, prerequisites)
        val bfsResult = Week5Queues.canFinishBFS(numCourses, prerequisites)
        assertEquals(false, dfsResult)
        assertEquals(false, bfsResult)
    }

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