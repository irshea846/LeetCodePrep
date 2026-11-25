package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import kotlin.test.assertEquals

class MasterW15GraphsDPSynthesisTest {

    // LC 847. Shortest Path Visiting All Nodes
    @Test
    fun testShortestPathLength() {
        var graph = arrayOf(
            intArrayOf(1, 2, 3),
            intArrayOf(0),
            intArrayOf(0),
            intArrayOf(0)
        )
        assertEquals(4, MasterW15GraphsDPSynthesis.shortestPathLengthBitPacking(graph))
        assertEquals(4, MasterW15GraphsDPSynthesis.shortestPathLength(graph))

        graph = arrayOf(
            intArrayOf(1),
            intArrayOf(0, 2, 4),
            intArrayOf(1, 3, 4),
            intArrayOf(2),
            intArrayOf(1, 2)
        )
        assertEquals(4, MasterW15GraphsDPSynthesis.shortestPathLengthBitPacking(graph))
        assertEquals(4, MasterW15GraphsDPSynthesis.shortestPathLength(graph))

        graph = arrayOf(
            intArrayOf(2, 3, 5, 7),
            intArrayOf(2, 3, 7),
            intArrayOf(0, 1),
            intArrayOf(0, 1,),
            intArrayOf(7),
            intArrayOf(0),
            intArrayOf(10),
            intArrayOf(9, 10, 0, 1, 4),
            intArrayOf(9),
            intArrayOf(7, 8),
            intArrayOf(7, 6)
        )
        assertEquals(14, MasterW15GraphsDPSynthesis.shortestPathLengthBitPacking(graph))
        assertEquals(14, MasterW15GraphsDPSynthesis.shortestPathLength(graph))

    }


    // LC 337. House Robber III
    @Test
    fun testRob() {
        var root = MasterW15GraphsDPSynthesis.TreeNode(3)
        root.left = MasterW15GraphsDPSynthesis.TreeNode(2)
        root.right = MasterW15GraphsDPSynthesis.TreeNode(3)
        root.left!!.right = MasterW15GraphsDPSynthesis.TreeNode(3)
        root.right!!.right = MasterW15GraphsDPSynthesis.TreeNode(1)
        assertEquals(7, MasterW15GraphsDPSynthesis.robBitPacking(root))
        assertEquals(7, MasterW15GraphsDPSynthesis.robDFS(root))

        root = MasterW15GraphsDPSynthesis.TreeNode(3)
        root.left = MasterW15GraphsDPSynthesis.TreeNode(4)
        root.right = MasterW15GraphsDPSynthesis.TreeNode(5)
        root.left!!.left = MasterW15GraphsDPSynthesis.TreeNode(1)
        root.left!!.right = MasterW15GraphsDPSynthesis.TreeNode(3)
        root.right!!.right = MasterW15GraphsDPSynthesis.TreeNode(1)
        assertEquals(9, MasterW15GraphsDPSynthesis.robBitPacking(root))
        assertEquals(9, MasterW15GraphsDPSynthesis.robDFS(root))

        root = MasterW15GraphsDPSynthesis.TreeNode(4)
        root.left = MasterW15GraphsDPSynthesis.TreeNode(1)
        root.left!!.left = MasterW15GraphsDPSynthesis.TreeNode(2)
        root.left!!.left!!.left = MasterW15GraphsDPSynthesis.TreeNode(3)
        assertEquals(7, MasterW15GraphsDPSynthesis.robBitPacking(root))
        assertEquals(7, MasterW15GraphsDPSynthesis.robDFS(root))

    }

