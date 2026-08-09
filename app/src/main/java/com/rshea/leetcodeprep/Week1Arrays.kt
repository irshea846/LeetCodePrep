package com.rshea.leetcodeprep

import androidx.collection.mutableIntIntMapOf

object Week1Arrays {

    // Day 7
    // LeetCode 238: Product of Array Except Self
    fun productExceptSelf(nums: IntArray): IntArray {
        // 1. Three Loops
        //        val rProd = IntArray(nums.size)
        //        val lProd = IntArray(nums.size)
        //        nums.size - 1
        //        lProd[0] = nums[0]
        //        for (i in 1 until nums.size) {
        //            lProd[i] = lProd[i - 1] * nums[i]
        //        }
        //        rProd[nums.size - 1] = nums[nums.size - 1]
        //        for (i in nums.size - 2 downTo 0) {
        //            rProd[i] = rProd[i + 1] * nums[i]
        //        }
        //
        //        val output = IntArray(nums.size)
        //        output[0] = rProd[1]
        //        output[nums.size - 1] = lProd[nums.size - 2]
        //        for (i in 1 until nums.size - 1) {
        //            output[i] = lProd[i - 1] * rProd[i + 1]
        //        }
        //        return output

        // 2. Two Loops and One Array for Result.
        val size = nums.size

        // Allocate the distinct output container requested by the problem
        val output = IntArray(size)

        // Forward Pass: Accumulate left-side prefix products directly into output
        output[0] = 1
        for (i in 1 until size) {
            output[i] = output[i - 1] * nums[i - 1]
        }

        // Backward Pass: Accumulate right-side suffix products inline via a primitive tracker
        var rightSuffixProduct = 1
        for (i in size - 1 downTo 0) {
            output[i] = output[i] * rightSuffixProduct // Combines prefix and suffix
            rightSuffixProduct *= nums[i] // Updates running product for the next leftward step
        }
        return output


        // 3. Two Pointers in One Pass
        //        val rProd = IntArray(nums.size)
        //        val lProd = IntArray(nums.size)
        //        var l = 0
        //        var r = nums.size - 1
        //        lProd[l] = nums[l]
        //        rProd[r] = nums[r]
        //        while (++l < --r) {
        //            lProd[l] = lProd[l - 1] * nums[l]
        //            rProd[r] = rProd[r + 1] * nums[r]
        //        }
        //
        //        while (l < nums.size - 1 && r > 0) {
        //            lProd[l] = lProd[l - 1] * nums[l]
        //            rProd[r] = rProd[r + 1] * nums[r]
        //            if (l == r) {
        //                nums[l] = lProd[l - 1] * rProd[r + 1]
        //            } else {
        //                nums[l] = rProd[l + 1] * lProd[l - 1]
        //                nums[r] = lProd[r - 1] * rProd[r + 1]
        //            }
        //            l++; r--
        //        }
        //        nums[r] = rProd[r + 1]
        //        nums[l] = lProd[l - 1]
        //        return nums

    }


    // Day 6
    // LeetCode 347: Top K Frequent Elements
    fun topKFrequent(nums: IntArray, k: Int): IntArray {
        // 1. Idiomatic Kotlin solution .asSequence() is faster than .toList() for large arrays
        // ◄── Lazy sequence processing loop
        return nums.asSequence().groupingBy { it }.eachCount().entries
            .sortedByDescending { it.value }.take(k).map { it.key }.toIntArray()

        // 2. HashMap + Idiomatic Kotlin solution
        //        val map = HashMap<Int, Int>()
        //        for (num in nums) {
        //            map[num] = map.getOrDefault(num, 0) + 1
        //        }
        //        return map.entries.sortedByDescending { it.value }.take(k)
        //            .map { it.key }.toIntArray()

        // 3. HashMap + PriorityQueue solution
        //        val map = HashMap<Int, Int>()
        //        nums.forEach {
        //            map[it] = map.getOrDefault(it, 0) + 1
        //        }
        //        val pq = PriorityQueue<Map.Entry<Int, Int>>(compareBy { it.value })
        //        for (entry in map.entries) {
        //            pq.add(entry)
        //            if (pq.size > k) {
        //                pq.poll()
        //            }
        //        }
        //        val res = IntArray(k)
        //        var i = 0
        //        while (pq.isNotEmpty()) {
        //            res[i++] = pq.poll()!!.key
        //        }
        //        return res

        // 4. HashMap + Bucket Sort solution
        //        val map = HashMap<Int, Int>()
        //        for (num in nums) {
        //            map[num] = map.getOrDefault(num, 0) + 1
        //        }
        //        val bucket = Array<MutableList<Int>>(nums.size + 1) { mutableListOf() }
        //        for (entry in map.entries) {
        //            bucket[entry.value].add(entry.key)
        //        }
        //
        //        val res = IntArray(k)
        //        var i = 0
        //        for (j in bucket.size - 1 downTo 0) {
        //            for (num in bucket[j]) {
        //                res[i++] = num
        //            }
        //            if (i == k) break
        //        }
        //        return res

    }

    // Day 5
    // LeetCode 49: Group Anagram
    fun groupAnagrams(strs: Array<String>): List<List<String>> {
        // 1. Idiomatic Kotlin solution
        // Sort each string lexicographically and group them together
        //return strs.groupBy {
        //    it.toCharArray().apply { sort() }.concatToString()
        //}.values.toList()

        // Count each character's frequency and group them together
        //return strs.groupBy { str ->
        //    str.groupingBy { it }.eachCount()
        //}.values.toList()

        // 2.Count each character's frequency in IntArray(26) and create the hashcode as a key in Map
        val letterCnt = IntArray(26)
        val map = HashMap<String, MutableList<String>>()
        for (str in strs) {
            for (c in str) {
                letterCnt[c - 'a']++
            }
            // Convert the frequency array to a distinct string signature key
            val keyString = letterCnt.joinToString(",")
            map.getOrPut(keyString) { mutableListOf() }.add(str)
            letterCnt.fill(0)
        }
        return map.values.toList()

    }

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