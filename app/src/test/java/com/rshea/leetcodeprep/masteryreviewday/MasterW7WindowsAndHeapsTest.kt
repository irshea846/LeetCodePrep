package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW7WindowsAndHeapsTest {

    // LC 739. Daily Temperatures
    @Test
    fun testDailyTemperatures_ValidCase1() {
        val temperatures = intArrayOf(73, 74, 75, 71, 69, 72, 76, 73)
        val expectedResult = intArrayOf(1, 1, 4, 2, 1, 1, 0, 0)
        val actualResult = MasterW7WindowsAndHeaps.dailyTemperatures(temperatures)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString())
    }

    @Test
    fun testDailyTemperatures_ValidCase2() {
        val temperatures = intArrayOf(30, 40, 50, 60)
        val expectedResult = intArrayOf(1, 1, 1, 0)
        val actualResult = MasterW7WindowsAndHeaps.dailyTemperatures(temperatures)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString())
    }

    @Test
    fun testDailyTemperatures_ValidCase3() {
        val temperatures = intArrayOf(30, 60, 90)
        val expectedResult = intArrayOf(1, 1, 0)
        val actualResult = MasterW7WindowsAndHeaps.dailyTemperatures(temperatures)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString())
    }

    // LC 23. Merge k Sorted Lists
    @Test
    fun testMergeKLists_ValidCase1() {
        val node1 = MasterW7WindowsAndHeaps.ListNode(1)
        val node2 = MasterW7WindowsAndHeaps.ListNode(4)
        val node3 = MasterW7WindowsAndHeaps.ListNode(5)
        node1.next = node2; node2.next = node3
        val node4 = MasterW7WindowsAndHeaps.ListNode(1)
        val node5 = MasterW7WindowsAndHeaps.ListNode(3)
        val node6 = MasterW7WindowsAndHeaps.ListNode(4)
        node4.next = node5; node5.next = node6
        val node7 = MasterW7WindowsAndHeaps.ListNode(2)
        val node8 = MasterW7WindowsAndHeaps.ListNode(6)
        node7.next = node8
        val lists = arrayOf<MasterW7WindowsAndHeaps.ListNode?>(node1, node4, node7)
        val actualResult = MasterW7WindowsAndHeaps.mergeKLists(lists)
        var head = actualResult
        assertEquals(head, node1)
        head = head?.next
        assertEquals(head, node4)
        head = head?.next
        assertEquals(head, node7)
        head = head?.next
        assertEquals(head, node5)
        head = head?.next
        assertEquals(head, node2)
        head = head?.next
        assertEquals(head, node6)
        head = head?.next
        assertEquals(head, node3)
        head = head?.next
        assertEquals(head, node8)
        head = head?.next
        assertEquals(head, null)
    }

    @Test
    fun testMergeKLists_ValidCase2() {
        val lists = arrayOf<MasterW7WindowsAndHeaps.ListNode?>()
        val actualResult = MasterW7WindowsAndHeaps.mergeKLists(lists)
        assertEquals(actualResult, null)
    }

    @Test
    fun testMergeKLists_ValidCase3() {
        val node = null
        val lists = arrayOf<MasterW7WindowsAndHeaps.ListNode?>(node)
        val actualResult = MasterW7WindowsAndHeaps.mergeKLists(lists)
        assertEquals(actualResult, null)
    }

    // LC 3. Longest Substring Without Repeating Characters
    @Test
    fun testLengthOfLongestSubstring_ValidCase1() {
        val s = "abcabcbb"
        val expectedResult = 3
        val actualResult = MasterW7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "The length of the longest substring should be found")
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase2() {
        val s = "bbbbb"
        val expectedResult = 1
        val actualResult = MasterW7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase3() {
        val s = "pwwkew"
        val expectedResult = 3
        val actualResult = MasterW7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase4() {
        val s = "edd"
        val expectedResult = 2
        val actualResult = MasterW7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase5() {
        val s = "baaabca"
        val expectedResult = 3
        val actualResult = MasterW7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

}