package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week2PointersTest {

    @Test
    fun testIsPalindrome_IsPalindromeCase() {
        val s = "A man, a plan, a canal: Panama"
        val expectedResult = true
        val actualResult = Week2Pointers.isPalindrome(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testIsPalindrome_IsNotPalindromeCase() {
        val s = "A 1 man, a p6lan, a canal: P6anam1a"
        val expectedResult = false
        val actualResult = Week2Pointers.isPalindrome(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testIsPalindrome_InvalidCase() {
        val s = "race a car"
        val expectedResult = false
        val actualResult = Week2Pointers.isPalindrome(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testIsPalindrome_NonAlphanumericCase() {
        val s = ".,"
        val expectedResult = true
        val actualResult = Week2Pointers.isPalindrome(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testIsPalindrome_OnlySpacesCase() {
        val s = "        "
        val expectedResult = true
        val actualResult = Week2Pointers.isPalindrome(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testTwoSumSorted_ValidCase1() {
        val numbers = intArrayOf(2, 7, 11, 15)
        val target = 9
        val expectedResult = intArrayOf(1, 2)
        val actualResult = Week2Pointers.twoSumSorted(numbers, target)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString(), "Result must be match")
    }

    @Test
    fun testTwoSumSorted_ValidCase2() {
        val numbers = intArrayOf(2, 3, 4)
        val target = 6
        val expectedResult = intArrayOf(1, 3)
        val actualResult = Week2Pointers.twoSumSorted(numbers, target)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString(), "Result must be match")
    }

    @Test
    fun testTwoSumSorted_ValidCase3() {
        val numbers = intArrayOf(-1, 0)
        val target = -1
        val expectedResult = intArrayOf(1, 2)
        val actualResult = Week2Pointers.twoSumSorted(numbers, target)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString(), "Result must be match")
    }
}