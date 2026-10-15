# LeetCodePrep Week 10

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 10: DYNAMIC PROGRAMMING (DP) (October 6, October 12 - October 16)
# ===============================================================

## Topics Covered
- [ ] DP
- [ ] Tail Recursion

## Day 49 - LC 62. Unique Paths
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Overlapping Subproblems Invariant**: To reach cell (i, j), you must come from cell (i-1, j) (top) 
  or cell (i, j-1) (left). Thus, uniquePaths(i, j) = uniquePaths(i-1, j) + uniquePaths}(i, j-1).
  * **1D Space Optimization**: Since computing row i only depends on row i-1, we compress 2D memory 
  from O(M * N) down to O(min(M, N)) by maintaining a 1D DP table representing the active row.
  * **Combinatorics Closed-Form**: Since every path requires (m - 1) Down moves and (n - 1) Right 
  moves out of (m + n - 2) total moves, paths equal the combination C(m + n - 2, min(m - 1, n - 1)). 
  This reduces time to O(min(M, N)) and space to O(1).

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Combinatorics (Interleaved Primitive)** | **O(min(M, N))** | **O(1)** | **Staff Level (Extreme)** | Unconstrained grid; zero heap allocations; $O(1)$ constant memory. |
 **1D Bottom-Up DP** | **O(M * N)** | **O(min(M, N))** | **Staff Level (Optimal DP)** | Extensible to grids with obstacles (LC 63) or weights (LC 64). |
 **2D Bottom-Up DP** | **O(M * N)** | **O(M * N)** | Baseline | Standard interview default for demonstrating full 2D state transitions. |

### 3. Native Kotlin Syntax Pitfalls
* **Orientation Space Optimization (`minOf`)**: Orienting the 1D DP table using `val cols = minOf(m, n)` 
ensures space complexity is strictly O(min(M, N)) even if M » N.
* **Branchless DP Array Initialization (`IntArray(cols) { 1 }`)**: Pre-populating the 1D DP array with 
1s (`IntArray(cols) { 1 }`) eliminates M * N inner `if (i == 0 || j == 0)` conditional branch checks!
* **Interleaved Multiplication/Division**: Interleaving multiplication and division 
(`result = result * (total - k + i) / i`) keeps intermediate values small, enabling pure 64-bit 
primitive `Long` computation without `BigInteger` object allocations.

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (1D Table Branchless)
// Time Complexity: O(M * N) | Auxiliary Space Complexity: O(min(M, N))
fun uniquePaths(m: Int, n: Int): Int {
    val cols = minOf(m, n)
    val rows = maxOf(m, n)
    val dp = IntArray(cols) { 1 } // Row 0 initialized to 1s (branchless)

    for (r in 1 until rows) {
        for (c in 1 until cols) {
            dp[c] += dp[c - 1]
        }
    }

    return dp[cols - 1]
}

// Approach 2: Zero-Allocation Primitive Combinatorics
// Time Complexity: O(min(M, N)) | Auxiliary Space Complexity: O(1)
fun uniquePathsMath(m: Int, n: Int): Int {
    val k = minOf(m - 1, n - 1)
    val total = m + n - 2
    var result = 1L

    for (i in 1..k) {
        result = result * (total - k + i) / i
    }

    return result.toInt()
}

