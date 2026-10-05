package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW9GraphsTest {

    // LC 133. Clone Graph
    @Test
    fun testCloneGraph_ValidCase1() {
        val node1 = MasterW9Graphs.Node(1)
        val node2 = MasterW9Graphs.Node(2)
        val node3 = MasterW9Graphs.Node(3)
        val node4 = MasterW9Graphs.Node(4)
        node1.neighbors = arrayListOf(node2, node4)
        node2.neighbors = arrayListOf(node1, node3)
        node3.neighbors = arrayListOf(node2, node4)
        node4.neighbors = arrayListOf(node1, node3)
        val clonedNode1 = MasterW9Graphs.cloneGraphDFS(node1)
        val clonedNode2 = MasterW9Graphs.cloneGraphBFS(node1)
        assertEquals(clonedNode1!!.`val`, clonedNode2!!.`val`)
        assertEquals(clonedNode1.neighbors[0]!!.`val`, clonedNode2.neighbors[0]!!.`val`)
        assertEquals(clonedNode1.neighbors[1]!!.`val`, clonedNode2.neighbors[1]!!.`val`)
    }

    // LC 743. Network Delay Time
    private fun createTimes1() = arrayOf(
        intArrayOf(2, 1, 1),
        intArrayOf(2, 3, 1),
        intArrayOf(3, 4, 1)
    )

    private fun createTimes2() = arrayOf(
        intArrayOf(1, 2, 1),
    )

    private fun createTimes3() = arrayOf(
        intArrayOf(1, 2, 1)
    )

    private fun createTimes4() = arrayOf(
        intArrayOf(1, 3, 68), intArrayOf(1, 4,20), intArrayOf(4, 1, 65),
        intArrayOf(3, 2, 74), intArrayOf(2, 1,44), intArrayOf(3, 4, 61),
        intArrayOf(4, 3, 68), intArrayOf(3, 1,26), intArrayOf(5, 1, 60),
        intArrayOf(5, 3, 3),  intArrayOf(4, 5,5),  intArrayOf(2, 5, 36),
        intArrayOf(2, 3, 94), intArrayOf(1, 2,0),  intArrayOf(3, 5, 90),
        intArrayOf(2, 4, 28), intArrayOf(4, 2,12), intArrayOf(5, 4, 52),
        intArrayOf(5, 2, 85), intArrayOf(1, 5,42)
    )

    private fun createTimes5() = arrayOf(
        intArrayOf(1, 2, 1), intArrayOf(2, 3, 7), intArrayOf(1, 3, 4), intArrayOf(2, 1, 2)
    )

    @Test
    fun testNetworkDelayTime_Times1() {
        assertEquals(2, MasterW9Graphs.networkDelayTimeBellmanFord(createTimes1(), 4, 2))
        assertEquals(2, MasterW9Graphs.networkDelayTimeFixedArray(createTimes1(), 4, 2))
        assertEquals(2, MasterW9Graphs.networkDelayTimeBitPacking(createTimes1(), 4, 2))
        assertEquals(2, MasterW9Graphs.networkDelayTimePair(createTimes1(), 4, 2))
    }

    @Test
    fun testNetworkDelayTime_Times2() {
        assertEquals(1, MasterW9Graphs.networkDelayTimeBellmanFord(createTimes2(), 2, 1))
        assertEquals(1, MasterW9Graphs.networkDelayTimeFixedArray(createTimes2(), 2, 1))
        assertEquals(1, MasterW9Graphs.networkDelayTimeBitPacking(createTimes2(), 2, 1))
        assertEquals(1, MasterW9Graphs.networkDelayTimePair(createTimes2(), 2, 1))
    }

    @Test
    fun testNetworkDelayTime_Times3() {
        assertEquals(-1, MasterW9Graphs.networkDelayTimeBellmanFord(createTimes3(), 2, 2))
        assertEquals(-1, MasterW9Graphs.networkDelayTimeFixedArray(createTimes3(), 2, 2))
        assertEquals(-1, MasterW9Graphs.networkDelayTimeBitPacking(createTimes3(), 2, 2))
        assertEquals(-1, MasterW9Graphs.networkDelayTimePair(createTimes3(), 2, 2))
    }

    @Test
    fun testNetworkDelayTime_Times4() {
        assertEquals(34, MasterW9Graphs.networkDelayTimeBellmanFord(createTimes4(), 5, 4))
        assertEquals(34, MasterW9Graphs.networkDelayTimeFixedArray(createTimes4(), 5, 4))
        assertEquals(34, MasterW9Graphs.networkDelayTimeBitPacking(createTimes4(), 5, 4))
        assertEquals(34, MasterW9Graphs.networkDelayTimePair(createTimes4(), 5, 4))
    }

    @Test
    fun testNetworkDelayTime_Times5() {
        assertEquals(6, MasterW9Graphs.networkDelayTimeBellmanFord(createTimes5(), 3, 2))
        assertEquals(6, MasterW9Graphs.networkDelayTimeFixedArray(createTimes5(), 3, 2))
        assertEquals(6, MasterW9Graphs.networkDelayTimeBitPacking(createTimes5(), 3, 2))
        assertEquals(6, MasterW9Graphs.networkDelayTimePair(createTimes5(), 3, 2))
    }


    // LC 207. Course Schedule
    private fun createPrereq1() = arrayOf(
        intArrayOf(1, 0)
    )

    private fun createPrereq2() = arrayOf(
        intArrayOf(1, 0), intArrayOf(0, 1)
    )

    private fun createPrereq3() = arrayOf(
        intArrayOf(0, 10), intArrayOf(3, 18), intArrayOf(5, 5),
        intArrayOf(6, 11), intArrayOf(11, 14), intArrayOf(13, 1),
        intArrayOf(15, 1), intArrayOf(17, 4)
    )

    private fun createPrereq4() = arrayOf(
        intArrayOf(1, 0),intArrayOf(2, 6),intArrayOf(1, 7),
        intArrayOf(5, 1),intArrayOf(6, 4),intArrayOf(7, 0),
        intArrayOf(0, 5)
    )

    private fun createPrereq5() = arrayOf(
        intArrayOf(1, 4), intArrayOf(2, 4), intArrayOf(3, 1), intArrayOf(3, 2)
    )

    @Test
    fun testCanFinish_Prereq1() {
        assertEquals(true, MasterW9Graphs.canFinishDFS(2, createPrereq1()))
        assertEquals(true, MasterW9Graphs.canFinishBFS(2, createPrereq1()))
    }

    @Test
    fun testCanFinish_Prereq2() {
        assertEquals(false, MasterW9Graphs.canFinishDFS(2, createPrereq2()))
        assertEquals(false, MasterW9Graphs.canFinishBFS(2, createPrereq2()))
    }

    @Test
    fun testCanFinish_Prereq3() {
        assertEquals(false, MasterW9Graphs.canFinishDFS(20, createPrereq3()))
        assertEquals(false, MasterW9Graphs.canFinishBFS(20, createPrereq3()))
    }

    @Test
    fun testCanFinish_Prereq4() {
        assertEquals(false, MasterW9Graphs.canFinishDFS(8, createPrereq4()))
        assertEquals(false, MasterW9Graphs.canFinishBFS(8, createPrereq4()))
    }

    @Test
    fun testCanFinish_Prereq5() {
        assertEquals(true, MasterW9Graphs.canFinishDFS(5, createPrereq5()))
        assertEquals(true, MasterW9Graphs.canFinishBFS(5, createPrereq5()))
    }

    // LC 200. Number of Islands
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
    fun testNumIslands_Grid1() {
        assertEquals(1, MasterW9Graphs.numIslandsDFS(createGrid1()))
        assertEquals(1, MasterW9Graphs.numIslandsBFS(createGrid1()))
    }

    @Test
    fun testNumIslands_Grid2() {
        assertEquals(3, MasterW9Graphs.numIslandsDFS(createGrid2()))
        assertEquals(3, MasterW9Graphs.numIslandsBFS(createGrid2()))
    }

    @Test
    fun testNumIslands_Grid3() {
        assertEquals(1, MasterW9Graphs.numIslandsDFS(createGrid3()))
        assertEquals(1, MasterW9Graphs.numIslandsBFS(createGrid3()))
    }
}