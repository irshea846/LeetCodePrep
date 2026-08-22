package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week3WindowsTest {

    // LeetCode 3. Longest Substring Without Repeating Characters
    @Test
    fun testLengthOfLongestSubstring_ValidCase1() {
        val s = "abcabcbb"
        val expectedResult = 3
        val actualResult = Week3Windows.lengthOfLongestSubstring(s)
        assertEquals(expectedResult, actualResult, "The length of the longest substring should be found")
    }

    @Test
    fun testLengthOfLongestSubstring_ValidCase2() {
        val s = "bbbbb"
        val expectedResult = 1
        val actualResult = Week3Windows.lengthOfLongestSubstring(s)
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
        val actualResult = Week3Windows.lengthOfLongestSubstring(s)
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
        val actualResult = Week3Windows.lengthOfLongestSubstring(s)
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
        val actualResult = Week3Windows.lengthOfLongestSubstring(s)
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
        val actualResult = Week3Windows.lengthOfLongestSubstring(s)
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
        val actualResult = Week3Windows.lengthOfLongestSubstring(s)
        assertEquals(
            expectedResult,
            actualResult,
            "The length of the longest substring should be found"
        )
    }

    // LeetCode 219. Contains Duplicate II
    @Test
    fun testContainsNearbyDuplicate_ValidCase1() {
        val nums = intArrayOf(1, 2, 3, 1)
        val k = 3
        val expectedResult = true
        val actualResult = Week3Windows.containsNearbyDuplicate(nums, k)
        assertEquals(expectedResult, actualResult, "Duplicates distance within k distance")
    }

    @Test
    fun testContainsNearbyDuplicate_ValidCase2() {
        val nums = intArrayOf(1, 0, 1, 1)
        val k = 1
        val expectedResult = true
        val actualResult = Week3Windows.containsNearbyDuplicate(nums, k)
        assertEquals(expectedResult, actualResult, "Duplicates distance within k distance")
    }

    @Test
    fun testContainsNearbyDuplicate_ValidCase3() {
        val nums = intArrayOf(1, 2, 3, 1, 2, 3)
        val k = 2
        val expectedResult = false
        val actualResult = Week3Windows.containsNearbyDuplicate(nums, k)
        assertEquals(expectedResult, actualResult, "No duplicates within k distance")
    }

    @Test
    fun testContainsNearbyDuplicate_ValidCase4() {
        val nums = intArrayOf(1, 2, 1)
        val k = 1
        val expectedResult = false
        val actualResult = Week3Windows.containsNearbyDuplicate(nums, k)
        assertEquals(expectedResult, actualResult, "No duplicates within k distance")
    }

    // LeetCode 121. Best Time to Buy and Sell Stock
    @Test
    fun testMaxProfit_ValidCase1() {
        val prices = intArrayOf(7, 1, 5, 3, 6, 4)
        val expectedResult = 5
        val actualResult = Week3Windows.maxProfit(prices)
        assertEquals(expectedResult, actualResult, "The maximum profit should be found")
    }

    @Test
    fun testMaxProfit_ValidCase2() {
        val prices = intArrayOf(7, 6, 4, 3, 1)
        val expectedResult = 0
        val actualResult = Week3Windows.maxProfit(prices)
        assertEquals(expectedResult, actualResult, "The maximum profit should be found")
    }

    @Test
    fun testMaxProfit_ValidCase3() {
        val prices = intArrayOf(2, 4, 1)
        val expectedResult = 2
        val actualResult = Week3Windows.maxProfit(prices)
        assertEquals(expectedResult, actualResult, "The maximum profit should be found")

    }

    @Test
    fun testMaxProfit_ValidCase4() {
        val prices = intArrayOf(2, 1, 4)
        val expectedResult = 3
        val actualResult = Week3Windows.maxProfit(prices)
        assertEquals(expectedResult, actualResult, "The maximum profit should be found")
    }

    // LeetCode 15. 3 Sum
    @Test
    fun testThreeSum_ValidCase1() {
        val nums = intArrayOf(-1, 0, 1, 2, -1, -4)
        val expectedResult = listOf(listOf(-1, -1, 2), listOf(-1, 0, 1))
        val actualResult = Week3Windows.threeSum(nums)
        assertEquals(expectedResult, actualResult, "The expected result should be found")
    }

    @Test
    fun testThreeSum_ValidCase2() {
        val nums = intArrayOf(0, 1, 1)
        val expectedResult = emptyList<List<Int>>()
        val actualResult = Week3Windows.threeSum(nums)
        assertEquals(expectedResult, actualResult, "The expected result should be empty")
    }

    @Test
    fun testThreeSum_ValidCase3() {
        val nums = intArrayOf(0, 0, 0)
        val expectedResult = listOf(listOf(0, 0, 0))
        val actualResult = Week3Windows.threeSum(nums)
        assertEquals(expectedResult, actualResult, "The expected result should be found")
    }

    @Test
    fun testThreeSum_ValidCase4() {
        val nums = intArrayOf(1,2,0,1,0,0,0,0)
        val expectedResult = listOf(listOf(0,0,0))
        val actualResult = Week3Windows.threeSum(nums)
        assertEquals(expectedResult, actualResult, "The expected result should be found")
    }

    @Test
    fun testThreeSum_ValidCase5() {
        val nums = intArrayOf(-2,0,0,2,2)
        val expectedResult = listOf(listOf(-2,0,2))
        val actualResult = Week3Windows.threeSum(nums)
        assertEquals(expectedResult, actualResult, "The expected result should be found")
    }

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