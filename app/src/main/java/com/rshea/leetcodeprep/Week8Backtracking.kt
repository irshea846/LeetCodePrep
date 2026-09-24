package com.rshea.leetcodeprep

import java.util.PriorityQueue

object Week8Backtracking {

    // Day 38 - LC 148. Sort List
    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    fun sortList(head: ListNode?): ListNode? {
        val pq = PriorityQueue<ListNode> { a, b -> a.`val` - b.`val` }
        var listNode = head
        while (listNode != null) {
            pq.add(listNode)
            listNode = listNode.next
        }

        val dummy = ListNode(0)
        var tail = dummy
        while (pq.isNotEmpty()) {
            val node = pq.poll()!!
            tail.next = node
            tail = node
        }
        tail.next = null

        return dummy.next
    }

    // Day 37 - LC 46. Permutations
    fun permute(nums: IntArray): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        val used = BooleanArray(nums.size)

        fun backtrack(sublist: MutableList<Int>) {
            if (sublist.size == nums.size) {
                list.add(ArrayList(sublist))
                return
            }

            for (i in nums.indices) {
                if (used[i]) continue
                used[i] = true
                sublist.add(nums[i])
                backtrack(sublist)
                used[i] = false
                sublist.removeAt(sublist.size - 1)
            }
        }

        backtrack(mutableListOf())
        return list
    }

    fun permuteDQ(nums: IntArray): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        val used = BooleanArray(nums.size)
        val dq = ArrayDeque<Int>(nums.size)

        fun backtrack() {
            if (dq.size == nums.size) {
                list.add(dq.toList())
                return
            }

            for (i in nums.indices) {
                if (used[i]) continue
                dq.addLast(nums[i])
                used[i] = true
                backtrack()
                dq.removeLast()
                used[i] = false
            }

        }

        backtrack()
        return list
    }

    fun permuteInPlace(nums: IntArray): List<List<Int>> {
        val list = mutableListOf<List<Int>>()

        fun swap(i: Int, j:Int) {
            val temp = nums[i]
            nums[i] = nums[j]
            nums[j] = temp
        }

        fun backtrack(start: Int) {
            if (start == nums.size) {
                list.add(nums.toList())
                return
            }

            for (i in start until nums.size) {
                swap(start, i)
                backtrack(start + 1)
                swap(start, i)
            }
        }

        backtrack(0)
        return list
    }


    // Day 36 - LC 78. Subsets
    fun subsets(nums: IntArray): List<List<Int>> {
        // Highly Optimized Backtracking Approach
        // Time Complexity: O(N * 2^N) | Space Complexity: O(N)
        val result = mutableListOf<List<Int>>()

        fun backtrack(start: Int, currentPath: MutableList<Int>) {
            // 1. Add a copy of the current state to results
            result.add(ArrayList(currentPath))

            // 2. Explore further combinations
            for (i in start until nums.size) {
                currentPath.add(nums[i])      // Choose
                backtrack(i + 1, currentPath)  // Explore
                currentPath.removeAt(currentPath.lastIndex) // Un-choose (Backtrack)
            }
        }

        backtrack(0, mutableListOf())
        return result
    }

    fun subsetsOriginal(nums: IntArray): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        findCombinations(nums, 0, mutableListOf<Int>(), list)
        return list
    }

    fun findCombinations(nums: IntArray, i: Int, sublist: MutableList<Int>, list: MutableList<List<Int>>) {
        list.add(sublist.toList())
        for (j in i .. nums.lastIndex) {
            sublist.add(nums[j])
            findCombinations(nums, j + 1, sublist, list)
            sublist.removeAt(sublist.lastIndex)
        }
        return
    }


}