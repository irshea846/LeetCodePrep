package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week3WindowsTest {

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