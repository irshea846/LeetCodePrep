package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW14AdvGraphTest {

    // LC 210. Course Schedule II
    @Test
    fun testFindOrder() {
        var prerequisites = arrayOf(intArrayOf(1, 0))
        var expected = intArrayOf(0, 1)
        assertEquals(
            expected.contentToString(),
            MasterW14AdvGraph.findOrder(2, prerequisites).contentToString()
        )

        prerequisites =
            arrayOf(intArrayOf(1, 0), intArrayOf(2, 0), intArrayOf(3, 1), intArrayOf(3, 2))
        val validOrders = listOf("[0, 1, 2, 3]", "[0, 2, 1, 3]")
        assert(validOrders.contains(MasterW14AdvGraph.findOrder(4, prerequisites).contentToString()))

        prerequisites = arrayOf()
        expected = intArrayOf(0)
        assertEquals(
            expected.contentToString(),
            MasterW14AdvGraph.findOrder(1, prerequisites).contentToString()
        )

        prerequisites = arrayOf(intArrayOf(1, 0), intArrayOf(1, 2), intArrayOf(0, 1))
        expected = intArrayOf()
        assertEquals(
            expected.contentToString(),
            MasterW14AdvGraph.findOrder(3, prerequisites).contentToString()
        )
    }

    // LC 1584. Min Cost to Connect All Points
    @Test
    fun testMinCostConnectPoints() {
        var points = arrayOf(
            intArrayOf(0, 0),
            intArrayOf(2, 2),
            intArrayOf(3, 10),
            intArrayOf(5, 2),
            intArrayOf(7, 0)
        )
        assertEquals(20, MasterW14AdvGraph.minCostConnectPoints(points))

        points = arrayOf(intArrayOf(3, 12), intArrayOf(-2, 5), intArrayOf(-4, 1))
        assertEquals(18, MasterW14AdvGraph.minCostConnectPoints(points))

        points = arrayOf(intArrayOf(0, 0), intArrayOf(1, 1), intArrayOf(1, 0), intArrayOf(-1, 1))
        assertEquals(4, MasterW14AdvGraph.minCostConnectPoints(points))

    }

    // LC 684. Redundant Connection
    @Test
    fun testFindRedundantConnection() {
        var edges = arrayOf(intArrayOf(1, 2), intArrayOf(1, 3), intArrayOf(2, 3))
        var expected = intArrayOf(2, 3)
        assertEquals(
            expected.contentToString(),
            MasterW14AdvGraph.findRedundantConnectionDSU(edges).contentToString()
        )
        assertEquals(
            expected.contentToString(),
            MasterW14AdvGraph.findRedundantConnection(edges).contentToString()
        )

        edges = arrayOf(
            intArrayOf(1, 2),
            intArrayOf(2, 3),
            intArrayOf(3, 4),
            intArrayOf(1, 4),
            intArrayOf(1, 5)
        )
        expected = intArrayOf(1, 4)
        assertEquals(
            expected.contentToString(),
            MasterW14AdvGraph.findRedundantConnectionDSU(edges).contentToString()
        )
        assertEquals(
            expected.contentToString(),
            MasterW14AdvGraph.findRedundantConnection(edges).contentToString()
        )
    }

    // LC 787. Cheapest Flights Within K Stops
    @Test
    fun testFindCheapestPrice() {
        var flights = arrayOf(
            intArrayOf(0, 1, 100), intArrayOf(1, 2, 100), intArrayOf(2, 0, 100),
            intArrayOf(1, 3, 600), intArrayOf(2, 3, 200)
        )
        var n = 4; var src = 0; var dst = 3; var k = 1
        assertEquals(700, MasterW14AdvGraph.findCheapestPrice(n, flights, src, dst, k))

        flights = arrayOf(intArrayOf(0, 1, 100), intArrayOf(1, 2, 100), intArrayOf(0, 2, 500))
        n = 3; src = 0; dst = 2; k = 1
        assertEquals(200, MasterW14AdvGraph.findCheapestPrice(n, flights, src, dst, k))

        flights = arrayOf(intArrayOf(0, 1, 100), intArrayOf(1, 2, 100), intArrayOf(0, 2, 500))
        n = 3; src = 0; dst = 2; k = 0
        assertEquals(500, MasterW14AdvGraph.findCheapestPrice(n, flights, src, dst, k))

        flights = arrayOf(
            intArrayOf(4, 1, 1), intArrayOf(1, 2, 3), intArrayOf(0, 3, 2),
            intArrayOf(0, 4, 10), intArrayOf(3, 1, 1), intArrayOf(1, 4, 3)
        )
        n = 5; src = 2; dst = 1; k = 1
        assertEquals(-1, MasterW14AdvGraph.findCheapestPrice(n, flights, src, dst, k))

        flights = arrayOf(intArrayOf(1, 0, 5), intArrayOf(2, 1, 5), intArrayOf(3, 0, 2),
            intArrayOf(1, 3, 2), intArrayOf(4, 1, 1), intArrayOf(2, 4, 1))
        n = 5; src = 2; dst = 0; k = 2
        assertEquals(7, MasterW14AdvGraph.findCheapestPrice(n, flights, src, dst, k))

    }

    // LC 743. Network Delay Time
    @Test
    fun testNetworkDelayTime() {
        var times = arrayOf(intArrayOf(2, 1, 1), intArrayOf(2, 3, 1), intArrayOf(3, 4, 1))
        var n = 4
        var k = 2
        assertEquals(2, MasterW14AdvGraph.networkDelayTimeBitPacked(times, n, k))
        assertEquals(2, MasterW14AdvGraph.networkDelayTime(times, n, k))

        times = arrayOf(intArrayOf(1, 2, 1))
        n = 2
        k = 1
        assertEquals(1, MasterW14AdvGraph.networkDelayTimeBitPacked(times, n, k))
        assertEquals(1, MasterW14AdvGraph.networkDelayTime(times, n, k))

        times = arrayOf(intArrayOf(1, 2, 1))
        n = 2
        k = 2
        assertEquals(-1, MasterW14AdvGraph.networkDelayTimeBitPacked(times, n, k))
        assertEquals(-1, MasterW14AdvGraph.networkDelayTime(times, n, k))

        times = arrayOf(intArrayOf(1, 2, 1), intArrayOf(2, 1, 3))
        n = 2
        k = 2
        assertEquals(3, MasterW14AdvGraph.networkDelayTimeBitPacked(times, n, k))
        assertEquals(3, MasterW14AdvGraph.networkDelayTime(times, n, k))

    }

}