package com.rshea.leetcodeprep

import java.util.PriorityQueue
import kotlin.math.max

object Week7WindowsAndHeaps {

    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    // Day 34 - LC 23. Merge k Sorted Lists
    fun mergeKListsDivideAndConquer(lists: Array<ListNode?>): ListNode? {
        // 1. Divide & Conquer Strategy (Merge Sort Style)
        // Time Complexity: O(N log k) | Space Complexity: O(log k) stack
        if (lists.isEmpty()) return null
        return binaryMergeKLists(lists, 0, lists.lastIndex)
    }

    fun binaryMergeKLists(lists: Array<ListNode?>, start: Int, end: Int): ListNode? {
        if (start == end) return lists[start]

        val mid = start + (end - start) / 2
        val left = binaryMergeKLists(lists, start, mid)
        val right = binaryMergeKLists(lists, mid + 1, end)

        return mergeTwoListsIteration(left, right)
    }

    fun mergeTwoListsIteration(list1: ListNode?, list2: ListNode?): ListNode? {
        val dummy = ListNode(0)
        var tail = dummy
        var l1 = list1
        var l2 = list2

        while (l1 != null && l2 != null) {
            if (l1.`val` <= l2.`val`) {
                tail.next = l1
                l1 = l1.next
            } else {
                tail.next = l2
                l2 = l2.next
            }
            tail = tail.next!!
        }

        tail.next = l1 ?: l2

        return dummy.next
    }

    fun mergeKLists(lists: Array<ListNode?>): ListNode? {
        val pq = PriorityQueue<ListNode> { a, b -> a.`val` - b.`val` } //(compareBy { it.`val` })
        for (list in lists) {
            list?.let { pq.add(it) }
        }

        val dummy = ListNode(0)
        var tail = dummy
        while (pq.isNotEmpty()) {
            val node = pq.poll()
            tail.next = node
            tail = node!!
            node.next?.let { pq.add(it) }
        }
        return dummy.next
    }


    // Day 33 - LC 3. Longest Substring Without Repeating Characters
    fun lengthOfLongestSubstring(s: String): Int {
        val n = 128 // 256 for Extended ASCII
        val ascii = IntArray(n) { -1 }
        var maxLen = 0
        var left = 0

        for (right in s.indices) {
            val charCode = s[right].code

            // If character was seen within the current window, "jump" the left pointer
            if (ascii[charCode] >= left) {
                maxLen = max(maxLen, right - left)
                left = ascii[charCode] + 1
            }
            ascii[charCode] = right
        }

        // Mandatory final check: handles cases where the longest substring
        // is at the very end of the input (e.g., "abc")
        return max(maxLen, s.length - left)
    }
}