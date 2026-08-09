package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals
//import kotlin.test.Test
import kotlin.test.assertTrue

class Week1ArraysTest {

    @Test
    fun testProductExceptSelf_EvenLength() {
        val inputArray = intArrayOf(1, 2, 3, 4)
        val expectedResult = intArrayOf(24, 12, 8, 6)
        val actualResult = Week1Arrays.productExceptSelf(inputArray)
        assertEquals(
            actualResult.toList(),
            expectedResult.toList(),
            "Result must match"
        )
    }

    @Test
    fun testProductExceptSelf_OddLength() {
        val inputArray = intArrayOf(-1,1,0,-3,3)
        val expectedResult = intArrayOf(0,0,9,0,0)
        val actualResult = Week1Arrays.productExceptSelf(inputArray)
        assertEquals(
            actualResult.toList(),
            expectedResult.toList(),
            "Result must match"
        )
    }

    @Test
    fun testTopKFrequent_ValidInput() {
        val inputArray = intArrayOf(1,2,1,2,1,2,3,1,3,2)
        val k = 2
        val expectedResult = intArrayOf(1, 2)
        val actualResult = Week1Arrays.topKFrequent(inputArray, k)
        assertEquals(
            HashSet(expectedResult.toList()),
            HashSet(actualResult.toList()),
            "Result must match"
        )
    }

    @Test
    fun testTopKFrequent_SingleElement() {
        val inputArray = intArrayOf(1)
        val k = 1
        val expectedResult = intArrayOf(1)
        val actualResult = Week1Arrays.topKFrequent(inputArray, k)
        assertEquals(
            HashSet(expectedResult.toList()),
            HashSet(actualResult.toList()),
            "Result must match"
        )
    }

    @Test
    fun testGroupAnagrams_ValidAnagrams() {
        val inputStrings = arrayOf("eat", "tea", "tan", "ate", "nat", "bat")
        val expectedGroups = listOf(
            listOf("tan", "nat"),
            listOf("bat"),
            listOf("eat", "tea", "ate")
        )
        val actualResult = Week1Arrays.groupAnagrams(inputStrings)
        assertEquals(HashSet(actualResult),
            HashSet(expectedGroups), "Anagrams must match")
    }

    @Test
    fun testGroupAnagrams_NoAnagrams() {
        val inputStrings = arrayOf("apple", "banana", "cherry")
        val expectedGroups = listOf(
            listOf("apple"),
            listOf("banana"),
            listOf("cherry")
        )
        val actualResult = Week1Arrays.groupAnagrams(inputStrings)
        assertEquals(HashSet(actualResult)
            , HashSet(expectedGroups), "No anagrams must match")
    }

    @Test
    fun testGroupAnagrams_MoreAnagrams() {
        val inputStrings = arrayOf("eat","tea","tan","ate","nat","bat","ac","bd","aac","bbd","aacc","bbdd","acc","bdd")
        val expectedGroups = listOf(listOf("tan","nat"),listOf("bdd"),listOf("bd"),listOf("bbdd"),
            listOf("bat"),listOf("aac"),listOf("ac"),listOf("eat","tea","ate"),
            listOf("bbd"),listOf("acc"),listOf("aacc")
        )
        val actualResult = Week1Arrays.groupAnagrams(inputStrings)
        assertEquals(HashSet(actualResult),
            HashSet(expectedGroups), "Some anagrams must match")
    }

    @Test
    fun testLongestCommonPrefix_ValidPrefix() {
        val inputStrings = arrayOf("flower", "flow", "flight")
        val expectedPrefix = "fl"
        val actualResult = Week1Arrays.longestCommonPrefix(inputStrings)
        assertEquals(actualResult, expectedPrefix, "Prefix must match")
    }

    @Test
    fun testLongestCommonPrefix_NoPrefix() {
        val inputStrings = arrayOf("dog", "racecar", "car")
        val actualResult = Week1Arrays.longestCommonPrefix(inputStrings)
        assertTrue(actualResult.isEmpty(), "Prefix must be empty")
    }

    @Test
    fun testLongestCommonPrefix_SimilarPrefixString() {
        val inputStrings = arrayOf("acc", "aaa", "aaba")
        val expectedPrefix = "a"
        val actualResult = Week1Arrays.longestCommonPrefix(inputStrings)
        assertEquals(actualResult, expectedPrefix, "Prefix must match")
    }

    @Test
    fun testLongestCommonPrefix_MiddleElementMismatch() {
        val inputStrings = arrayOf("refactor", "abc", "reface")
        val expectedPrefix = ""
        val actualResult = Week1Arrays.longestCommonPrefix(inputStrings)
        assertEquals(expectedPrefix, actualResult, "Prefix should be empty because of 'abc'")
    }

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
