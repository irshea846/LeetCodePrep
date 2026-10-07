package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week10DPTest {

    // Day 46 - LC 70. Climbing Stairs
    @Test
    fun testClimbStairsTable() {
        assertEquals(2, Week10DP.climbStairsTable(2))
        assertEquals(3, Week10DP.climbStairsTable(3))
        assertEquals(701408733, Week10DP.climbStairsTable(43))
        assertEquals(1134903170, Week10DP.climbStairsTable(44))
        assertEquals(1836311903, Week10DP.climbStairsTable(45))
    }

    @Test
    fun testClimbStairsInPlace() {
        assertEquals(2, Week10DP.climbStairsInPlace(2))
        assertEquals(3, Week10DP.climbStairsInPlace(3))
        assertEquals(701408733, Week10DP.climbStairsInPlace(43))
        assertEquals(1134903170, Week10DP.climbStairsInPlace(44))
        assertEquals(1836311903, Week10DP.climbStairsInPlace(45))
    }

    @Test
    fun testClimbStairsTailRec() {
        assertEquals(2, Week10DP.climbStairsTailRec(2))
        assertEquals(3, Week10DP.climbStairsTailRec(3))
        assertEquals(701408733, Week10DP.climbStairsTailRec(43))
        assertEquals(1134903170, Week10DP.climbStairsTailRec(44))
        assertEquals(1836311903, Week10DP.climbStairsTailRec(45))
    }

    @Test
    fun testClimbStairsBinetClosedFormFormula() {
        assertEquals(2, Week10DP.climbStairsBinetClosedFormFormula(2))
        assertEquals(3, Week10DP.climbStairsBinetClosedFormFormula(3))
        assertEquals(701408733, Week10DP.climbStairsBinetClosedFormFormula(43))
        assertEquals(1134903170, Week10DP.climbStairsBinetClosedFormFormula(44))
        assertEquals(1836311903, Week10DP.climbStairsBinetClosedFormFormula(45))
    }
}