package com.rshea.leetcodeprep.masteryreviewday

object MasterW6Sprint {

    // LC 150. Evaluate Reverse Polish Notation
    // IntArray Approach (Extreme Zero Allocation)
    // Time Complexity: O(N) | Space Complexity: O(N)
    fun evalRPN(tokens: Array<String>): Int {
        // Zero-allocation, overflow-safe stack
        val stack = LongArray(tokens.size)
        var top = -1

        for (token in tokens) {
            when (token) {
                "+" -> {
                    val sum = stack[top--] + stack[top--]
                    stack[++top] = sum
                }
                "-" -> {
                    val op2 = stack[top--]
                    val op1 = stack[top--]
                    stack[++top] = op1 - op2
                }
                "*" -> {
                    val prod = stack[top--] * stack[top--]
                    stack[++top] = prod
                }
                "/" -> {
                    val op2 = stack[top--]
                    val op1 = stack[top--]
                    stack[++top] = op1 / op2
                }
                else -> stack[++top] = token.toLong()
            }
        }
        // Final result is the only remaining element on the stack
        return stack[0].toInt()

        // ArraryDeque Approach
        // Time Complexity: O(N) | Space Complexity: O(N)
        //        val dq = ArrayDeque<Int>(tokens.size)
        //        tokens.forEach { it ->
        //            when (it) {
        //                "+" -> dq.addLast(dq.removeLast() + dq.removeLast())
        //                "-" -> {
        //                    val op2 = dq.removeLast(); val op1 = dq.removeLast()
        //                    dq.addLast(op1 - op2)
        //                }
        //                "*" -> dq.addLast(dq.removeLast() * dq.removeLast())
        //                "/" -> {
        //                    val op2 = dq.removeLast(); val op1 = dq.removeLast()
        //                    dq.addLast(op1 / op2)
        //                }
        //                else -> dq.addLast(it.toInt())
        //            }
        //        }
        //        return dq.removeLast()
    }

}