// Approach 3: BigInteger Combinatorics
// Time Complexity: O(min(M, N)) | Auxiliary Space Complexity: O(1)
fun uniquePathsCombinatorics(m: Int, n: Int): Int {
    val totalSlots = m + n - 2
    val optimalMoves = minOf(m - 1, n - 1)

    var steps = 1.toBigInteger()
    val lowerBound = totalSlots - optimalMoves + 1
    for (i in totalSlots downTo lowerBound) {
        steps = steps.times(i.toBigInteger())
    }

    for (i in optimalMoves downTo 2) {
        steps = steps.div(i.toBigInteger())
    }

    return steps.toInt()
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Dynamic Programming vs. Closed-Form Combinatoric Math**:
  * **1D Dynamic Programming (O(M * N)$ time, O(min(M, N)) space)**: Standard production choice when
  grid dimensions are small/medium (M, N ≤ 100). Highly extensible to variations like **LC 63 
  (Unique Paths II with Obstacles)** or **LC 64 (Minimum Path Sum with Weighted Cells)** where 
  mathematical combinatorics fails due to broken grid symmetry.
  * **Combinatorics O(min(M, N) time, O(1) space**: Optimal choice for unconstrained grids. Computes
  **C(m + n - 2, min(m - 1, n - 1))** directly in O(min(M, N) time.
* **Interleaved Division vs. `BigInteger` Heap Allocations**:
  * Factorials grow faster than exponential functions (N!). Multiplying N · (N - 1) ... before 
  dividing by K! causes `Long` overflow.
  * Interleaving multiplication and division (`result = result * (total - k + i) / i`) keeps 
  intermediate values small, enabling pure primitive `Long` computation with **zero `BigInteger` 
  heap object allocations**.
* **Extension to D-Dimensional Grid Navigation**:
  * In a D-dimensional grid with dimensions N₁, N₂, ..., N_D, total moves = `Σ (N_i - 1) 
  [for i = 1 to D]`.
  * The total unique paths formula generalizes to the **Multinomial Coefficient**: 
                  ( Σ (N_i - 1) )! / ∏ ( (N_i - 1)! )


---

## Day 48 - LC 139. Word Break
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **String Segmentation Overlapping Subproblems**: Determining if string **s[0 .. N-1]** can be 
  segmented into dictionary words requires finding at least one split index **j** (**0 ⩽ j < i**) 
  where **s[0 ⋯ j-1]** is valid (DP[j] == true) AND s[j .. i-1] exists in `wordSet`.
  * **Max Word Length Bounded Search Window**: Instead of scanning j all the way back to index 0 
  (O(N^2)) inner iterations), dictionary word lengths are bounded (L = maxLen ⩽ 20). Pruning the 
  inner loop to scan only `j in (i-1) downTo max(0, i - L) reduces inner checks from $\mathcal{O}(N)$ 
  to O(L).

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                                        |
 :--- | :--- | :--- | :--- |:-----------------------------------------------------------------------------------------|
 **DP + Max Word Length Pruning** | **O(N x L^2)** | **O(N + M)** | **Staff Level (Optimal)** | Standard production choice; L <= 20 converts O(N^3) string slicing to fast O(N * L^2).   |
 **DP + Unbounded Search Window** | **O(N^3)** | **O(N + M)** | Moderate | Vulnerable to Time Limit Exceeded (TLE) on long repetitive strings like `"aaaa...b"`.    |
 **Trie + DP Traversal** | **O(N x L)** | **O(N + M x L)** | **Staff Level (Zero Allocation)** | Zero `String.substring` heap allocations; ideal for high-throughput dictionary searches. |

*(where $N = s.\text{length}$, $M = \text{wordDict.size}$, $L = \text{max word length}$)*

### 3. Native Kotlin Syntax Pitfalls
* **`wordDict.toHashSet()` Idiom**: Avoid manual `HashSet` instantiation loops 
(`for (word in wordDict) wordSet.add(word)`). Use `val wordSet = wordDict.toHashSet()` or 
`HashSet(wordDict)`.
* **Max Word Length Calculation**: `val maxLen = wordDict.maxOf { it.length }` provides the exact 
upper bound for string slicing windows in a single readable pass.
* **Substring Allocation Churn**: Calling `s.substring(j, i)` creates a **new `String` heap object** 
on every check. Bounding j by `maxLen` prevents excessive Young Generation Garbage Collection (GC) 
pauses on long strings.

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up DP + Max Word Length Pruning (Staff Level Optimal)
// Time Complexity: O(N * L^2) where L = max word length | Auxiliary Space Complexity: O(N + M)
fun wordBreak(s: String, wordDict: List<String>): Boolean {
    if (s.isEmpty() || wordDict.isEmpty()) return false

    val wordSet = wordDict.toHashSet()
    val maxLen = wordDict.maxOf { it.length }
    val dp = BooleanArray(s.length + 1)
    dp[0] = true

    for (i in 1..s.length) {
        // Prune search space: only check inner split j up to max word length
        for (j in (i - 1) downTo maxOf(0, i - maxLen)) {
            if (dp[j] && wordSet.contains(s.substring(j, i))) {
                dp[i] = true
                break
            }
        }
    }

    return dp[s.length]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **The "aaaa...b" Stress Test (TLE Vulnerability)**:
  * Consider s = "aaaaaaaaaaaaaaaaaaaa...b" with `wordDict = ["a", "aa", "aaa", ...]`.
  * Without L = maxLen pruning, an unbounded inner loop scans j across all 0 .. i-1, generating O(N^2) 
  substring allocations and hashing operations (O(N^3) total time), causing **Time Limit Exceeded (TLE)**.
  * With L = maxLen} pruning, the inner loop executes at most L steps, cutting execution time by over 
  **100 times** on long inputs.
* **Trie (Prefix Tree) vs. HashSet**:
  * In production search engines or spellcheckers (e.g., search autocompletion), building a **Trie** 
  from `wordDict` allows traversing characters in $s$ directly on Trie nodes without calling 
  `String.substring()`, achieving **Zero Heap Object Allocations**.

---

## Day 47 - LC 198. House Robber
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Non-Adjacent Selection Invariant**: You cannot rob two adjacent houses without triggering 
  security alarms. At each house **i**, you must decide:
    1. **Rob house **i****: Earn nums[i] + loot(i - 2)
    2. **Skip house **i****: Earn loot(i - 1)
       loot(i) = max(loot(i - 1), loot(i - 2) + nums[i])
  * **Space Optimization Progression**: Computing loot(i) depends strictly on the previous two 
  decisions (loot(i - 1) and loot(i - 2)). We can optimize space from an O(N) DP table down to 
  O(1) constant memory using two scalar variables (`prev1` and `prev2`).

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                              |
 :--- | :--- | :--- | :--- |:-------------------------------------------------------------------------------|
 **In-Place Bottom-Up DP** | **O(N)** | **O(1)** | **Staff Level (Optimal)** | Standard production choice; zero heap allocation and **O(1)** constant memory. |
 **`tailrec` Functional Recursion** | **O(N)** | **O(1)** | **Staff Level (Functional)** | Idiomatic Kotlin; compiler transforms tail recursion into a bytecode loop.     |
 **DP Array Table** | **O(N)** | **O(N)** | Peak (Canonical) | Standard interview default for demonstrating full DP table state transitions.  |

### 3. Native Kotlin Syntax Pitfalls
* **Zero-Initialization Prev Variables**: Initializing `prev2 = 0` and `prev1 = 0` allows a single 
uniform `for (num in nums)` loop that handles all N ≥ 1 without edge-case branching for N = 1 or 
N = 2.
* **Forward Tail-Recursive Parameters**: Rather than using complex reverse index math `nums[n - i + 1]`, 
pass `prev2` and `prev1` directly as parameters moving forward from index `0 .. N - 1`:
  `helper(idx + 1, prev1, maxOf(prev1, prev2 + nums[idx]))`

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (In-Place O(1) Space)
// Time Complexity: O(N) | Auxiliary Space Complexity: O(1)
fun robInPlace(nums: IntArray): Int {
    var prev2 = 0 // Max profit up to house i-2
    var prev1 = 0 // Max profit up to house i-1

    for (num in nums) {
        val curr = maxOf(prev1, prev2 + num)
        prev2 = prev1
        prev1 = curr
    }

    return prev1
}

// Approach 2: Bottom-Up Dynamic Programming (DP Table)
// Time Complexity: O(N) | Auxiliary Space Complexity: O(N)
fun robTable(nums: IntArray): Int {
    if (nums.isEmpty()) return 0
    if (nums.size == 1) return nums[0]

    val dp = IntArray(nums.size)
    dp[0] = nums[0]
    dp[1] = maxOf(nums[0], nums[1])

    for (i in 2 until nums.size) {
        dp[i] = maxOf(dp[i - 1], dp[i - 2] + nums[i])
    }

    return dp[dp.lastIndex]
}

// Approach 3: Tail-Recursive Functional DP (Kotlin 'tailrec' O(1) Space)
// Time Complexity: O(N) | Auxiliary Space Complexity: O(1) bytecode loop
fun robTailRec(nums: IntArray): Int {
    tailrec fun helper(idx: Int, prev2: Int, prev1: Int): Int {
        if (idx == nums.size) return prev1
        return helper(idx + 1, prev1, maxOf(prev1, prev2 + nums[idx]))
    }

    return helper(0, 0, 0)
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **House Robber Problem Variations**:
  * **LC 213. House Robber II (Circular Neighborhood)**:
    * First and last houses are adjacent.
    * **Staff Insight**: Break the circular dependency by solving two linear House Robber subproblems: 
    `rob(0 until n - 1)` and `rob(1 until n)`, returning **max(sub1, sub2)**.
  * **LC 337. House Robber III (Binary Tree Neighborhood)**:
    * Houses form a Binary Tree.
    * **Staff Insight**: Tree Post-Order Traversal (DFS) returning `Pair(robThisNode, skipThisNode)` 
    for each subtree in O(N) time and O(height) stack space.
* **Space Optimization Progression**:
  * O(N) DP table allocates unnecessary array memory. Since each state only depends on i - 1 and i - 2, 
  sliding scalar state variables (`prev2`, `prev1`) achieve 100\% optimal O}(1) space performance.

---

## Day 46 - LC 1137. N-th Tribonacci Number
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Overlapping Subproblems Invariant**: To reach tribonacci(n), we must come from `tribonacci(n - 1)`, 
  `tribonacci(n - 2)` and `tribonacci(n - 3)`.
  * **Space Optimization Progression**: Since computing tribonacci(n) only depends on the previous 
  three values, we can optimize memory from an O(N) DP table down to O(1) constant space using three
  variables or directly assign previous values in the tail recursive function parameters. Since it's
  a tail recursion, it won't take any heap allocation or stack frame creation.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time         | Space | Performance | Best Used When...                                                             |
 :--- |:-------------| :--- | :--- |:------------------------------------------------------------------------------|
 **Matrix Exponentiation** | **O(log N)** | **O(1)** | **Staff Level (Extreme N)** | Very large **N** (**N > 10⁹ or N = 10¹⁸**); uses repeated matrix cubing.      |
 **In-Place Bottom-Up DP** | **O(N)**     | **O(1)** | **Staff Level (Optimal)** | Standard production choice; zero heap allocation and O(1) constant memory.    |
 **`tailrec` Functional Recursion** | **O(N)**     | **O(1)** | **Staff Level (Functional)** | Idiomatic Kotlin; compiler transforms tail recursion into a bytecode loop.    |
 **DP Array Table** | **O(N)**     | **O(N)** | Peak (Canonical) | Standard interview default for demonstrating full DP table state transitions. |

### 3. Native Kotlin Syntax Pitfalls
* **The `tailrec` Compiler Optimization**: Marking a recursive function as `tailrec` in Kotlin 
instructs the compiler to optimize tail call recursion into a fast, iterative `while` loop in 
bytecode, eliminating call stack frame creation and guaranteeing 𝒪(1) space!
* **Previous Values Storage**: Instead of creating three variables `prev_3`, `prev_2`, and `prev_1`,
we use three parameters in the tail recursive function to save extra memory. When `tribonacci(n)` 
is called, we can persist the top-down counter starting with `n` in the first parameter, 
`tribonacci(n)` in the second parameter, `tribonacci(n-1)` in the third parameter and 
`tribonacci(n-2)` in the fourth parameter until the last `i == 0 .. 2` is reached. We've already 
had the last three values in the tail recursive function and return the second parameter when n > 2.

### 4. Code Block
```kotlin
// Approach 1: Top-Down Recursion with Memoization
// Time Complexity: O(n) | Auxiliary Space Complexity: O(1)
fun tribonacciTailRec(n: Int): Int {

    tailrec fun tribonacciEx(i: Int, c: Int, b: Int, a: Int): Int {
        return if (i > 2) tribonacciEx(i - 1, a + b + c, c, b) else
          if (i == 2) c else if (i == 1) b else a
    }

    return tribonacciEx(n, 1, 1, 0)
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **𝒪(K³ log N) Matrix Exponentiation**
  * where K is the size of the matrix
  * Because K = 3 for Tribonacci, the matrix multiplication overhead is a fixed constant of 3³ = 27 
  operations. Since 27 is a fixed number that never changes regardless of how large N gets, 
  **𝒪(27 * Log N)** simplifies perfectly into a pure O(log N) runtime.
  * Representing state transitions as matrix multiplication:
    [T(n+1); T(n); T(n-1)] = [[1, 1, 1];; [0, 1, 0]]ⁿ * [T(1); T(0); T(-1)]

---


## Day 46 - LC 509. Fibonacci Number
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Overlapping Subproblems Invariant**: To reach Fib(n), we must come from Fib(n - 1) and 
  Fib(n - 2).
  * **Space Optimization Progression**: Since computing Fib(n) only depends on the previous two 
  states.
  * **Binet's Closed-Form Formula**: Since Binet's already found the **Golden Ratio 
  (𝜑 = (1 + √5) / 2)** in the Fibonacci Series and created a closed-form formula for 
  **Fib(n) = (𝜑ⁿ + 𝜓ⁿ) / √5 where 𝜑 = (1 + √5) / 2 and 𝜓 = (1 - √5) / 2**. From the induction in
  Fibonacci Series, 𝜓ⁿ is approaching to 0 very quick, we can safely exclude it from the formula. 
  So the formula can be simplified to: **Fib(n) = 𝜑ⁿ / √5**
  

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time         | Space | Performance | Best Used When...                                                                                 |
 :--- |:-------------| :--- | :--- |:--------------------------------------------------------------------------------------------------|
 **Binet's Closed-Form Formula** | **O(1)**     | **O(1)** | Good | Mathematical closed-form; susceptible to IEEE 754 floating-point floor truncation for **N > 70**. |
 **Matrix Exponentiation** | **O(log N)** | **O(1)** | **Staff Level (Extreme N)** | Very large **N** (**N > 10⁹ or N = 10¹⁸**); uses repeated matrix squaring.                        |
 **In-Place Bottom-Up DP** | **O(N)**     | **O(1)** | **Staff Level (Optimal)** | Standard production choice; zero heap allocation and O(1) constant memory.                        |
 **`tailrec` Functional Recursion** | **O(N)**     | **O(1)** | **Staff Level (Functional)** | Idiomatic Kotlin; compiler transforms tail recursion into a bytecode loop.                        |
 **DP Array Table** | **O(N)**     | **O(N)** | Peak (Canonical) | Standard interview default for demonstrating full DP table state transitions.                     |

### 3. Native Kotlin Syntax Pitfalls
* **Index Shifting**: In this problem, Fibonacci Series are 0, 1, 1, 2, 3, 5, 8, 13, ... Using Binet 
should be very careful to put the right number of power which is `phi.pow(n) / sqrt5` while 
`phi.pow(n + 1) / sqrt5` is for Fibonacci Series 1, 1, 2, 3, 5, 8, 13, ...
* **Floating-Point Floor Truncation (`round()` vs `.toInt()`)**: Converting floating-point mathematical
results (e.g. `(phi.pow(n + 1) ...).toInt()`) directly with `.toInt()` performs floor truncation.

### 4. Code Block
```kotlin
// Approach 1: Binet's Closed-Form Formula Excluding 𝜓 = (1 - √5) / 2 (The Disappearing Component)
// Time Complexity: O(1) | Auxiliary Space Complexity: O(1)
fun fibBitnet(n: Int): Int {
        val sqrt5 = sqrt(5.0)
        val phi = (1.0 + sqrt5) / 2

        return round(phi.pow(n) / sqrt5).toInt()
    }
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **𝒪(log N) Matrix Exponentiation**:
  * For **N > 10⁹ or N = 10¹⁸**, linear 𝒪(N) loops are too slow.
  * Representing state transitions as matrix multiplication:
    [F(n+1); F(n)] = [[1, 1]; [1, 0]]ⁿ * [F(1); F(0)]
  * Using **Binary Exponentiation** (repeated matrix squaring), [1, 1; 1, 0]ⁿ can be computed in
    **𝒪(log N) time**!
* **Binet's Closed-Form Formula Limitations**:
  * F(n) = (φⁿ - ψⁿ) / √5 where φ = (1 + √5) / 2 and ψ = (1 - √5) / 2.
  * Runs in 𝒪(Log N) math due to underlying double exponentiation `phi.pow(n)`. But IEEE 754
  * floating-point precision issues limit accuracy for **N > 70**.
* **Precision Degradation**:
  * IEEE 754 64-bit doubles allocate a 53-bit mantissa (≈ 15–17 decimal digits).
  * For **N > 70**, precision loss creates off-by-one errors. Matrix Exponentiation is strictly
    preferred for exact integer outputs.

---

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
