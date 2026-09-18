package com.rshea.leetcodeprep

import kotlin.math.max

object Week7WindowsAndHeaps {

    // Day 33 - LC 3. Longest Substring Without Repeating Characters
    fun lengthOfLongestSubstring(s: String): Int {
        val n = 128 // 256 for Extended ASCII
        val ascii = IntArray(n) { -1 }
        var maxLen = 0
        var left = 0

        for (right in s.indices) {
            val charCode = s[right].code

            // If character was seen within the current window, "jump" the left pointer
            if (ascii[charCode] >= left) {
                maxLen = max(maxLen, right - left)
                left = ascii[charCode] + 1
            }
            ascii[charCode] = right
        }

        // Mandatory final check: handles cases where the longest substring
        // is at the very end of the input (e.g., "abc")
        return max(maxLen, s.length - left)
    }
}