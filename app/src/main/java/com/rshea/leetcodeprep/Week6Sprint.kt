package com.rshea.leetcodeprep

object Week6Sprint {
    class TreeNode(var `val`: Int) {
        var left: TreeNode? = null
        var right: TreeNode? = null
    }

    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    // Day 32 - LC 622. Design Circular Queue
    class MyOptimizedCircularQueue(private val k: Int) {
        // Optimized Circular Buffer Approach
        // Time Complexity: O(1) for all operations | Space Complexity: O(K)
        private val buffer = IntArray(k)
        private var head = 0
        private var tail = -1
        private var size = 0

        fun enQueue(value: Int): Boolean {
            if (isFull()) return false
            tail = (tail + 1) % k
            buffer[tail] = value
            size++
            return true
        }

        fun deQueue(): Boolean {
            if (isEmpty()) return false
            head = (head + 1) % k
            size--
            return true
        }

        fun Front(): Int = if (isEmpty()) -1 else buffer[head]

        fun Rear(): Int = if (isEmpty()) -1 else buffer[tail]

        fun isEmpty(): Boolean = size == 0

        fun isFull(): Boolean = size == k
    }

    class MyDynamicMemoCircularQueue(val k: Int) {
        private val capacity = k
        private val buffer = IntArray(capacity) { -1 }
        private var size = 0
        private var head = 0
        private var tail = 0

        fun enQueue(value: Int): Boolean {
            if (isFull()) {
                return false
            }
            buffer[head] = value
            size++
            if (++head == capacity) head = 0
            return true
        }

        fun deQueue(): Boolean {
            if (isEmpty()) {
                return false
            }
            buffer[tail] = -1
            size--
            if (++tail == capacity) tail = 0
            return true
        }

        fun Front(): Int {
            return if (isEmpty()) -1 else buffer[tail]
        }

        fun Rear(): Int {
            return if (isEmpty()) -1 else if(head == 0) buffer[k - 1] else buffer[head - 1]
        }

        fun isEmpty(): Boolean {
            return 0 == size
        }

        fun isFull(): Boolean {
            return k == size
        }

    }


    class MyStaticMemoCircularQueue(val k: Int) {
        val capacity = 2000
        val buffer = IntArray(capacity) { -1 }
        var start = 0
        var current = 0

        fun enQueue(value: Int): Boolean {
            if (isFull()) return false
            buffer[current++] = value
            if (current == capacity) current = 0
            return true
        }

        fun deQueue(): Boolean {
            if (isEmpty()) return false
            buffer[start++] = -1
            if (start == capacity) start = 0
            return true
        }

        fun Front(): Int {
            return if (isEmpty()) -1 else buffer[start]
        }

        fun Rear(): Int {
            return if (isEmpty()) -1 else buffer[current - 1]
        }

        fun isEmpty(): Boolean {
            return current == start
        }

        fun isFull(): Boolean {
            val diff = if (current < start) current + capacity - start else current - start
            return k == diff
        }

    }

    // Day 31 - LC 547. Number of Provinces
    fun findCircleNumDSU(isConnected: Array<IntArray>): Int {
        val cities = isConnected.size
        val dsu = DisjoinSetUnion(cities)
        var provinces = cities

        for (i in 0 until cities) {
            for (j in i + 1 until cities) {
                if (isConnected[i][j] == 1) {
                    if (dsu.union(i, j)) {
                        provinces--
                    }
                }
            }
        }

        return provinces
    }

    class DisjoinSetUnion(n: Int) {
        val parent = IntArray(n) { it }
        val rank = IntArray(n) { 1 }

        fun find(i: Int): Int {
            if (parent[i] == i) return i
            parent[i] = find(parent[i])
            return parent[i]
        }

        fun union(i: Int, j: Int): Boolean {
            val rootI = find(i)
            val rootJ = find(j)

            if (rootI != rootJ) {
                when {
                    rank[rootI] > rank[rootJ] -> parent[rootJ] = rootI
                    rank[rootI] < rank[rootJ] -> parent[rootI] = rootJ
                    else -> {
                        parent[rootJ] = rootI
                        rank[rootI]++
                    }
                }
                return true
            }
            return false
        }
    }

    fun findCircleNumDFS(isConnected: Array<IntArray>): Int {
        val cities = isConnected.size
        val visited = BooleanArray(cities)

        var provinces = 0
        for (i in isConnected.indices) {
            if (visited[i]) continue
            dfs(i, isConnected, visited)
            provinces++
        }
        return provinces
    }

    fun dfs(city: Int, neighbors: Array<IntArray>, visited: BooleanArray) {
        if (visited[city]) return
        visited[city] = true
        for (i in neighbors[city].indices) {
            if (1 == neighbors[city][i] && !visited[i]) {
                dfs(i, neighbors, visited)
            }
        }
    }

    fun findCircleNumBFS(isConnected: Array<IntArray>): Int {
        val n = isConnected.size
        val visited = BooleanArray(n)
        var provinces = 0

        for (city in 0 until n) {
            if (!visited[city]) {
                provinces++
                bfs(city, isConnected, visited)
            }
        }
        return provinces
    }

    fun bfs(node: Int, isConnected: Array<IntArray>, visited: BooleanArray) {
        val n = isConnected.size
        val dq = ArrayDeque<Int>(n)
        dq.addLast(node)
        visited[node] = true
        while (dq.isNotEmpty()) {
            val i = dq.removeFirst()
            for (j in 0 until n) {
                if (isConnected[i][j] == 1) {
                    if (!visited[j]) {
                        visited[j] = true
                        dq.addLast(j)
                    }
                }
            }
        }
    }

