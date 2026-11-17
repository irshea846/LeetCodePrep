package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week15GraphsDPSynthesisTest {

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