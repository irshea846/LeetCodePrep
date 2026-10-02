package com.rshea.leetcodeprep

import java.util.PriorityQueue
import kotlin.math.max

object Week9Graphs {

    // Day 44 - LC 133. Clone Graph
    class Node(var `val`: Int) {
        var neighbors: ArrayList<Node?> = ArrayList<Node?>()
    }

    fun cloneGraphDFS(node: Node?): Node? {
        if (node == null) return null
        val cloneMap = HashMap<Node, Node>()

        fun cloneHelper(v: Node): Node {
            cloneMap[v]?.let { return it }

            val clone = Node(v.`val`)
            cloneMap[v] = clone

            for (neighbor in v.neighbors) {
                neighbor?.let {
                    clone.neighbors.add(cloneHelper(it))
                }
            }
            return clone
        }

        return cloneHelper(node)
    }

    fun cloneGraphBFS(node: Node?): Node? {
        if (node == null) return null
        val cloneMap = HashMap<Node, Node>()
        val dq = ArrayDeque<Node>()

        val cloneHead = Node(node.`val`)
        cloneMap[node] = cloneHead
        dq.addLast(node)

        while (dq.isNotEmpty()) {
            val curr = dq.removeFirst()
            val currClone = cloneMap[curr]!!

            for (neighbor in curr.neighbors) {
                if (neighbor == null) continue
                if (neighbor !in cloneMap) {
                    cloneMap[neighbor] = Node(neighbor.`val`)
                    dq.addLast(neighbor)
                }
                currClone.neighbors.add(cloneMap[neighbor])
            }
        }

        return cloneHead
    }

    // Day 43 - LC 743. Network Delay Time
    fun networkDelayTime(times: Array<IntArray>, n: Int, k: Int): Int {
        val adjMap = Array(n + 1) { _ -> mutableListOf<Pair<Int, Int>>() }
        val dist = IntArray(n + 1) { Int.MAX_VALUE }
        dist[k] = 0

        for (time in times) {
            val (u, v, w) = time
            adjMap[u].add(Pair(w, v))
        }

        val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })
        pq.add(Pair(0, k))

        while (pq.isNotEmpty()) {
            val (currentWeight, curNode) = pq.poll()!!
            if (currentWeight > dist[curNode]) continue
            for (adjacent in adjMap[curNode]) {
                val (adjWeight, adjNode) = adjacent
                val newWeight = currentWeight + adjWeight
                if (newWeight < dist[adjNode]) {
                    dist[adjNode] = newWeight
                    pq.add(Pair(dist[adjNode], adjNode))
                }
            }
        }

        var maxWeight = Int.MIN_VALUE
        for (i in 1 .. n) {
            maxWeight = max(maxWeight, dist[i])
        }

        return if (maxWeight == Int.MAX_VALUE) -1 else maxWeight
    }


    // Day 42 - LC 207. Course Schedule
    fun canFinishDFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
        val courseStates = IntArray(numCourses)
        val graphs = Array(numCourses) { mutableListOf<Int>() }

        for (prerequisite in prerequisites) {
            val post = prerequisite[0]
            val prev = prerequisite[1]
            graphs[prev].add(post)
        }

        fun hasCycle(curr: Int): Boolean {
            if (courseStates[curr] == 1) return true
            if (courseStates[curr] == 2) return false

            courseStates[curr] = 1
            for (postrequisite in graphs[curr]) {
                if (hasCycle(postrequisite)) return true
            }
            courseStates[curr] = 2
            return false
        }

        for (i in 0 until numCourses) {
            if (courseStates[i] == 0) {
                if (hasCycle(i)) return false
            }
        }

        return true
    }

    fun canFinishBFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
        val inDegree = IntArray(numCourses)
        val graphs = Array(numCourses) { mutableListOf<Int>() }
        var totalCourses = 0
        val dq = ArrayDeque<Int>(numCourses)
        for (prerequisite in prerequisites) {
            val prev = prerequisite[1]
            val post = prerequisite[0]
            graphs[prev].add(post)
            inDegree[post]++
        }

        for (i in 0 until numCourses) {
            if (inDegree[i] == 0) {
                dq.addLast(i)
            }
        }
        while (dq.isNotEmpty()) {
            val course = dq.removeLast()
            totalCourses++
            for (postrequisite in graphs[course]) {
                if (--inDegree[postrequisite] == 0) {
                    dq.addLast(postrequisite)
                }
            }
        }

        return totalCourses == numCourses
    }

    // Day 41 - LC 200. Number of Islands
    fun numIslands(grid: Array<CharArray>): Int {
        if (grid.isEmpty() || grid[0].isEmpty()) return 0

        val rows = grid.size
        val cols = grid[0].size
        var islands = 0

        fun dfs(r: Int, c: Int) {
            if ((r !in 0 until rows) || (c !in 0 until cols) || (grid[r][c] == '0')) {
                return
            }

            grid[r][c] = '0' // Sink visited land
            dfs(r + 1, c)
            dfs(r - 1, c)
            dfs(r, c + 1)
            dfs(r, c - 1)
        }

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (grid[r][c] == '1') {
                    islands++
                    dfs(r, c)
                }
            }
        }

        return islands
    }

    // Approach 2: Queue-Based BFS (Bounded Space)
    fun numIslandsBFS(grid: Array<CharArray>): Int {
        if (grid.isEmpty() || grid[0].isEmpty()) return 0

        val rows = grid.size
        val cols = grid[0].size
        var islands = 0
        val dirs = arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(0, -1))

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (grid[r][c] == '1') {
                    islands++
                    grid[r][c] = '0'
                    val queue = ArrayDeque<IntArray>()
                    queue.addLast(intArrayOf(r, c))

                    while (queue.isNotEmpty()) {
                        val (currR, currC) = queue.removeFirst()
                        for (dir in dirs) {
                            val nr = currR + dir[0]
                            val nc = currC + dir[1]
                            if (nr in 0 until rows && nc in 0 until cols && grid[nr][nc] == '1') {
                                grid[nr][nc] = '0'
                                queue.addLast(intArrayOf(nr, nc))
                            }
                        }
                    }
                }
            }
        }

        return islands
    }

    // Approach 3: Queue-Based BFS (Bit Packing Technique - Zero Heap Allocation)
    fun numIslandsBitPacking(grid: Array<CharArray>): Int {
        if (grid.isEmpty() || grid[0].isEmpty()) return 0

        val neighbors = arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(0, -1))
        var islands = 0
        val m = grid.size
        val n = grid[0].size
        val capacity = kotlin.math.min(m, n)
        val dq = ArrayDeque<Int>(capacity)

        for (i in 0 until m) {
            for (j in 0 until n) {
                if (grid[i][j] == '0') continue
                grid[i][j] = '0'
                islands++
                dq.addLast((i shl 16) or j)

                while (dq.isNotEmpty()) {
                    val encode = dq.removeFirst()
                    val x = encode ushr 16
                    val y = encode and 0xFFFF

                    for (neighbor in neighbors) {
                        val adjx = x + neighbor[0]
                        val adjy = y + neighbor[1]
                        if (adjx !in 0 until m || adjy !in 0 until n || grid[adjx][adjy] == '0') {
                            continue
                        }
                        grid[adjx][adjy] = '0'
                        dq.addLast((adjx shl 16) or adjy)
                    }
                }
            }
        }
        return islands
    }
}