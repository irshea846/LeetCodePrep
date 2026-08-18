package com.rshea.leetcodeprep

object Week3Windows {

    // Day 13
    // LeetCode 680. Valid Palindrome II
    fun validPalindrome(s: String): Boolean {
        // 1. Greedy Shrink Window Strategy
        // Suggestions: Extracting the palindrome check into a helper function would significantly
        // simplify the nested conditional branching and variable tracking.
        var l = 0
        var r = s.lastIndex
        while (l < r) {
            if (s[l] != s[r]) {
                // Mismatch found! You have one chance to skip:
                // Path A: Skip s[l] and check the rest
                // Path B: Skip s[r] and check the rest
                return isPurePalindrome(s, l + 1, r) || isPurePalindrome(s, l, r - 1)
            }
            l++; r--
        }
        return true
        // Consider: If we were allowed to delete up to k characters, how would you change your
        // strategy to avoid the complexity growing exponentially?

        // 2. Two Pointers Recursive Strategy
        // Suggestions: Your recursive approach is elegant but consumes stack space. Using an
        // iterative while loop for the character checks would achieve constant auxiliary space.
        //return isValidPalindrome(s, 0, s.lastIndex, 0)
        // Consider: If the problem allowed deleting up to two characters, how would you modify your
        // recursive logic to prevent redundant subproblem calculations?
    }

    private fun isPurePalindrome(str: String, l: Int, r: Int): Boolean {
        var i = l
        var j = r
        while (i < j) {
            if (str[i++] != str[j--]) return false
        }
        return true
    }
    private fun isValidPalindrome(str: String, i: Int, j: Int, k: Int): Boolean {
        if (j <= i) return true

        if (str[i] != str[j]) {
            if (k > 0) return false
            return isValidPalindrome(str, i, j - 1, k + 1) ||
                    isValidPalindrome(str, i + 1, j, k + 1)
        } else {
            return isValidPalindrome(str, i + 1, j - 1, k)
        }
    }

    // LeetCode 125. Valid Palindrome
    fun isPalindrome(s: String): Boolean {
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
            if (!s[i++].equals(s[j--], true))
                return false
        }
        return true
    }
}