package com.rshea.leetcodeprep

object Week4Stacks {

    // Day 20
    // LeetCode 739. Daily Temperatures
    fun dailyTemperatures(temperatures: IntArray): IntArray {
        val mono = IntArray(temperatures.size)
        val waitingDays = IntArray(temperatures.size)
        var top = -1

        for (i in temperatures.indices) {
            while (top >= 0 && temperatures[i] > temperatures[mono[top]]) {
                waitingDays[mono[top]] = i - mono[top]
                top--
            }
            mono[++top] = i
        }

        return waitingDays
    }

    // Day 19
    // LeetCode 155. Min Stack
    class MinStack {
        val st = ArrayDeque<Int>()
        var minSt = ArrayDeque<Int>()

        fun push(value: Int) {
            st.addLast(value)
            if (minSt.isEmpty() || value <= minSt.last()) {
                minSt.addLast(value)
            }
        }

        fun pop() {
            if (st.isEmpty()) return
            // EXPLICIT UNBOXING: Capture the popped element.
            // Comparing it directly to minSt.last() ensures zero reference ambiguity.
            val poppedValue = st.removeLast()
            if (poppedValue == minSt.last()) {
                minSt.removeLast()
            }
        }

        fun top(): Int {
            return st.last()
        }

        fun getMin(): Int {
            return minSt.last()
        }

    }


    // Day 18
    // LeetCode 20. Valid Parentheses
    // Allocate a primitive array outside the function.
    // Unused slots default to the null character '\u0000'.
    private val EXPECTED_CLOSER = CharArray(128).apply {
        this['('.code] = ')'
        this['['.code] = ']'
        this['{'.code] = '}'
    }

    private val OPEN_TO_CLOSE = mapOf('(' to ')', '[' to ']', '{' to '}')

    fun isValid(s: String): Boolean {
        val stack = ArrayDeque<Char>()

        for (i in s.indices) {
            val c = s[i]
            val charCode = c.code

            // 1. O(1) Check: See if this character is a known opener
            if (charCode < 128 && EXPECTED_CLOSER[charCode] != '\u0000') {
                stack.addLast(EXPECTED_CLOSER[charCode])
            } else {
                // 2. It's a closer, so it must match the top expected character on the stack
                if (stack.isEmpty() || stack.removeLast() != c) {
                    return false
                }
            }
        }
        return stack.isEmpty()

        // Expected Character strategy
        // Time Complexity: O(N) | Space Complexity: O(N)
        //        val stack = ArrayDeque<Char>()
        //
        //        for (char in s) {
        //            when (char) {
        //                // If it's an opener, push the bracket we EXPECT to see later
        //                '(', '[', '{' -> stack.addLast(OPEN_TO_CLOSE[char]!!)
        //                ')', ']', '}' -> {
        //                    // If it's a closer, it MUST match the top of the stack
        //                    if (stack.isEmpty() || stack.removeLast() != char) {
        //                        return false
        //                    }
        //                }
        //                else -> continue
        //            }
        //        }
        //        return stack.isEmpty()

        // Optimized Stack Approach using ArrayDeque (non-synchronized)
        // Time Complexity: O(N) | Space Complexity: O(N)
        //        val stack = ArrayDeque<Char>()
        //
        //        for (char in s) {
        //            when (char) {
        //                '(', '[', '{' -> stack.addLast(char)
        //
        //                // For closing brackets, pop and verify the match
        //                ')' -> if (stack.isEmpty() || stack.removeLast() != '(') return false
        //                ']' -> if (stack.isEmpty() || stack.removeLast() != '[') return false
        //                '}' -> if (stack.isEmpty() || stack.removeLast() != '{') return false
        //                else -> continue
        //            }
        //        }
        //        return stack.isEmpty()
    }

    // TODO: Consider: If the string only contained one type of bracket, say just parentheses, could
    //  you solve this without using a stack to save space?
    fun isValidOnlyOneType(s: String): Boolean {
        var counter = 0

        for (char in s) {
            when (char) {
                '(' -> counter++
                ')' -> {
                    counter--
                    // Early exit: cannot close what was never opened
                    if (counter < 0) return false
                }
                else -> continue
            }
        }

        return counter == 0
    }
}
