# LeetCodePrep Week 11 Mastery & Review Day

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 11: 2D DYNAMIC PROGRAMMING (DP) (October 23 - October 24)
# ===============================================================

## Topics Covered
- [ ] DP
- [ ] Tail Recursion

## LC 1143. Longest Common Subsequence
* **What specific constraint triggered the solution design?**
    * **2D Grid Subsequence Matching**: Finding the length of the longest subsequence common to two
      strings `text1` and `text2` requires comparing character prefixes `text1[0 ... i-1]` and
      `text2[0 ... j-1]`.
    * **Match vs. Non-Match State Transitions**:
        - **Match (`text1[i-1] == text2[j-1]`)**: Extend the common subsequence length from the top-left
          diagonal (`DP[i][j] = DP[i-1][j-1] + 1`).
        - **Non-Match (`text1[i-1] != text2[j-1]`)**: Take the maximum of skipping a character from
          `text1` or `text2` (`DP[i][j] = max(DP[i-1][j], DP[i][j-1])`).
    * **Single 1D Array Space Optimization (O(min(N, M)) Memory)**:
      Since row i depends only on row i-1 (specifically top-left diagonal `DP[i-1][j-1]` and top
      `DP[i-1][j]`), we can compress memory down to a single 1D `IntArray(min(N, M) + 1)` by tracking
      the top-left diagonal value in a scalar variable `prevDiag`!

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                                   |
 :--- | :--- | :--- | :--- |:------------------------------------------------------------------------------------|
 **Single 1D DP Array** | **O(N x M)** | **O(min(N, M))** | **Staff Level (Optimal DP)** | Standard production choice; zero heap object allocations; minimal memory footprint. |
 **Two 1D DP Arrays** | **O(N x M)** | **O(M)** | Good | Baseline 1D DP; requires array swap logic or object reference swapping.             |
 **2D DP Table** | **O(N x M)** | **O(N x M)** | Baseline | Standard interview default for demonstrating full 2D state transitions.             |
 **Bit-Parallel LCS (Myers)** | **O(N x M / 64)** | **O(M / 64)** | **Staff Level (Extreme)** | Short strings (N ≤ 64); evaluates 64 character comparisons per CPU cycle.           |

*(where N = text1.length, M = text2.length)*

### 3. Native Kotlin Syntax Pitfalls
* **Avoid `curr.clone()` Memory Churn**: Calling `prev = curr.clone()` inside an outer loop allocates
  a **new `IntArray` on every row iteration**! Instead, track top-left diagonal state `prevDiag` in a
  scalar variable within a single `IntArray`.
* **String Swap Orientation Optimization**: Swapping strings (`if (text1.length < text2.length) text1 
to text2 else text2 to text1`) guarantees that `IntArray` size is strictly bounded by O(min(N, M)).

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (Single 1D Table - Staff Level Optimal)
// Time Complexity: O(N * M) | Auxiliary Space Complexity: O(min(N, M))
fun longestCommonSubsequence1D(text1: String, text2: String): Int {
    // Ensure s1 is shorter to guarantee O(min(N, M)) space
    val (s1, s2) = if (text1.length < text2.length) text1 to text2 else text2 to text1
    val n = s1.length
    val dp = IntArray(n + 1)

    for (c2 in s2) {
        var prevDiag = 0 // Represents dp[i-1][j-1] (top-left diagonal)
        for (j in 1..n) {
            val temp = dp[j] // Save top value before overwriting
            if (s1[j - 1] == c2) {
                dp[j] = prevDiag + 1
            } else {
                dp[j] = maxOf(dp[j], dp[j - 1])
            }
            prevDiag = temp // Advance top-left diagonal for next iteration
        }
    }

    return dp[n]
}

// Approach 2: Bottom-Up Dynamic Programming (2D Table)
// Time Complexity: O(N * M) | Auxiliary Space Complexity: O(N * M)
fun longestCommonSubsequence2D(text1: String, text2: String): Int {
    val m = text1.length + 1
    val n = text2.length + 1
    val dp = Array(m) { IntArray(n) }

    for (i in 1 until m) {
        for (j in 1 until n) {
            if (text1[i - 1] == text2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1] + 1
            } else {
                dp[i][j] = maxOf(dp[i][j - 1], dp[i - 1][j])
            }
        }
    }
    return dp[m - 1][n - 1]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Two 1D Arrays (`clone()`) vs. Single 1D Array with Diagonal Tracking (`prevDiag`)**:
    * Using `prev = curr.clone()` instantiates $M$ array allocations on the JVM Heap, triggering Young
      Generation Garbage Collection (GC) pauses.
    * Maintaining a single `IntArray(min(N, M) + 1)` and capturing `prevDiag = temp` eliminates object
      allocations completely during execution!
