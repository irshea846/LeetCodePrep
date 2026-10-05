package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week9GraphsTest {

    // Day 44 - LC 133. Clone Graph
    @Test
    fun testCloneGraph_ValidCase1() {
        val node1 = Week9Graphs.Node(1)
        val node2 = Week9Graphs.Node(2)
        val node3 = Week9Graphs.Node(3)
        val node4 = Week9Graphs.Node(4)
        node1.neighbors = arrayListOf(node2, node4)
        node2.neighbors = arrayListOf(node1, node3)
        node3.neighbors = arrayListOf(node2, node4)
        node4.neighbors = arrayListOf(node1, node3)
        val clonedNode1 = Week9Graphs.cloneGraphDFS(node1)
        val clonedNode2 = Week9Graphs.cloneGraphBFS(node1)
        assertEquals(clonedNode1!!.`val`, clonedNode2!!.`val`)
        assertEquals(clonedNode1.neighbors[0]!!.`val`, clonedNode2.neighbors[0]!!.`val`)
        assertEquals(clonedNode1.neighbors[1]!!.`val`, clonedNode2.neighbors[1]!!.`val`)
    }


    // Day 43 - LC 743. Network Delay Time
    @Test
    fun testNetworkDelayTime_ValidCase1() {
        val times = arrayOf(intArrayOf(2, 1, 1), intArrayOf(2, 3, 1), intArrayOf(3, 4, 1))
        val n = 4
        val k = 2
        assertEquals(2, Week9Graphs.networkDelayTime(times, n, k))
        assertEquals(2, Week9Graphs.networkDelayTimeBitPacking(times, n, k))
        assertEquals(2, Week9Graphs.networkDelayTimeBellmanFord(times, n, k))
    }

    @Test
    fun testNetworkDelayTime_ValidCase2() {
        val times = arrayOf(intArrayOf(1, 2, 1))
        val n = 2
        val k = 1
        assertEquals(1, Week9Graphs.networkDelayTime(times, n, k))
        assertEquals(1, Week9Graphs.networkDelayTimeBitPacking(times, n, k))
        assertEquals(1, Week9Graphs.networkDelayTimeBellmanFord(times, n, k))
    }

    @Test
    fun testNetworkDelayTime_InvalidCase3() {
        val times = arrayOf(intArrayOf(1, 2, 1))
        val n = 2
        val k = 2
        assertEquals(-1, Week9Graphs.networkDelayTime(times, n, k))
        assertEquals(-1, Week9Graphs.networkDelayTimeBitPacking(times, n, k))
        assertEquals(-1, Week9Graphs.networkDelayTimeBellmanFord(times, n, k))
    }

    @Test
    fun testNetworkDelayTime_InvalidCase4() {
        val times = arrayOf(intArrayOf(1, 2, 1), intArrayOf(2, 3, 7), intArrayOf(1, 3, 4), intArrayOf(2, 1, 2))
        val n = 3
        val k = 2
        assertEquals(6, Week9Graphs.networkDelayTime(times, n, k))
        assertEquals(6, Week9Graphs.networkDelayTimeBitPacking(times, n, k))
        assertEquals(6, Week9Graphs.networkDelayTimeBellmanFord(times, n, k))
    }

    @Test
    fun testNetworkDelayTime_InvalidCase5() {
        val times = arrayOf(intArrayOf(1, 3, 68), intArrayOf(1, 4, 20), intArrayOf(4, 1, 65),
            intArrayOf(3, 2, 74), intArrayOf(2, 1, 44), intArrayOf(4, 3, 68), intArrayOf(3, 1, 26),
            intArrayOf(5, 1, 60), intArrayOf(5, 3, 3), intArrayOf (4,5,5), intArrayOf(2, 5, 36),
            intArrayOf(2, 3, 94), intArrayOf(1 ,2, 0), intArrayOf(3, 5, 90), intArrayOf(2, 4, 28),
            intArrayOf(4, 2, 12), intArrayOf(5, 4, 52), intArrayOf(5, 2, 85),intArrayOf(1, 5, 42))
        val n = 5
        val k = 4
        assertEquals(34, Week9Graphs.networkDelayTime(times, n, k))
    }


    // Day 42 - LC 207. Course Schedule
    @Test
    fun testCanFinish_ValidCase1() {
        val numCourses = 2
        val prerequisites = arrayOf(intArrayOf(1, 0))
        assertEquals(true, Week9Graphs.canFinishDFS(numCourses, prerequisites))
        assertEquals(true, Week9Graphs.canFinishBFS(numCourses, prerequisites))
    }

    @Test
    fun testCanFinish_ValidCase2() {
        val numCourses = 2
        val prerequisites = arrayOf(intArrayOf(1, 0), intArrayOf(0, 1))
        assertEquals(false, Week9Graphs.canFinishDFS(numCourses, prerequisites))
        assertEquals(false, Week9Graphs.canFinishBFS(numCourses, prerequisites))
    }

    @Test
    fun testCanFinish_InvalidCase3() {
        val numCourses = 8
        val prerequisites = arrayOf(
            intArrayOf(1, 0), intArrayOf(2, 6), intArrayOf(1, 7), intArrayOf(5, 1),
            intArrayOf(6, 4), intArrayOf(7, 0), intArrayOf(0, 5)
        )
        assertEquals(false, Week9Graphs.canFinishDFS(numCourses, prerequisites))
        assertEquals(false, Week9Graphs.canFinishBFS(numCourses, prerequisites))
    }

    @Test
    fun testCanFinish_InvalidCase4() {
        val numCourses = 5
        val prerequisites = arrayOf(
            intArrayOf(1, 4), intArrayOf(2, 4), intArrayOf(3, 1), intArrayOf(3, 2)
        )
        assertEquals(true, Week9Graphs.canFinishDFS(numCourses, prerequisites))
        assertEquals(true, Week9Graphs.canFinishBFS(numCourses, prerequisites))
    }

    @Test
    fun testCanFinish_InvalidCase5() {
        val numCourses = 20
        val prerequisites = arrayOf(
            intArrayOf(0, 10), intArrayOf(3, 18), intArrayOf(5, 5), intArrayOf(6, 11),
            intArrayOf(11, 14), intArrayOf(13, 1), intArrayOf(15, 1), intArrayOf(17, 4)
        )
        assertEquals(false, Week9Graphs.canFinishDFS(numCourses, prerequisites))
        assertEquals(false, Week9Graphs.canFinishBFS(numCourses, prerequisites))
    }


    // Day 41 - LC 200. Number of Islands
    private fun createGrid1() = arrayOf(
        charArrayOf('1', '1', '1', '1', '0'),
        charArrayOf('1', '1', '0', '1', '0'),
        charArrayOf('1', '1', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0', '0')
    )

    private fun createGrid2() = arrayOf(
        charArrayOf('1', '1', '0', '0', '0'),
        charArrayOf('1', '1', '0', '0', '0'),
        charArrayOf('0', '0', '1', '0', '0'),
        charArrayOf('0', '0', '0', '1', '1')
    )

    private fun createGrid3() = arrayOf(
        charArrayOf('1', '1', '1'),
        charArrayOf('0', '1', '0'),
        charArrayOf('1', '1', '1')
    )

    @Test
    fun testNumIslands_DFS() {
        assertEquals(1, Week9Graphs.numIslands(createGrid1()))
        assertEquals(3, Week9Graphs.numIslands(createGrid2()))
        assertEquals(1, Week9Graphs.numIslands(createGrid3()))
    }

    @Test
    fun testNumIslands_BFS() {
        assertEquals(1, Week9Graphs.numIslandsBFS(createGrid1()))
        assertEquals(3, Week9Graphs.numIslandsBFS(createGrid2()))
        assertEquals(1, Week9Graphs.numIslandsBFS(createGrid3()))
    }

    @Test
    fun testNumIslands_BitPacking() {
        assertEquals(1, Week9Graphs.numIslandsBitPacking(createGrid1()))
        assertEquals(3, Week9Graphs.numIslandsBitPacking(createGrid2()))
        assertEquals(1, Week9Graphs.numIslandsBitPacking(createGrid3()))

    }
}