package com.rshea.leetcodeprep

import androidx.core.text.isDigitsOnly

object Week6Sprint {

    // Day28 - LC 150. Evaluate Reverse Polish Notation

    fun evalRPNZeroBoxing(tokens: Array<String>): Int {
        // True Zero Allocation / Zero Boxing Approach
        // Uses a primitive IntArray as a stack to avoid Integer object creation (Boxing)
        val st = IntArray(tokens.size)
        var top = -1

        for (str in tokens) {
            // 1. Manual check is cheaper than a failed toIntOrNull() parse
            if (str != "+" && str != "-" && str != "*" && str != "/") {
                // 2. Direct primitive assignment (No Boxing)
                st[++top] = str.toInt()
            } else {
                val op2 = st[top--]
                val op1 = st[top--]
                st[++top] = when (str) {
                    "+" -> op1 + op2
                    "-" -> op1 - op2
                    "*" -> op1 * op2
                    else -> op1 / op2
                }
            }
        }
        return st[0]
    }

    fun evalRPN(tokens: Array<String>): Int {
        val st = ArrayDeque<Int>(tokens.size)
        var op1: Int
        var op2: Int

        for (str in tokens) {
            if (str != "+" && str != "-" && str != "*" && str != "/") {
                st.addLast(str.toInt())
            } else {
                op2 = st.removeLast()
                op1 = st.removeLast()
                when (str) {
                    "+" -> st.addLast(op1 + op2)
                    "-" -> st.addLast(op1 - op2)
                    "*" -> st.addLast(op1 * op2)
                    else -> st.addLast(op1 / op2)
                }
            }
        }
        return st.removeLast()
    }

    inline fun calculate(x: Int, y: Int, operation: (Int, Int) -> Int): Int {
        return operation(x, y)
    }

    fun evalRPNHigherOrderFunction(tokens: Array<String>): Int {
        val plus: ((Int, Int) -> Int) = { a, b -> a + b }
        val minus: ((Int, Int) -> Int) = { a, b -> a - b }
        val times: ((Int, Int) -> Int) = { a, b -> a * b }
        val divide: ((Int, Int) -> Int) = { a, b -> a / b }

        val st = ArrayDeque<Int>(tokens.size)
        var op1: Int
        var op2: Int

        for (str in tokens) {
            val validNumber = str.toIntOrNull()
            if (validNumber != null) {
                st.addLast(validNumber)
            } else {
                op2 = st.removeLast()
                op1 = st.removeLast()
                when (str) {
                    "+" -> st.addLast(calculate(op1, op2, plus))
                    "-" -> st.addLast(calculate(op1, op2, minus))
                    "*" -> st.addLast(calculate(op1, op2, times))
                    else -> st.addLast(calculate(op1, op2, divide))
                }
            }
        }
        return st.removeLast()
    }
}