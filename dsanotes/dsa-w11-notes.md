# LeetCodePrep Week 11

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 11: DYNAMIC PROGRAMMING (DP) (October 19 - October 23)
# ===============================================================

## Topics Covered
- [ ] 2D DP
- [ ] Tail Recursion

## Day 53 - LC 518. Coin Change II
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

*(where $A = \text{amount}, N = \text{coins.size}$)*

### 3. Native Kotlin Syntax Pitfalls
* **Direct Array Indexing (`dp[amount]` vs `dp[dp.lastIndex]`)**: Accessing `dp[amount]` directly is 
clearer and eliminates property getter evaluation overhead.
* **Array Mutation Side Effects**: `coins.sort()` mutates the caller's parameter in-place. If API 
contract requires immutability, sort a copy (`val sortedCoins = coins.sortedArray()`).
* **Zero Amount Guard**: `if (amount == 0) return 1` provides an immediate **O(1)** exit condition 
for amount 0 (1 way: empty set).

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (1D Table Pruned)
// Time Complexity: O(N * amount) where N = coins.size | Auxiliary Space Complexity: O(amount)
fun change(amount: Int, coins: IntArray): Int {
    if (amount == 0) return 1
    val dp = IntArray(amount + 1)
    dp[0] = 1

    coins.sort() // Sort coins to enable early outer loop break

    for (coin in coins) {
        if (coin > amount) break // Skip coins larger than amount
        for (j in coin..amount) {
            dp[j] += dp[j - coin]
        }
    }

    return dp[amount]
}

