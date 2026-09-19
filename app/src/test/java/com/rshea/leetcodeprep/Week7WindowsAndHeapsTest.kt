package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week7WindowsAndHeapsTest {

    // Day 35 - LC 739. Daily Temperatures
    @Test
    fun testDailyTemperatures_ValidCase1() {
        val temperatures = intArrayOf(73, 74, 75, 71, 69, 72, 76, 73)
        val expectedResult = intArrayOf(1, 1, 4, 2, 1, 1, 0, 0)
        val actualResult = Week7WindowsAndHeaps.dailyTemperatures(temperatures)
        val actualResultBackward = Week7WindowsAndHeaps.dailyTemperaturesBackward(temperatures)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString())
        assertEquals(expectedResult.contentToString(), actualResultBackward.contentToString())
    }

    @Test
    fun testDailyTemperatures_ValidCase2() {
        val temperatures = intArrayOf(30, 40, 50, 60)
        val expectedResult = intArrayOf(1, 1, 1, 0)
        val actualResult = Week7WindowsAndHeaps.dailyTemperatures(temperatures)
        val actualResultBackward = Week7WindowsAndHeaps.dailyTemperaturesBackward(temperatures)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString())
        assertEquals(expectedResult.contentToString(), actualResultBackward.contentToString())
    }

    @Test
    fun testDailyTemperatures_ValidCase3() {
        val temperatures = intArrayOf(30, 60, 90)
        val expectedResult = intArrayOf(1, 1, 0)
        val actualResult = Week7WindowsAndHeaps.dailyTemperatures(temperatures)
        val actualResultBackward = Week7WindowsAndHeaps.dailyTemperaturesBackward(temperatures)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString())
        assertEquals(expectedResult.contentToString(), actualResultBackward.contentToString())
    }


    // Day 34 - LC 23. Merge k Sorted Lists
    @Test
    fun testMergeKListsDivideAndConquer_ValidCase1() {
        val node1 = Week7WindowsAndHeaps.ListNode(1)
        val node2 = Week7WindowsAndHeaps.ListNode(4)
        val node3 = Week7WindowsAndHeaps.ListNode(5)
        node1.next = node2; node2.next = node3
        val node4 = Week7WindowsAndHeaps.ListNode(1)
        val node5 = Week7WindowsAndHeaps.ListNode(3)
        val node6 = Week7WindowsAndHeaps.ListNode(4)
        node4.next = node5; node5.next = node6
        val node7 = Week7WindowsAndHeaps.ListNode(2)
        val node8 = Week7WindowsAndHeaps.ListNode(6)
        node7.next = node8
        val lists = arrayOf<Week7WindowsAndHeaps.ListNode?>(node1, node4, node7)
        val actualResult = Week7WindowsAndHeaps.mergeKListsDivideAndConquer(lists)
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
    fun testMergeKLists_ValidCase1() {
        val node1 = Week7WindowsAndHeaps.ListNode(1)
        val node2 = Week7WindowsAndHeaps.ListNode(4)
        val node3 = Week7WindowsAndHeaps.ListNode(5)
        node1.next = node2; node2.next = node3
        val node4 = Week7WindowsAndHeaps.ListNode(1)
        val node5 = Week7WindowsAndHeaps.ListNode(3)
        val node6 = Week7WindowsAndHeaps.ListNode(4)
        node4.next = node5; node5.next = node6
        val node7 = Week7WindowsAndHeaps.ListNode(2)
        val node8 = Week7WindowsAndHeaps.ListNode(6)
        node7.next = node8
        val lists = arrayOf<Week7WindowsAndHeaps.ListNode?>(node1, node4, node7)
        val actualResult = Week7WindowsAndHeaps.mergeKLists(lists)
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
    fun testMergeKListsDivideAndConquer_ValidCase2() {
        val lists = arrayOf<Week7WindowsAndHeaps.ListNode?>()
        val actualResult = Week7WindowsAndHeaps.mergeKListsDivideAndConquer(lists)
        assertEquals(actualResult, null)
    }

    @Test
    fun testMergeKLists_ValidCase2() {
        val lists = arrayOf<Week7WindowsAndHeaps.ListNode?>()
        val actualResult = Week7WindowsAndHeaps.mergeKLists(lists)
        assertEquals(actualResult, null)
    }

    @Test
    fun testMergeKListsDivideAndConquer_ValidCase3() {
        val node1 = null
        val lists = arrayOf<Week7WindowsAndHeaps.ListNode?>(node1)
        val actualResult = Week7WindowsAndHeaps.mergeKListsDivideAndConquer(lists)
        assertEquals(actualResult, null)
    }

    @Test
    fun testMergeKLists_ValidCase3() {
        val node1 = null
        val lists = arrayOf<Week7WindowsAndHeaps.ListNode?>(node1)
        val actualResult = Week7WindowsAndHeaps.mergeKLists(lists)
        assertEquals(actualResult, null)
    }

    // Day 33 - LC 3. Longest Substring Without Repeating Characters
    @Test
    fun testLengthOfLongestSubstring_ValidCase1() {
        val s = "abcabcbb"
        val expectedResult = 3
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "The length of the longest substring should be found")
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase2() {
        val s = "bbbbb"
        val expectedResult = 1
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
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
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase4() {
        val s = "STUG"
        val expectedResult = 4
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase5() {
        val s = "ccbbcc"
        val expectedResult = 2
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase6() {
        val s = "bccddadcb"
        val expectedResult = 4
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase7() {
        val s = "Mi~"
        val expectedResult = 3
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase8() {
        val s = "ff"
        val expectedResult = 1
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase9() {
        val s = "eaa"
        val expectedResult = 2
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase10() {
        val s = "edd"
        val expectedResult = 2
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase11() {
        val s = "1R1T7"
        val expectedResult = 4
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase12() {
        val s = "a"
        val expectedResult = 1
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase13() {
        val s = ""
        val expectedResult = 0
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }
}