package com.rshea.leetcodeprep.masteryreviewday

object MasterW8Backtracking {

    // LC 148. Sort List
    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    fun sortList(head: ListNode?): ListNode? {
        if (head?.next == null){
            return head
        }
        var slow = head
        var fast = head
        var prev: ListNode? = null

        while (fast != null && fast.next != null) {
            prev = slow!!
            slow = slow.next
            fast = fast.next!!.next
        }
        prev?.next = null

        val left = sortList(head)
        val right = sortList(slow)

        fun merge(l: ListNode?, r: ListNode?): ListNode? {
            var l1 = l
            var l2 = r
            val dummy = ListNode(0)
            var tail: ListNode = dummy
            while (l1 != null && l2 != null) {
                if (l1.`val` <= l2.`val`) {
                    tail.next = l1
                    l1 = l1.next
                } else {
                    tail.next = l2
                    l2 = l2.next
                }
                tail = tail.next!!
            }
            tail.next = l1 ?: l2
            return dummy.next
        }

        return merge(left, right)
    }


    // LC 47. Permutations II
    fun permuteUnique(nums: IntArray): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        val visited = BooleanArray(nums.size)
        nums.sort()

        fun backtrack(sublist: MutableList<Int>) {
            if (sublist.size == nums.size) {
                list.add(ArrayList(sublist))
                return
            }

            for (i in nums.indices) {
                if (visited[i] || i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]) {
                    continue
                }
                visited[i] = true
                sublist.add(nums[i])
                backtrack(sublist)
                visited[i] = false
                sublist.removeAt(sublist.lastIndex)
            }
        }

        backtrack(mutableListOf())
        return list
    }

    // LC 46. Permutations
    fun permute(nums: IntArray): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        val visited = BooleanArray(nums.size)

        fun backtrack(sublist: MutableList<Int>) {
            if (sublist.size == nums.size) {
                list.add(ArrayList(sublist))
                return
            }

            for (i in nums.indices){
                if (visited[i]) {
                    continue
                }

                visited[i] = true
                sublist.add(nums[i])
                backtrack(sublist)
                visited[i] = false
                sublist.removeAt(sublist.lastIndex)
            }
        }

        backtrack(mutableListOf())
        return list
    }

    fun permuteInPlace(nums: IntArray): List<List<Int>> {
        val list = mutableListOf<List<Int>>()

        fun swap(i: Int, j: Int) {
            val temp = nums[i]
            nums[i] = nums[j]
            nums[j] = temp
        }

        fun backtrack(start: Int) {
            if (start == nums.size) {
                list.add(nums.toList())
                return
            }

            for (i in start until nums.size) {
                swap(start, i)
                backtrack(start + 1)
                swap(start, i)
            }
        }

        backtrack(0)
        return list
    }


    // LC 90. Subsets II
    fun subsetsWithDup(nums: IntArray): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        val sublist = mutableListOf<Int>()
        nums.sort()

        fun backtrack(start: Int) {
            list.add(ArrayList(sublist))

            for (i in start until nums.size) {
                if (i > start && nums[i] == nums[i - 1]) {
                    continue
                }

                sublist.add(nums[i])
                backtrack(i + 1)
                sublist.removeAt(sublist.lastIndex)
            }

        }
        backtrack(0)
        return list
    }

    // LC 78. Subsets
    fun subsets(nums: IntArray): List<List<Int>> {
        val list = mutableListOf<List<Int>>()
        val sublist = ArrayList<Int>(nums.size)

        fun backtrack(start: Int) {
            list.add(ArrayList(sublist))

            for (i in start until nums.size) {
                sublist.add(nums[i])
                backtrack(i + 1)
                sublist.removeAt(sublist.lastIndex)
            }
        }
        backtrack(0)
        return list
    }
}