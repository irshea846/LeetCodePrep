package com.rshea.leetcodeprep

object Week8Backtracking {

    // Day 36 - LC 78. Subsets
    fun subsets(nums: IntArray): List<List<Int>> {
        // Highly Optimized Backtracking Approach
        // Time Complexity: O(N * 2^N) | Space Complexity: O(N)
        val result = mutableListOf<List<Int>>()

        fun backtrack(start: Int, currentPath: MutableList<Int>) {
            // 1. Add a copy of the current state to results
            result.add(ArrayList(currentPath))

            // 2. Explore further combinations
            for (i in start until nums.size) {
                currentPath.add(nums[i])      // Choose
                backtrack(i + 1, currentPath)  // Explore
                currentPath.removeAt(currentPath.lastIndex) // Un-choose (Backtrack)
            }
        }

        backtrack(0, mutableListOf())
        return result
    }

    fun subsetsOriginal(nums: IntArray): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        findCombinations(nums, 0, mutableListOf<Int>(), list)
        return list
    }

    fun findCombinations(nums: IntArray, i: Int, sublist: MutableList<Int>, list: MutableList<List<Int>>) {
        list.add(sublist.toList())
        for (j in i .. nums.lastIndex) {
            sublist.add(nums[j])
            findCombinations(nums, j + 1, sublist, list)
            sublist.removeAt(sublist.lastIndex)
        }
        return
    }


}