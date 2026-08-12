package com.rshea.leetcodeprep

object Week2Pointers {

    // Day 9
    // LeetCode 15. Three Sum
    fun threeSum(nums: IntArray): List<List<Int>> {
        // 1. Sort + Two-Pointer Approach (Manual Deduplication)
        // Time Complexity: O(N^2) | Space Complexity: O(Log(N))
        nums.sort()
        val triplets = mutableListOf<List<Int>>()
        for (i in nums.indices) {
            // Optimization: If the smallest number is > 0, sum can't be 0
            if (nums[i] > 0) break

            // Skip duplicate anchor elements
            if (i == 0 || nums[i] != nums[i - 1]) {
                val target = -nums[i]
                var left = i + 1
                var right = nums.lastIndex
                while (left < right) {
                    val sum = nums[left] + nums[right]
                    when {
                        sum == target -> {
                            triplets.add(listOf(nums[i], nums[left++], nums[right--]))
                            // Skip duplicates for the second element
                            while (left < right && nums[left] == nums[left - 1]) left++
                            while (left < right && nums[right] == nums[right + 1]) right--
                        }

                        sum < target -> left++
                        else -> right--
                    }
                }
            }
        }
        return triplets

        // 2. Sort + Two-Pointer Approach (Deduplication via Set)
        // Time: O(N^2) | Space: O(N) for the Set
        //        nums.sort()
        //        val triplets = mutableSetOf<List<Int>>()
        //        for (i in nums.indices) {
        //            if (nums[i] > 0) break
        //            val target = -nums[i]
        //            var left = i + 1
        //            var right = nums.lastIndex
        //            while (left < right) {
        //                val sum = nums[left] + nums[right]
        //                when {
        //                    sum == target -> triplets.add(listOf(nums[i], nums[left++], nums[right--]))
        //                    sum < target -> left++
        //                    else -> right--
        //                }
        //            }
        //        }
        //        return triplets.toList()

        // 3. Sort + Two-Pointer Approach (Post-Generation .distinct())
        // Time: O(N^2) | Space: O(N) for triplets list
        // Note: Least efficient due to overhead of collecting duplicates and distinct() call.
        //        nums.sort()
        //        val triplets = mutableListOf<List<Int>>()
        //        for (i in nums.indices) {
        //            if (nums[i] > 0) break
        //            val target = -nums[i]
        //            var left = i + 1
        //            var right = nums.lastIndex
        //            while (left < right) {
        //                val sum = nums[left] + nums[right]
        //                when {
        //                    sum == target -> triplets.add(listOf(nums[i], nums[left++], nums[right--]))
        //                    sum < target -> left++
        //                    else -> right--
        //                }
        //            }
        //        }
        //        return triplets.distinct()

    }

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