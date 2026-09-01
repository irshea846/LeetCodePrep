package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week5QueuesTest {

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