package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week9GraphsTest {

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