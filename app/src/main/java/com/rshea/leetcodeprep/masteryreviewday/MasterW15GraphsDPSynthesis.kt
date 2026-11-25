package com.rshea.leetcodeprep.masteryreviewday

import java.util.PriorityQueue

object MasterW15GraphsDPSynthesis {

    // LC 847. Shortest Path Visiting All Nodes
    // Approach 1: Bit-Packed Primitive Int BFS (Staff Level Extreme - 1D Array allocation)
    // Time Complexity: O(N² x 2ᴺ) | Auxiliary Space Complexity: O(N x 2ᴺ)
    fun shortestPathLengthBitPacking(graph: Array<IntArray>): Int {
        val n = graph.size
        if (n == 1) return 0
        val visited = BooleanArray(n shl 12)
        val targetMask = (1 shl n) - 1

        var head = 0
        var tail = 0
        val q = IntArray(n * (1 shl n))
        println("vistited = ${visited.size} q = ${q.size}")
        for (i in 0 until n) {
            val state = (i shl 12) or (1 shl i)
            println("(${state.toString(2)})")
            visited[state] = true
            q[tail++] = state
        }

        var steps = 0
        while (head < tail) {
            val wave = tail - head
            for (k in 0 until wave) {
                val currState = q[head++]
                val currNode = currState ushr 12
                val currMask = currState and 0xFFF // <-- Remember it has to be `and` operation
                print("($currNode, ${currMask.toString(2)}) ->")
                for (nextNode in graph[currNode]) {
                    val nextMask = currMask or (1 shl nextNode)
                    if (targetMask == nextMask) return steps + 1
                    val nextState = (nextNode shl 12) or nextMask // <-- nextMask does not need to 'and'
                    print(" ($nextNode, ${nextMask.toString(2)}) = ${nextState.toString(2)}, ")
                    if (!visited[nextState]) {
                        visited[nextState] = true
                        q[tail++] = nextState
                    }
                }
                println()
            }
            steps++
        }

        return steps
    }

    // Approach 2: Standard BFS (Staff Level Optimal - 2D Array Allocations)
    // Time Complexity: O(N² x 2ᴺ) | Auxiliary Space Complexity: O(N x 2ᴺ)
    fun shortestPathLength(graph: Array<IntArray>): Int {
        val m = graph.size
        val visited = Array(m) { BooleanArray(1 shl m) }
        val q = ArrayDeque<Triple<Int, Int, Int>>()
        val targetMask = (1 shl m) - 1
        for (i in 0 until m) {
            val initialMask = 1 shl i
            visited[i][initialMask] = true
            q.addLast(Triple(i, initialMask, 0))
        }

        while (q.isNotEmpty()) {
            val (u, bitMask, steps) = q.removeFirst()

            for (v in graph[u]) {
                val nexMask = bitMask or (1 shl v)
                if (targetMask == nexMask) return steps + 1
                if (!visited[v][nexMask]) {
                    visited[v][nexMask] = true
                    q.addLast(Triple(v, nexMask, steps + 1))
                }
            }
        }
        return 0
    }


    // LC 337. House Robber III
    class TreeNode(var `val`: Int) {
        var left: TreeNode? = null
        var right: TreeNode? = null
    }

    // Approach 1: Bit-Packed Primitive Long DFS (Zero Heap Allocations)
    // Time Complexity: O(N) | Auxiliary Space Complexity: O(H)
    fun robBitPacking(root: TreeNode?): Int {

        fun dfs(node: TreeNode?): Long {
            if (node == null) return 0L

            val left = dfs(node.left)
            val right = dfs(node.right)

            val robLeft = (left ushr 32).toInt()
            val unrobLeft = (left and 0xFFFFFFFFL).toInt()

            val robRight = (right ushr 32).toInt()
            val unrobRight = (right and 0xFFFFFFFFL).toInt()

            val robSum = node.`val` + unrobLeft + unrobRight
            val unrobSum = maxOf(robLeft, unrobLeft) + maxOf(robRight, unrobRight)

            return (robSum.toLong() shl 32) or (unrobSum.toLong() and 0xFFFFFFFFL)
        }

        val code = dfs(root)
        return maxOf((code ushr 32).toInt(), (code and 0xFFFFFFFFL).toInt())
    }

