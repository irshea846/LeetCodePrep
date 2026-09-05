package com.rshea.leetcodeprep

import java.util.LinkedList
import java.util.Queue

object Week5Queues {

    class TreeNode(var `val`: Int) {
        var left: TreeNode? = null
        var right: TreeNode? = null
    }

    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    // Day 27 - LC 142. Linked List Cycle II

    // Peer Review: detectCycleRecursion (The "HashSet" Version)
    //  • Risk: State Leaks. Using a publicSet outside the function means if you call this function
    //    twice on different lists, the second call will see nodes from the first list.
    //    This would cause a bug.
    //  • Risk: Stack Overflow. On the JVM, a list with 10,000 nodes will crash this function.
    //  • Verdict: Avoid recursion for linked lists unless the problem specifically asks for it.
    val publicSet = HashSet<ListNode>()
    fun detectCycleRecursion(head: ListNode?): ListNode? {
        if (head == null) return null
        if (publicSet.contains(head)) return head
        publicSet.add(head)
        return detectCycleRecursion(head.next)
    }

    //Peer Review: detectCycleIteration (The "HashSet" Version)
    //  • Syntax Bug: You initialized the set as privateSet but tried to add to nodesSeen.
    //  • Performance: $O(N)$ Time | $O(N)$ Space. This is a great "Plan B" if you forget the math
    //    for Floyd's, but it's less impressive to interviewers because of the memory cost.
    fun detectCycleIteration(head: ListNode?): ListNode? {
        val privateSet = HashSet<ListNode>()
        var next = head
        while (next != null) {
            if (privateSet.contains(next)) return next
            privateSet.add(next)
            next = next.next
        }
        return next
    }

    //Peer Review: detectCycle (The Optimized Version)
    //    • Performance: Optimal $O(N)$ Time | $O(1)$ Space. This is the gold standard for interviews.
    //    • Logic: Your Phase 1 and Phase 2 implementations are perfectly synchronized.
    //    • Syntax: Your use of fast.next!!.next is safe because of the loop guard.
    //    • Verdict: This is the version you should lead with. It demonstrates mathematical depth and
    //    memory awareness.
    fun detectCycle(head: ListNode?): ListNode? {
        // Highly Optimized Two-Phase Floyd's Algorithm
        // Time Complexity: O(N) | Space Complexity: O(1)
        var slow = head
        var fast = head

        // Phase 1: Detect Cycle
        while (fast != null && fast.next != null) {
            slow = slow?.next
            fast = fast.next!!.next

            if (slow == fast) {
                // Phase 2: Find Entry Point
                // Reset entry pointer to head; move both at 1x speed
                var entry = head
                while (entry != slow) {
                    entry = entry?.next
                    slow = slow?.next
                }
                return entry
            }
        }

        return null
    }

    // Day 26 - LC 21. Merge Two Sorted Lists
    fun mergeTwoListsRecursion(list1: ListNode?, list2: ListNode?): ListNode? {
        if (list1 == null || list2 == null) return list1 ?: list2

        if (list1.`val` <= list2.`val`) {
            list1.next = mergeTwoListsRecursion(list1.next, list2)
            return list1
        } else {
            list2.next = mergeTwoListsRecursion(list1, list2.next)
            return list2
        }
    }

    fun mergeTwoListsIteration(list1: ListNode?, list2: ListNode?): ListNode? {
        var l1 = list1
        var l2 = list2
        var head: ListNode? = null
        var next: ListNode? = null

        var node: ListNode? = null
        while (l1 != null && l2 != null) {
            if (l1.`val` <= l2.`val`) {
                node = l1
                l1 = l1.next
                node.next = null
            } else {
                node = l2
                l2 = l2.next
                node.next = null
            }

            head = head ?: node

            if (next != null) {
                next.next = node
                next = node
            } else {
                next = head
            }
        }

        if (next != null) {
            next.next = l1 ?: l2
        } else {
            next = l1 ?: l2
        }

        return head ?: next
    }

