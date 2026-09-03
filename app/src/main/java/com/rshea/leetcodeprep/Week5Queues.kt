package com.rshea.leetcodeprep

import java.util.LinkedList
import java.util.Queue

object Week5Queues {

    class TreeNode(var `val`: Int) {
        var left: TreeNode? = null
        var right: TreeNode? = null
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