    // Approach 2: Top-Down DFS (Peak Canonical - IntArray(2))
    // Time Complexity: O(N) | Auxiliary Space Complexity: O(H)
    fun robDFS(root: TreeNode?): Int {

        fun dfs(node: TreeNode?): IntArray {
            if (node == null) return intArrayOf(0, 0)

            val left = dfs(node.left)
            val right = dfs(node.right)

            return intArrayOf(node.`val` + left[1] + right[1],
                maxOf(left[0], left[1]) + maxOf(right[0], right[1]))
        }

        val (rob, unrob) = dfs(root)
        return maxOf(rob, unrob)
    }


    // LC 542. 01 Matrix
    // Approach 1: Two-Pass 2D Dynamic Programming (Staff Level Optimal - Zero Heap Allocations)
    // Time Complexity: O(M * N) | Auxiliary Space Complexity: O(1)
    fun updateMatrix(mat: Array<IntArray>): Array<IntArray> {
        val m = mat.size
        val n = mat[0].size

        for (r in 0 until m) {
            for (c in 0 until n) {
                if (mat[r][c] == 1) {
                    mat[r][c] = 10_000
                }
            }
        }

        for (r in 0 until m) {
            for (c in 0 until n) {
                if (mat[r][c] == 0) continue
                if (r > 0) mat[r][c] = minOf(mat[r][c], 1 + mat[r - 1][c])
                if (c > 0) mat[r][c] = minOf(mat[r][c], 1 + mat[r][c - 1])
            }
        }

        for (r in m - 1 downTo 0) {
            for (c in n - 1 downTo 0) {
                if (mat[r][c] == 0) continue
                if (r < m - 1) mat[r][c] = minOf(mat[r][c], 1 + mat[r + 1][c])
                if (c < n - 1) mat[r][c] = minOf(mat[r][c], 1 + mat[r][c + 1])
            }
        }

        return mat
    }

    // Approach 2: Multi-Source BFS (Bit-Packed Primitive Int Queue - Zero Heap Allocations)
    // Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
    fun updateMatrixBFS(mat: Array<IntArray>): Array<IntArray> {
        val m = mat.size
        val n = mat[0].size
        val q = ArrayDeque<Int>(m * n)

        for (r in 0 until m) {
            for (c in 0 until n) {
                if (mat[r][c] == 0) {
                    q.addLast((r shl 16) or (c and 0xFFFF))
                } else {
                    mat[r][c] = -1
                }
            }
        }

        val adj = intArrayOf(0, 1, 0, -1, 0)
        while (q.isNotEmpty()) {
            val axis = q.removeFirst()
            val r = axis ushr 16
            val c = axis and 0xFFFF
            for (k in 0 until adj.size - 1) {
                val nr = r + adj[k]
                val nc = c + adj[k + 1]
                if (nr in 0 until m && nc in 0 until n && mat[nr][nc] == -1) {
                    mat[nr][nc] = mat[r][c] + 1
                    q.addLast((nr shl 16) or (nc and 0xFFFF))
                }
            }
        }

        return mat
    }


