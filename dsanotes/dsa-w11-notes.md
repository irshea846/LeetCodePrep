# LeetCodePrep Week 11

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 11: DYNAMIC PROGRAMMING (DP) (October 19 - October 23)
# ===============================================================

## Topics Covered
- [ ] 2D DP
- [ ] Tail Recursion

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
