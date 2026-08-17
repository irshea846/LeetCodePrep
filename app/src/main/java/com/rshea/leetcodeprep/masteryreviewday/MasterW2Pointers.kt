package com.rshea.leetcodeprep.masteryreviewday

import kotlin.math.max

object MasterW2Pointers {

    // LC 3 Longest Substring Without Repeating Characters

    fun findLongestSubstringNoRepeatingChars(s: String): Int {
        val seen = IntArray(126) { -1 }
        var left  = 0
        var maxLen = 0

        for (right in 0 until s.length) {
            val c = s[right]
            val idx = seen[c.code]
            if (idx != -1 && idx >= left && idx < right) {
                left = idx + 1
            }
            maxLen = max(maxLen, right - left + 1)
            seen[c.code] = right
        }
        return maxLen
    }

}