    // LC 1976. Number of Ways to Arrive at Destination
    // Approach 1: Bit-Packed Primitive Long Dijkstra + DP (Zero Heap Allocations)
    // Time Complexity: O((V + E) log V) | Auxiliary Space Complexity: O(V + E)
    fun countPaths(n: Int, roads: Array<IntArray>): Int {
        val currMinTime = LongArray(n) { Long.MAX_VALUE }
        val graph = Array(n) { mutableListOf<Long>() }
        val ways = IntArray(n)
        currMinTime[0] = 0L
        ways[0] = 1

        val encode: (Long, Int) -> Long = { t, v -> (t shl 16) or (v.toLong() and 0xFFFFL) }

        for (road in roads) {
            val u = road[0]
            val v = road[1]
            val t = road[2].toLong()
            graph[u].add(encode(t, v))
            graph[v].add(encode(t, u))
        }

        val pq = PriorityQueue<Long>()
        pq.add(encode(0L, 0))

        while (pq.isNotEmpty()) {
            val currNode = pq.poll()!!
            val w = currNode ushr 16
            val u = (currNode and 0xFFFFL).toInt()
            if (w > currMinTime[u]) continue

            for (nextNode in graph[u]) {
                val d = nextNode ushr 16
                val v = (nextNode and 0xFFFFL).toInt()

                val newDist = w + d

                if (newDist < currMinTime[v]) {
                    currMinTime[v] = newDist
                    ways[v] = ways[u]
                    pq.add(encode(currMinTime[v], v))
                } else if (newDist == currMinTime[v]) {
                    ways[v] = (ways[v] + ways[u]) % 1_000_000_007
                }
            }
        }

        return ways[n - 1]
    }


    // LC 329. Longest Increasing Path in a Matrix
    // Approach 1: Top-Down Memoized DFS (Static Direction Vector)
    // Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
    fun longestIncreasingPathDFS(matrix: Array<IntArray>): Int {
        val m = matrix.size
        val n = matrix[0].size
        val dp = IntArray(m * n)
        val adj = intArrayOf(0, 1, 0, -1, 0)

        fun dfs(r: Int, c: Int): Int {
            val index = r * n + c
            if (dp[index] != 0) return dp[index]

            var maxLen = 1
            for (k in 0 until adj.size - 1) {
                val nr = r + adj[k]
                val nc = c + adj[k + 1]
                if (nr in 0 until m && nc in 0 until n && matrix[nr][nc] > matrix[r][c]) {
                    maxLen = maxOf(maxLen, 1 + dfs(nr, nc))
                }
            }
            dp[index] = maxLen
            return dp[index]
        }

        var globalMaxLen = 0
        for (i in 0 until m) {
            for (j in 0 until n) {
                globalMaxLen = maxOf(globalMaxLen, dfs(i, j))
            }
        }

        return globalMaxLen
    }

    // Approach 2: BFS (Topological Sort)
    // Time Complexity: O(M * N) | Auxiliary Space Complexity: O(M * N)
    fun longestIncreasingPathBFS(matrix: Array<IntArray>): Int {
        val m = matrix.size
        val n = matrix[0].size
        val outOrder = Array(m) { IntArray(n) }
        val adj = intArrayOf(0, 1, 0, -1, 0)

        for (r in 0 until m) {
            for (c in 0 until n) {
                for (k in 0 until adj.size - 1) {
                    val nr = r + adj[k]
                    val nc = c + adj[k + 1]
                    if (nr in 0 until m && nc in 0 until n && matrix[nr][nc] < matrix[r][c]) {
                        outOrder[r][c]++
                    }
                }
            }
        }

        val q = ArrayDeque<IntArray>()
        for (r in 0 until m) {
            for (c in 0 until n) {
                if (outOrder[r][c] == 0) {
                    q.addLast(intArrayOf(r, c))
                }
            }
        }

        var maxPath = 0
        while (q.isNotEmpty()) {
            maxPath++
            val size = q.size
            for (i in 0 until size) {
                val cell = q.removeFirst()
                val r = cell[0]
                val c = cell[1]
                for (j in 0 until adj.size - 1) {
                    val nr = r + adj[j]
                    val nc = c + adj[j + 1]
                    if (nr in 0 until m && nc in 0 until n && matrix[nr][nc] > matrix[r][c]) {
                        if (--outOrder[nr][nc] == 0) q.addLast(intArrayOf(nr, nc))
                    }
                }
            }
        }

        return maxPath
    }

}