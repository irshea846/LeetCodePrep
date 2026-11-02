package com.rshea.leetcodeprep.masteryreviewday

object MasterW12AdvDP2D {

    // LC 97. Interleaving String
    // Approach 1: Bottom-Up Dynamic Programming (1D Table - Staff Level Optimal)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
    fun isInterleave1D(str1: String, str2: String, s3: String): Boolean {
        if (s3.length != str1.length + str2.length) return false
        if (str1.isEmpty()) return str2 == s3
        if (str2.isEmpty()) return str1 == s3
        val (s1, s2) = if (str1.length >= str2.length) str1 to str2 else str2 to str1
        val m = s1.length + 1
        val n = s2.length + 1
        val dp = BooleanArray(n)
        dp[0] = true

        for (j in 1 until n) {
            if (s2[j - 1] == s3[j - 1]) {
                dp[j] = true
            } else {
                break
            }
        }

        var idx: Int
        var anyTrue: Boolean
        for (i in 1 until m) {
            dp[0] = dp[0] && s1[i - 1] == s3[i - 1]
            anyTrue = dp[0]
            for (j in 1 until n) {
                idx = i + j - 1
                dp[j] = (dp[j - 1] && (s2[j - 1] == s3[idx])) || (dp[j] && s1[i - 1] == s3[idx])
                if (dp[j]) anyTrue = true
            }
            if (!anyTrue) return false
        }

        return dp[n - 1]
    }

    // Approach 2: Bottom-Up Dynamic Programming (2x 1D Table)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
    fun isInterleave2x1D(str1: String, str2: String, s3: String): Boolean {
        if (s3.length != str1.length + str2.length) return false
        if (str1.isEmpty()) return str2 == s3
        if (str2.isEmpty()) return str1 == s3
        val (s1, s2) = if (str1.length >= str2.length) str1 to str2 else str2 to str1
        val m = s1.length + 1
        val n = s2.length + 1
        var prev = BooleanArray(n)
        var curr = BooleanArray(n)
        prev[0] = true

        for (j in 1 until n) {
            if (s2[j - 1] == s3[j - 1]) {
                prev[j] = true
            } else {
                break
            }
        }

        var idx: Int
        for (i in 1 until m) {
            curr[0] = prev[0] && s1[i - 1] == s3[i - 1]
            for (j in 1 until n) {
                idx = i + j - 1
                curr[j] = (curr[j - 1] && (s2[j - 1] == s3[idx])) || (prev[j] && s1[i - 1] == s3[idx])
            }
            val temp = prev
            prev = curr
            curr = temp
        }

        return prev[n - 1]
    }

    // Approach 3: Bottom-Up Dynamic Programming (2D Table)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(m * n)
    fun isInterleave2D(s1: String, s2: String, s3: String): Boolean {
        if (s3.length != s1.length + s2.length) return false
        val m = s1.length + 1
        val n = s2.length + 1
        val dp = Array(m) { BooleanArray(n) }
        dp[0][0] = true

        for (i in 1 until m) {
            if (s1[i - 1] == s3[i - 1]) {
                dp[i][0] = true
            } else {
                break
            }
        }

        for (j in 1 until n) {
            if (s2[j - 1] == s3[j - 1]) {
                dp[0][j] = true
            } else {
                break
            }
        }

        var idx: Int
        for (i in 1 until m) {
            for (j in 1 until n) {
                idx = i + j - 1
                dp[i][j] = (dp[i][j - 1] && (s2[j - 1] == s3[idx])) || (dp[i - 1][j] && s1[i - 1] == s3[idx])
            }
        }

        return dp[m - 1][n - 1]
    }


    // LC 72. Edit Distance
    // Approach 1: Bottom-Up Dynamic Programming (1D Table - Staff Level Optimal)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
    fun minDistance1D(word1: String, word2: String): Int {
        val (s1, s2) = if (word1.length >= word2.length) word1 to word2 else word2 to word1
        val m = s1.length + 1
        val n = s2.length + 1
        val dp = IntArray(n) { it }

        for (i in 1 until m) {
            dp[0] = i
            var diag = i - 1
            for (j in 1 until n) {
                val temp = dp[j]
                if (s1[i - 1] == s2[j - 1]) {
                    dp[j] = diag
                } else {
                    dp[j] = 1 + minOf(dp[j], diag, dp[j - 1])
                }
                diag = temp
            }
        }
        return dp[n - 1]
    }

    // Approach 2: Bottom-Up Dynamic Programming (2 1D Arrays)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(min(m, n))
    fun minDistance2x1D(word1: String, word2: String): Int {
        val (s1, s2) = if (word1.length >= word2.length) word1 to word2 else word2 to word1
        val m = s1.length + 1
        val n = s2.length + 1
        var prev = IntArray(n) { it }
        var curr = IntArray(n)

        for (i in 1 until m) {
            curr[0] = i
            for (j in 1 until n) {
                if (s1[i - 1] == s2[j - 1]) {
                    curr[j] = prev[j - 1]
                } else {
                    curr[j] = 1 + minOf(prev[j], prev[j - 1], curr[j - 1])
                }
            }
            val temp = curr
            curr = prev
            prev = temp
        }
        return prev[n - 1]
    }

    // Approach 3: Bottom-Up Dynamic Programming (2D Table)
    // Time Complexity: O(m * n) | Auxiliary Space Complexity: O(m * n)
    fun minDistance2D(word1: String, word2: String): Int {
        val m = word1.length + 1
        val n = word2.length + 1
        val dp = Array(m) { IntArray(n) }
        for (i in 1 until m) dp[i][0] = i
        for (j in 1 until n) dp[0][j] = j

        for (i in 1 until m) {
            for (j in 1 until n) {
                if (word1[i - 1] == word2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1]
                } else {
                    dp[i][j] = 1 + minOf(dp[i - 1][j], dp[i - 1][j - 1], dp[i][j - 1])
                }
            }
        }
        return dp[m - 1][n - 1]
    }

}