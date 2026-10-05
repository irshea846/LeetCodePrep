package com.rshea.leetcodeprep.masteryreviewday

import kotlin.math.min
import java.util.PriorityQueue
object MasterW9Graphs {

    // LC 133. Clone Graph
    class Node(var `val`: Int) {
        var neighbors: ArrayList<Node?> = ArrayList<Node?>()
    }

    fun cloneGraphDFS(node: Node?): Node? {
        if (node == null) return null
        val map = HashMap<Node, Node>()

        fun clone(v: Node): Node {
            map[v]?.let { return it }

            val clonedNode = Node(v.`val`)
            map[v] = clonedNode
            for (adj in v.neighbors) {
                adj?.let {
                    clonedNode.neighbors.add(clone(it))
                }
            }
            return clonedNode
        }
        return clone(node)
    }

    fun cloneGraphBFS(node: Node?): Node? {
        if (node == null) return null
        val map = HashMap<Node, Node>()
        val dq = ArrayDeque<Node>()
        val cloneHead = Node(node.`val`)
        map[node] = cloneHead
        dq.addLast(node)

        while (dq.isNotEmpty()) {
            val currNode = dq.removeFirst()
            val clonedNode = map[currNode]
            for (adj in currNode.neighbors) {
                if (adj == null) continue
                if (adj !in map) {
                    map[adj] = Node(adj.`val`)
                    dq.addLast(adj)
                }
                clonedNode!!.neighbors.add(map[adj]!!)
            }
        }
        return cloneHead
    }


    // LC 743. Network Delay Time
    fun networkDelayTimeBellmanFord(times: Array<IntArray>, n: Int, k: Int): Int {
        val dist = IntArray(n + 1) { Int.MAX_VALUE }
        dist[k] = 0

        for (i in 1 .. n) {
            for (time in times) {
                val (u, v, w) = time
                if (dist[u] != Int.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w
                }
            }
        }

        var maxTime = 0
        for (j in 1 until dist.size) {
            if (dist[j] == Int.MAX_VALUE) return -1
            if (maxTime < dist[j]) {
                maxTime = dist[j]
            }
        }

        return maxTime
    }

    fun networkDelayTimeFixedArray(times: Array<IntArray>, n: Int, k: Int): Int {
        val dist = IntArray(n + 1) { Int.MAX_VALUE }
        val graph = Array(n + 1) { _ -> ArrayList<IntArray>(n + 1) }
        val pq = PriorityQueue<IntArray> { a, b -> a[0] - b[0] }

        for (time in times) {
            val u = time[0]
            val v = time[1]
            val w = time[2]
            graph[u].add(intArrayOf(w, v))
        }

        dist[k] = 0
        pq.add(intArrayOf(dist[k], k))

        while (pq.isNotEmpty()) {
            val (currDist, currNode) = pq.poll()!!
            if (currDist > dist[currNode]) continue
            for (pair in graph[currNode]) {
                val (adjDist, adjNode) = pair
                val newDist = currDist + adjDist
                if (newDist < dist[adjNode]) {
                    dist[adjNode] = newDist
                    pq.add(intArrayOf(dist[adjNode], adjNode))
                }
            }
        }

        var maxDistance = Int.MIN_VALUE
        for (i in 1 until dist.size) {
            if (dist[i] > maxDistance) {
                maxDistance = dist[i]
            }
        }

        return if (maxDistance == Int.MAX_VALUE) -1 else maxDistance
    }

    fun networkDelayTimeBitPacking(times: Array<IntArray>, n: Int, k: Int): Int {
        val dist = IntArray(n + 1) { Int.MAX_VALUE }
        val graph = Array(n + 1) { _ -> ArrayList<Int>(n + 1) }
        val pq = PriorityQueue<Int> { a, b -> (a and 0xFFFF) - (b and 0xFFFF) }

        fun encode(v: Int, w: Int): Int {
            return (v shl 16) or w
        }

        fun decode(code: Int): IntArray {
            return intArrayOf(code ushr 16, code and 0XFFF)
        }

        for (time in times) {
            val u = time[0]
            val v = time[1]
            val w = time[2]
            graph[u].add(encode(v, w))
        }

        dist[k] = 0
        pq.add(encode(k, dist[k]))

        while (pq.isNotEmpty()) {
            val (currNode, currDist) = decode(pq.poll()!!)
            for (code in graph[currNode]) {
                val (adjNode, adjDist) = decode(code)
                val newDist = currDist + adjDist
                if (newDist < dist[adjNode]) {
                    dist[adjNode] = newDist
                    pq.add(encode(adjNode, dist[adjNode]))
                }
            }
        }

        var maxDistance = Int.MIN_VALUE
        for (i in 1 until dist.size) {
            if (dist[i] > maxDistance) {
                maxDistance = dist[i]
            }
        }

        return if (maxDistance == Int.MAX_VALUE) -1 else maxDistance

    }

