package com.rshea.leetcodeprep

import org.junit.Test
import kotlin.test.assertEquals

class Week13AdvDPLinearTest {

    // Day 62 - LC 343. Integer Break
    @Test
    fun testIntegerBreak() {
        assertEquals(1, Week13AdvDPLinear.integerBreakMath(2))
        assertEquals(36, Week13AdvDPLinear.integerBreakMath(10))
        assertEquals(1549681956, Week13AdvDPLinear.integerBreakMath(58))

        assertEquals(1, Week13AdvDPLinear.integerBreak(2))
        assertEquals(36, Week13AdvDPLinear.integerBreak(10))
        assertEquals(1549681956, Week13AdvDPLinear.integerBreak(58))

        assertEquals(1, Week13AdvDPLinear.integerBreak1D(2))
        assertEquals(36, Week13AdvDPLinear.integerBreak1D(10))
        assertEquals(1549681956, Week13AdvDPLinear.integerBreak1D(58))
    }

}