* **String Reconstructability (Reconstructing the LCS String)**:
    * To reconstruct the actual LCS string (not just its length), a full 2D DP matrix is required to
      backtrack pointers from `DP[M][N]` back to `DP[0][0]`.
    * **Hirschberg's Algorithm (Divide & Conquer + 1D DP)**: A mix-and-match and convoluted algorithm
      which reconstructs the actual LCS string in **O(N * M)** time, but it can achieve **O(min(N, M))**
      space! It is kind of rescue tool when dealing with large data like DNA alignment or git diff from
      large files to avoid memory crash.
* **Bit-Parallel LCS (Myers' Bit-Vector Algorithm)**:
    * For **N ≤ 64**, LCS can be formulated as bitwise operations on a 64-bit `Long` bitmask, achieving
      64x SIMD speedup.

---

## LC 518. Coin Change II
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **Unbounded Knapsack Combination Counting**: Finding the total number of unique coin combinations
      to form `amount` with infinite coin reuse.
    * **Loop Ordering Invariant (Outer Coins vs Outer Amounts)**:
        - **Outer loop `for (coin in coins)`**: Forces coins to be processed in a fixed order, ensuring
          **1+2+2** and **2+1+2** are counted as **1** unique combination.
        - **Reversing loop order (`for (j in 1..amount)` outer)** would count **Permutations (Order matters
        - LC 377)** instead of **Combinations (LC 518)**!
    * **Forward Inner Loop (`for (j in coin..amount)`)**:
      Iterating forward allows the current coin to be reused multiple times (**DP[j] += DP[j - coin]**),
      satisfying Unbounded Knapsack rules.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                       |
 :--- | :--- | :--- | :--- |:------------------------------------------------------------------------|
 **1D DP (Pruned & Sorted)** | **O(N x A)** | **O(A)** | **Staff Level (Optimal)** | Standard production choice; **A = amount, N = coins.size**              |
 **2D DP Table** | **O(N x A)** | **O(N x A)** | Baseline | Standard interview default for demonstrating full 2D state transitions. |

*(where A = amount, N = coins.size)*

### 3. Native Kotlin Syntax Pitfalls
* **Direct Array Indexing (`dp[amount]` vs `dp[dp.lastIndex]`)**: Accessing `dp[amount]` directly is
  clearer and eliminates property getter evaluation overhead.
* **Array Mutation Side Effects**: `coins.sort()` mutates the caller's parameter in-place. If API
  contract requires immutability, sort a copy (`val sortedCoins = coins.sortedArray()`).
* **Zero Amount Guard**: `if (amount == 0) return 1` provides an immediate **O(1)** exit condition
  for amount 0 (1 way: empty set).

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (1D Table)
// Time Complexity: O(N * amount) | Auxiliary Space Complexity: O(amount)
// Consider: What would happen to the result if you swapped the order of the two loops, and why
// does that change the logic from combinations to permutations?
fun change1D(amount: Int, coins: IntArray): Int {
    val dp = IntArray(amount + 1)
    dp[0] = 1

    for (coin in coins) {
        for (j in coin .. amount) {
            dp[j] += dp[j - coin]
        }
    }
    return dp[amount]
}

// Approach 2: Bottom-Up Memoization Dynamic Programming (2D Table)
// Time Complexity: O(N * amount) | Auxiliary Space Complexity: O(N * amount)
fun changeMemoization(amount: Int, coins: IntArray): Int {
    val dp = Array(coins.size + 1) { IntArray(amount + 1) }
    for (i in dp.indices) dp[i][0] = 1

    for (i in 1 until dp.size) {
        for (j in 1 until dp[i].size) {
            if (j >= coins[i - 1]) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]]
            } else {
                dp[i][j] = dp[i - 1][j]
            }
        }
    }
    return dp[coins.size][amount]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Combinations (LC 518) vs. Permutations (LC 377) Loop Ordering**:
    * **Combinations (LC 518)**: Outer loop = `for (coin in coins)`, Inner loop = `for (j in coin..amount)`.
      Processes coins one by one, eliminating duplicate ordering permutations.
    * **Permutations (LC 377)**: Outer loop = `for (j in 1..amount)`, Inner loop = `for (coin in coins)`.
      Evaluates all coins for each amount step, counting different orderings (**1+2** vs **2+1**) as
      distinct paths.
