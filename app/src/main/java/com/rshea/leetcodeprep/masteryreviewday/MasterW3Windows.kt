package com.rshea.leetcodeprep.masteryreviewday

import java.util.TreeSet
import kotlin.math.abs

object MasterW3Windows {

    // LeetCode 220. Contains Duplicate III
    // TODO: Consider: How would you handle the math if valueDiff was zero, and how does
    // TODO: the bucket approach avoid the log factor of the TreeSet?
    // TODO: Suggestions: Consider using a bucket-based hashing approach (sliding window with
    // TODO: map) to achieve linear time complexity by mapping values to intervals of size
    // TODO: valueDiff + 1.
    fun containsNearbyAlmostDuplicate(nums: IntArray, indexDiff: Int, valueDiff: Int): Boolean {
        // Optimal Bucket Hashing Approach - HashMap as Bucket using Value Difference as Bucket Size
        val bucketMap = HashMap<Long, Long>()
        // Use Long to prevent overflow during bucket size calculation
        //        val bucketSize = valueDiff.toLong() + 1
        //
        //        for (i in nums.indices) {
        //            val num = nums[i].toLong()
        //            // Calculate bucket ID using Long logic
        //            val bucketId = if (num >= 0) num / bucketSize else (num + 1) / bucketSize - 1
        //
        //            // Case 1: Same bucket exists (Difference is guaranteed to be <= valueDiff)
        //            if (bucketMap.containsKey(bucketId)) return true
        //
        //            // Case 2: Adjacent buckets (Must check actual difference)
        //            bucketMap[bucketId - 1]?.let { if (num - it <= valueDiff) return true }
        //            bucketMap[bucketId + 1]?.let { if (it - num <= valueDiff) return true }
        //
        //            // Maintain sliding window
        //            if (i >= indexDiff) {
        //                val head = nums[i - indexDiff].toLong()
        //                val headBucketId = if (head >= 0) head / bucketSize else (head + 1) / bucketSize - 1
        //                bucketMap.remove(headBucketId)
        //            }
        //
        //            bucketMap[bucketId] = num
        //        }
        //
        //        return false

        // Bucket Hashing Approach - HashMap as Bucket using Value Difference as Bucket Size
        // Time Complexity: O(N) | Space Complexity: O(min(n,indexDiff))
        //        val bucketMap = HashMap<Int, Int>()
        //        val bucketSize = valueDiff + 1
        //
        //        for (i in nums.indices) {
        //            val bucketId = if (nums[i] >= 0) nums[i] / bucketSize else (nums[i] + 1) / bucketSize - 1
        //
        //            if (bucketMap[bucketId] != null) return true
        //
        //            if (bucketMap[bucketId - 1] != null && abs(nums[i] - bucketMap[bucketId - 1]!!) <= valueDiff
        //                || bucketMap[bucketId + 1] != null && abs(nums[i] - bucketMap[bucketId + 1]!!) <= valueDiff) {
        //                return true
        //            }
        //
        //            if (i >= indexDiff) {
        //                val head = nums[i - indexDiff]
        //                val headBucketId = if (head >= 0) head / bucketSize else (head + 1) / bucketSize - 1
        //                bucketMap.remove(headBucketId)
        //            }
        //
        //            bucketMap[bucketId] = nums[i]
        //
        //        }
        //
        //        return false

        // TreeSet Approach
        // Time Complexity: O(N * Log(N)) | Space Complexity: O(K)
        val treeSet = TreeSet<Int>()
        var i = 0

        for (j in nums.indices) {
            if (treeSet.contains(nums[j])) {
                return true
            } else {
                val floor = treeSet.floor(nums[j])
                val ceiling = treeSet.ceiling(nums[j])
                if (floor != null && valueDiff >= nums[j] - floor ||
                    ceiling != null && valueDiff >= ceiling - nums[j]) {
                    return true
                }
                treeSet.add(nums[j])
            }
            if (j - i >= indexDiff) {
                treeSet.remove(nums[i])
                i++
            }
        }
        return false
    }

    // LeetCode 259. 3Sum Smaller
    // Greedy Shrinking Window Approach
    // Time Complexity: O(N^2) | Space Complexity: O(1)
    // TODO: Consider: If the constraints were much smaller but the values were very large,
    // TODO: would you consider a different approach or stick with this one?
    fun threeSumSmaller(nums: IntArray, target: Int): Int {
        nums.sort()
        var j: Int
        var k: Int
        val boundary = nums.size - 2
        var noOfResults = 0
        for (i in 0 until boundary) {
            val complement = target - nums[i]
            j = i + 1
            k = nums.lastIndex
            while (j < k) {
                val sum = nums[j] + nums[k]
                when {
                    sum >= complement -> k--
                    else -> {
                        noOfResults += (k - j)
                        j++
                    }
                }
            }
        }
        return noOfResults
    }


    // LeetCode 16. 3Sum Closest
    // Greedy Shrinking Window Approach
    // Time Complexity: O(N^2) | Space Complexity: O(1)
    // TODO: Since you mastered the sum of three, how would you handle a scenario where
    // TODO: you need to find the closest sum for k integers?

    fun threeSumClosest(nums: IntArray, target: Int): Int {
        nums.sort()
        var j: Int
        var k: Int
        val boundary = nums.size - 2
        var closest3Sum = nums[0] + nums[1] + nums[2]
        for (i in 0 until boundary) {

            if (i > 0 && nums[i] == nums[i - 1]) continue
            j = i + 1
            k = nums.lastIndex

            while (j < k) {
                val curSum = nums[i] + nums[j] + nums[k]

                if (target == curSum) return curSum

                if (abs(target - curSum) < abs(target - closest3Sum)) {
                    closest3Sum = curSum
                }

                if (curSum < target) j++ else k--
            }
        }
        return closest3Sum
    }

}