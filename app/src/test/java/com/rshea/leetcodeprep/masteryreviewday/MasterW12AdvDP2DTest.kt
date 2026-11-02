package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW12AdvDP2DTest {

    // LC 97. Interleaving String
    @Test
    fun isInterleaveTest() {
        var s1 = "aabcc"
        var s2 = "dbbca"
        var s3 = "aadbbcbcac"
        assertEquals(true, MasterW12AdvDP2D.isInterleave1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2x1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2D(s1, s2, s3))

        s1 = "aabcc"
        s2 = "dbbca"
        s3 = "aadbbbaccc"
        assertEquals(false, MasterW12AdvDP2D.isInterleave1D(s1, s2, s3))
        assertEquals(false, MasterW12AdvDP2D.isInterleave2x1D(s1, s2, s3))
        assertEquals(false, MasterW12AdvDP2D.isInterleave2D(s1, s2, s3))

        s1 = ""
        s2 = ""
        s3 = ""
        assertEquals(true, MasterW12AdvDP2D.isInterleave1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2x1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2D(s1, s2, s3))

        s1 = "a"
        s2 = ""
        s3 = "a"
        assertEquals(true, MasterW12AdvDP2D.isInterleave1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2x1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2D(s1, s2, s3))

        s1 = "aaa"
        s2 = "bbb"
        s3 = "aaaaaa"
        assertEquals(false, MasterW12AdvDP2D.isInterleave1D(s1, s2, s3))
        assertEquals(false, MasterW12AdvDP2D.isInterleave2x1D(s1, s2, s3))
        assertEquals(false, MasterW12AdvDP2D.isInterleave2D(s1, s2, s3))

        s1 = "aabaac"
        s2 = "aadaaeaaf"
        s3 = "aadaaeaabaafaac"
        assertEquals(true, MasterW12AdvDP2D.isInterleave1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2x1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2D(s1, s2, s3))

        s1 = ""
        s2 = "b"
        s3 = "b"
        assertEquals(true, MasterW12AdvDP2D.isInterleave1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2x1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2D(s1, s2, s3))

        s1 = "a"
        s2 = "b"
        s3 = "ab"
        assertEquals(true, MasterW12AdvDP2D.isInterleave1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2x1D(s1, s2, s3))
        assertEquals(true, MasterW12AdvDP2D.isInterleave2D(s1, s2, s3))

        s1 = "db"
        s2 = "b"
        s3 = "cbb"
        assertEquals(false, MasterW12AdvDP2D.isInterleave1D(s1, s2, s3))
        assertEquals(false, MasterW12AdvDP2D.isInterleave2x1D(s1, s2, s3))
        assertEquals(false, MasterW12AdvDP2D.isInterleave2D(s1, s2, s3))
    }

    // LC 72. Edit Distance
    @Test
    fun minDistanceTest() {
        var word1 = "horse"
        var word2 = "ros"
        assertEquals(3, MasterW12AdvDP2D.minDistance1D(word1, word2))
        assertEquals(3, MasterW12AdvDP2D.minDistance2x1D(word1, word2))
        assertEquals(3, MasterW12AdvDP2D.minDistance2D(word1, word2))


        word1 = "intention"
        word2 = "execution"
        assertEquals(5, MasterW12AdvDP2D.minDistance1D(word1, word2))
        assertEquals(5, MasterW12AdvDP2D.minDistance2x1D(word1, word2))
        assertEquals(5, MasterW12AdvDP2D.minDistance2D(word1, word2))

        word1 = "zoologicoarchaeologist"
        word2 = "zoogeologist"
        assertEquals(10, MasterW12AdvDP2D.minDistance1D(word1, word2))
        assertEquals(10, MasterW12AdvDP2D.minDistance2x1D(word1, word2))
        assertEquals(10, MasterW12AdvDP2D.minDistance2D(word1, word2))

        word1 = ""
        word2 = "a"
        assertEquals(1, MasterW12AdvDP2D.minDistance1D(word1, word2))
        assertEquals(1, MasterW12AdvDP2D.minDistance2x1D(word1, word2))
        assertEquals(1, MasterW12AdvDP2D.minDistance2D(word1, word2))

        word1 = "b"
        word2 = ""
        assertEquals(1, MasterW12AdvDP2D.minDistance1D(word1, word2))
        assertEquals(1, MasterW12AdvDP2D.minDistance2x1D(word1, word2))
        assertEquals(1, MasterW12AdvDP2D.minDistance2D(word1, word2))

        word1 = "sea"
        word2 = "eat"
        assertEquals(2, MasterW12AdvDP2D.minDistance1D(word1, word2))
        assertEquals(2, MasterW12AdvDP2D.minDistance2x1D(word1, word2))
        assertEquals(2, MasterW12AdvDP2D.minDistance2D(word1, word2))
    }

}