    // Day 30 - LC 103. Binary Tree Zigzag Level Order Traversal
    fun zigzagLevelOrderDFS(root: TreeNode?): List<List<Int>> {
        val result = mutableListOf<ArrayDeque<Int>>()
        dfs(root, 0, result)
        return result
    }

    private fun dfs(node: TreeNode?, level: Int, result: MutableList<ArrayDeque<Int>>) {
        if (node == null) return

        // Dynamic Growth: Only allocate levels as needed
        if (level == result.size) {
            result.add(ArrayDeque())
        }

        // Zero-Reversal Zigzag: use addLast for L->R, addFirst for R->L
        if (level % 2 == 0) {
            result[level].addLast(node.`val`)
        } else {
            result[level].addFirst(node.`val`)
        }

        dfs(node.left, level + 1, result)
        dfs(node.right, level + 1, result)
    }

    fun zigzagLevelOrderBFS(root: TreeNode?): List<List<Int>> {
        // Supreme Approach: Standard BFS + Deque-based Sublist
        // Achieves zero reversals and peak CPU performance through monotonic traversal
        if (root == null) return emptyList()

        val result = mutableListOf<List<Int>>()
        val queue = ArrayDeque<TreeNode>(1024) // Initial capacity for performance
        queue.addLast(root)
        var leftToRight = true

        while (queue.isNotEmpty()) {
            val levelSize = queue.size
            // Use ArrayDeque for the sublist to allow O(1) addFirst/addLast
            // Note: ArrayDeque implements List<Int>, making it zero-copy compatible with the result
            val sublist = ArrayDeque<Int>(levelSize)

            repeat(levelSize) {
                val node = queue.removeFirst()

                // Handle zigzag by choosing insertion end in the sublist
                if (leftToRight) {
                    sublist.addLast(node.`val`)
                } else {
                    sublist.addFirst(node.`val`)
                }

                // Traversal is ALWAYS standard Left-to-Right (Monotonic)
                node.left?.let { queue.addLast(it) }
                node.right?.let { queue.addLast(it) }
            }

            result.add(sublist)
            leftToRight = !leftToRight
        }
        return result
    }

    // Day 29 - LC 61. Rotate List
    fun rotateRight(head: ListNode?, k: Int): ListNode? {
        var nodes = 0
        var ptr = head
        var moves = k

        while (ptr != null && ptr.next != null) {
            nodes++
            ptr = ptr.next
        }
        nodes++
        val tail = ptr
        ptr = head

        if (nodes == moves || nodes == 0) return head
        moves = if (nodes < moves) nodes - (moves % nodes) - 1
        else nodes - moves - 1
        nodes = 0
        while (ptr != null && nodes++ < moves) {
            ptr = ptr.next
        }

        tail?.next = head
        val temp = ptr
        ptr = ptr?.next
        temp?.next = null

        return ptr
    }

    // Day 28 - LC 150. Evaluate Reverse Polish Notation
    fun evalRPNZeroBoxing(tokens: Array<String>): Int {
        // True Zero Allocation / Zero Boxing Approach
        // Uses a primitive IntArray as a stack to avoid Integer object creation (Boxing)
        val st = IntArray(tokens.size)
        var top = -1

        for (str in tokens) {
            // 1. Manual check is cheaper than a failed toIntOrNull() parse
            if (str != "+" && str != "-" && str != "*" && str != "/") {
                // 2. Direct primitive assignment (No Boxing)
                st[++top] = str.toInt()
            } else {
                val op2 = st[top--]
                val op1 = st[top--]
                st[++top] = when (str) {
                    "+" -> op1 + op2
                    "-" -> op1 - op2
                    "*" -> op1 * op2
                    else -> op1 / op2
                }
            }
        }
        return st[0]
    }

    fun evalRPN(tokens: Array<String>): Int {
        val st = ArrayDeque<Int>(tokens.size)
        var op1: Int
        var op2: Int

        for (str in tokens) {
            if (str != "+" && str != "-" && str != "*" && str != "/") {
                st.addLast(str.toInt())
            } else {
                op2 = st.removeLast()
                op1 = st.removeLast()
                when (str) {
                    "+" -> st.addLast(op1 + op2)
                    "-" -> st.addLast(op1 - op2)
                    "*" -> st.addLast(op1 * op2)
                    else -> st.addLast(op1 / op2)
                }
            }
        }
        return st.removeLast()
    }

    inline fun calculate(x: Int, y: Int, operation: (Int, Int) -> Int): Int {
        return operation(x, y)
    }

    fun evalRPNHigherOrderFunction(tokens: Array<String>): Int {
        val plus: ((Int, Int) -> Int) = { a, b -> a + b }
        val minus: ((Int, Int) -> Int) = { a, b -> a - b }
        val times: ((Int, Int) -> Int) = { a, b -> a * b }
        val divide: ((Int, Int) -> Int) = { a, b -> a / b }

        val st = ArrayDeque<Int>(tokens.size)
        var op1: Int
        var op2: Int

        for (str in tokens) {
            val validNumber = str.toIntOrNull()
            if (validNumber != null) {
                st.addLast(validNumber)
            } else {
                op2 = st.removeLast()
                op1 = st.removeLast()
                when (str) {
                    "+" -> st.addLast(calculate(op1, op2, plus))
                    "-" -> st.addLast(calculate(op1, op2, minus))
                    "*" -> st.addLast(calculate(op1, op2, times))
                    else -> st.addLast(calculate(op1, op2, divide))
                }
            }
        }
        return st.removeLast()
    }
}