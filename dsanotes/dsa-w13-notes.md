# LeetCodePrep Week 13

A collection of LeetCode problem solutions implemented in Kotlin.

# ==================================================================
# WEEK 13: Advanced Linear DP  (November 2 - November 6)
# ==================================================================

## Topics Covered
- [ ] Advanced Linear DP
- [ ] Recursion

## Day 62 - LC 343. Integer Break
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Euler's Constant (e ≈ 2.718) Mathematical Invariant**:
    Maximizing the product function f(x) = x^(N / x) yields its mathematical maximum at x = e ≈ 2.718.
    Since factors must be integers, the integer closest to e is **3**!
    Therefore, to maximize ∏ (a_i) such that ∑(a_i) = N:
    - Break N into as many **3s** as possible.
    - **Remainder 0**: Multiply **3^(N / 3)**.
    - **Remainder 1**: **3 × 1 = 3 < 4 = 2 × 2**. Replace one 3 and remainder 1 with **2 × 2 = 4**. 
    Product = 3^((N - 4) / 3) × 4.
    - **Remainder 2**: Multiply **3^(N / 3) * 2**.
  * **1D DP Inner Loop Pruning (j ∈ {2, 3})**:
    Any integer j ≥ 4 can be broken into 2 × (j - 2) ≥ j, so in 1D DP 
    DP[i] = max(2 * DP[i - 2], 3 * DP[i - 3]), reducing inner DP loop iterations from O(N^2) down to
    **O(N)**!

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Pure Integer Modulo 3 Math** | **O(1)** | **O(1)** | **Staff Level (Extreme)** | Instant mathematical evaluation; zero floating-point conversions (`pow`). |
 **Floating-Point Math (`pow`)** | **O(1)** | **O(1)** | Peak | Standard mathematical choice using `Math.pow`. |
 **Pruned 1D DP (j ∈ {2, 3})** | **O(N)** | **O(N)** | **Staff Level (Optimal DP)** | Standard interview choice for proving max product state transitions. |
 **Un-pruned 1D DP** | **O(N^2)** | **O(N)** | Baseline | Standard DP default. |

### 3. Native Kotlin Syntax Pitfalls
* **Avoid Floating-Point Conversion Overhead**: `3.0.pow(...)` converts integer to double, invokes IEEE 754 floating-point exponentiation library `pow`, and casts back `.toInt()`. A pure integer binary exponentiation loop avoids IEEE 754 precision loss and double conversion churn.
* **Base Case Edge Branching**: For N ≤ 3, breaking into k ≥ 2 parts gives N - 1:
  - N = 2  ⇒  1 × 1 = 1.
  - N = 3  ⇒  2 × 1 = 2.

### 4. Code Block
```kotlin
// Approach 1: Mathematical Constant Pipeline (Pure Integer Math - Staff Level Extreme)
// Time Complexity: O(1) | Auxiliary Space Complexity: O(1)
fun integerBreakMath(n: Int): Int {
    if (n <= 3) return n - 1

    fun pow3(exp: Int): Int {
        var res = 1
        var base = 3
        var e = exp
        while (e > 0) {
            if (e % 2 == 1) res *= base
            base *= base
            e /= 2
        }
        return res
    }

    return when (n % 3) {
        0 -> pow3(n / 3)
        1 -> pow3((n - 4) / 3) * 4
        else -> pow3(n / 3) * 2
    }
}

// Approach 2: Mathematical Constant Pipeline (Math.pow)
// Time Complexity: O(1) | Auxiliary Space Complexity: O(1)
fun integerBreak(n: Int): Int {
    if (n <= 3) return n - 1
    return when (n % 3) {
        0 -> 3.0.pow(n / 3).toInt()
        1 -> 3.0.pow((n - 4) / 3).toInt() * 4
        else -> 3.0.pow(n / 3).toInt() * 2
    }
}

// Approach 3: Pruned Bottom-Up Dynamic Programming (1D Table - O(N) Time)
// Time Complexity: O(N) | Auxiliary Space Complexity: O(N)
fun integerBreak1D(n: Int): Int {
    if (n <= 3) return n - 1
    val dp = IntArray(n + 1)
    dp[1] = 1
    dp[2] = 2
    dp[3] = 3

    for (i in 4..n) {
        dp[i] = maxOf(2 * dp[i - 2], 3 * dp[i - 3]) // Inner loop pruned to j = 2 and j = 3
    }

    return dp[n]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Why 3 is the Optimal Factor (Calculus Derivation)**:
  * To maximize P(x) = x^(N / x), take natural log: ln P(x) = (N / x) × ln x .
  * Derivative: d/dx (ln P(x))  = N · (1 - ln x) / x^2. Setting derivative to 0 yields 1 - x = 0 ⇒ 
  x = e ≈ 2.71828.
  * Integer candidate choices are 2 and 3. 3^(1/3) ≈ 1.4422 > 2^(1/2) ≈ 1.4142, making 3 strictly 
  optimal!
* **1D DP Inner Loop Reduction from O(N^2) to O(N)**:
  * Since any factor j ≥ 4 decomposes into 2 × (j-2) ≥ j, DP[i] only needs to transition from DP[i-2] 
  and DP[i-3]:
    DP[i] = Max(2 × DP[i-2], 3 × DP[i-3])
  * This eliminates the inner `for (j in 1 until i)` loop completely, optimizing 1D DP from O(N^2) 
  down to **O(N)**!

---
