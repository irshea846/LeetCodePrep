package com.rshea.leetcodeprep

import java.util.PriorityQueue

object Week15GraphsDPSynthesis {

    // Day 76 - LC 542. 01 Matrix
    // Approach 1: Two-Pass 2D Dynamic Programming (Staff Level Optimal - Clean Boundary Guards)
    // Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
    fun updateMatrix(mat: Array<IntArray>): Array<IntArray> {
        val m = mat.size
        val n = mat[0].size
        val INF = 10_000
        val dist = Array(m) { r -> IntArray(n) { c -> if (mat[r][c] == 0) 0 else INF } }

        // Pass 1: Top-Left to Bottom-Right (Check Top and Left neighbors)
        for (r in 0 until m) {
            for (c in 0 until n) {
                if (mat[r][c] == 0) continue
                if (r > 0) dist[r][c] = minOf(dist[r][c], dist[r - 1][c] + 1)
                if (c > 0) dist[r][c] = minOf(dist[r][c], dist[r][c - 1] + 1)
            }
        }

        // Pass 2: Bottom-Right to Top-Left (Check Bottom and Right neighbors)
        for (r in m - 1 downTo 0) {
            for (c in n - 1 downTo 0) {
                if (mat[r][c] == 0) continue
                if (r < m - 1) dist[r][c] = minOf(dist[r][c], dist[r + 1][c] + 1)
                if (c < n - 1) dist[r][c] = minOf(dist[r][c], dist[r][c + 1] + 1)
            }
        }

        return dist
    }

    // Approach 2: Multi-Source BFS (Bit-Packed Primitive Int Queue - Zero Heap Allocations)
    // Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
    fun updateMatrixMultiSourceBFS(mat: Array<IntArray>): Array<IntArray> {
        val m = mat.size
        val n = mat[0].size
        val dist = Array(m) { IntArray(n) { -1 } }
        val queue = ArrayDeque<Int>() // Bit-packed coordinate (r shl 16) or c

        // Enqueue all 0 cells simultaneously
        for (r in 0 until m) {
            for (c in 0 until n) {
                if (mat[r][c] == 0) {
                    dist[r][c] = 0
                    queue.addLast((r shl 16) or c)
                }
            }
        }

        val dirs = intArrayOf(0, 1, 0, -1, 0)
        while (queue.isNotEmpty()) {
            val code = queue.removeFirst()
            val r = code ushr 16
            val c = code and 0xFFFF

            for (k in 0 until 4) {
                val nr = r + dirs[k]
                val nc = c + dirs[k + 1]
                if (nr in 0 until m && nc in 0 until n && dist[nr][nc] == -1) {
                    dist[nr][nc] = dist[r][c] + 1
                    queue.addLast((nr shl 16) or nc)
                }
            }
        }

        return dist
    }


    // Day 75 - LC 1976. Number of Ways to Arrive at Destination
    // Approach 1: Bit-Packed Primitive Long Dijkstra + DP (Staff Level Extreme - Zero Heap Allocations)
    // Time Complexity: O((V + E) log V) | Auxiliary Space Complexity: O(V + E)
    // Bit Packing Capacity: 16 bits for node v (N <= 200 < 65,536), 48 bits for cumulative distance w (up to 2.8 * 10^14)
    fun countPathsBitPacking(n: Int, roads: Array<IntArray>): Int {
        val MOD = 1_000_000_007
        val dist = LongArray(n) { Long.MAX_VALUE }
        val ways = IntArray(n)
        dist[0] = 0L
        ways[0] = 1

        val graph = Array(n) { mutableListOf<Long>() }

        fun encode(w: Long, v: Int): Long {
            return (w shl 16) or (v.toLong() and 0xFFFFL)
        }

        for (road in roads) {
            val u = road[0]
            val v = road[1]
            val w = road[2].toLong()
            graph[u].add(encode(w, v))
            graph[v].add(encode(w, u))
        }

        val pq = PriorityQueue<Long>() // Natural ascending Long order (Zero Comparator Heap)
        pq.add(encode(0L, 0))

        while (pq.isNotEmpty()) {
            val code = pq.poll()!!
            val d = code ushr 16
            val u = (code and 0xFFFFL).toInt()

            if (d > dist[u]) continue

            for (edge in graph[u]) {
                val w = edge ushr 16
                val v = (edge and 0xFFFFL).toInt()
                val newTime = d + w

                if (newTime < dist[v]) {
                    dist[v] = newTime
                    ways[v] = ways[u]
                    pq.add(encode(newTime, v))
                } else if (newTime == dist[v]) {
                    ways[v] = (ways[v] + ways[u]) % MOD
                }
            }
        }

        return ways[n - 1]
    }

