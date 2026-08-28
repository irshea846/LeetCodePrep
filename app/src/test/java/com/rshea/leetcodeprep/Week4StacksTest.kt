package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals
class Week4StacksTest {

    // Day 21
    // LeetCode 206. Reverse Linked List
    @Test
    fun testReverseList_ValidCase1() {
        var head: Week4Stacks.ListNode? = null
        head = Week4Stacks.ListNode(1)
        head.next = Week4Stacks.ListNode(2)
        head.next!!.next = Week4Stacks.ListNode(3)
        head.next!!.next!!.next = Week4Stacks.ListNode(4)
        head.next!!.next!!.next!!.next = Week4Stacks.ListNode(5)

        val reversedHead = Week4Stacks.reverseList((head))

        assertEquals(5, reversedHead!!.`val`)
        assertEquals(4, reversedHead.next!!.`val`)
        assertEquals(3, reversedHead.next!!.next!!.`val`)
        assertEquals(2, reversedHead.next!!.next!!.next!!.`val`)
        assertEquals(1, reversedHead.next!!.next!!.next!!.next!!.`val`)
        assertEquals(null, reversedHead.next!!.next!!.next!!.next!!.next)
    }

    @Test
    fun testReverseList_ValidCase2() {
        var head: Week4Stacks.ListNode? = null
        head = Week4Stacks.ListNode(2)
        head.next = Week4Stacks.ListNode(1)

        val reversedHead = Week4Stacks.reverseList((head))

        assertEquals(1, reversedHead!!.`val`)
        assertEquals(2, reversedHead.next!!.`val`)
        assertEquals(null, reversedHead.next!!.next)
    }

    @Test
    fun testReverseList_ValidCase3() {
        val head: Week4Stacks.ListNode? = null

        val reversedHead = Week4Stacks.reverseList((head))

        assertEquals(null, reversedHead)
    }



    // Day 20
    // LeetCode 739. Daily Temperatures
    @Test
    fun testDailyTemperatures_ValidCase1() {
        val temperatures = intArrayOf(73, 74, 75, 71, 69, 72, 76, 73)
        val expected = intArrayOf(1, 1, 4, 2, 1, 1, 0, 0)
        val result = Week4Stacks.dailyTemperatures(temperatures)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testDailyTemperatures_ValidCase2() {
        val temperatures = intArrayOf(30, 40, 50, 60)
        val expected = intArrayOf(1, 1, 1, 0)
        val result = Week4Stacks.dailyTemperatures(temperatures)
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun testDailyTemperatures_ValidCase3() {
        val temperatures = intArrayOf(30, 60, 90)
        val expected = intArrayOf(1, 1, 0)
        val result = Week4Stacks.dailyTemperatures(temperatures)
        assertEquals(expected.toList(), result.toList())
    }

    // Day 19
    // LeetCode 155. Min Stack
    @Test
    fun testMinStack_ValidCase1() {
        val minStack = Week4Stacks.MinStack()
        minStack.push(-2)
        minStack.push(0)
        minStack.push(-3)
        assertEquals(-3, minStack.getMin())
    }

    @Test
    fun testMinStack_ValidCase2() {
        val minStack = Week4Stacks.MinStack()

        minStack.push(0)
        minStack.push(1)
        minStack.push(0)
        assertEquals(0, minStack.getMin())

        minStack.pop()
        assertEquals(0, minStack.getMin())
        minStack.pop()
        assertEquals(0, minStack.getMin())
        minStack.pop()

        minStack.push(-2)
        minStack.push(-1)
        minStack.push(-2)
        assertEquals(-2, minStack.getMin())

        minStack.pop()
        assertEquals(-1, minStack.top())
        assertEquals(-2, minStack.getMin())

        minStack.pop()
        assertEquals(-2, minStack.getMin())

        minStack.pop()

    }

    @Test
    fun testMinStack_ValidCase3() {
        val minStack = Week4Stacks.MinStack()
        minStack.push(2147483646)
        minStack.push(2147483646)
        minStack.push(2147483647)
        assertEquals(2147483647, minStack.top())

        minStack.pop()
        assertEquals(2147483646, minStack.getMin())

        minStack.pop()
        assertEquals(2147483646, minStack.getMin())
        minStack.pop()
        minStack.push(2147483647)
        assertEquals(2147483647, minStack.top())
        assertEquals(2147483647, minStack.getMin())

        minStack.push(-2147483648)
        assertEquals(-2147483648, minStack.top())
        assertEquals(-2147483648, minStack.getMin())

        minStack.pop()
        assertEquals(2147483647, minStack.getMin())

    }

    // Day 18
    // LeetCode 20. Valid Parentheses
    @Test
    fun testIsValid_ValidCase1() {
        val s = "()"
        val result = Week4Stacks.isValid(s)
        assertEquals(true, result)
    }

    @Test
    fun testIsValid_ValidCase2() {
        val s = "()[]{}"
        val result = Week4Stacks.isValid(s)
        assertEquals(true, result)
    }

    @Test
    fun testIsValid_ValidCase3() {
        val s = "(]"
        val result = Week4Stacks.isValid(s)
        assertEquals(false, result)
    }

    @Test
    fun testIsValid_ValidCase4() {
        val s = "([)]"
        val result = Week4Stacks.isValid(s)
        assertEquals(false, result)
    }

    @Test
    fun testIsValid_ValidCase5() {
        val s = "{[]}"
        val result = Week4Stacks.isValid(s)
        assertEquals(true, result)
    }

    @Test
    fun testIsValid_ValidCase6() {
        val s = "]"
        val result = Week4Stacks.isValid(s)
        assertEquals(false, result)
    }

    @Test
    fun testIsValid_ValidCase7() {
        val s = "["
        val result = Week4Stacks.isValid(s)
        assertEquals(false, result)
    }

    @Test
    fun testIsValidOnlyOneType_ValidCase1() {
        val s = "()"
        val result = Week4Stacks.isValidOnlyOneType(s)
        assertEquals(true, result)
    }

    @Test
    fun testIsValidOnlyOneType_ValidCase2() {
        val s = "()()()"
        val result = Week4Stacks.isValidOnlyOneType(s)
        assertEquals(true, result)
    }

    @Test
    fun testIsValidOnlyOneType_InvalidCase1() {
        val s = "((())"
        val result = Week4Stacks.isValidOnlyOneType(s)
        assertEquals(false, result)
    }

    @Test
    fun testIsValidOnlyOneType_InvalidCase2() {
        val s = ")("
        val result = Week4Stacks.isValidOnlyOneType(s)
        assertEquals(false, result)
    }
}