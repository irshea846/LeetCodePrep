package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week12AdvDP2DTest {

    // Day 59 - LC 72. Edit Distance
    @Test
    fun minDistanceTest() {
        var word1 = "horse"
        var word2 = "ros"
        assertEquals(3, Week12AdvDP2D.minDistance(word1, word2))
        assertEquals(3, Week12AdvDP2D.minDistance21D(word1, word2))
        assertEquals(3, Week12AdvDP2D.minDistance2D(word1, word2))
        assertEquals(3, Week12AdvDP2D.minDistanceBanded(word1, word2))
        assertEquals(3, Week12AdvDP2D.damerauLevenshteinDistance(word1, word2))

        word1 = "intention"
        word2 = "execution"
        assertEquals(5, Week12AdvDP2D.minDistance(word1, word2))
        assertEquals(5, Week12AdvDP2D.minDistance21D(word1, word2))
        assertEquals(5, Week12AdvDP2D.minDistance2D(word1, word2))
        assertEquals(5, Week12AdvDP2D.minDistanceBanded(word1, word2))
        assertEquals(5, Week12AdvDP2D.damerauLevenshteinDistance(word1, word2))


        word1 = "zoologicoarchaeologist"
        word2 = "zoogeologist"
        assertEquals(10, Week12AdvDP2D.minDistance(word1, word2))
        assertEquals(10, Week12AdvDP2D.minDistance21D(word1, word2))
        assertEquals(10, Week12AdvDP2D.minDistance2D(word1, word2))
        assertEquals(10, Week12AdvDP2D.minDistanceBanded(word1, word2))
        assertEquals(10, Week12AdvDP2D.damerauLevenshteinDistance(word1, word2))

        word1 = ""
        word2 = "a"
        assertEquals(1, Week12AdvDP2D.minDistance(word1, word2))
        assertEquals(1, Week12AdvDP2D.minDistance21D(word1, word2))
        assertEquals(1, Week12AdvDP2D.minDistance2D(word1, word2))
        assertEquals(1, Week12AdvDP2D.minDistanceBanded(word1, word2))
        assertEquals(1, Week12AdvDP2D.damerauLevenshteinDistance(word1, word2))


        word1 = "b"
        word2 = ""
        assertEquals(1, Week12AdvDP2D.minDistance(word1, word2))
        assertEquals(1, Week12AdvDP2D.minDistance21D(word1, word2))
        assertEquals(1, Week12AdvDP2D.minDistance2D(word1, word2))
        assertEquals(1, Week12AdvDP2D.minDistanceBanded(word1, word2))
        assertEquals(1, Week12AdvDP2D.damerauLevenshteinDistance(word1, word2))

        // Damerau-Levenshtein Transposition Test Cases
        word1 = "ca"
        word2 = "ac"
        assertEquals(2, Week12AdvDP2D.minDistance(word1, word2))
        assertEquals(2, Week12AdvDP2D.minDistance21D(word1, word2))
        assertEquals(2, Week12AdvDP2D.minDistance2D(word1, word2))
        assertEquals(2, Week12AdvDP2D.minDistanceBanded(word1, word2)) // Levenshtein requires 2 substitutions
        assertEquals(1, Week12AdvDP2D.damerauLevenshteinDistance(word1, word2)) // Damerau-Levenshtein requires 1 transposition

        word1 = "teh"
        word2 = "the"
        assertEquals(2, Week12AdvDP2D.minDistance(word1, word2))
        assertEquals(2, Week12AdvDP2D.minDistance21D(word1, word2))
        assertEquals(2, Week12AdvDP2D.minDistance2D(word1, word2))
        assertEquals(2, Week12AdvDP2D.minDistanceBanded(word1, word2)) // Levenshtein requires 2 substitutions
        assertEquals(1, Week12AdvDP2D.damerauLevenshteinDistance(word1, word2)) // Damerau-Levenshtein requires 1 transposition
    }


    // Day 58 - LC 64. Minimum Path Sum
    @Test
    fun minPathSumTest() {
        val grid1 = arrayOf(intArrayOf(1, 3, 1), intArrayOf(1, 5, 1), intArrayOf(4, 2, 1))
        val grid2 = arrayOf(intArrayOf(1, 2, 3), intArrayOf(4, 5, 6))

        assertEquals(7, Week12AdvDP2D.minPathSum1D(grid1.map { it.clone() }.toTypedArray()))
        assertEquals(7, Week12AdvDP2D.minPathSumInPlace(grid1.map { it.clone() }.toTypedArray()))

        assertEquals(12, Week12AdvDP2D.minPathSum1D(grid2.map { it.clone() }.toTypedArray()))
        assertEquals(12, Week12AdvDP2D.minPathSumInPlace(grid2.map { it.clone() }.toTypedArray()))
    }


    // Day 57 - LC 62. Unique Paths
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