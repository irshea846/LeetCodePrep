package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week14AdvGraphsTest {

    // Day 68 - LC 743. Network Delay Time
    @Test
    fun testNetworkDelayTime() {
        var times = arrayOf(intArrayOf(2, 1, 1), intArrayOf(2, 3, 1), intArrayOf(3, 4, 1))
        var n = 4
        var k = 2

        assertEquals(2, Week14AdvGraphs.networkDelayTimeBitPacking(times, n, k))
        assertEquals(2, Week14AdvGraphs.networkDelayTimeDijkstra(times, n, k))
        assertEquals(2, Week14AdvGraphs.networkDelayTimeBellmanFord(times, n, k))

        times = arrayOf(intArrayOf(1, 2, 1))
        n = 2
        k = 1
        assertEquals(1, Week14AdvGraphs.networkDelayTimeBitPacking(times, n, k))
        assertEquals(1, Week14AdvGraphs.networkDelayTimeDijkstra(times, n, k))
        assertEquals(1, Week14AdvGraphs.networkDelayTimeBellmanFord(times, n, k))

        times = arrayOf(intArrayOf(1, 2, 1))
        n = 2
        k = 2
        assertEquals(-1, Week14AdvGraphs.networkDelayTimeBitPacking(times, n, k))
        assertEquals(-1, Week14AdvGraphs.networkDelayTimeDijkstra(times, n, k))
        assertEquals(-1, Week14AdvGraphs.networkDelayTimeBellmanFord(times, n, k))

        times = arrayOf(intArrayOf(1, 2, 1), intArrayOf(2, 1, 3))
        n = 2
        k = 2
        assertEquals(3, Week14AdvGraphs.networkDelayTimeBitPacking(times, n, k))
        assertEquals(3, Week14AdvGraphs.networkDelayTimeDijkstra(times, n, k))
        assertEquals(3, Week14AdvGraphs.networkDelayTimeBellmanFord(times, n, k))

    }

}