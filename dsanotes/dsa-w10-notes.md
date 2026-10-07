# LeetCodePrep Week 10

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 10: DYNAMIC PROGRAMMING (DP) (October 6 - October 9)
# ===============================================================

## Topics Covered
- [ ] DP
- [ ] Recursive

## Day 46 - LC 70. Climbing Stairs
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Overlapping Subproblems Invariant**: To reach step n, you must come from step n-1 (taking 1 step) 
  or step n-2 (taking 2 steps). Thus, ways(n) = ways(n-1) + ways(n-2), mapping directly to the Fibonacci 
  recurrence relation offset by 1 step.
  * **Space Optimization Progression**: Since computing ways(n) only depends on the previous two states 
  (ways(n-1) and ways(n-2)), we can optimize memory from an O(N) DP table down to O(1) constant space 
  using two scalar state variables.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                                                 |
 :--- | :--- | :--- | :--- |:--------------------------------------------------------------------------------------------------|
 **In-Place Bottom-Up DP** | **O(N)** | **O(1)** | **Staff Level (Optimal)** | Standard production choice; zero heap allocation and O(1) constant memory.                        |
 **`tailrec` Functional Recursion** | **O(N)** | **O(1)** | **Staff Level (Functional)** | Idiomatic Kotlin; compiler transforms tail recursion into a bytecode loop.                        |
 **DP Array Table** | **O(N)** | **O(N)** | Peak (Canonical) | Standard interview default for demonstrating full DP table state transitions.                     |
 **Matrix Exponentiation** | **O(log N)** | **O(1)** | **Staff Level (Extreme N)** | Very large **N** (**N > 10⁹ or N = 10¹⁸**); uses repeated matrix squaring.                        |
 **Binet's Closed-Form Formula** | **O(log N)** | **O(1)** | Good | Mathematical closed-form; susceptible to IEEE 754 floating-point floor truncation for **N > 70**. |


### 3. Native Kotlin Syntax Pitfalls
* **The `tailrec` Compiler Optimization**: Marking a recursive function as `tailrec` in Kotlin instructs 
the compiler to optimize tail call recursion into a fast, iterative `while` loop in bytecode, eliminating 
call stack frame creation and guaranteeing 𝒪(1) space!
* **Floating-Point Floor Truncation (`round()` vs `.toInt()`)**: Converting floating-point mathematical 
results (e.g. `(phi.pow(n + 1) ...).toInt()`) directly with `.toInt()` performs floor truncation. Due 
to IEEE 754 floating-point representation limits (e.g., $2.9999999999999996$), casting directly truncates 
**2.9999** to `2` instead of `3`. Always use `round(...).toInt()`!
* **Loop Boundary Simplification**: Instead of branching for n = 1 vs n ≥ 3, starting **prev_2 = 1, 
prev_1 = 1$** with loop `for (i in 2..n)` uniformly covers all n ≥ 1 without special edge-case branching.
* **32-Bit Integer Overflow for Large **N****: For **N > 45**, Fibonacci values exceed `Int.MAX_VALUE` 
(**2 * 10⁹**) and overflow to negative integers. In production or for **N > 45**, use `Long` or `BigInteger`.

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (Table)
// Time Complexity: O(N) | Auxiliary Space Complexity: O(N)
fun climbStairsTable(n: Int): Int {
    if (n <= 1) return 1
    val table = IntArray(n + 1)
    table[0] = 1
    table[1] = 1

    for (i in 2..n) {
        table[i] = table[i - 1] + table[i - 2]
    }

    return table[n]
}

// Approach 2: Bottom-Up Dynamic Programming (In-Place O(1) Space)
// Time Complexity: O(N) | Auxiliary Space Complexity: O(1)
fun climbStairsInPlace(n: Int): Int {
    if (n <= 1) return 1
    var prev2 = 1 // ways(0)
    var prev1 = 1 // ways(1)

    for (i in 2..n) {
        val curr = prev1 + prev2
        prev2 = prev1
        prev1 = curr
    }

    return prev1
}

// Approach 3: Tail-Recursive Functional DP (Kotlin 'tailrec' O(1) Space)
// Time Complexity: O(N) | Auxiliary Space Complexity: O(1) bytecode loop
fun climbStairsTailRec(n: Int): Int {
    tailrec fun findDistinctWays(steps: Int, a: Int, b: Int): Int {
        if (steps == 1) return b
        return findDistinctWays(steps - 1, b, a + b)
    }

    return findDistinctWays(n, 1, 1)
}

// Approach 4: Binet's Closed-Form Formula
// Time Complexity: O(Log N) | Auxiliary Space Complexity: O(1)
fun climbStairsBinetClosedFormFormula(n: Int): Int {
    val rootOfFive = sqrt(5.0)
    val phi = (1.0 + rootOfFive) / 2
    val psi = (1.0 - rootOfFive) / 2

    // Use round() to protect against IEEE 754 floating-point floor truncation
    return round((phi.pow(n + 1) - psi.pow(n + 1)) / rootOfFive).toInt()
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **𝒪(log N) Matrix Exponentiation**:
  * For **N > 10⁹ or N = 10¹⁸**, linear 𝒪(log N) loops are too slow.
  * Representing state transitions as matrix multiplication:
    [F(n+1); F(n)] = [[1, 1]; [1, 0]]ⁿ * [F(1); F(0)]
  * Using **Binary Exponentiation** (repeated matrix squaring), [1, 1; 1, 0]ⁿ can be computed in 
  **𝒪(log N) time**!
* **Binet's Closed-Form Formula Limitations**:
  * F(n) = (φⁿ - ψⁿ) / √5 where φ = (1 + √5) / 2 and ψ = (1 - √5) / 2.
  * Runs in 𝒪(Log N) math due to underlying double exponentiation `phi.pow(n+1)`. But IEEE 754 
  * floating-point precision issues limit accuracy for **N > 70**. 
* **Precision Degradation**: 
  * IEEE 754 64-bit doubles allocate a 53-bit mantissa (≈ 15–17 decimal digits). 
  * For **N > 70**, precision loss creates off-by-one errors. Matrix Exponentiation is strictly 
  preferred for exact integer outputs.
---
