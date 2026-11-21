package com.rshea.leetcodeprep

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import kotlin.test.assertEquals

class Week15GraphsDPSynthesisTest {

    // Day 78 - LC 847. Shortest Path Visiting All Nodes
    @Test
    fun testShortestPathLength() {
        var graph = arrayOf(intArrayOf(1, 2, 3), intArrayOf(0), intArrayOf(0), intArrayOf(0))
        var expected = 4
        assertEquals(expected, Week15GraphsDPSynthesis.shortestPathLengthBitPacking(graph))
        assertEquals(expected, Week15GraphsDPSynthesis.shortestPathLengthBFS(graph))

        graph = arrayOf(
            intArrayOf(1),
            intArrayOf(0, 2, 4),
            intArrayOf(1, 3, 4),
            intArrayOf(2),
            intArrayOf(1, 2)
        )
        expected = 4
        assertEquals(expected, Week15GraphsDPSynthesis.shortestPathLengthBitPacking(graph))
        assertEquals(expected, Week15GraphsDPSynthesis.shortestPathLengthBFS(graph))

    }

    // Day 77 - LC 337. House Robber III
    @Test
    fun testRob() {
        var root = Week15GraphsDPSynthesis.TreeNode(3)
        root.left = Week15GraphsDPSynthesis.TreeNode(2)
        root.right = Week15GraphsDPSynthesis.TreeNode(3)
        root.left!!.right = Week15GraphsDPSynthesis.TreeNode(3)
        root.right!!.right = Week15GraphsDPSynthesis.TreeNode(1)
        var expected = 7
        assertEquals(expected, Week15GraphsDPSynthesis.robBitPacking(root))
        assertEquals(expected, Week15GraphsDPSynthesis.robDFS(root))

        root = Week15GraphsDPSynthesis.TreeNode(3)
        root.left = Week15GraphsDPSynthesis.TreeNode(4)
        root.right = Week15GraphsDPSynthesis.TreeNode(5)
        root.left!!.left = Week15GraphsDPSynthesis.TreeNode(1)
        root.left!!.right = Week15GraphsDPSynthesis.TreeNode(3)
        root.right!!.right = Week15GraphsDPSynthesis.TreeNode(1)
        expected = 9
        assertEquals(expected, Week15GraphsDPSynthesis.robBitPacking(root))
        assertEquals(expected, Week15GraphsDPSynthesis.robDFS(root))

        root = Week15GraphsDPSynthesis.TreeNode(4)
        root.left = Week15GraphsDPSynthesis.TreeNode(1)
        root.left!!.left = Week15GraphsDPSynthesis.TreeNode(2)
        root.left!!.left!!.left = Week15GraphsDPSynthesis.TreeNode(3)
        expected = 7
        assertEquals(expected, Week15GraphsDPSynthesis.robBitPacking(root))
        assertEquals(expected, Week15GraphsDPSynthesis.robDFS(root))

    }


    // Day 76 - LC 542. 01 Matrix
    @Test
    fun testUpdateMatrix() {
        var mat = arrayOf(
            intArrayOf(0, 1, 1),
            intArrayOf(1, 1, 1),
            intArrayOf(1, 1, 1)
        )
        var expected = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(1, 2, 3),
            intArrayOf(2, 3, 4)
        )
        assertArrayEquals(expected, Week15GraphsDPSynthesis.updateMatrix(mat))
        assertArrayEquals(expected, Week15GraphsDPSynthesis.updateMatrixMultiSourceBFS(mat))

        mat = arrayOf(
            intArrayOf(1, 1, 1),
            intArrayOf(1, 1, 1),
            intArrayOf(1, 1, 0)
        )
        expected = arrayOf(
            intArrayOf(4, 3, 2),
            intArrayOf(3, 2, 1),
            intArrayOf(2, 1, 0)
        )
        assertArrayEquals(expected, Week15GraphsDPSynthesis.updateMatrix(mat))
        assertArrayEquals(expected, Week15GraphsDPSynthesis.updateMatrixMultiSourceBFS(mat))

