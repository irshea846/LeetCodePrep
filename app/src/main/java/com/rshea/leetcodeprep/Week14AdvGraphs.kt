package com.rshea.leetcodeprep

import java.util.PriorityQueue
import kotlin.math.abs

object Week14AdvGraphs {

    // Day 71 - LC 1584. Min Cost to Connect All Points
    // Approach 1: Dense Prim's Algorithm (Staff Level Optimal - O(N²) Time, O(N) Space)
    // Time Complexity: O(N²) | Auxiliary Space Complexity: O(N)
    fun minCostConnectPoints(points: Array<IntArray>): Int {
        val n = points.size
        val minDist = IntArray(n) { Int.MAX_VALUE }
        minDist[0] = 0
        val connected = BooleanArray(n)
        var totalCost = 0

        for (step in 0 until n) {
            var currNode = -1
            var currMinDist = Int.MAX_VALUE

            for (node in 0 until n) {
                if (!connected[node] && minDist[node] < currMinDist) {
                    currMinDist = minDist[node]
                    currNode = node
                }
            }

            connected[currNode] = true
            totalCost += currMinDist

            val currX = points[currNode][0]
            val currY = points[currNode][1]

            for (nextNode in 0 until n) {
                if (!connected[nextNode]) {
                    val dist = abs(currX - points[nextNode][0]) + abs(currY - points[nextNode][1])
                    if (dist < minDist[nextNode]) {
                        minDist[nextNode] = dist
                    }
                }
            }
        }

        return totalCost
    }

    // Approach 2: Kruskal's Algorithm + DSU (O(N² log N) Time, O(N²) Space)
    // Time Complexity: O(N² log N) | Auxiliary Space Complexity: O(N²)
    fun minCostConnectPointsKruskal(points: Array<IntArray>): Int {
        val n = points.size
        val edges = mutableListOf<IntArray>() // [cost, u, v]

        for (i in 0 until n) {
            for (j in i + 1 until n) {
                val dist = abs(points[i][0] - points[j][0]) + abs(points[i][1] - points[j][1])
                edges.add(intArrayOf(dist, i, j))
            }
        }

        edges.sortBy { it[0] }

        val parent = IntArray(n) { it }
        fun find(i: Int): Int {
            var root = i
            while (root != parent[root]) {
                parent[root] = parent[parent[root]]
                root = parent[root]
            }
            return root
        }

        var mstCost = 0
        var edgesCount = 0

        for (edge in edges) {
            val rootU = find(edge[1])
            val rootV = find(edge[2])

            if (rootU != rootV) {
                parent[rootU] = rootV
                mstCost += edge[0]
                edgesCount++
                if (edgesCount == n - 1) break
            }
        }

        return mstCost
    }


    // Day 70 - LC 684. Redundant Connection
    // Approach 1: Flat Primitive IntArray DSU (Staff Level Extreme - Zero Heap Object Allocations)
    // Time Complexity: O(N * α(N)) ≈ O(N) | Auxiliary Space Complexity: O(N)
    fun findRedundantConnectionArrayDSU(edges: Array<IntArray>): IntArray {
        val n = edges.size
        val parent = IntArray(n + 1) { it }
        val rank = IntArray(n + 1)

        fun find(i: Int): Int {
            var root = i
            while (root != parent[root]) {
                parent[root] = parent[parent[root]] // Path halving path compression
                root = parent[root]
            }
            return root
        }

        fun union(u: Int, v: Int): Boolean {
            val rootU = find(u)
            val rootV = find(v)
            if (rootU == rootV) return false // Cycle detected!

            if (rank[rootU] < rank[rootV]) {
                parent[rootU] = rootV
            } else if (rank[rootU] > rank[rootV]) {
                parent[rootV] = rootU
            } else {
                parent[rootV] = rootU
                rank[rootU]++
            }
            return true
        }

        for (edge in edges) {
            if (!union(edge[0], edge[1])) return edge
        }
        return intArrayOf()
    }

    // Approach 2: HashMap Disjoint Set (Union-Find)
    // Time Complexity: O(N * α(N)) | Auxiliary Space Complexity: O(N)
    fun findRedundantConnection(edges: Array<IntArray>): IntArray {
        val disjointSet = DisjointSet()
        for (edge in edges) {
            if (!disjointSet.union(edge[0], edge[1])) {
                return edge
            }
        }
        return intArrayOf()
    }

