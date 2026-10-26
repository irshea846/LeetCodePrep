package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week12AdvDP2DTest {

    // Day 56 - LC 62. Unique Paths
    @Test
    fun uniquePathsTest() {
        assertEquals(28, Week12AdvDP2D.uniquePathsMath(3, 7))
        assertEquals(28, Week12AdvDP2D.uniquePathsCombinatorics(3, 7))
        assertEquals(28, Week12AdvDP2D.uniquePaths1D(3, 7))
        assertEquals(28, Week12AdvDP2D.uniquePaths2D(3, 7))

        assertEquals(3, Week12AdvDP2D.uniquePathsMath(3, 2))
        assertEquals(3, Week12AdvDP2D.uniquePathsCombinatorics(3, 2))
        assertEquals(3, Week12AdvDP2D.uniquePaths1D(3, 2))
        assertEquals(3, Week12AdvDP2D.uniquePaths2D(3, 2))

        assertEquals(1, Week12AdvDP2D.uniquePathsMath(1, 1))
        assertEquals(1, Week12AdvDP2D.uniquePathsCombinatorics(1, 1))
        assertEquals(1, Week12AdvDP2D.uniquePaths1D(1, 1))
        assertEquals(1, Week12AdvDP2D.uniquePaths2D(1, 1))

        assertEquals(80, Week12AdvDP2D.uniquePathsMath(80, 2))
        assertEquals(80, Week12AdvDP2D.uniquePathsCombinatorics(80, 2))
        assertEquals(80, Week12AdvDP2D.uniquePaths1D(80, 2))
        assertEquals(80, Week12AdvDP2D.uniquePaths2D(80, 2))
    }
}