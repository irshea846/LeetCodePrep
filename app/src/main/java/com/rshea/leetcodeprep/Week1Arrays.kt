package com.rshea.leetcodeprep

import androidx.collection.mutableIntIntMapOf

object Week1Arrays {

    // Day 4
    // LeetCode 14: Longest Common Prefix
    // Time Complexity: O(N) | Space Complexity: O(N)
    fun longestCommonPrefix(strs: Array<String>): String {
        if (strs.isEmpty()) return ""

        // 1. Sort and compare the first and last string.
        // val sortedList = strs.sorted()
        // return sortedList.first().commonPrefixWith(sortedList.last())

        // 2. Horizontal Linear Scan
        // For loop solution -
        // var commonPrefix = strs[0]
        // for (i in 1 until strs.size) {
        //    commonPrefix = commonPrefix.commonPrefixWith(strs[i])
        //    if (commonPrefix.isEmpty()) return ""
        // }
        // return commonPrefix
        // Idiomatic function solution -
        return strs.reduceOrNull { prefix, s ->
            if (prefix.isEmpty()) return ""
            prefix.commonPrefixWith(s)
        } ?: ""
    }

    // Day 3
    // LeetCode 1: Two Sum
    // Time Complexity: O(N) | Space Complexity: O(N)
    fun twoSum(nums: IntArray, target: Int): IntArray {
        val visited = HashMap<Int, Int>() // Format: Value -> Index

        for ((index, value) in nums.withIndex()) {
            val complementIndex = visited[target - value]
            if (complementIndex != null) {
                return intArrayOf(complementIndex, index)
            }
            visited[value] = index
        }
        return intArrayOf()
    }

    fun twoSumPrimitive(nums: IntArray, target: Int): IntArray {
        val visited = mutableIntIntMapOf() // Format: Value -> Index
        for ((index, value) in nums.withIndex()) {
            val complement = target - value
            if (visited.containsKey(complement)) {
                return intArrayOf(visited[complement], index)
            }
            visited[value] = index
        }
        return intArrayOf()
    }

    // Day 2
    // LeetCode 242: Valid Anagram
    // Time Complexity: O(N) | Space Complexity: O(1)
    fun isAnagram(s: String, t: String): Boolean {
        if (s.length != t.length) return false

        // 1. Sort both of the strings and compare each element
        return s.toCharArray().apply { sort() }.concatToString() ==
                t.toCharArray().apply { sort() }.concatToString()

        // 2. Grouping: when considering the Unicode case
        //return s.groupingBy { it }.eachCount() == t.groupingBy { it }.eachCount()

        // 3. Counter: Only if memory is limited to 'a'..'z' alphabets
        //val letterCnt = IntArray(26)
        //for (i in s.indices) {
        //    letterCnt[s[i] - 'a']++
        //    letterCnt[t[i] - 'a']--
        //}
        //return letterCnt.all { it == 0 }

        // 4. HashMap: Use `HashMap[s[i]] = HashMap.getOrDefault(s[i], 0) + 1` to increase the letter count
        //val map = HashMap<Char, Int>()
        //for (i in s.indices) {
        //    map[s[i]] = map.getOrDefault(s[i], 0) + 1
        //    map[t[i]] = map.getOrDefault(t[i], 0) - 1
        //}
        //return map.all { it.value == 0 }
    }

    // Day 1
    // LeetCode 217: Contains Duplicate
    // Time Complexity: O(N) | Space Complexity: O(N)
    fun containsDuplicate(nums: IntArray): Boolean {
        val visited = HashSet<Int>()
        for (num in nums) {
            if (!visited.add(num)) {
                return true
            }
        }
        return false
    }

}