        mat = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(1, 1, 1)
        )
        expected = arrayOf(
            intArrayOf(0, 0, 0),
            intArrayOf(0, 1, 0),
            intArrayOf(1, 2, 1)
        )
        assertArrayEquals(expected, Week15GraphsDPSynthesis.updateMatrix(mat))
        assertArrayEquals(expected, Week15GraphsDPSynthesis.updateMatrixMultiSourceBFS(mat))

        mat = arrayOf(
            intArrayOf(0, 1, 0, 1, 1),
            intArrayOf(1, 1, 0, 0, 1),
            intArrayOf(0, 0, 0, 1, 0),
            intArrayOf(1, 0, 1, 1, 1)
        )
        expected = arrayOf(
            intArrayOf(0, 1, 0, 1, 2),
            intArrayOf(1, 1, 0, 0, 1),
            intArrayOf(0, 0, 0, 1, 0),
            intArrayOf(1, 0, 1, 2, 1)
        )
        assertArrayEquals(expected, Week15GraphsDPSynthesis.updateMatrix(mat))
        assertArrayEquals(expected, Week15GraphsDPSynthesis.updateMatrixMultiSourceBFS(mat))

    }


    // Day 75 - LC 1976. Number of Ways to Arrive at Destination
    @Test
    fun testCountPaths() {
        var n = 7
        var roads = arrayOf(
            intArrayOf(0, 6, 7), intArrayOf(0, 1, 2), intArrayOf(1, 2, 3), intArrayOf(1, 3, 3),
            intArrayOf(6, 3, 3), intArrayOf(3, 5, 1), intArrayOf(6, 5, 1), intArrayOf(2, 5, 1),
            intArrayOf(0, 4, 5), intArrayOf(4, 6, 2)
        )
        var expected = 4
        assertEquals(expected, Week15GraphsDPSynthesis.countPaths(n, roads))
        assertEquals(expected, Week15GraphsDPSynthesis.countPathsBitPacking(n, roads))

        n = 2
        roads = arrayOf(intArrayOf(0, 1, 2))
        expected = 1
        assertEquals(expected, Week15GraphsDPSynthesis.countPaths(n, roads))
        assertEquals(expected, Week15GraphsDPSynthesis.countPathsBitPacking(n, roads))

        n = 7
        roads = arrayOf(
            intArrayOf(0, 1, 1), intArrayOf(0, 2, 1), intArrayOf(1, 5, 1), intArrayOf(2, 5, 1),
            intArrayOf(5, 3, 1), intArrayOf(5, 4, 1), intArrayOf(3, 6, 1), intArrayOf(4, 6, 1)
        )
        expected = 4
        n = 3
        roads = arrayOf(
            intArrayOf(0, 1, 1_000_000_000), intArrayOf(1, 2, 1_000_000_000), intArrayOf(0, 2, 2_000_000_000)
        )
        expected = 2
        assertEquals(expected, Week15GraphsDPSynthesis.countPaths(n, roads))
        assertEquals(expected, Week15GraphsDPSynthesis.countPathsBitPacking(n, roads))

        n = 12
        roads = arrayOf(
            intArrayOf(1, 0, 2348),   intArrayOf(2, 1, 2852),    intArrayOf(2, 0, 5200),   intArrayOf(3, 1, 12480),
            intArrayOf(2, 3, 9628),   intArrayOf(4, 3, 7367),    intArrayOf(4, 0, 22195),  intArrayOf(5, 4, 5668),
            intArrayOf(1, 5, 25515),  intArrayOf(0, 5, 27863),   intArrayOf(6, 5, 836),    intArrayOf(6, 0, 28699),
            intArrayOf(2, 6, 23499),  intArrayOf(6, 3, 13871),   intArrayOf(1, 6, 26351),  intArrayOf(5, 7, 6229),
            intArrayOf(2, 7, 28892),  intArrayOf(1, 7, 31744),   intArrayOf(3, 7, 19264),  intArrayOf(6, 7, 5393),
            intArrayOf(2, 8, 31998),  intArrayOf(8, 7, 3106),    intArrayOf(3, 8, 22370),  intArrayOf(8, 4, 15003),
            intArrayOf(8, 6, 8499),   intArrayOf(8, 5, 9335),    intArrayOf(8, 9, 5258),   intArrayOf(9, 2, 37256),
            intArrayOf(3, 9, 27628),  intArrayOf(7, 9, 8364),    intArrayOf(1, 9, 40108),  intArrayOf(9, 5, 14593),
            intArrayOf(2, 10, 45922), intArrayOf(5, 10, 23259),  intArrayOf(9, 10, 8666),  intArrayOf(10, 0, 51122),
            intArrayOf(10, 3, 36294), intArrayOf(10, 4, 28927),  intArrayOf(11, 4, 25190), intArrayOf(11, 9, 4929),
            intArrayOf(11, 8, 10187), intArrayOf(11, 6, 18686),  intArrayOf(2, 11, 42185), intArrayOf(11, 3, 32557),
            intArrayOf(1, 11, 45037)
        )
        expected = 166
        assertEquals(expected, Week15GraphsDPSynthesis.countPaths(n, roads))

    }


    // Day 74 - LC 329. Longest Increasing Path in a Matrix
    @Test
    fun testLongestIncreasingPath() {
        var matrix = arrayOf(
            intArrayOf(9, 9, 4),
            intArrayOf(6, 6, 8),
            intArrayOf(2, 1, 1)
        )
        var expected = 4
        assertEquals(expected, Week15GraphsDPSynthesis.longestIncreasingPath(matrix))
        assertEquals(expected, Week15GraphsDPSynthesis.longestIncreasingPathTopologicalBFS(matrix))

        matrix = arrayOf(
            intArrayOf(3, 4, 5),
            intArrayOf(3, 2, 6),
            intArrayOf(2, 2, 1)
        )
        expected = 4
        assertEquals(expected, Week15GraphsDPSynthesis.longestIncreasingPath(matrix))
        assertEquals(expected, Week15GraphsDPSynthesis.longestIncreasingPathTopologicalBFS(matrix))

        matrix = arrayOf(
            intArrayOf(1)
        )
        expected = 1
        assertEquals(expected, Week15GraphsDPSynthesis.longestIncreasingPath(matrix))
        assertEquals(expected, Week15GraphsDPSynthesis.longestIncreasingPathTopologicalBFS(matrix))

        matrix = arrayOf(
            intArrayOf(6, 8),
            intArrayOf(7, 2)
        )
        expected = 2
        assertEquals(expected, Week15GraphsDPSynthesis.longestIncreasingPath(matrix))
        assertEquals(expected, Week15GraphsDPSynthesis.longestIncreasingPathTopologicalBFS(matrix))
    }
}