    class DisjointSet() {
        val parents = HashMap<Int, Int>()
        val ranks = HashMap<Int, Int>()

        fun find(x: Int): Int {
            parents[x] = parents.getOrDefault(x, x)
            if (x != parents[x]!!) {
                return find(parents[x]!!)
            }
            return parents[x]!!
        }

        fun union(x: Int, y: Int): Boolean {
            val rootX = find(x)
            val rootY = find(y)

            if (rootX == rootY) {
                return false
            }

            ranks[rootX] = ranks.getOrDefault(rootX, 0)
            ranks[rootY] = ranks.getOrDefault(rootY, 0)
            when {
                ranks[rootX]!! < ranks[rootY]!! -> parents[rootX] = rootY
                ranks[rootX]!! > ranks[rootY]!! -> parents[rootY] = rootX
                else -> {
                    parents[rootY] = rootX
                    ranks[rootX] = ranks[rootX]!! + 1
                }
            }

            return true
        }
    }

    // Day 69 - LC 787. Cheapest Flights Within K Stops
    // Approach 1: Level-Order BFS Queue (Staff Level Extreme - Sparse Active Node Processing)
    // Time Complexity: O(V + E * K) | Auxiliary Space Complexity: O(V + E)
    fun findCheapestPriceBFS(n: Int, flights: Array<IntArray>, src: Int, dst: Int, k: Int): Int {
        val adj = Array(n) { mutableListOf<Pair<Int, Int>>() }
        for (f in flights) {
            adj[f[0]].add(Pair(f[1], f[2]))
        }

        val dist = IntArray(n) { Int.MAX_VALUE }
        dist[src] = 0

        val queue = ArrayDeque<Pair<Int, Int>>() // Pair(node, current_cost)
        queue.addLast(Pair(src, 0))

        var stops = 0
        while (queue.isNotEmpty() && stops <= k) {
            val size = queue.size
            for (i in 0 until size) {
                val (currNode, currCost) = queue.removeFirst()

                for (edge in adj[currNode]) {
                    val nextNode = edge.first
                    val weight = edge.second
                    val newCost = currCost + weight

                    if (newCost < dist[nextNode]) {
                        dist[nextNode] = newCost
                        queue.addLast(Pair(nextNode, newCost))
                    }
                }
            }
            stops++
        }

        return if (dist[dst] == Int.MAX_VALUE) -1 else dist[dst]
    }

    // Approach 2: Double-Buffered Bellman-Ford Algorithm (Zero Heap Allocations)
    // Time Complexity: O(K * E) | Auxiliary Space Complexity: O(N)
    fun findCheapestPriceBellmanFord(n: Int, flights: Array<IntArray>, src: Int, dst: Int, k: Int): Int {
        var cost = IntArray(n) { Int.MAX_VALUE }
        var nextCost = IntArray(n) { Int.MAX_VALUE }
        cost[src] = 0
        nextCost[src] = 0

        for (i in 0..k) {
            cost.copyInto(nextCost) // Zero array allocation buffer copy
            var anyUpdate = false

            for (flight in flights) {
                val u = flight[0]
                val v = flight[1]
                val w = flight[2]

                if (cost[u] != Int.MAX_VALUE && cost[u] + w < nextCost[v]) {
                    nextCost[v] = cost[u] + w
                    anyUpdate = true
                }
            }

            if (!anyUpdate) break
            val temp = cost
            cost = nextCost
            nextCost = temp
        }

        return if (cost[dst] == Int.MAX_VALUE) -1 else cost[dst]
    }