* **Polynomial Generating Functions (∏ (1 / (1 - x^coin)) for coin ∈ coins)**:
    * Mathematically, Coin Change II calculates the coefficient of (x^amount) in the power series expansion:
      P(x) = ∏ [1 / (1 - x^coin)] = (1 + x^c_1 + x^(2c_1) + ...) · (1 + x^c_2 + ...) for coin ∈ coins
    * In 1D DP, `dp[j] += dp[j - coin]` evaluates this polynomial expansion in-place in O(N * A) time!
    * Bitset Manipulation won't work on Unbound Knapsack case!!!

---


## LC 494. Target Sum
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **Mathematical Reduction to 0/1 Subset Sum**:
      Let **P** be the sum of positive numbers, and **N** be the sum of negative numbers:
      **P - N = target**
      **P + N = totalSum**
      Adding both equations: **2P = totalSum + target ⇒ P = (totalSum + target) / 2**.
      This reduces LC 494 to **finding the number of subsets with sum equal to P = (totalSum + target) / 2 **!
    * **Parity & Bounds Guard**:
      If **target + totalSum < 0** or **(totalSum + target) % 2 != 0**, it is mathematically impossible
      to assign signs to reach target; exit in O(N) time immediately with `return 0`.
    * **0/1 Knapsack Reverse Traversal Invariant**:
      Reversing the inner loop (`for (j in sumOfPos downTo num)`) ensures each number is used at most
      once per subset path.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time                | Space               | Performance | Best Used When...                                                                        |
 :--- |:--------------------|:--------------------| :--- |:-----------------------------------------------------------------------------------------|
 **Bitset Shift Optimization** | **O(N x S / 32)**   | **O(S / 32)**       | **Staff Level (Extreme)** | Zero object allocations; 32x hardware SIMD parallelism.                                  |
 **0/1 Subset Sum 1D DP** | **O(N x S)**        | **O(S)**            | **Staff Level (Optimal)** | Standard production choice; S = (totalSum + target) / 2.                                 |
 **Top-Down Memoized DFS** | **O(N x totalSum)** | **O(N x totalSum)** | Peak (Canonical) | Standard interview choice for demonstrating memoization on negative index state offsets. |
 **Un-memoized Brute Force DFS** | **O(2^N)**          | **O(N)**            | Poor | Exponential time limit exceeded (TLE) for N > 25.                                        |

*(where N = nums.size, S = (totalSum + target) / 2)*

### 3. Native Kotlin Syntax Pitfalls
* **Math Guard Condition**: Writing `if (kotlin.math.abs(target) > totalSum || (totalSum + target) 
% 2 != 0) return 0` handles both positive/negative `target` boundary checks in a single line.
* **Array Indexing**: Allocating `IntArray(sumOfPos + 1)` and initializing `dp[0] = 1` (1 way to form
  subset sum 0: the empty set) provides the base case for 0/1 Knapsack subset counting.

