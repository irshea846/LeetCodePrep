package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week2PointersTest {

    @Test
    fun testLengthOfLongestSubstring_ValidCase1() {
        val s = "abcabcbb"
        val expectedResult = 3
        val actualResult = Week2Pointers.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase2() {
        val s = "bbbbb"
        val expectedResult = 1
        val actualResult = Week2Pointers.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase3() {
        val s = "pwwkew"
        val expectedResult = 3
        val actualResult = Week2Pointers.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase4() {
        val s = "ccbbcc"
        val expectedResult = 2
        val actualResult = Week2Pointers.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase5() {
        val s = "ff"
        val expectedResult = 1
        val actualResult = Week2Pointers.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase6() {
        val s = "a"
        val expectedResult = 1
        val actualResult = Week2Pointers.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testLengthOfLongestSubstring_InvalidCase() {
        val s = "dvdf"
        val expectedResult = 3
        val actualResult = Week2Pointers.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testMaxProfit_ValidCase() {
        val prices = intArrayOf(7, 1, 5, 3, 6, 4)
        val expectedResult = 5
        val actualResult = Week2Pointers.maxProfit(prices)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testMaxProfit_InvalidCase() {
        val prices = intArrayOf(7, 6, 4, 3, 1)
        val expectedResult = 0
        val actualResult = Week2Pointers.maxProfit(prices)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testMaxProfit_EdgeCase1() {
        val prices = intArrayOf(2,4,1)
        val expectedResult = 2
        val actualResult = Week2Pointers.maxProfit(prices)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testMaxProfit_EdgeCase2() {
        val prices = intArrayOf(2,1,4)
        val expectedResult = 3
        val actualResult = Week2Pointers.maxProfit(prices)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testMaxArea_ValidCase() {
        val height = intArrayOf(1,8,6,2,5,4,8,3,7)
        val expectedResult = 49
        val actualResult = Week2Pointers.maxArea(height)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testMaxArea_OnlyOneGapCase() {
        val height = intArrayOf(1, 1)
        val expectedResult = 1
        val actualResult = Week2Pointers.maxArea(height)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testThreeSum_ValidCase() {
        val numbers = intArrayOf(-1, 0, 1, 2, -1, -4)
        val expectedResult = listOf(listOf(-1, -1, 2), listOf(-1, 0, 1))
        val actualResult = Week2Pointers.threeSum(numbers)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testThreeSum_InvalidCase() {
        val numbers = intArrayOf(0, 1, 1)
        val expectedResult = emptyList<List<Int>>()
        val actualResult = Week2Pointers.threeSum(numbers)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testThreeSum_AllZerosCase() {
        val numbers = intArrayOf(0, 0, 0)
        val expectedResult = listOf(listOf(0, 0, 0))
        val actualResult = Week2Pointers.threeSum(numbers)
        assertEquals(expectedResult, actualResult, "Result must be match")
    }

    @Test
    fun testThreeSum_EdgeCase() {
        val numbers = intArrayOf(-2, 0, 0, 2, 2)
        val expectedResult = listOf(listOf(-2, 0, 2))
        val actualResult = Week2Pointers.threeSum(numbers)
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

}