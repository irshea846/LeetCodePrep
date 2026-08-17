package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW2PointersTest {

    @Test
    fun testFindLongestSubstringNoRepeatingChars_ValidCase1() {
        val s = "abcabcbb"
        val expectedResult = 3
        val actualResult = MasterW2Pointers.findLongestSubstringNoRepeatingChars(s)
        assertEquals(expectedResult, actualResult, "Should find length 3 for 'abcabcbb'")
    }

    @Test
    fun testFindLongestSubstringNoRepeatingChars_AllUnique() {
        val s = "abcdef"
        val expectedResult = 6
        val actualResult = MasterW2Pointers.findLongestSubstringNoRepeatingChars(s)
        assertEquals(expectedResult, actualResult, "Should find length 6 for 'abcdef'")
    }

    @Test
    fun testFindLongestSubstringNoRepeatingChars_Empty() {
        val s = ""
        val expectedResult = 0
        val actualResult = MasterW2Pointers.findLongestSubstringNoRepeatingChars(s)
        assertEquals(expectedResult, actualResult, "Should return 0 for empty string")
    }
}