    // Approach 3: Single-Buffer Bellman-Ford Algorithm (Multiple Heap Allocation)
    // Time Complexity: O(K * E) | Auxiliary Space Complexity: O(N)
    fun findCheapestPrice(n: Int, flights: Array<IntArray>, src: Int, dst: Int, k: Int): Int {
        var cost = IntArray(n) { Int.MAX_VALUE }
        cost[src] = 0

        for (i in 0 .. k) {
            val nextCost = cost.clone()
            var anyUpdate = false

            for (flight in flights) {
                val v = flight[0]
                val u = flight[1]
                val w = flight[2]

                if (cost[v] != Int.MAX_VALUE) {
                    if (cost[v] + w < nextCost[u]) {
                        nextCost[u] = cost[v] + w
                        anyUpdate = true
                    }
                }
            }
            if (!anyUpdate) break
            cost = nextCost
        }

        return if (cost[dst] == Int.MAX_VALUE) -1 else cost[dst]
    }
    // Code Analysis : findCheapestPriceDP(n: Int, flights: Array<IntArray>, src: Int, dst: Int,
    // k: Int): Int
    //
    //    Staff-Level Review Findings for Bellman-Ford:
    //
    //    1. The Ripple Effect Guard (cost.clone()):
    //      ◦ In standard Bellman-Ford (unbounded steps), we update cost in-place.
    //      ◦ For bounded K stops, updating cost in-place in a single pass could allow a chain
    //      A → B → C → D to relax 3 edges in 1 pass, violating the step limit constraint!
    //      ◦ Cloning val nextCost = cost.clone() and evaluating cost[v] + w < nextCost[u] guarantees
    //      that pass 𝒊 ONLY uses paths of length ≤ 𝒊.
    //      ◦ Performance Improvement (Double Buffer Swapping):
    //          ▪ cost.clone() allocates a new IntArray on every outer loop iteration
    //          (K + 1 array allocations)!
    //          ▪ Staff Optimization: Allocate two arrays upfront:
    //                  var cost = IntArray(n); var nextCost = IntArray(n).
    //          ▪ In each pass, copy cost.copyInto(nextCost), and after the pass, swap array
    //          references (val temp = cost; cost = nextCost; nextCost = temp)!
    //          ▪ This achieves ZERO heap array allocations during execution!
    //
    //    2. Is there a better top-tier performance solution for LC 787?
    //
    //    YES! Level-Order BFS / Modified Dijkstra with Stops Constraint O(E + V x Log V) / O(E x K)!
    //
    //      • Why Standard Dijkstra Fails for LC 787:
    //          ◦ Standard Dijkstra chooses nodes purely by minimum cost. A lower-cost path with
    //          K + 2 stops might be polled first and mark a node as visited, blocking a slightly
    //          higher-cost path with ≤ K stops from reaching dst!
    //      • Modified Dijkstra with State (cost, node, stops):
    //          ◦ Maintain a min-heap prioritized by cost.
    //          ◦ Store minStops[node] array to prune states that reach node with ≥ stops than
    //          previously seen.
    //          ◦ Or run BFS Level-Order Queue Traversal (K + 1 levels max):
    //              ▪ Level 𝒊 processes nodes reachable in 𝒊 edges.
    //              ▪ BFS processes level-by-level without Min-Heap O(Log V) polling overhead!
    //              ▪ BFS Level-Order is strictly faster than Bellman-Ford because it ONLY relaxes
    //              outgoing edges from active nodes at the current level, rather than scanning all
    //              E flights on every pass!


    // Day 68 - LC 743. Network Delay Time
    // Approach 1: Bit-Packed Long Dijkstra (Staff Level - Zero Heap Object Allocations)
    // Time Complexity: O((V + E) log V) | Auxiliary Space Complexity: O(V + E)
    fun networkDelayTimeBitPacking(times: Array<IntArray>, n: Int, k: Int): Int {
        val dist = IntArray(n + 1) { Int.MAX_VALUE }
        val graph = Array(n + 1) { mutableListOf<Long>() }

        fun encode(d: Int, v: Int): Long {
            return (d.toLong() shl 32) or (v.toLong() and 0xFFFFFFFFL)
        }

        for (time in times) {
            val u = time[0]
            val v = time[1]
            val w = time[2]
            graph[u].add(encode(w, v))
        }

        dist[k] = 0
        val pq = PriorityQueue<Long>() // Natural ascending Long order (Zero Comparator Heap)
        pq.add(encode(0, k))

        while (pq.isNotEmpty()) {
            val code = pq.poll()!!
            val currDist = (code ushr 32).toInt()
            val currNode = (code and 0xFFFFFFFFL).toInt()

            if (currDist > dist[currNode]) continue // Lazy deletion pruning

            for (edge in graph[currNode]) {
                val adjWeight = (edge ushr 32).toInt()
                val adjNode = (edge and 0xFFFFFFFFL).toInt()
                val newDist = currDist + adjWeight
                if (newDist < dist[adjNode]) {
                    dist[adjNode] = newDist
                    pq.add(encode(newDist, adjNode))
                }
            }
        }

        var maxDistance = Int.MIN_VALUE
        for (i in 1..n) {
            maxDistance = maxOf(maxDistance, dist[i])
        }

        return if (maxDistance == Int.MAX_VALUE) -1 else maxDistance
    }

