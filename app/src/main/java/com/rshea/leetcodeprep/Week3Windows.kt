package com.rshea.leetcodeprep

import kotlin.math.max
import kotlin.math.min

object Week3Windows {

    // Day 16
    // LeetCode 219. Contains Duplicate II
    fun containsNearbyDuplicate(nums: IntArray, k: Int): Boolean {
        // 1. HashSet Sliding Window Approach
        // Time Complexity: O(N) | Space Complexity: O(Min(n,k))
        val set = HashSet<Int>()
        for (i in nums.indices) {
            if (!set.add(nums[i])) return true
            if (i >= k) set.remove(nums[i - k])
        }
        return false


        // 2. Hash Map Approach
        // Time Complexity: O(N) | Space Complexity: O(N)
        //        val map = HashMap<Int, Int>()
        //        for (i in nums.indices) {
        //            if (map[nums[i]] != null) {
        //                val pos = map[nums[i]]!!
        //                if (i - pos <= k) return true
        //            }
        //            map[nums[i]] = i
        //        }
        //        return false
    }


    // Day 16
    // LeetCode 121. Best Time to Buy and Sell Stock
    fun maxProfit(prices: IntArray): Int {
        // Greedy Approach
        // Time Complexity: O(N) | Space Complexity: O(1)
        if (prices.size < 2) return 0

        var curHighestPrice = prices[prices.lastIndex]
        var maxProfit = 0

        for (i in prices.lastIndex - 1 downTo 0) {
            if (prices[i] > curHighestPrice) {
                curHighestPrice = prices[i]
            } else {
                val profit = curHighestPrice - prices[i]
                if (profit > maxProfit) maxProfit = profit
            }
        }
        return maxProfit
    }

    // Day 15
    // LeetCode 15. 3 Sum
    fun threeSum(nums: IntArray): List<List<Int>> {
        // Highly Optimized Triple-Greedy Squeeze (Manual Deduplication)
        // Time Complexity: O(N^2) | Space Complexity: O(1) (excluding output)
        //        nums.sort()
        //        val list = mutableListOf<List<Int>>()
        //
        //        for (i in 0 until nums.size - 2) {
        //            // Optimization: if anchor is positive, no triplet can sum to 0
        //            if (nums[i] > 0) break
        //
        //            // Skip duplicate anchor elements
        //            if (i > 0 && nums[i] == nums[i - 1]) continue
        //
        //            val target = -nums[i]
        //            var left = i + 1
        //            var right = nums.lastIndex
        //
        //            while (left < right) {
        //                val sum = nums[left] + nums[right]
        //                when {
        //                    sum < target -> left++
        //                    sum > target -> right--
        //                    else -> {
        //                        list.add(listOf(nums[i], nums[left++], nums[right--]))
        //                        // Triple-Skip: Avoid re-processing the same values for left/right
        //                        while (left < right && nums[left] == nums[left - 1]) left++
        //                        while (left < right && nums[right] == nums[right + 1]) right--
        //                    }
        //                }
        //            }
        //        }
        //        return list

        // Sort + Triple Greedy Approach
        // Time Complexity: O(N^2) | Space Complexity: O(Log(N))
        nums.sort()
        val list = mutableListOf<List<Int>>()
        var i = 0; var j: Int; var k = nums.lastIndex
        while (i < k && nums[i] <= 0) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                i++
                continue
            }
            val sum = 0 - nums[i]
            j = i + 1
            k = nums.lastIndex
            while (j < k) {
                val complement = sum - nums[j]
                when {
                    complement > nums[k] -> j++
                    complement < nums[k] -> k--
                    else -> {
                        list.add(listOf(nums[i], nums[j], nums[k]))
                        j++; k--
                        while (j < k && nums[j] == nums[j - 1]) j++
                        while (j < k && nums[k] == nums[k + 1]) k--
                    }
                }
            }
            i++
        }
        return list
    }

    // Day 14
    // LeetCode 11. Container With Most Water
    fun maxArea(height: IntArray): Int {
        // Highly Optimized Greedy Two-Pointer (Merged Logic)
        // Time Complexity: O(N) | Space Complexity: O(1)
        var i = 0
        var j = height.lastIndex
        var mostWater = 0

        while (i < j) {
            val width = j - i

            // Combine height comparison with area calculation to minimize CPU jumps
            if (height[i] < height[j]) {
                val area = height[i] * width
                if (area > mostWater) mostWater = area
                i++
            } else if (height[i] > height[j]) {
                val area = height[j] * width
                if (area > mostWater) mostWater = area
                j--
            } else {
                // "Equal Heights" optimization: move both pointers as neither can
                // contribute to a larger container given the decreasing width.
                val area = height[i] * width
                if (area > mostWater) mostWater = area
                i++; j--
            }
        }
        return mostWater

        // Greedy Two-Pointer Approach
        //        var i = 0
        //        var j = height.lastIndex
        //        var mostWater = 0
        //
        //        while (i < j) {
        //            val minHeight = minOf(height[i], height[j])
        //            val currWater = (j - i) * minHeight
        //            if (currWater > mostWater) mostWater = currWater
        //            when {
        //                height[i] > height[j] -> j--
        //                height[i] < height[j] -> i++
        //                else -> {
        //                    i++; j--
        //                }
        //            }
        //        }
        //        return mostWater
    }


    // Day 14
    // LeetCode 167. Two Sum II - Input Array Is Sorted
    fun twoSum(numbers: IntArray, target: Int): IntArray {
        // 1. Two Pointers Approach
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

        // 2. Binary Search Approach
        // Time Complexity: O(N * Log (N)) | Space Complexity: O(1)
        // The Nuance: Kotlin's built-in IntArray.binarySearch(element, fromIndex, toIndex) accepts
        // an optional toIndex parameter, which defaults to the full size of the array (numbers.size).
        // By omitting it, your code automatically scans all the way to the end of the array, which
        // is perfectly correct.
        // The Optimization Edge: Since your for loop ranges until numbers.lastIndex, your search
        // path is already safely bounded. If you ever scale this pattern to a sliding sub-window or
        // a partitioned chunk inside a concurrent data pipeline, explicitly passing the exclusive
        // upper boundary parameter toIndex = numbers.size ensures your code bounds remain completely
        // clear to anyone reading the source file.
        for (i in 0 until numbers.lastIndex) {
            val complement = target - numbers[i]
            val j = numbers.binarySearch(complement, fromIndex = i + 1, numbers.size)
            if (j >= 0) return intArrayOf(i + 1, j + 1)
        }
        return intArrayOf()
    }

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
        // return isValidPalindrome(s, 0, s.lastIndex, 0)
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