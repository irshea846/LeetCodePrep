package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week3WindowsTest {

    // LeetCode 11. Container With Most Water
    @Test
    fun testMaxArea_ValidCase1() {
        val height = intArrayOf(1, 8, 6, 2, 5, 4, 8, 3, 7)
        val expectedResult = 49
        val actualResult = Week3Windows.maxArea(height)
        assertEquals(expectedResult, actualResult, "The maximum area should be found")
    }

    @Test
    fun testMaxArea_ValidCase2() {
        val height = intArrayOf(1, 1)
        val expectedResult = 1
        val actualResult = Week3Windows.maxArea(height)
        assertEquals(expectedResult, actualResult, "The maximum area should be found")
    }

    @Test
    fun testMaxArea_ValidCase3() {
        val height = intArrayOf(4, 3, 2, 1, 4)
        val expectedResult = 16
        val actualResult = Week3Windows.maxArea(height)
        assertEquals(expectedResult, actualResult, "The maximum area should be found")
    }

    // LeetCode 167. Two Sum II - Input Array Is Sorted
    @Test
    fun testTwoSum_ValidCase1() {
        val numbers = intArrayOf(2, 7, 11, 15)
        val target = 9
        val expectedResult = intArrayOf(1, 2)
        val actualResult = Week3Windows.twoSum(numbers, target)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString(), "The pair should be found")
    }

    @Test
    fun testTwoSum_ValidCase2() {
        val numbers = intArrayOf(2, 3, 4)
        val target = 6
        val expectedResult = intArrayOf(1, 3)
        val actualResult = Week3Windows.twoSum(numbers, target)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString(), "The pair should be found")
    }

    @Test
    fun testTwoSum_ValidCase3() {
        val numbers = intArrayOf(-1, 0)
        val target = -1
        val expectedResult = intArrayOf(1, 2)
        val actualResult = Week3Windows.twoSum(numbers, target)
        assertEquals(expectedResult.contentToString(), actualResult.contentToString(), "The pair should be found")
    }

    // LeetCode 680. Valid Palindrome II
    @Test
    fun testValidPalindrome_ValidCase1() {
        val s = "aba"
        val expectedResult = true
        val actualResult = Week3Windows.validPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should be a Palindrome")
    }

    @Test
    fun testValidPalindrome_ValidCase2() {
        val s = "abca"
        val expectedResult = true
        val actualResult = Week3Windows.validPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should be a Palindrome")
    }

    @Test
    fun testValidPalindrome_ValidCase3() {
        val s = "mlcuppuculm"
        val expectedResult = true
        val actualResult = Week3Windows.validPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should be a Palindrome")
    }

    @Test
    fun testValidPalindrome_ValidCase4() {
        val s = "cbbcc"
        val expectedResult = true
        val actualResult = Week3Windows.validPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should be a Palindrome")
    }

    @Test
    fun testValidPalindrome_ValidCase5() {
        val s = "acxcybycxcxa"
        val expectedResult = true
        val actualResult = Week3Windows.validPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should be a Palindrome")
    }

    @Test
    fun testValidPalindrome_ValidCase6() {
        val s = "deeee"
        val expectedResult = true
        val actualResult = Week3Windows.validPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should be a Palindrome")
    }

    @Test
    fun testValidPalindrome_InvalidCase1() {
        val s = "abc"
        val expectedResult = false
        val actualResult = Week3Windows.validPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should not be a Palindrome")
    }

    @Test
    fun testValidPalindrome_InvalidCase2() {
        val s = "eddboebddcaacddkbebdde"
        val expectedResult = false
        val actualResult = Week3Windows.validPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should not be a Palindrome")
    }

    // LeetCode 125. Valid Palindrome
    @Test
    fun testIsPalindrome_ValidCase1() {
        val s = "A man, a plan, a canal: Panama"
        val expectedResult = true
        val actualResult = Week3Windows.isPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should be a palindrome")
    }

    @Test
    fun testIsPalindrome_ValidCase2() {
        val s = ".,"
        val expectedResult = true
        val actualResult = Week3Windows.isPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should be a palindrome")
    }

    @Test
    fun testIsPalindrome_InvalidCase() {
        val s = "race a car"
        val expectedResult = false
        val actualResult = Week3Windows.isPalindrome(s)
        assertEquals(expectedResult, actualResult, "s should not be a palindrome")
    }
}