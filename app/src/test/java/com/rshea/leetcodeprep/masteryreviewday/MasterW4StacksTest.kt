package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW4StacksTest {

    // LC 716. Max Stack
    @Test
    fun testMaxStack_ValidCase1() {
        val maxStack = MasterW4Stacks.MaxStack()
        maxStack.push(5)
        maxStack.push(1)
        maxStack.push(5)
        assertEquals(5, maxStack.top())
        assertEquals(5, maxStack.popMax())
        assertEquals(1, maxStack.top())
        assertEquals(5, maxStack.peekMax())
        assertEquals(1, maxStack.pop())
        assertEquals(5, maxStack.top())
    }

    @Test
    fun testMaxStack_ValidCase2() {
        val maxStack = MasterW4Stacks.MaxStack()
        maxStack.push(1)
        maxStack.push(2)
        assertEquals(2, maxStack.popMax())
        assertEquals(1, maxStack.peekMax())
    }

    // LC 142. Linked List Cycle II
    @Test
    fun testDetectCycle_ValidCase1() {
        val node1 = MasterW4Stacks.ListNode(3)
        val node2 = MasterW4Stacks.ListNode(2)
        val node3 = MasterW4Stacks.ListNode(0)
        val node4 = MasterW4Stacks.ListNode(-4)
        node1.next = node2
        node2.next = node3
        node3.next = node4
        node4.next = node2
        val result = MasterW4Stacks.detectCycle(node1)
        assertEquals(node2, result)
    }

    @Test
    fun testDetectCycle_ValidCase2() {
        val node1 = MasterW4Stacks.ListNode(1)
        val node2 = MasterW4Stacks.ListNode(2)
        node1.next = node2
        node2.next = node1
        val result = MasterW4Stacks.detectCycle(node1)
        assertEquals(node1, result)
    }

    @Test
    fun testDetectCycle_ValidCase3() {
        val node1 = MasterW4Stacks.ListNode(1)
        val result = MasterW4Stacks.detectCycle(node1)
        assertEquals(null, result)
    }

    @Test
    fun testDetectCycle_ValidCase4() {
        val node1 = MasterW4Stacks.ListNode(1)
        val node2 = MasterW4Stacks.ListNode(2)
        val node3 = MasterW4Stacks.ListNode(3)
        val node4 = MasterW4Stacks.ListNode(4)
        val node5 = MasterW4Stacks.ListNode(5)
        val node6 = MasterW4Stacks.ListNode(6)
        val node7 = MasterW4Stacks.ListNode(7)
        val node8 = MasterW4Stacks.ListNode(8)
        val node9 = MasterW4Stacks.ListNode(9)
        node1.next = node2
        node2.next = node3
        node3.next = node4
        node4.next = node5
        node5.next = node6
        node6.next = node7
        node7.next = node8
        node8.next = node9
        node9.next = node3
        val result = MasterW4Stacks.detectCycle(node1)
        assertEquals(node3, result)
    }

    // LC 496. Next Greater Element I
    @Test
    fun testNextGreaterElement_ValidCase1() {
        val nums1 = intArrayOf(4, 1, 2)
        val nums2 = intArrayOf(1, 3, 4, 2)
        val expected = intArrayOf(-1, 3, -1)
        val result = MasterW4Stacks.nextGreaterElement(nums1, nums2)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testNextGreaterElement_ValidCase2() {
        val nums1 = intArrayOf(2, 4)
        val nums2 = intArrayOf(1, 2, 3, 4)
        val expected = intArrayOf(3, -1)
        val result = MasterW4Stacks.nextGreaterElement(nums1, nums2)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testNextGreaterElement_ValidCase3() {
        val nums1 = intArrayOf(1, 3, 5, 2, 4)
        val nums2 = intArrayOf(6, 5, 4, 3, 2, 1, 7)
        val expected = intArrayOf(7, 7, 7, 7, 7)
        val result = MasterW4Stacks.nextGreaterElement(nums1, nums2)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testNextGreaterElement_ValidCase4() {
        val nums1 = intArrayOf(1, 3, 5, 2, 4)
        val nums2 = intArrayOf(7, 6, 5, 4, 3, 2, 1)
        val expected = intArrayOf(-1, -1, -1, -1, -1)
        val result = MasterW4Stacks.nextGreaterElement(nums1, nums2)
        assertEquals(expected.toList(), result.toList())
    }

}