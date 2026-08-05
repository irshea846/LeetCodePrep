package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals
//import kotlin.test.Test
import kotlin.test.assertTrue

class Week1ArraysTest {

    @Test
    fun testTwoSum_ValidMatch() {
        val inputNumbers = intArrayOf(2, 7, 11, 15)
        val targetValue = 9
        val expectedIndices = intArrayOf(0, 1)

        val actualResult = Week1Arrays.twoSum(inputNumbers, targetValue)

        // Verifies the contents of the returned index positions match perfectly
        assertTrue(actualResult.contentEquals(expectedIndices),
            "Indices must match [0, 1]")
    }

    @Test
    fun testTwoSum_NoMatchFound() {
        val inputNumbers = intArrayOf(1, 2, 3)
        val targetValue = 10

        val actualResult = Week1Arrays.twoSum(inputNumbers, targetValue)

        assertTrue(actualResult.isEmpty(),
            "Array must return empty on invalid target configurations")
    }

    @Test
    fun testTwoSumPrimitive_ValidMatch() {
        val inputNumbers = intArrayOf(2, 7, 11, 15)
        val targetValue = 9
        val expectedIndices = intArrayOf(0, 1)

        val actualResult = Week1Arrays.twoSumPrimitive(inputNumbers, targetValue)

        // Verifies the contents of the returned index positions match perfectly
        assertTrue(actualResult.contentEquals(expectedIndices),
            "Indices must match [0, 1]")
    }

    @Test
    fun testTwoSumPrimitive_NoMatchFound() {
        val inputNumbers = intArrayOf(5, 10, 15)
        val targetValue = 100

        val actualResult = Week1Arrays.twoSumPrimitive(inputNumbers, targetValue)

        assertTrue(actualResult.isEmpty(),
            "Array must return empty on invalid target configurations")
    }

    @Test
    fun testIsAnagram_ValidAnagram() {
        val s = "anagram"
        val t = "nagaram"
        val expectedResult = true
        val actualResult = Week1Arrays.isAnagram(s, t)
        assertEquals(actualResult, expectedResult,
            "s and t are anagram!")
    }

    @Test
    fun testIsAnagram_NotAnagram() {
        val s = "anagram"
        val t = "nagarmm"
        val expectedResult = false
        val actualResult = Week1Arrays.isAnagram(s, t)
        assertEquals(actualResult, expectedResult,
            "s and t are not anagram!")
    }

    @Test
    fun testIsAnagram_DifferentLength() {
        val s = "aabbcc"
        val t = "abc"
        val expectedResult = false
        val actualResult = Week1Arrays.isAnagram(s, t)
        assertEquals(actualResult, expectedResult,
            "s and t are not the same length!")
    }


    @Test
    fun testContainsDuplicate_DuplicateFound() {
        val inputArray = intArrayOf(1, 2, 3, 4, 5, 2)
        val expectedResult = true
        val actualResult = Week1Arrays.containsDuplicate(inputArray)
        assertEquals(actualResult, expectedResult, "Array must contain duplicates")
    }

    @Test
    fun testContainsDuplicate_NoDuplicates() {
        val inputArray = intArrayOf(1, 2, 3, 4, 5)
        val expectedResult = false
        val actualResult = Week1Arrays.containsDuplicate(inputArray)

        assertEquals(actualResult, expectedResult, "Array must not contain duplicates")
    }

}