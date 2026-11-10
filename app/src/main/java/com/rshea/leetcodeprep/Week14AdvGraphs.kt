package com.rshea.leetcodeprep

import java.util.PriorityQueue

object Week14AdvGraphs {

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
    //      ◦ Therefore, the outer loop only needs to run $V - 1$ times (for (i in 1 until n)).
    //      ◦ If an edge relaxation updates a distance on iteration $V$ (the n-th iteration),
    //        that indicates the presence of a negative weight cycle!

}