### 4. Code Block
```kotlin
    // Approach 1: Bitwise Parallelism / Polynomial Shift Optimization (Staff Level)
// Time Complexity: O(N * targetPos * K / 64) = O(N * targetPos / 2)
// Auxiliary Space Complexity: O(targetPos / 2)
fun findTargetSumWaysBitset(nums: IntArray, target: Int): Int {
    val totalSum = nums.sum()
    if (kotlin.math.abs(target) > totalSum || (totalSum + target) % 2 != 0) return 0
    val targetPos = (totalSum + target) / 2

    val k = 32 // 32 bits per counter slot (supports counts up to 4,294,967,295)
    var poly = 1.toBigInteger() // Slot 0 initialized to 1 way

    for (num in nums) {
        // Polynomial addition: poly = poly + (poly << (num * K))
        poly = poly.add(poly.shiftLeft(num * k))
    }

    // Extract 32-bit counter value at slot targetPos
    return poly.shiftRight(targetPos * k).and(0xFFFFFFFFL.toBigInteger()).toInt()
}

// Approach 2: 0/1 knapsack Bottom-Up Dynamic Programming (1D Table)
// Time Complexity: O(N * S) where N = nums.size, S = (sum + target) / 2 | Auxiliary Space Complexity: O(S)
fun findTargetSumWays01Knapsack1D(nums: IntArray, target: Int): Int {
    val totalSum = nums.sum()
    if (abs(target) > totalSum || 0 != (totalSum + target) % 2) return 0
    val newTarget = (totalSum + target) / 2
    val dp = IntArray(newTarget + 1)
    dp[0] = 1

    for (num in nums) {
        for (j in newTarget downTo num) {
            dp[j] += dp[j - num]
        }
    }
    return dp[newTarget]
}

// Approach 3: Top-Down Memoized DFS (Negative Index Offset)
// Time Complexity: O(N * totalSum) | Auxiliary Space Complexity: O(N * 2 * totalSum + 1)
fun findTargetSumWaysMemoization(nums: IntArray, target: Int): Int {
    val totalSum = nums.sum()
    if (abs(target) > totalSum) return 0

    val memo = Array(nums.size) { IntArray(2 * totalSum + 1) { -1 } }

    fun findTargetSumWaysEx(i: Int, sum: Int): Int {
        if (i == nums.size) {
            return if (sum == target) 1 else 0
        }

        val j = sum + totalSum
        if (memo[i][j] != -1) {
            return memo[i][j]   // Since the subproblem is resolved, no need to recompute again
        }

        val inclusion = findTargetSumWaysEx(i + 1, sum + nums[i]) // Include the current item
        val exclusion = findTargetSumWaysEx(i + 1, sum - nums[i]) // Exclude the current item

        memo[i][j] = inclusion + exclusion
        return memo[i][j]
    }

    return findTargetSumWaysEx(0, 0)
}

// Approach 4: 0/1 Knapsack (Bottom-Up Recursion + Memoization (Un-memoized Baseline)
// Time Complexity: O(2^N) where N = nums.size | Auxiliary Space Complexity: O(N)
fun findTargetSumWaysRecursion(nums: IntArray, target: Int): Int {
    var ways = 0

    fun findTargetSumWaysEx(i: Int, sum: Int) {
        if (i == nums.size) {
            if (sum == target) ways++
            return
        }
        findTargetSumWaysEx(i + 1, sum + nums[i]) // Include the current item
        findTargetSumWaysEx(i + 1, sum - nums[i]) // Exclude the current item
    }

    findTargetSumWaysEx(0, 0)
    return ways
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **0/1 Subset Sum Reduction vs 2D DP Table**:
    * **Un-reduced 2D DP**: **DP[i][s]** = ways to reach sum **s** using first **i** numbers. Sum range
      is **[-totalSum, totalSum], requiring array size `2 x totalSum + 1` with index offset mapping
      (`s + totalSum`).
    * **Reduced 1D Subset Sum DP**: **2 * ∑(P) = target + ∑(nums)  ⇒  ∑(P) = (target + ∑(nums)) / 2**.
      Reduces space down to a 1D array of size P + 1 with zero negative index offsets!
* **Bitwise Parallelism for Subset Sum Counting (Polynomial Generating Functions)**:
    * **Boolean Reachability (LC 416 - $1$ Bit Per Slot)**: `bitset = bitset or (bitset shl num)`
      evaluates 64 boolean target sums simultaneously in 1 CPU cycle (64x speedup).
    * **Subset Sum Counting (LC 494 - $32$ Bits Per Slot)**: The number of ways to form target sums
      corresponds to polynomial coefficients of the generating function:
      `P(x) = (1 + x^num_1) × (1 + x^num_2) × ... × (1 + x^num_N) = ∑ dp[j] x^j`
      By packing K-bit counter slots ($K = 32$ bits per slot) into a `BigInteger`, the state transition
      `DP[j] = DP[j] + DP[j - num]` simplifies to a single bitwise shift and addition:
      `poly = poly + (poly << (num * K))`
    * **Parallelism Ratio**: With K = 32 bits per slot, a single 64-bit CPU word addition adds
      **64 / 32 = 2 counter slots in parallel per 64-bit register word (2x SIMD word parallelism)**,
      while shifting all S slots in 1 bitwise shift operation!
    * **Counter Slot Size Requirement (K = 32 bits)**: Since N elements can produce up to 2^N ways
      (e.g., 20 zeros produces 2^{20} = 524,288 ways), each slot must be allocated K = 32 bits
      (0xFFFFFFFFL mask) to hold counts up to (2^{32} - 1 = 4,294,967,295) without carrying over into
      adjacent slots!

---

## LC 416. Partition Equal Subset Sum
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **0/1 Knapsack Decision Model**: Partitioning array into two equal subset sums requires finding
      a subset A ⊆ nums such that the sum over x ∈ A ... x = target = sum(nums) / 2. This is 0/1 Knapsack
      with item weight = value = x and capacity = sum / 2.
    * **Odd Sum Parity Guard**: If total sum(nums) is odd (sum % 2 != 0), equal integer partitioning
      is mathematically impossible; exit in O}(N) time immediately.
    * **0/1 Knapsack Reverse Traversal Invariant**: In 1D DP, the inner loop MUST iterate backwards
      (`for (j in target downTo num)`) to ensure each number x is used at most once per subset path!

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                       |
 :--- | :--- | :--- | :--- |:------------------------------------------------------------------------|
 **Bitset Shift Optimization** | **O(N x S / 64)** | **O(S / 64)** | **Staff Level (Extreme)** | Zero object allocations; 64x hardware SIMD parallelism.                 |
 **1D Bottom-Up DP** | **O(N x S)** | **O(S)** | **Staff Level (Optimal DP)** | Standard production choice; **S = sum / 2**.                            |
 **2D Bottom-Up DP** | **O(N x S)** | **O(N x S)** | Baseline | Standard interview default for demonstrating full 2D state transitions. |

*(where N = nums.size, S = sum / 2)*

### 3. Native Kotlin Syntax Pitfalls
* **Inner Loop Boundary Pruning**: Stop inner loop at `num` (`for (j in target downTo num)`) instead
  of decrementing down to 1 with `if (j >= num)`. This cuts unnecessary inner checks by up to 50\% on
  large values.
* **Early Exit Invariant**: Checking `if (dp[target]) return true` after processing each item exits
  the loop early as soon as target sum becomes reachable, avoiding remaining item evaluations.
* **2D Array Allocation Overhead**: `Array(m) { BooleanArray(n) }` allocates N+1 separate heap arrays.
  1D DP uses a single contiguous primitive `BooleanArray(target + 1)` with superior L1/L2 cache line
  locality.

### 4. Code Block
```kotlin
// Approach 1: Bitset / BigInteger Shift Optimization (Staff Level - 64x Speedup)
// Time Complexity: O(N * target / 64) | Auxiliary Space Complexity: O(target / 64)
fun canPartition(nums: IntArray): Boolean {
    val totalSum = nums.sum()
    if (1 == (totalSum and 1)) return false
    val target = totalSum shr 1

    var bitset = 1.toBigInteger()

    for (num in nums) {
        bitset = bitset or (bitset shl num)
        if (bitset.testBit(target)) return true
    }

    return bitset.testBit(target)
}

