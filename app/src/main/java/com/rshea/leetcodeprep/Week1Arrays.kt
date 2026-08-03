package com.rshea.leetcodeprep

object Week1Arrays {

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

    // LeetCode 1: Two Sum
    // Time Complexity: O(N) | Space Complexity: O(N)
    fun twoSum(nums: IntArray, target: Int): IntArray {
        val visited = HashMap<Int, Int>() // Format: Value -> Index

        for ((index, value) in nums.withIndex()) {
            val complement = target - value
            if (visited.containsKey(complement)) {
                return intArrayOf(visited[complement]!!, index)
            }
            visited[value] = index
        }
        return intArrayOf()
    }

}