    // Day 25 - LC 207. Course Schedule
    fun canFinishDFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
        // State representation: 0 = UNVISITED, 1 = VISITING, 2 = SAFE
        val state = IntArray(numCourses)
        val connectedNodes = Array(numCourses) { mutableListOf<Int>() }
        for (preq in prerequisites) {
            val source = preq[1]
            val destination = preq[0]
            connectedNodes[source].add(destination)
        }

        fun dsf(startNode: Int): Boolean {
            if (state[startNode] == 1) return false
            if (state[startNode] == 2) return true

            state[startNode] = 1
            for (node in connectedNodes[startNode]) {
                if (!dsf(node)) {
                    return false
                }
            }
            state[startNode] = 2
            return true
        }

        for (i in 0 until numCourses) {
            if (state[i] == 0) {
                if (!dsf(i)) {
                    return false
                }
            }
        }

        return true
    }


    fun canFinishBFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
        val adjacentList = Array<MutableList<Int>>(numCourses) { mutableListOf() }
        val inDegree = IntArray(numCourses)

        for (preq in prerequisites) {
            val destination = preq[0]
            val source = preq[1]

            adjacentList[source].add(destination)
            inDegree[destination]++
        }

        val q = ArrayDeque<Int>(numCourses)
        for (i in inDegree.indices) {
            if (inDegree[i] == 0) {
                q.addLast(i)
            }
        }

        val topologicalOrder = IntArray(numCourses) // In case we need to return the order
        var index = 0
        while (q.isNotEmpty()) {
            val course = q.removeFirst()
            val neighbors = adjacentList[course]

            if (neighbors.isNotEmpty()) {
                for (neighbor in neighbors) {
                    if (--inDegree[neighbor] == 0) {
                        q.addLast(neighbor)
                    }
                }
            }
            topologicalOrder[index++] = course
        }
        return index == numCourses
    }

    // Day 24 - LC 239. Sliding Window Maximum
    fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
        val result = IntArray(nums.size - k + 1)
        val q = ArrayDeque<Int>()
        for (i in 0 until k) {
            while (q.isNotEmpty() && nums[i] > nums[q.last()]) {
                q.removeLast()
            }
            q.addLast(i)
        }

        for (i in k until nums.size) {
            val idx = i - k

            result[idx] = nums[q.first()]

            if (idx == q.first()) q.removeFirst()

            while (q.isNotEmpty() && nums[i] > nums[q.last()]) {
                q.removeLast()
            }

            q.add(i)
        }

        result[result.lastIndex] = nums[q.first()]

        return result
    }


    // Day 23 - LC 102. Binary Tree Level Order Traversal
    fun levelOrder(root: TreeNode?): List<List<Int>> {
        if (root == null) return emptyList()

        // 1. High-performance Queue using ArrayDeque (Circular Array)
        // Initial capacity set to 1024 to avoid resizing overhead for medium trees
        val queue = ArrayDeque<TreeNode>(1024)
        val result = mutableListOf<List<Int>>()

        queue.addLast(root)

        while (queue.isNotEmpty()) {
            val levelSize = queue.size
            val currentLevel = ArrayList<Int>(levelSize) // Pre-size for performance

            // Process current level only
            repeat(levelSize) {
                val node = queue.removeFirst()
                currentLevel.add(node.`val`)

                // Queue children for the next level
                node.left?.let { queue.addLast(it) }
                node.right?.let { queue.addLast(it) }
            }
            result.add(currentLevel)
        }

        return result

        // 2. BFS Approach (LinkedList)
        // Time Complexity: O(N) | Space Complexity: O(N)
        //        val q: Queue<TreeNode> = LinkedList<TreeNode>()
        //        val list = mutableListOf<List<Int>>()
        //
        //        if (root != null) q.add(root)
        //        while (q.isNotEmpty()) {
        //            val size = q.size
        //            val sub = mutableListOf<Int>()
        //            var node: TreeNode
        //            for (i in 0 until size) {
        //                node = q.poll()!!
        //                sub.add(node.`val`)
        //                if (node.left != null) q.add(node.left!!)
        //                if (node.right != null) q.add(node.right!!)
        //            }
        //            list.add(sub.toList())
        //        }
        //        return list
    }

}