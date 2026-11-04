# LeetCodePrep Week 13

A collection of LeetCode problem solutions implemented in Kotlin.

# ==================================================================
# WEEK 13: Advanced Linear DP  (November 2 - November 6)
# ==================================================================

## Topics Covered
- [ ] Advanced Linear DP
- [ ] Recursion

## Day 63 - LC 131. Palindrome Partitioning
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **All Possible Valid Partition Combinations (Exhaustive Search)**: The requirement to generate 
  every valid string partition where every substring is a palindrome identifies this as a 
  **Backtracking (DFS)** problem.
  * **2D DP Palindrome Matrix Pre-computation (O(N²) Memoization)**:
    Pre-computing whether s[i ... j] is a palindrome using 2D DP:
    **`isPalindrome[i][j] = (s[i] == s[j]) and (j - i ≤ 2 or isPalindrome[i + 1][j - 1])`** reduces 
    palindrome validity checks during backtracking from O(N) down to O(1)!
  * **Decision Tree Pruning**: At index `i`, we explore all candidate split endpoints `j in i .. N-1`. 
  If `isPalindrome[i][j]` is `false`, the branch is pruned immediately, avoiding dead recursive calls.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Backtracking + 2D DP Pre-computation** | **O(N × 2ⁿ)** | **O(N²)** matrix + stack | **Staff Level (Optimal)** | Pre-computes palindrome matrix in $\mathcal{O}(N^2)$ time; $\mathcal{O}(1)$ palindrome checks. |
 **Backtracking + On-the-Fly Two-Pointers** | **O(N × 2ⁿ)** | **O(N)** recursion stack | Good | Eliminates $O(N^2)$ matrix allocation; ideal for short string inputs ($N \le 16$). |

*(where $N = s.\text{length}$)*

### 3. Native Kotlin Syntax Pitfalls
* **`s.substring(i, j + 1)` Allocation Churn**: `s.substring` creates a new `String` heap object. 
While necessary for final output lists, pre-allocating the single mutable stack `path` 
(`ArrayList<String>(n)`) avoids object allocations per recursive frame.
* **`path.removeAt(path.lastIndex)`**: Use `path.removeAt(path.lastIndex)` for O(1) stack pop 
operations. (Avoid `path.removeLast()` when targeting minSdk < 35).

### 4. Code Block
```kotlin
// Approach 1: Backtracking + 2D DP Pre-computation (Staff Level Optimal)
// Time Complexity: O(N * 2^N) | Auxiliary Space Complexity: O(N^2)
fun partition(s: String): List<List<String>> {
    val result = mutableListOf<List<String>>()
    val n = s.length
    val isPalindrome = Array(n) { BooleanArray(n) }

    // Pre-compute 2D DP Palindrome Table in O(N^2) time
    for (i in n - 1 downTo 0) {
        for (j in i until n) {
            isPalindrome[i][j] = s[i] == s[j] && (j - i <= 2 || isPalindrome[i + 1][j - 1])
        }
    }

    val path = ArrayList<String>(n)

    fun backtrack(start: Int) {
        if (start == n) {
            result.add(ArrayList(path)) // Snapshot current partition path
            return
        }

        for (j in start until n) {
            if (isPalindrome[start][j]) { // O(1) Palindrome lookup
                path.add(s.substring(start, j + 1))
                backtrack(j + 1)
                path.removeAt(path.lastIndex) // Backtrack
            }
        }
    }

    backtrack(0)
    return result
}

// Approach 2: Backtracking + On-the-Fly Two-Pointers (O(N) Space)
// Time Complexity: O(N * 2^N) | Auxiliary Space Complexity: O(N) recursion stack
fun partitionTwoPointers(s: String): List<List<String>> {
    val result = mutableListOf<List<String>>()
    val n = s.length
    val path = ArrayList<String>(n)

    fun isPalindrome(l: Int, r: Int): Boolean {
        var left = l
        var right = r
        while (left < right) {
            if (s[left++] != s[right--]) return false
        }
        return true
    }

    fun backtrack(start: Int) {
        if (start == n) {
            result.add(ArrayList(path)) // Snapshot current state
            return
        }

        for (j in start until n) {
            if (isPalindrome(start, j)) { // On-the-fly two pointers check
                path.add(s.substring(start, j + 1))
                backtrack(j + 1)
                path.removeAt(path.lastIndex) // Backtrack
            }
        }
    }

    backtrack(0)
    return result
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Pre-computation 2D DP vs. On-The-Fly Expanding Two-Pointers**:
  * **2D DP Pre-computation (O(N²) space)**: Pre-computes all substring palindrome states in O(N²) 
  time up-front, reducing in-loop palindrome checks to O(1).
  * **On-the-Fly Two-Pointers (O(N) space)**: Checks if substring s[i ... j] is a palindrome 
  on-the-fly using two pointers in O(j - i) time. This saves O(N²) matrix memory, but increases 
  palindrome verification time to O(N) per branch.
* **Worst-Case Search Space (2^(N-1) Partitions)**:
  * For a string of all identical characters (e.g., s = "aaaa...a"), every substring is a valid 
  palindrome, producing exactly 2^(N-1) valid partitions!
  * Thus, the O(N × 2ⁿ) time complexity is **tight** due to output generation bounds rather than 
  algorithm overhead.

---

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
* **Avoid Floating-Point Conversion Overhead**: `3.0.pow(...)` converts integer to double, invokes 
  IEEE 754 floating-point exponentiation library `pow`, and casts back `.toInt()`. A pure integer 
  binary exponentiation loop avoids IEEE 754 precision loss and double conversion churn.
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
  * Derivative: d/dx (ln P(x))  = N · (1 - ln x) / x². Setting derivative to 0 yields 1 - x = 0 ⇒ 
  x = e ≈ 2.71828.
  * Integer candidate choices are 2 and 3. 3^(1/3) ≈ 1.4422 > 2^(1/2) ≈ 1.4142, making 3 strictly 
  optimal!
* **1D DP Inner Loop Reduction from O(N²) to O(N)**:
  * Since any factor j ≥ 4 decomposes into 2 × (j-2) ≥ j, DP[i] only needs to transition from DP[i-2] 
  and DP[i-3]:
    DP[i] = Max(2 × DP[i-2], 3 × DP[i-3])
  * This eliminates the inner `for (j in 1 until i)` loop completely, optimizing 1D DP from O(N^2) 
  down to **O(N)**!

---