// Approach 2: Bottom-Up Dynamic Programming (2D Table)
// Time Complexity: O(N * amount) | Auxiliary Space Complexity: O(N * amount)
fun change2D(amount: Int, coins: IntArray): Int {
    val m = coins.size + 1
    val n = amount + 1
    val dp = Array(m) { IntArray(n) }

    for (i in 0 until m) dp[i][0] = 1

    for (i in 1 until m) {
        val coin = coins[i - 1]
        for (j in 1 until n) {
            dp[i][j] = dp[i - 1][j] // Exclude current coin
            if (j >= coin) {
                dp[i][j] += dp[i][j - coin] // Include current coin
            }
        }
    }

    return dp[m - 1][amount]
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

---

## Day 52 - LC 494. Target Sum
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

 Approach | Time | Space | Performance | Best Used When...                                                                        |
 :--- | :--- | :--- | :--- |:-----------------------------------------------------------------------------------------|
 **0/1 Subset Sum 1D DP** | **O(N x S)** | **O(S)** | **Staff Level (Optimal)** | Standard production choice; S = (totalSum + target) / 2.                                 |
 **Top-Down Memoized DFS** | **O(N x totalSum)** | **O(N x totalSum)** | Peak (Canonical) | Standard interview choice for demonstrating memoization on negative index state offsets. |
 **Un-memoized Brute Force DFS** | **O(2^N)** | **O(N)** | Poor | Exponential time limit exceeded (TLE) for N > 25.                                        |

*(where N = nums.size, S = (totalSum + target) / 2)*

### 3. Native Kotlin Syntax Pitfalls
* **Math Guard Condition**: Writing `if (kotlin.math.abs(target) > totalSum || (totalSum + target) 
% 2 != 0) return 0` handles both positive/negative `target` boundary checks in a single line.
* **Array Indexing**: Allocating `IntArray(sumOfPos + 1)` and initializing `dp[0] = 1` (1 way to form 
subset sum 0: the empty set) provides the base case for 0/1 Knapsack subset counting.

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (1D Subset Sum Table)
// Time Complexity: O(N * S) where S = (totalSum + target) / 2 | Auxiliary Space Complexity: O(S)
fun findTargetSumWays(nums: IntArray, target: Int): Int {
    val totalSum = nums.sum()
    if (kotlin.math.abs(target) > totalSum || (totalSum + target) % 2 != 0) return 0
    val sumOfPos = (totalSum + target) / 2
    val dp = IntArray(sumOfPos + 1)
    dp[0] = 1

    for (num in nums) {
        for (j in sumOfPos downTo num) {
            dp[j] += dp[j - num]
        }
    }

    return dp[sumOfPos]
}

// Approach 2: Top-Down Memoized DFS (Negative Index Offset)
// Time Complexity: O(N * totalSum) | Auxiliary Space Complexity: O(N * totalSum)
fun findTargetSumWaysMemoization(nums: IntArray, target: Int): Int {
    val totalSum = nums.sum()
    if (kotlin.math.abs(target) > totalSum) return 0

    // Memo array size: N x (2 * totalSum + 1)
    val memo = Array(nums.size) { IntArray(2 * totalSum + 1) { -1 } }

    fun dfs(idx: Int, currentSum: Int): Int {
        if (idx == nums.size) {
            return if (currentSum == target) 1 else 0
        }

        // Offset negative sum to non-negative array index [0 .. 2 * totalSum]
        val memoIdx = currentSum + totalSum
        if (memo[idx][memoIdx] != -1) {
            return memo[idx][memoIdx]
        }

        val add = dfs(idx + 1, currentSum + nums[idx])
        val subtract = dfs(idx + 1, currentSum - nums[idx])

        memo[idx][memoIdx] = add + subtract
        return memo[idx][memoIdx]
    }

    return dfs(0, 0)
}

// Approach 3: Un-memoized Recursion (Exponential Baseline)
// Time Complexity: O(2^N) | Auxiliary Space Complexity: O(N) stack
fun findTargetSumWaysRecursion(nums: IntArray, target: Int): Int {

    fun findTargetSumWaysRecursionEx(nums: IntArray, target: Int, curIdx: Int, curSum: Int): Int {
        if (curIdx == nums.size) {
            return if (target == curSum) 1 else 0
        }

        return findTargetSumWaysRecursionEx(nums, target, curIdx + 1, curSum - nums[curIdx]) +
                findTargetSumWaysRecursionEx(nums, target, curIdx + 1, curSum + nums[curIdx])
    }

    return findTargetSumWaysRecursionEx(nums, target, 0, 0)
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
  * **Boolean Reachability (LC 416)**: `bitset = bitset or (bitset shl num)` evaluates 64 target sum 
  transitions in 1 CPU cycle (O(N × S / 64) time).
  * **Subset Sum Counting (LC 494)**: The number of ways to form target sums corresponds to polynomial 
  coefficients of the generating function:
    `P(x) = (1 + x^num_1) × (1 + x^num_2) × ... × (1 + x^num_N) = ∑ dp[j] x^j`
  By packing K-bit counter slots (e.g., K = 16 bits per slot) into a `BigInteger`, the state 
  transition `DP[j] = DP[j] + DP[j - num]` simplifies to a single bitwise shift and addition:
    `poly = poly + (poly << (num * K))`
  Evaluating **`64 / K`** slot additions per CPU instruction achieves hardware SIMD parallelism for subset sum counting!
  transitions per CPU instruction, achieving hardware SIMD parallelism.

---

## Day 51 - LC 416. Partition Equal Subset Sum
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
// Approach 1: Bottom-Up Dynamic Programming (1D Table Pruned)
// Time Complexity: O(N * S) where S = sum / 2 | Auxiliary Space Complexity: O(S)
fun canPartition(nums: IntArray): Boolean {
    val sum = nums.sum()
    if (sum % 2 != 0) return false
    val target = sum / 2

    val dp = BooleanArray(target + 1)
    dp[0] = true

    for (num in nums) {
        for (j in target downTo num) { // Reverse inner loop for 0/1 Knapsack
            dp[j] = dp[j] || dp[j - num]
        }
        if (dp[target]) return true // Early exit
    }

    return dp[target]
}

// Approach 2: Bottom-Up Dynamic Programming (2D Table)
// Time Complexity: O(N * S) where S = sum / 2 | Auxiliary Space Complexity: O(N * S)
fun canPartition2D(nums: IntArray): Boolean {
    val sum = nums.sum()
    if (sum % 2 != 0) return false
    val target = sum / 2
    val m = nums.size + 1
    val n = target + 1
    val dp = Array(m) { BooleanArray(n) }

    for (i in 0 until m) dp[i][0] = true

    for (i in 1 until m) {
        for (j in 1 until n) {
            if (j >= nums[i - 1]) {
                dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]]
            } else {
                dp[i][j] = dp[i - 1][j]
            }

            if (dp[i][j] && j == target) {
                return true
            }
        }
    }
    return dp[m - 1][target]
}

// Approach 3: Bitset / BigInteger Shift Optimization (Staff Level - 64x Speedup)
// Time Complexity: O(N * S / 64) | Auxiliary Space Complexity: O(S / 64)
fun canPartitionBitset(nums: IntArray): Boolean {
    val sum = nums.sum()
    if (sum % 2 != 0) return false
    val target = sum / 2

    var bitset = 1.toBigInteger() // Bit 0 is set (0 sum achievable)

    for (num in nums) {
        bitset = bitset or (bitset shl num) // Bitwise shift evaluates 64 target sums in 1 CPU cycle
    }

    return bitset.testBit(target)
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