    fun networkDelayTimePair(times: Array<IntArray>, n: Int, k: Int): Int {
        val dist = IntArray(n + 1) { Int.MAX_VALUE }
        val graph = Array(n + 1) { _ -> ArrayList<Pair<Int, Int>>(n + 1) }
        val pq = PriorityQueue<Pair<Int, Int>> { a, b -> a.first - b.first }

        for (time in times) {
            val u = time[0]
            val v = time[1]
            val w = time[2]
            graph[u].add(Pair(w, v))
        }

        dist[k] = 0
        pq.add(Pair(dist[k], k))

        while (pq.isNotEmpty()) {
            val (currDist, currNode) = pq.poll()!!
            if (currDist > dist[currNode]) continue
            for (pair in graph[currNode]) {
                val (adjDist, adjNode) = pair
                val newDist = currDist + adjDist
                if (newDist < dist[adjNode]) {
                    dist[adjNode] = newDist
                    pq.add(Pair(dist[adjNode], adjNode))
                }
            }
        }

        var maxDistance = Int.MIN_VALUE
        for (i in 1 until dist.size) {
            if (dist[i] > maxDistance) {
                maxDistance = dist[i]
            }
        }

        return if (maxDistance == Int.MAX_VALUE) -1 else maxDistance
    }


    // LC 207. Course Schedule
    fun canFinishDFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
        val graph = Array(numCourses) { _ -> ArrayList<Int>(numCourses) }
        val triStates = IntArray(numCourses)

        for (prerequisite in prerequisites) {
            val prev = prerequisite[1]
            val post = prerequisite[0]
            graph[prev].add(post)
        }

        fun hasCycle(course: Int): Boolean {
            if (triStates[course] == 1) return true
            if (triStates[course] == 2) return false

            triStates[course] = 1
            for (postCourse in graph[course]) {
                if (hasCycle(postCourse)) return true
            }
            triStates[course] = 2
            return false
        }

        for (i in triStates.indices) {
            if (triStates[i] == 0) {
                if (hasCycle(i)) return false
            }
        }

        return true
    }

    fun canFinishBFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
        var totalCourses = 0
        val level = IntArray(numCourses)
        val graph = Array(numCourses) { _ -> ArrayList<Int>(numCourses) }

        for (prerequisite in prerequisites) {
            val prev = prerequisite[1]
            val post = prerequisite[0]
            graph[prev].add(post)
            level[post]++
        }

        val dq = ArrayDeque<Int>(numCourses)

        for (i in level.indices) {
            if (level[i] == 0) {
                dq.addLast(i)
            }
        }

        while (dq.isNotEmpty()) {
            val course = dq.removeFirst()
            totalCourses++

            for (post in graph[course]) {
                if (--level[post] == 0) {
                    dq.addLast(post)
                }
            }
        }

        return numCourses == totalCourses
    }


    // LC 200. Number of Islands
    fun numIslandsDFS(grid: Array<CharArray>): Int {
        if (grid.isEmpty() || grid[0].isEmpty()) return 0
        var islands = 0

        val neighbors = arrayOf(intArrayOf(0, -1), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(1, 0))
        fun dfs(x: Int, y: Int) {
            if (x !in grid.indices || y !in grid[x].indices || grid[x][y] == '0') {
                return
            }
            grid[x][y] = '0'
            for (neighbor in neighbors) {
                val adjX = x + neighbor[0]
                val adjY = y + neighbor[1]
                dfs(adjX, adjY)
            }
        }

        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == '1') {
                    islands++
                    dfs(i, j)
                }
            }
        }

        return islands
    }

    fun numIslandsBFS(grid: Array<CharArray>): Int {
        if (grid.isEmpty() || grid[0].isEmpty()) return 0

        val neighbors = arrayOf(intArrayOf(0, -1), intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(1, 0))
        var islands = 0
        val m = grid.size
        val n = grid[0].size
        val capacity = min(m, n)
        val dq = ArrayDeque<Int>(capacity)

        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == '1') {
                    islands++
                    grid[i][j] = '0'
                    dq.addLast((i shl 16) or j)

                    while (dq.isNotEmpty()) {
                        val encode = dq.removeFirst()
                        val x = encode shr 16
                        val y = encode and 0xFFFF
                        for (neighbor in neighbors) {
                            val adjX = x + neighbor[0]
                            val adjY = y + neighbor[1]
                            if (adjX !in 0 until m || adjY !in 0 until n || grid[adjX][adjY] == '0') {
                                continue
                            }
                            grid[adjX][adjY] = '0'
                            dq.addLast((adjX shl 16) or adjY)
                        }
                    }
                }
            }
        }
        return islands
    }

}