    // Approach 2: Dijkstra (Min-Heap Pair Object)
    // Time Complexity: O(E log V) | Auxiliary Space Complexity: O(V + E)
    fun networkDelayTimeDijkstra(times: Array<IntArray>, n: Int, k: Int): Int {
        val adj = Array(n + 1) { mutableListOf<Pair<Int, Int>>() }
        val dist = IntArray(n + 1) { Int.MAX_VALUE }
        dist[k] = 0

        for (time in times) {
            val v = time[0]
            val u = time[1]
            val w = time[2]
            adj[v].add(Pair(w, u))
        }

        val pq = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })
        pq.add(Pair(0, k))

        while (pq.isNotEmpty()) {
            val (d, v) = pq.poll()!!
            if (d > dist[v]) continue
            for (adjacent in adj[v]) {
                val (w, u) = adjacent
                if (w + d < dist[u]) {
                    dist[u] = w + d
                    pq.add(Pair(dist[u], u))
                }
            }
        }

        var minDist = 0
        for (i in 1 .. n) {
            if (dist[i] == Int.MAX_VALUE) return -1
            minDist = maxOf(minDist, dist[i])
        }

        return minDist
    }
    // Code Analysis 1: networkDelayTimeDijkstra(times: Array<IntArray>, n: Int, k: Int): Int
    //    Staff-Level Review & Performance Pitfalls:
    //
    //    1. Lazy Deletion Pruning (if (d > dist[v]) continue):
    //      ◦ Correctly handles lazy deletion of stale heap nodes!
    //      ◦ Because PriorityQueue on the JVM cannot perform O(Log V) decreaseKey operations
    //      efficiently, multiple pairs for the same node $v$ may exist in the min-heap.
    //      ◦ if (d > dist[v]) continue skips stale polled entries in O(1) time.
    //
    //    2. Heap Object Allocation Churn (Pair<Int, Int>):
    //      ◦ Pair(w, u) instantiates a new heap object on every edge addition and relaxation step!
    //      ◦ For high-throughput JVM graph algorithms or large graphs, Pair allocations induce
    //      Garbage Collection (GC) pauses.
    //      ◦ Top-Tier Bit-Packing Solution (Zero Heap Object Allocations):
    //          ▪ Pack (distance shl 32) or node into a primitive Long!
    //          ▪ PriorityQueue<Long>() sorts primitive Long values in natural ascending order
    //          without needing any comparator object!
    //          ▪ Distance fits in upper 32 bits, node ID in lower 32 bits.
    //          ▪ Extract distance: (code ushr 32).toInt()
    //          ▪ Extract node: (code and 0xFFFFFFFFL).toInt()
    //          ▪ Result: ZERO heap object allocations during Dijkstra traversal!

    // Approach 3: Bellman-Ford Algorithm
    // Time Complexity: O(V x E) | Auxiliary Space Complexity: O(V)
    fun networkDelayTimeBellmanFord(times: Array<IntArray>, n: Int, k: Int): Int {
        val dp = IntArray(n + 1) { Int.MAX_VALUE }
        dp[k] = 0

        for (i in 1 .. n) {
            var anyUpdate = false
            for (time in times) {
                val v = time[0]
                val u = time[1]
                val w = time[2]
                if (dp[v] != Int.MAX_VALUE && dp[v] + w < dp[u]) {
                    dp[u] = dp[v] + w
                    anyUpdate = true
                }
            }

            if (!anyUpdate) break
        }

        var minDist = 0
        for (i in 1 .. n) {
            if (dp[i] == Int.MAX_VALUE) return -1
            minDist = maxOf(minDist, dp[i])
        }

        return minDist
    }
    // Code Analysis 2: networkDelayTimeBellmanFord(times: Array<IntArray>, n: Int, k: Int): Int
    //    Review Findings for Bellman-Ford:
    //
    //    1. Outer Loop Iteration Bound:
    //      ◦ A shortest path in a graph without negative weight cycles contains at most V - 1 edges.
    //      ◦ Therefore, the outer loop only needs to run V - 1 times (for (i in 1 until n)).
    //      ◦ If an edge relaxation updates a distance on iteration V (the n-th iteration),
    //        that indicates the presence of a negative weight cycle!

}