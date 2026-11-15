package com.rshea.leetcodeprep.masteryreviewday

import java.util.PriorityQueue
import kotlin.math.abs

object MasterW14AdvGraph {

    // LC 210. Course Schedule II
    // Approach 1: Kahn's Algorithm (Topological Sort)
    // Zero-Allocation Two-Pointer Ring Queue (Staff Level Extreme)
    // Time Complexity: O(V + E) | Auxiliary Space Complexity: O(V + E)
    fun findOrder(numCourses: Int, prerequisites: Array<IntArray>): IntArray {
        val n = numCourses
        val inOrder = IntArray(n)
        val nextCourses = Array(n) { mutableListOf<Int>() }

        for (prep in prerequisites) {
            nextCourses[prep[1]].add(prep[0])
            inOrder[prep[0]]++
        }

        val schedule = IntArray(n)
        var head = 0
        var tail = 0

        for (i in 0 until n) {
            if (inOrder[i] == 0) schedule[tail++] = i
        }

        while (head < tail) {
            val course = schedule[head++]
            for (next in nextCourses[course]) {
                if (--inOrder[next] == 0) schedule[tail++] = next
            }
        }

        return if (head == n) schedule else intArrayOf()
    }

    // LC 1584. Min Cost to Connect All Points
    // Approach 1: Dense Prim's Algorithm (Staff Level Optimal - O(N²) Time, O(N) Space)
    // Time Complexity: O(N²) | Auxiliary Space Complexity: O(N)
    fun minCostConnectPoints(points: Array<IntArray>): Int {
        val n = points.size
        val minCost = IntArray(n) { Int.MAX_VALUE }
        minCost[0] = 0
        val connected = BooleanArray(n)
        var currNode: Int
        var currMinCost: Int
        var totalMinCost = 0

        for (i in 0 until n) {
            currNode = -1
            currMinCost = Int.MAX_VALUE
            for (j in 0 until n) {
                if (!connected[j] && minCost[j] < currMinCost) {
                    currMinCost = minCost[j]
                    currNode = j
                }
            }

            connected[currNode] = true
            totalMinCost += currMinCost
            val currNodeX = points[currNode][0]
            val currNodeY = points[currNode][1]

            for (nextNode in 0 until n) {
                if (!connected[nextNode]) {
                    val dist = abs(currNodeX - points[nextNode][0]) +
                            abs(currNodeY - points[nextNode][1])
                    if (dist < minCost[nextNode]) {
                        minCost[nextNode] = dist
                    }
                }
            }
        }

        return totalMinCost
    }

    // LC 684. Redundant Connection
    // Approach 1: Flat Primitive IntArray DSU (Staff Level Extreme - Zero Heap Object Allocations)
    // Time Complexity: O(N * α(N)) ≈ O(N) | Auxiliary Space Complexity: O(N)
    fun findRedundantConnectionDSU(edges: Array<IntArray>): IntArray {
        val n = edges.size
        val parents = IntArray(n + 1) { it }
        val rank = IntArray(n + 1)

        fun find(v: Int): Int {
            var root = v
            while (root != parents[root]) {
                parents[root] = parents[parents[root]]
                root = parents[root]
            }
            return root
        }

        fun union(x: Int, y: Int): Boolean {
            val rootX = find(x)
            val rootY = find(y)

            if (rootX == rootY) return false

            when {
                rank[rootX] > rank[rootY] -> parents[rootY] = rootX
                rank[rootX] < rank[rootY] -> parents[rootX] = rootY
                else -> {
                    parents[rootY] = rootX
                    rank[rootX]++
                }
            }
            return true
        }

        for (edge in edges) {
            if (!union(edge[0], edge[1])) return edge
        }

        return intArrayOf()
    }

    // Approach 2: Standard Disjoint Set (Union-Find)
    // Time Complexity: O(N * α(N)) | Auxiliary Space Complexity: O(N)
    fun findRedundantConnection(edges: Array<IntArray>): IntArray {
        val disjointSet = DisjointSet(edges.size)

        for (edge in edges) {
            if (!disjointSet.union(edge[0], edge[1])) return edge
        }

        return intArrayOf()
    }

    class DisjointSet(n: Int) {
        val parents = IntArray(n + 1) { it }
        val rank = IntArray(n + 1)

        fun find(v: Int): Int {
            var root = v
            while (root != parents[root]) {
                parents[root] = parents[parents[root]]
                root = parents[root]
            }
            return root
        }

