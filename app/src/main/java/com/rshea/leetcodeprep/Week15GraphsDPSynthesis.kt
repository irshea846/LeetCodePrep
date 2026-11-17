package com.rshea.leetcodeprep

object Week15GraphsDPSynthesis {

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