// Approach 2: Bottom-Up Dynamic Programming (1D Table Pruned)
// Time Complexity: O(N * target) | Auxiliary Space Complexity: O(target)
fun canPartition1D(nums: IntArray): Boolean {
    val totalSum = nums.sum()
    if (0 != totalSum % 2) return false
    val target = totalSum / 2
    val dp = BooleanArray(target + 1)
    dp[0] = true

    for (num in nums) {
        for (j in target downTo num) {
            dp[j] = dp[j] || dp[j - num]
        }
        if (dp[target]) return true
    }
    return dp[dp.lastIndex]
}

// Approach 3: Bottom-Up Dynamic Programming (2D Table)
// Time Complexity: O(N * target) | Auxiliary Space Complexity: O(N * target)
fun canPartition2D(nums: IntArray): Boolean {
    val totalSum = nums.sum()
    if (0 != totalSum % 2) return false
    val target = totalSum / 2
    val dp = Array(nums.size + 1) { BooleanArray(target + 1) }
    for (i in 0 .. nums.size) dp[i][0] = true

    for (i in 1 until dp.size) {
        for (j in 1 until dp[i].size) {
            if (j >= nums[i - 1]) {
                dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]]
            } else {
                dp[i][j] = dp[i - 1][j]
            }
        }
        if (dp[i][target]) return true
    }
    return dp[dp.lastIndex][target]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **0/1 Knapsack vs. Unbounded Knapsack**:
    * In **0/1 Knapsack** (Partition Equal Subset Sum), inner loop runs **backwards** (`target downTo 
    num`) so item **num** is used at most once.
    * In **Unbounded Knapsack** (Coin Change), inner loop runs **forwards** (`num..target`) so items
      can be reused infinitely many times.
* **Hardware SIMD Parallelism via Bitset (`bitset or (bitset shl num)`)**:
    * **Staff Power Insight**: Representing target sum reachability as a bitmask evaluates 64 target
      sum transitions in a single 64-bit CPU bitwise OR `|` and shift `<<` instruction!
    * Delivers a **64\times execution speedup** and **64\times memory reduction** over standard DP tables.
* **Pseudo-Polynomial Time Complexity Limitations**:
    * O(N * S) is **Pseudo-Polynomial** because S = sum / 2 depends on numerical magnitude rather than
      input length N. If array elements are huge (e.g., 10^9), DP table memory explodes, whereas
      Meet-in-the-Middle (O(2^(N/2))) becomes superior for small N <= 40.

---
