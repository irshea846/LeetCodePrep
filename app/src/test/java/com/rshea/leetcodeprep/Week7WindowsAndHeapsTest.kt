package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week7WindowsAndHeapsTest {

    // Day 33 - LC 3. Longest Substring Without Repeating Characters
    @Test
    fun testLengthOfLongestSubstring_ValidCase1() {
        val s = "abcabcbb"
        val expectedResult = 3
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "The length of the longest substring should be found")
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase2() {
        val s = "bbbbb"
        val expectedResult = 1
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase3() {
        val s = "pwwkew"
        val expectedResult = 3
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase4() {
        val s = "STUG"
        val expectedResult = 4
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase5() {
        val s = "ccbbcc"
        val expectedResult = 2
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase6() {
        val s = "bccddadcb"
        val expectedResult = 4
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase7() {
        val s = "Mi~"
        val expectedResult = 3
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase8() {
        val s = "ff"
        val expectedResult = 1
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase9() {
        val s = "eaa"
        val expectedResult = 2
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase10() {
        val s = "edd"
        val expectedResult = 2
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase11() {
        val s = "1R1T7"
        val expectedResult = 4
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase12() {
        val s = "a"
        val expectedResult = 1
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase13() {
        val s = ""
        val expectedResult = 0
        val actualResult = Week7WindowsAndHeaps.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }
}