package com.rshea.leetcodeprep

import java.util.LinkedList
import java.util.Queue

object Week5Queues {

    class TreeNode(var `val`: Int) {
        var left: TreeNode? = null
        var right: TreeNode? = null
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