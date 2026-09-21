package com.rshea.leetcodeprep.masteryreviewday

import java.util.PriorityQueue
import kotlin.math.max

object MasterW7WindowsAndHeaps {

    // LC 739. Daily Temperatures
    fun dailyTemperatures(temperatures: IntArray): IntArray {
        val n = temperatures.size
        val result = IntArray(n)
        val monoStack = IntArray(n)
        var top = -1
        for (i in 0 until n) {
            while (top >= 0 && temperatures[i] > temperatures[monoStack[top]]) {
                result[monoStack[top]] = i - monoStack[top]
                top--
            }
            monoStack[++top] = i
        }
        return result
    }


    // LC 23. Merge k Sorted Lists
    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    fun mergeKLists(lists: Array<ListNode?>): ListNode? {
        if (lists.isEmpty()) return null
        val pq = PriorityQueue<ListNode> { a, b -> a.`val` - b.`val` }

        for (list in lists) {
            list?.let { pq.add(it) }
        }

        val dummy = ListNode(0)
        var tail = dummy
        while (pq.isNotEmpty()) {
            val node = pq.poll()!!
            tail.next = node
            tail = node
            node.next?.let { pq.add(it) }
        }
        return dummy.next
    }

    // LC 3. Longest Substring Without Repeating Characters
    fun lengthOfLongestSubstring(s: String): Int {
        val n = 128 //256 for extended ASCII
        val ascii = IntArray(128) { -1 }
        var left = 0
        var maxLength = 0
        for (right in s.indices) {
            val c = s[right].code
            if (ascii[c] >= left) {
                maxLength = max(maxLength, right - left)
                left = ascii[c] + 1
            }
            ascii[c] = right
        }

        return max(maxLength, s.length - left)
    }

}