        fun union(x: Int, y: Int): Boolean {
            val rootX = find(x)
            val rootY = find(y)

            if (rootX == rootY) return false

            when {
                rank[rootX] > rank[rootY] -> parents[rootY] = rootX
                rank[rootX] < rank[rootY] -> parents[rootX] = rootY
                else -> {
                    parents[rootY] = rootX
                    rank[rootX]++
                }
            }
            return true
        }

    }

    // LC 787. Cheapest Flights Within K Stops
    // Approach 1: Double-Buffered Bellman-Ford Algorithm (Zero Heap Allocations)
    // Time Complexity: O(K * E) | Auxiliary Space Complexity: O(N)
    fun findCheapestPrice(n: Int, flights: Array<IntArray>, src: Int, dst: Int, k: Int): Int {
        val cost = IntArray(n) { Int.MAX_VALUE }
        val newCost = IntArray(n) { Int.MAX_VALUE }
        cost[src] = 0
        newCost[src] = 0

        for (i in 0 .. k) {
            var anyUpdate = false

            for (f in flights) {
                val u = f[0]
                val v = f[1]
                val w = f[2]

                if (cost[u] != Int.MAX_VALUE && cost[u] + w < newCost[v]) {
                    newCost[v] = cost[u] + w
                    anyUpdate = true
                }
            }

            if (!anyUpdate) break

            for (j in 0 until n) {
                cost[j] = newCost[j]
            }
        }

        return if (cost[dst] == Int.MAX_VALUE) -1 else cost[dst]
    }

    // LC 743. Network Delay Time
    // Approach 1: Bit-Packed Long Dijkstra (Staff Level Extreme - Zero Heap Allocations)
    // Time Complexity: O((E) log V) | Auxiliary Space Complexity: O(V + E)
    fun networkDelayTimeBitPacked(times: Array<IntArray>, n: Int, k: Int): Int {
        // Distance MUST sit in the upper 32 bits so natural Long sorting orders by shortest distance!
        fun encode(dist: Int, node: Int): Long =
            (dist.toLong() shl 32) or (node.toLong() and 0xFFFFFFFFL)

        val adjacent = Array(n + 1) { mutableListOf<Long>() }
        val dist = IntArray(n + 1) { Int.MAX_VALUE }
        dist[k] = 0

        for (time in times) {
            adjacent[time[0]].add(encode(time[2], time[1]))
        }

        val pq = PriorityQueue<Long>(n)
        pq.add(encode(0, k))

        while (pq.isNotEmpty()) {
            val code = pq.poll()!!
            val w = (code ushr 32).toInt()
            val u = (code and (0xFFFFFFFFL)).toInt()
            if (w > dist[u]) continue

            for (edge in adjacent[u]) {
                val wv = (edge ushr 32).toInt()
                val v = (edge and 0xFFFFFFFFL).toInt()
                val newDist = w + wv
                if (newDist < dist[v]) {
                    dist[v] = newDist
                    pq.add(encode(newDist, v))
                }
            }
        }

        var minDelayTime = 0
        for (i in 1 .. n) {
            if (dist[i] == Int.MAX_VALUE) return -1
            minDelayTime = maxOf(minDelayTime, dist[i])
        }

        return minDelayTime
    }

    // Approach 2: Dijkstra (Min-Heap Pair Object)
    // Time Complexity: O((V + E) log V) | Auxiliary Space Complexity: O(V + E)
    fun networkDelayTime(times: Array<IntArray>, n: Int, k: Int): Int {
        val adjacent = Array(n + 1) { mutableListOf<Pair<Int, Int>>() }
        val dist = IntArray(n + 1) { Int.MAX_VALUE }
        dist[k] = 0

        for (time in times) {
            adjacent[time[0]].add(Pair(time[1], time[2]))
        }

        val pq = PriorityQueue<Pair<Int, Int>>(n,compareBy { it.second })
        pq.add(Pair(k, 0))

        while (pq.isNotEmpty()) {
            val (u, w) = pq.poll()!!
            if (w > dist[u]) continue
            for (adj in adjacent[u]) {
                val (v, wv) = adj
                if (w + wv < dist[v]) {
                    dist[v] = w + wv
                    pq.add(Pair(v, dist[v]))
                }
            }
        }

        var minDelayTime = 0
        for (i in 1 .. n) {
            if (dist[i] == Int.MAX_VALUE) return -1
            minDelayTime = maxOf(minDelayTime, dist[i])
        }

        return minDelayTime
    }

}