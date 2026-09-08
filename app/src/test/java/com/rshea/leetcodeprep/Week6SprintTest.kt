package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week6SprintTest {

    // Day28 - LC 150. Evaluate Reverse Polish Notation
    @Test
    fun testEvalRPN_ValidCase1() {
        val tokens = arrayOf("2", "1", "+", "3", "*")
        val result0 = Week6Sprint.evalRPNZeroBoxing(tokens)
        val result1 = Week6Sprint.evalRPN(tokens)
        val result2 = Week6Sprint.evalRPNHigherOrderFunction(tokens)
        assertEquals(9, result0)
        assertEquals(9, result1)
        assertEquals(9, result2)
    }

    @Test
    fun testEvalRPN_ValidCase2() {
        val tokens = arrayOf("4", "13", "5", "/", "+")
        val result0 = Week6Sprint.evalRPNZeroBoxing(tokens)
        val result1 = Week6Sprint.evalRPN(tokens)
        val result2 = Week6Sprint.evalRPNHigherOrderFunction(tokens)
        assertEquals(6, result0)
        assertEquals(6, result1)
        assertEquals(6, result2)
    }

    @Test
    fun testEvalRPN_ValidCase3() {
        val tokens = arrayOf("10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+")
        val result0 = Week6Sprint.evalRPNZeroBoxing(tokens)
        val result1 = Week6Sprint.evalRPN(tokens)
        val result2 = Week6Sprint.evalRPNHigherOrderFunction(tokens)
        assertEquals(22, result0)
        assertEquals(22, result1)
        assertEquals(22, result2)
    }
}