package com.rshea.leetcodeprep.masteryreviewday

object MasterW5Queues {

    class TreeNode(var `val`: Int) {
        var left: TreeNode? = null
        var right: TreeNode? = null
    }

    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    // LC 142. Linked List Cycle II
    // Current: Linked List / Two Pointers / Floyd's Cycle Detection
    // Suggested: Linked List / Two Pointers / Floyd's Cycle Detection
    // Key Idea: Using two pointers at different speeds to detect a cycle and determine its
    // entrance via mathematical distance relationships.
    // Consider: Can you prove mathematically why the two pointers meet at the cycle entrance when
    // one restarts from the head?
    fun detectCycle(head: ListNode?): ListNode? {
        // Implement Using Time O(N) and Space O(1)
        var slow = head
        var fast = head

        while (fast != null && fast.next != null) {
            slow = slow?.next
            fast = fast.next!!.next

            if (slow == fast) {
                slow = head
                while (slow != fast) {
                    slow = slow!!.next
                    fast = fast!!.next
                }
                return slow
            }
        }
        return null
    }

    // LC 21. Merge Two Sorted Lists
    // Current: Linked List / Two Pointers / Simulation
    // Suggested: Linked List / Two Pointers / Recursion
    // Key Idea: Merging two sorted sequences by comparing the head elements and maintaining a
    // pointer to the tail of the new list.
    // Consider: How would you adapt this logic if you were asked to merge k sorted lists instead
    // of just two?
    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
        // Time O(M + N) Space O(1)
        val dummy = ListNode(-1)
        var next = dummy
        var l1 = list1
        var l2 = list2
        while (l1 != null && l2 != null) {
            if (l1.`val` <= l2.`val`) {
                next.next = l1; next = l1;
                l1 = l1.next
            } else {
                next.next = l2; next = l2;
                l2 = l2.next
            }
        }
        next.next = l1 ?: l2
        return dummy.next
    }

    // LC 207. Course Schedule
    // Current: Topological Sort / Breadth-First Search / Queue
    // Suggested: Topological Sort / Depth-First Search / Directed Acyclic Graph (DAG)
    // Key Idea: Detecting cycles in a directed graph using in-degree counts to verify if a valid
    // linear ordering of nodes exists.
    // Consider: If you needed to return the actual order of courses instead of just a boolean,
    // how would you modify your current collection logic?
    fun canFinish(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
        // Time O(V + E) Space(V + E) Khan's or DFS
        val neighbors = Array(numCourses) { mutableListOf<Int>() }
        val inDegree = IntArray(numCourses)

        for (preq in prerequisites) {
            neighbors[preq[1]].add(preq[0])
            inDegree[preq[0]]++
        }

        val dq = ArrayDeque<Int>(numCourses)
        var coursesCnt = 0
        for (i in inDegree.indices) {
            if (inDegree[i] == 0) dq.addLast(i)
        }

        while (dq.isNotEmpty()) {
            val course = dq.removeFirst()
            coursesCnt++
            for (neighbor in neighbors[course]) {
                inDegree[neighbor]--
                if (inDegree[neighbor] == 0) {
                    dq.addLast(neighbor)
                }
            }
        }

        return coursesCnt == numCourses
    }

    // LC 239. Sliding Window Maximum
    // Current: Array / Queue / Monotonic Queue / Sliding Window
    // Suggested: Array / Queue / Monotonic Queue / Sliding Window
    // Key Idea: Maintaining a monotonic deque to track indices of maximal elements within a rolling window.
    // Consider: What if you needed to find both the minimum and maximum in each window?
    // How would you modify your deque strategy to handle both efficiently?
    fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
        // Time O(N) Space O(K)
        val maxdq = ArrayDeque<Int>(k)
        val result = IntArray(nums.size - k + 1)

        for (i in nums.indices) {
            while (maxdq.isNotEmpty() && nums[i] > nums[maxdq.last()]) {
                maxdq.removeLast()
            }
            maxdq.addLast(i)
            val idx = i - k + 1
            if (idx >= 0) {
                if (maxdq.first() < idx) maxdq.removeFirst()
                result[idx] = nums[maxdq.first()]
            }
        }
        return result
    }

    // LC 102. Binary Tree Level Order Traversal
    // Current: Breadth-First Search / Queue / Binary Tree
    // Suggested: Breadth-First Search / Queue / Binary Tree
    // Key Idea: Using a queue to track nodes level by level ensures that the result maintains the required hierarchical order.
    // Consider: How would you modify this to return the levels in reverse order, starting from the leaf level up to the root?
    fun levelOrder(root: TreeNode?): List<List<Int>> {
        // Time O(N) Space O(W)
        if (root == null) return emptyList()

        val q = ArrayDeque<TreeNode>(2000)
        val wholeTreeList = mutableListOf<List<Int>>()
        q.addLast(root)

        while (q.isNotEmpty()) {
            val times = q.size
            val list = ArrayList<Int>(times)
            repeat(times) {
                val node = q.removeFirst()
                node.left?.let { q.addLast(node.left!!) }
                node.right?.let { q.addLast(node.right!!) }
                list.add(node.`val`)
            }
            wholeTreeList.add(list)
        }
        return wholeTreeList
    }

}