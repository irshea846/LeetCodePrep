package com.rshea.leetcodeprep.masteryreviewday

import org.junit.Test
import kotlin.test.assertEquals

class MasterW6SprintTest {

    // LC 150. Evaluate Reverse Polish Notation
    @Test
    fun testEvalRPN_ValidCase1() {
        val tokens = arrayOf("2", "1", "+", "3", "*")
        val expected = 9
        val actual = MasterW6Sprint.evalRPN(tokens)
        assertEquals(expected, actual)
    }

    @Test
    fun testEvalRPN_ValidCase2() {
        val tokens = arrayOf("4", "13", "5", "/", "+")
        val expected = 6
        val actual = MasterW6Sprint.evalRPN(tokens)
        assertEquals(expected, actual)
    }

    @Test
    fun testEvalRPN_ValidCase3() {
        val tokens = arrayOf("10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+")
        val expected = 22
        val actual = MasterW6Sprint.evalRPN(tokens)
        assertEquals(expected, actual)
    }

}