    // Approach 2: Dijkstra's Algorithm + Heap (Priority Queue) + DP
    // Time Complexity: O(E x Log(V)) | Auxiliary Space Complexity: O(V + E)
    fun countPaths(n: Int, roads: Array<IntArray>): Int {
        val MOD = 1_000_000_007L
        val currMinTime = LongArray(n) { Long.MAX_VALUE }
        val ways = LongArray(n)
        currMinTime[0] = 0L
        ways[0] = 1L
        val adjacent = Array(n) { mutableListOf<LongArray>() }

        for (road in roads) {
            adjacent[road[0]].add(longArrayOf(road[2].toLong(), road[1].toLong()))
            adjacent[road[1]].add(longArrayOf(road[2].toLong(), road[0].toLong()))
        }

        val pq =
            PriorityQueue<LongArray>(if (roads.isEmpty()) 1 else roads.size, compareBy { it[0] })
        pq.add(longArrayOf(0L, 0))

        while (pq.isNotEmpty()) {
            val src = pq.poll()!!
            val d = src[0]
            val u = src[1].toInt()
            if (d > currMinTime[u]) continue
            for (adj in adjacent[u]) {
                val w = adj[0]
                val v = adj[1].toInt()
                val time = d + w
                if (time < currMinTime[v]) {
                    currMinTime[v] = time
                    ways[v] = ways[u]
                    pq.add(longArrayOf(time, v.toLong()))
                } else if (time == currMinTime[v]){
                    ways[v] = (ways[v] + ways[u]) % MOD
                }
            }
        }

        return ways[n - 1].toInt()
    }


    // Day 74 - LC 329. Longest Increasing Path in a Matrix
    // Approach 1: Top-Down Memoized DFS (Staff Level Optimal - Static Direction Vector)
    // Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
    fun longestIncreasingPath(matrix: Array<IntArray>): Int {
        if (matrix.isEmpty() || matrix[0].isEmpty()) return 0
        val m = matrix.size
        val n = matrix[0].size
        val dp = Array(m) { IntArray(n) }
        val dirs = intArrayOf(0, 1, 0, -1, 0) // Static 1D direction vector (Zero Array Object Allocation)

        fun dfs(r: Int, c: Int): Int {
            if (dp[r][c] != 0) return dp[r][c]

            var maxLen = 1
            val currVal = matrix[r][c]

            for (k in 0 until 4) {
                val nr = r + dirs[k]
                val nc = c + dirs[k + 1]
                if (nr in 0 until m && nc in 0 until n && matrix[nr][nc] > currVal) {
                    maxLen = maxOf(maxLen, 1 + dfs(nr, nc))
                }
            }

            dp[r][c] = maxLen
            return maxLen
        }

        var globalMaxLen = 0
        for (r in 0 until m) {
            for (c in 0 until n) {
                globalMaxLen = maxOf(globalMaxLen, dfs(r, c))
            }
        }

        return globalMaxLen
    }

    // Approach 2: Out-Degree Topological Sort BFS (Kahn's Algorithm - StackOverflow Safe)
    // Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
    fun longestIncreasingPathTopologicalBFS(matrix: Array<IntArray>): Int {
        if (matrix.isEmpty() || matrix[0].isEmpty()) return 0
        val m = matrix.size
        val n = matrix[0].size
        val outDegree = Array(m) { IntArray(n) }
        val dirs = intArrayOf(0, 1, 0, -1, 0)

        // Step 1: Compute out-degrees for every cell (count neighbors with strictly GREATER value)
        for (r in 0 until m) {
            for (c in 0 until n) {
                for (k in 0 until 4) {
                    val nr = r + dirs[k]
                    val nc = c + dirs[k + 1]
                    if (nr in 0 until m && nc in 0 until n && matrix[nr][nc] > matrix[r][c]) {
                        outDegree[r][c]++
                    }
                }
            }
        }

        // Step 2: Enqueue all sink cells (outDegree == 0) into BFS queue
        val queue = ArrayDeque<Int>() // Bit-packed coordinate (r shl 16) or c
        for (r in 0 until m) {
            for (c in 0 until n) {
                if (outDegree[r][c] == 0) {
                    queue.addLast((r shl 16) or c)
                }
            }
        }

        // Step 3: Level-order BFS peeling
        var pathLength = 0
        while (queue.isNotEmpty()) {
            pathLength++
            val size = queue.size
            for (i in 0 until size) {
                val code = queue.removeFirst()
                val r = code ushr 16
                val c = code and 0xFFFF

                for (k in 0 until 4) {
                    val nr = r + dirs[k]
                    val nc = c + dirs[k + 1]
                    // Decrement out-degree of smaller neighbors (matrix[nr][nc] < matrix[r][c])
                    if (nr in 0 until m && nc in 0 until n && matrix[nr][nc] < matrix[r][c]) {
                        if (--outDegree[nr][nc] == 0) {
                            queue.addLast((nr shl 16) or nc)
                        }
                    }
                }
            }
        }

        return pathLength
    }

}