package com.rshea.leetcodeprep.masteryreviewday

import java.util.TreeMap

object MasterW4Stacks {

    // LC 716. Max Stack
    class MaxStack() {
        // Two TreeMaps Strategy
        // Uses time-based IDs to replace the DLL, maintaining O(log N) for all ops.

        private val valueMap = TreeMap<Int, MutableList<Int>>() // Value -> List of seqIDs
        private val timeMap = TreeMap<Int, Int>() // seqID -> Value
        private var seqID = 0

        fun push(x: Int) {
            seqID++
            timeMap[seqID] = x
            valueMap.computeIfAbsent(x) { mutableListOf() }.add(seqID)
        }

        fun pop(): Int {
            val id = timeMap.lastKey()
            val x = timeMap.remove(id)!!
            val ids = valueMap[x]!!
            ids.removeAt(ids.size - 1)
            if (ids.isEmpty()) valueMap.remove(x)
            return x
        }

        fun top(): Int = timeMap.lastEntry()!!.value

        fun peekMax(): Int = valueMap.lastKey()

        fun popMax(): Int {
            val x = valueMap.lastKey()
            val ids = valueMap[x]!!
            val id = ids.removeAt(ids.size - 1)
            if (ids.isEmpty()) valueMap.remove(x)
            timeMap.remove(id)
            return x
        }
    }

    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    // LC 142. Linked List Cycle II
    fun detectCycle(head: ListNode?): ListNode? {
        // HashSet Approach
        // Time Complexity: O(N) | Space Complexity: O(N)

        // Initialize an empty hash set
        val nodesSeen = HashSet<ListNode?>()

        // Start from the head of the linked list
        var node = head
        while (node != null) {
            // If the current node is in nodesSeen, we have a cycle
            if (nodesSeen.contains(node)) {
                return node
            } else {
                // Add this node to nodesSeen and move to the next node
                nodesSeen.add(node)
                node = node.next
            }
        }

        // If we reach a null node, there is no cycle
        return null


        // 2. Floyd's Cycle-Finding Algorithm
        // Time Complexity: O(N) | Space Complexity: O(1)
        //        var tortoise = head
        //        var hare = head
        //        var meet = false
        //
        //        while (hare != null && hare.next != null) {
        //            hare = hare.next!!.next
        //            tortoise = tortoise?.next
        //            if (tortoise != null && tortoise == hare) {
        //                tortoise = head
        //                meet = true
        //                break
        //            }
        //        }
        //
        //        if (meet) {
        //            while (tortoise != hare) {
        //                tortoise = tortoise?.next
        //                hare = hare?.next
        //            }
        //            return tortoise
        //        } else {
        //            return null
        //        }
    }

    // LC 496. Next Greater Element I
    fun nextGreaterElement(nums1: IntArray, nums2: IntArray): IntArray {
        // 1. Highly Optimized Monotonic Stack (Manual Pointer)
        // Time Complexity: O(N1 + N2) | Space Complexity: O(N2)
        val stack = IntArray(nums2.size)
        var top = -1
        val nextGreaterMap = HashMap<Int, Int>(nums2.size)

        // Phase 1: Build the map of "Next Greater" for all elements in nums2
        for (num in nums2) {
            // While current is greater than the top element, we found its next greater
            while (top >= 0 && num > stack[top]) {
                nextGreaterMap[stack[top--]] = num
            }
            stack[++top] = num
        }

        // Phase 2: Populate result for nums1 using the pre-calculated map
        val result = IntArray(nums1.size)
        for (i in nums1.indices) {
            result[i] = nextGreaterMap.getOrDefault(nums1[i], -1)
        }

        return result

        // 2. Manual Stack Pointer Approach (Optimized)
        // Time Complexity: O(nums1.size + nums2.size) | Space Complexity: O(nums1.size)
        //        val stack = IntArray(nums2.size)
        //        val map = HashMap<Int, Int>()
        //        val result = IntArray(nums1.size)
        //        var top = -1
        //
        //        for (curr in nums2.indices) {
        //            while (top >= 0 && nums2[curr] > nums2[stack[top]]) {
        //                val prev = stack[top--]
        //                map[nums2[prev]] = nums2[curr]
        //            }
        //            stack[++top] = curr
        //        }
        //
        //        for (i in nums1.indices) {
        //            result[i] = if (map.contains(nums1[i])) map[nums1[i]]!! else -1
        //        }
        //        return result
    }


}