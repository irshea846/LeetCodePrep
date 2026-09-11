package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week6SprintTest {

    // Day 31 - LC 547. Number of Provinces
    @Test
    fun testFindCircleNum_ValidCase1() {
        val isConnected = arrayOf(
            intArrayOf(1, 1, 0),
            intArrayOf(1, 1, 0),
            intArrayOf(0, 0, 1)
        )
        val expectedResult = 2
        val dsuResult = Week6Sprint.findCircleNumDSU(isConnected)
        val dfsResult = Week6Sprint.findCircleNumDFS(isConnected)
        val bfsResult = Week6Sprint.findCircleNumBFS(isConnected)
        assertEquals(expectedResult, dsuResult)
        assertEquals(expectedResult, dfsResult)
        assertEquals(expectedResult, bfsResult)
    }

    @Test
    fun testFindCircleNum_ValidCase2() {
        val isConnected = arrayOf(
            intArrayOf(1, 0, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(0, 0, 1)
        )
        val expectedResult = 3
        val dsuResult = Week6Sprint.findCircleNumDSU(isConnected)
        val dfsResult = Week6Sprint.findCircleNumDFS(isConnected)
        val bfsResult = Week6Sprint.findCircleNumBFS(isConnected)
        assertEquals(expectedResult, dsuResult)
        assertEquals(expectedResult, dfsResult)
        assertEquals(expectedResult, bfsResult)
    }

    @Test
    fun testFindCircleNum_ValidCase3() {
        val isConnected = arrayOf(
            intArrayOf(1,1,0,0),
            intArrayOf(1,1,1,0),
            intArrayOf(0,1,1,1),
            intArrayOf(0,0,1,1)
        )
        val expectedResult = 1
        val dsuResult = Week6Sprint.findCircleNumDSU(isConnected)
        val dfsResult = Week6Sprint.findCircleNumDFS(isConnected)
        val bfsResult = Week6Sprint.findCircleNumBFS(isConnected)
        assertEquals(expectedResult, dsuResult)
        assertEquals(expectedResult, dfsResult)
        assertEquals(expectedResult, bfsResult)
    }

    @Test
    fun testFindCircleNum_ValidCase4() {
        val isConnected = arrayOf(
            intArrayOf(1,1,0,0,0,0,0,1,0,0,0,0,0,0,0),
            intArrayOf(1,1,0,0,0,0,0,0,0,0,0,0,0,0,0),
            intArrayOf(0,0,1,0,0,0,0,0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,1,0,1,1,0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,1,0,0,0,0,1,1,0,0,0,0),
            intArrayOf(0,0,0,1,0,1,0,0,0,0,1,0,0,0,0),
            intArrayOf(0,0,0,1,0,0,1,0,1,0,0,0,0,1,0),
            intArrayOf(1,0,0,0,0,0,0,1,1,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,1,1,1,0,0,0,0,1,0),
            intArrayOf(0,0,0,0,1,0,0,0,0,1,0,1,0,0,1),
            intArrayOf(0,0,0,0,1,1,0,0,0,0,1,1,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0,0,1,1,1,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,1,0,0),
            intArrayOf(0,0,0,0,0,0,1,0,1,0,0,0,0,1,0),
            intArrayOf(0,0,0,0,0,0,0,0,0,1,0,0,0,0,1)
        )
        val expectedResult = 3
        val dsuResult = Week6Sprint.findCircleNumDSU(isConnected)
        val dfsResult = Week6Sprint.findCircleNumDFS(isConnected)
        val bfsResult = Week6Sprint.findCircleNumBFS(isConnected)
        assertEquals(expectedResult, dsuResult)
        assertEquals(expectedResult, dfsResult)
        assertEquals(expectedResult, bfsResult)
    }

    @Test
    fun testFindCircleNum_ValidCase5() {
        val isConnected = arrayOf(
            intArrayOf(1,0,0,0,1,0,0,0,0,0,0,0,0,0,0), intArrayOf(0,1,0,0,0,1,0,0,0,0,0,0,0,0,0),
            intArrayOf(0,0,1,0,0,0,0,0,0,0,0,0,0,0,0), intArrayOf(0,0,0,1,0,0,0,0,0,0,0,0,0,0,0),
            intArrayOf(1,0,0,0,1,0,0,0,0,0,0,0,1,0,0), intArrayOf(0,1,0,0,0,1,0,0,0,0,0,0,1,0,0),
            intArrayOf(0,0,0,0,0,0,1,0,0,0,0,1,0,0,0), intArrayOf(0,0,0,0,0,0,0,1,0,0,0,1,1,0,0),
            intArrayOf(0,0,0,0,0,0,0,0,1,0,0,0,0,0,0), intArrayOf(0,0,0,0,0,0,0,0,0,1,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0,0,0,1,1,0,0,0), intArrayOf(0,0,0,0,0,0,1,1,0,0,1,1,0,0,1),
            intArrayOf(0,0,0,0,1,1,0,1,0,0,0,0,1,0,0), intArrayOf(0,0,0,0,0,0,0,0,0,0,0,0,0,1,0),
            intArrayOf(0,0,0,0,0,0,0,0,0,0,0,1,0,0,1)
        )
        val expectedResult = 6
        val dsuResult = Week6Sprint.findCircleNumDSU(isConnected)
        val dfsResult = Week6Sprint.findCircleNumDFS(isConnected)
        val bfsResult = Week6Sprint.findCircleNumBFS(isConnected)
        assertEquals(expectedResult, dsuResult)
        assertEquals(expectedResult, dfsResult)
        assertEquals(expectedResult, bfsResult)
    }

    // Day30 - LC 103. Binary Tree Zigzag Level Order Traversal
    @Test
    fun testZigzagLevelOrder_ValidCase1() {
        val root = Week6Sprint.TreeNode(3)
        val left = Week6Sprint.TreeNode(9)
        val right = Week6Sprint.TreeNode(20)
        right.left = Week6Sprint.TreeNode(15)
        right.right = Week6Sprint.TreeNode(7)
        root.left = left
        root.right = right
        val expected = listOf(listOf(3), listOf(20, 9), listOf(15, 7))
        val dfsResult = Week6Sprint.zigzagLevelOrderDFS(root)
        val bfsResult = Week6Sprint.zigzagLevelOrderBFS(root)
        assertEquals(expected, dfsResult)
        assertEquals(expected, bfsResult)
    }

    @Test
    fun testZigzagLevelOrder_ValidCase2() {
        val root = Week6Sprint.TreeNode(1)
        val expected = listOf(listOf(1))
        val dfsResult = Week6Sprint.zigzagLevelOrderDFS(root)
        val bfsResult = Week6Sprint.zigzagLevelOrderBFS(root)
        assertEquals(expected, dfsResult)
        assertEquals(expected, bfsResult)
    }

    @Test
    fun testZigzagLevelOrder_ValidCase3() {
        val root = null
        val expected = emptyList<List<Int>>()
        val dfsResult = Week6Sprint.zigzagLevelOrderDFS(root)
        val bfsResult = Week6Sprint.zigzagLevelOrderBFS(root)
        assertEquals(expected, dfsResult)
        assertEquals(expected, bfsResult)
    }

    @Test
    fun testZigzagLevelOrder_ValidCase4() {
        val root = Week6Sprint.TreeNode(1)
        val left = Week6Sprint.TreeNode(2)
        val right = Week6Sprint.TreeNode(3)
        left.left = Week6Sprint.TreeNode(4)
        right.right = Week6Sprint.TreeNode(5)
        root.left = left
        root.right = right
        val expected = listOf(listOf(1), listOf(3, 2), listOf(4, 5))
        val dfsResult = Week6Sprint.zigzagLevelOrderDFS(root)
        val bfsResult = Week6Sprint.zigzagLevelOrderBFS(root)
        assertEquals(expected, dfsResult)
        assertEquals(expected, bfsResult)
    }

    // Day29 - LC 61. Rotate List
    @Test
    fun testRotateRight_ValidCase1() {
        val node1 = Week6Sprint.ListNode(1)
        val node2 = Week6Sprint.ListNode(2)
        val node3 = Week6Sprint.ListNode(3)
        val node4 = Week6Sprint.ListNode(4)
        val node5 = Week6Sprint.ListNode(5)
        node1.next = node2
        node2.next = node3
        node3.next = node4
        node4.next = node5
        val k = 2
        val result = Week6Sprint.rotateRight(node1, k)
        assertEquals(node4, result)
    }

    @Test
    fun testRotateRight_ValidCase2() {
        val node0 = Week6Sprint.ListNode(0)
        val node1 = Week6Sprint.ListNode(1)
        val node2 = Week6Sprint.ListNode(2)
        node0.next = node1
        node1.next = node2
        val k = 4
        val result = Week6Sprint.rotateRight(node0, k)
        assertEquals(node2, result)
    }

    @Test
    fun testRotateRight_ValidCase3() {
        val node0 = Week6Sprint.ListNode(0)
        val node1 = Week6Sprint.ListNode(1)
        val node2 = Week6Sprint.ListNode(2)
        node0.next = node1
        node1.next = node2
        val k = 0
        val result = Week6Sprint.rotateRight(node0, k)
        assertEquals(node0, result)
    }

    @Test
    fun testRotateRight_ValidCase4() {
        val node1 = Week6Sprint.ListNode(1)
        val node2 = Week6Sprint.ListNode(2)
        val node3 = Week6Sprint.ListNode(3)
        val node4 = Week6Sprint.ListNode(4)
        val node5 = Week6Sprint.ListNode(5)
        node1.next = node2
        node2.next = node3
        node3.next = node4
        node4.next = node5
        val k = 8
        val result = Week6Sprint.rotateRight(node1, k)
        assertEquals(node3, result)
    }

    @Test
    fun testRotateRight_ValidCase5() {
        val node = null
        val k = 0
        val result = Week6Sprint.rotateRight(node, k)
        assertEquals(null, result)
    }

    @Test
    fun testRotateRight_ValidCase6() {
        val node = null
        val k = 1
        val result = Week6Sprint.rotateRight(node, k)
        assertEquals(null, result)
    }

    // Day28 - LC 150. Evaluate Reverse Polish Notation
    @Test
    fun testEvalRPN_ValidCase1() {
        val tokens = arrayOf("2", "1", "+", "3", "*")
        val result0 = Week6Sprint.evalRPNZeroBoxing(tokens)
        val result1 = Week6Sprint.evalRPN(tokens)
        val result2 = Week6Sprint.evalRPNHigherOrderFunction(tokens)
        assertEquals(9, result0)
        assertEquals(9, result1)
        assertEquals(9, result2)
    }

    @Test
    fun testEvalRPN_ValidCase2() {
        val tokens = arrayOf("4", "13", "5", "/", "+")
        val result0 = Week6Sprint.evalRPNZeroBoxing(tokens)
        val result1 = Week6Sprint.evalRPN(tokens)
        val result2 = Week6Sprint.evalRPNHigherOrderFunction(tokens)
        assertEquals(6, result0)
        assertEquals(6, result1)
        assertEquals(6, result2)
    }

    @Test
    fun testEvalRPN_ValidCase3() {
        val tokens = arrayOf("10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+")
        val result0 = Week6Sprint.evalRPNZeroBoxing(tokens)
        val result1 = Week6Sprint.evalRPN(tokens)
        val result2 = Week6Sprint.evalRPNHigherOrderFunction(tokens)
        assertEquals(22, result0)
        assertEquals(22, result1)
        assertEquals(22, result2)
    }
}