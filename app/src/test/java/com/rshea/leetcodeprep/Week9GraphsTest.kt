package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week9GraphsTest {

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


    // Day 41 - LC 200. Number of Islands
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