    // LC 542. 01 Matrix
    @Test
    fun testUpdateMatrix() {

        var matrix = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(0, 0, 0)
        )
        var expected = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(0, 0, 0)
        )
        assertArrayEquals(expected, MasterW15GraphsDPSynthesis.updateMatrix(matrix))
        assertArrayEquals(expected, MasterW15GraphsDPSynthesis.updateMatrixBFS(matrix))


        matrix = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(1, 1, 1)
        )
        expected = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(1, 2, 1)
        )
        assertArrayEquals(expected, MasterW15GraphsDPSynthesis.updateMatrix(matrix))
        assertArrayEquals(expected, MasterW15GraphsDPSynthesis.updateMatrixBFS(matrix))

        matrix = arrayOf(
            intArrayOf(0, 1, 0, 1, 1),
            intArrayOf(1, 1, 0, 0, 1),
            intArrayOf(0, 0, 0, 1, 0),
            intArrayOf(1, 0, 1, 1, 1),
            intArrayOf(1, 0, 0, 0, 1)
        )
        expected = arrayOf(
            intArrayOf(0, 1, 0, 1, 2),
            intArrayOf(1, 1, 0, 0, 1),
            intArrayOf(0, 0, 0, 1, 0),
            intArrayOf(1, 0, 1, 1, 1),
            intArrayOf(1, 0, 0, 0, 1)
        )
        assertArrayEquals(expected, MasterW15GraphsDPSynthesis.updateMatrix(matrix))
        assertArrayEquals(expected, MasterW15GraphsDPSynthesis.updateMatrixBFS(matrix))

    }

    // LC 1976. Number of Ways to Arrive at Destination
    @Test
    fun testCountPaths() {
        var n = 7
        var roads = arrayOf(
            intArrayOf(0, 6, 7),
            intArrayOf(0, 1, 2),
            intArrayOf(1, 2, 3),
            intArrayOf(1, 3, 3),
            intArrayOf(6, 3, 3),
            intArrayOf(3, 5, 1),
            intArrayOf(6, 5, 1),
            intArrayOf(2, 5, 1),
            intArrayOf(0, 4, 5),
            intArrayOf(4, 6, 2)
        )
        assertEquals(4, MasterW15GraphsDPSynthesis.countPaths(n, roads))

        n = 2
        roads = arrayOf(
            intArrayOf(1, 0, 10)
        )
        assertEquals(1, MasterW15GraphsDPSynthesis.countPaths(n, roads))

        n = 10
        roads = arrayOf(
            intArrayOf(0, 4, 2),
            intArrayOf(4, 1, 5),
            intArrayOf(5, 4, 2),
            intArrayOf(2, 5, 3),
            intArrayOf(1, 3, 2),
            intArrayOf(4, 6, 2),
            intArrayOf(3, 9, 1),
            intArrayOf(8, 6, 5),
            intArrayOf(2, 7, 5),
            intArrayOf(2, 6, 2),
            intArrayOf(4, 7, 5),
            intArrayOf(6, 1, 2)
        )
        assertEquals(1, MasterW15GraphsDPSynthesis.countPaths(n, roads))

    }


    // LC 329. Longest Increasing Path in a Matrix
    @Test
    fun testLongestIncreasingPath() {

        var matrix = arrayOf(
            intArrayOf(9, 9, 4),
            intArrayOf(6, 6, 8),
            intArrayOf(2, 1, 1)
        )
        assertEquals(4, MasterW15GraphsDPSynthesis.longestIncreasingPathDFS(matrix))
        assertEquals(4, MasterW15GraphsDPSynthesis.longestIncreasingPathBFS(matrix))


        matrix = arrayOf(
            intArrayOf(3, 4, 5),
            intArrayOf(3, 2, 6),
            intArrayOf(2, 2, 1)
        )
        assertEquals(4, MasterW15GraphsDPSynthesis.longestIncreasingPathDFS(matrix))
        assertEquals(4, MasterW15GraphsDPSynthesis.longestIncreasingPathBFS(matrix))

        matrix = arrayOf(
            intArrayOf(0), intArrayOf(1), intArrayOf(5), intArrayOf(5)
        )
        assertEquals(3, MasterW15GraphsDPSynthesis.longestIncreasingPathDFS(matrix))
        assertEquals(3, MasterW15GraphsDPSynthesis.longestIncreasingPathBFS(matrix))

    }

}