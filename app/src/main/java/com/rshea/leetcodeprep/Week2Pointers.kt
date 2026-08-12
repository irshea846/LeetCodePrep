package com.rshea.leetcodeprep

object Week2Pointers {

    // Day 8
    // LeetCode 125. Valid Palindrome
    fun isPalindrome(s: String): Boolean {
        // 1. Optimized Two-Pointer In-Place Approach (Most Efficient)
        // Time Complexity: O(N) | Space Complexity: O(1)
        // Using if/continue provides a flatter control flow that is often
        // faster on the JVM due to better JIT optimization and branch prediction.
        var i = 0
        var j = s.lastIndex
        while (i < j) {
            if (!s[i].isLetterOrDigit()) {
                i++
                continue
            }
            if (!s[j].isLetterOrDigit()) {
                j--
                continue
            }
            if (!s[i++].equals(s[j--], ignoreCase = true)) return false
        }
        return true

        // 1. Two Pointers In-Place Approach for LeetCode
        //        var i = 0
        //        var j = s.lastIndex
        //        while (i < j) {
        //            while (i < j && !s[i].isLetterOrDigit()) i++
        //            while (i < j && !s[j].isLetterOrDigit()) j--
        //            if (!s[i++].equals(s[j--], ignoreCase = true)) return false
        //        }
        //        return true

        // 1. Two Pointers In-Place Approach for Team Project
        //        var i = 0
        //        var j = s.lastIndex
        //        while (i < j) {
        //            when {
        //                !s[i].isLetterOrDigit() -> i++
        //                !s[j].isLetterOrDigit() -> j--
        //                else -> {
        //                    if (!s[i++].equals(s[j--], ignoreCase = true)) return false
        //                }
        //            }
        //        }
        //        return true

        // 2. Filtering + Two Pointers Approach
        //        val str = s.filter { it.isLetterOrDigit() }
        //        val lastIdx = str.lastIndex
        //        for (i in 0 until str.length / 2) {
        //            if (str[i].lowercase() != str[lastIdx - i].lowercase()) {
        //                return false
        //            }
        //        }
        //        return true

        // 3. Filtering + Reverse and Compare Approach
        //        val str = s.filter { it.isLetterOrDigit() }.lowercase()
        //        return str == str.reversed()
    }

    // LeetCode 167. Two Sum II - Sorted Array
    fun twoSumSorted(numbers: IntArray, target: Int): IntArray {
        // 1. Two-Pointer In-Place Approach
        // Time Complexity: O(N) | Space Complexity: O(1)
        //        var i = 0
        //        var j = numbers.lastIndex
        //        while (i < j) {
        //            val complement = target - numbers[i]
        //            when {
        //                numbers[j] == complement -> return intArrayOf(i + 1, j + 1)
        //                numbers[j] > complement -> j--
        //                else -> i++
        //            }
        //        }
        //        return intArrayOf()

        // 2. One Pointer + Binary Search Approach
        // Time Complexity: O(N * Log(N)) | Space Complexity: O(1)
        for (i in numbers.indices) {
            val idx = numbers.binarySearch(target - numbers[i], i + 1, numbers.size)
            if (idx >= 0) { // Instead of if (idx > i),
                return intArrayOf(i + 1, idx + 1)
            }
        }
        return intArrayOf()
    }
}