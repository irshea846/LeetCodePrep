package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals
class Week4StacksTest {

    // Day 18
    // LeetCode 20. Valid Parentheses
    @Test
    fun testIsValid_ValidCase1() {
        val s = "()"
        val result = Week4Stacks.isValid(s)
        assertEquals(true, result)
    }

    @Test
    fun testIsValid_ValidCase2() {
        val s = "()[]{}"
        val result = Week4Stacks.isValid(s)
        assertEquals(true, result)
    }

    @Test
    fun testIsValid_ValidCase3() {
        val s = "(]"
        val result = Week4Stacks.isValid(s)
        assertEquals(false, result)
    }

    @Test
    fun testIsValid_ValidCase4() {
        val s = "([)]"
        val result = Week4Stacks.isValid(s)
        assertEquals(false, result)
    }

    @Test
    fun testIsValid_ValidCase5() {
        val s = "{[]}"
        val result = Week4Stacks.isValid(s)
        assertEquals(true, result)
    }

    @Test
    fun testIsValid_ValidCase6() {
        val s = "]"
        val result = Week4Stacks.isValid(s)
        assertEquals(false, result)
    }

    @Test
    fun testIsValid_ValidCase7() {
        val s = "["
        val result = Week4Stacks.isValid(s)
        assertEquals(false, result)
    }

    @Test
    fun testIsValidOnlyOneType_ValidCase1() {
        val s = "()"
        val result = Week4Stacks.isValidOnlyOneType(s)
        assertEquals(true, result)
    }

    @Test
    fun testIsValidOnlyOneType_ValidCase2() {
        val s = "()()()"
        val result = Week4Stacks.isValidOnlyOneType(s)
        assertEquals(true, result)
    }

    @Test
    fun testIsValidOnlyOneType_InvalidCase1() {
        val s = "((())"
        val result = Week4Stacks.isValidOnlyOneType(s)
        assertEquals(false, result)
    }

    @Test
    fun testIsValidOnlyOneType_InvalidCase2() {
        val s = ")("
        val result = Week4Stacks.isValidOnlyOneType(s)
        assertEquals(false, result)
    }
}