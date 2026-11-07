# LeetCodePrep Week 13

A collection of LeetCode problem solutions implemented in Kotlin.

# ==================================================================
# WEEK 13: Advanced Linear DP  (November 2 - November 6)
# ==================================================================

## Topics Covered
- [ ] Advanced Linear DP
- [ ] Recursion

## Day 66 - LC 465. Optimal Account Balancing
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **NP-Hard Subset Partitioning / Maximum Zero-Sum Subsets**:
    - Aggregate net balance per person (map[x] -= balance, map[y] += balance$).
    - Filter out zero balances (settled people).
    - Let M be non-zero balances (M <= 12).
    - **Key Invariant**: If a subset K of people has a net balance sum of 0, all debts within K can 
      be settled in K - 1 transactions.
    - Partitioning M people into G_max disjoint zero-sum subsets yields total minimum transactions:
      Minimum Transactions = M - G_max
      where G_max is the **maximum number of disjoint zero-sum subsets**!

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                                                 |
 :--- | :--- | :--- | :--- |:--------------------------------------------------------------------------------------------------|
 **Bitmask DP + Submask Enumeration** | **O(3ᵐ)** | **O(2ᵐ)** | **Staff Level (Optimal Guarantee)** | Rigorous O(3ᵐ) worst-case bound; guarantees zero-sum subset partition (M - G_max).                |
 **Backtracking DFS + Opposite Sign Pruning** | **O(M!)** worst-case | **O(M)** stack | **Staff Level (Fast Average)** | M <= 12; early exit pruning on exact zero-sum complements makes it blazingly fast in practice. |

*(where $M = \text{non-zero balance count} \le 12$)*

### 3. Native Kotlin Syntax Pitfalls
* **Destructuring Overhead in Hot Loops**: Avoid `val (x, y, balance) = transaction` in tight loops 
as it invokes `component1()`, `component2()`, `component3()`. Direct index access `t[0]`, `t[1]`, 
`t[2]` eliminates iterator and method invocation overhead.
* **Primitive Array Filter**: `map.values.filter { it != 0 }.toIntArray()` creates an unboxed 
primitive `IntArray` containing only non-zero balances.
* **Submask Enumeration Trick (`(submask - 1) and mask`)**: Iterating submasks of a bitmask in O(3ᵐ) 
total time:
  ```kotlin
  var submask = (mask - 1) and mask
  while (submask > 0) {
      dp[mask] = maxOf(dp[mask], dp[submask] + dp[mask xor submask])
      submask = (submask - 1) and mask
  }
  ```

### 4. Code Block
```kotlin
// Approach 1: Bitmask + DP (Staff Level)
// Time Complexity: O(3ᵐ) | Auxiliary Space Complexity: O(2ᵐ)
fun minTransfers(transactions: Array<IntArray>): Int {
    val map = HashMap<Int, Int>()
    for (t in transactions) {
        map[t[0]] = map.getOrDefault(t[0], 0) - t[2]
        map[t[1]] = map.getOrDefault(t[1], 0) + t[2]
    }
    val balances = map.values.filter { it != 0 }.toIntArray()
    val people = balances.size
    if (people == 0) return 0

    val groups = 1 shl people
    val dp = IntArray(groups)
    val sumBuffer = IntArray(groups)

    for (mask in 1 until groups) {
        for (i in 0 until people) {
            if ((mask and (1 shl i)) != 0) {
                sumBuffer[mask] = sumBuffer[mask xor (1 shl i)] + balances[i]
                break
            }
        }

        if (sumBuffer[mask] == 0) {
            dp[mask] = 1

            var submask = (mask - 1) and mask
            while (submask > 0) {
                dp[mask] = maxOf(dp[mask], dp[submask] + dp[mask xor submask])
                submask = (submask - 1) and mask
            }
        }
    }

    return people - dp[groups - 1]
}

// Approach 2: Backtracking DFS + Opposite Sign Pruning (Fast Average Case)
// Time Complexity: O(M!) worst-case, fast in practice | Auxiliary Space Complexity: O(M) stack
fun minTransfersDFS(transactions: Array<IntArray>): Int {
    val map = HashMap<Int, Int>()
    for (t in transactions) {
        map[t[0]] = map.getOrDefault(t[0], 0) - t[2]
        map[t[1]] = map.getOrDefault(t[1], 0) + t[2]
    }
    val balances = map.values.filter { it != 0 }.toIntArray()
    if (balances.isEmpty()) return 0
  
    fun dfs(start: Int): Int {
        var s = start
        while (s < balances.size && balances[s] == 0) s++
        if (s == balances.size) return 0
    
        var minOps = Int.MAX_VALUE
        for (i in s + 1 until balances.size) {
            // Prune: only transfer between opposite signs (debtor to creditor or vice versa)
            if (balances[i] * balances[s] < 0) {
                balances[i] += balances[s] // Transfer balance
                minOps = minOf(minOps, 1 + dfs(s + 1))
                balances[i] -= balances[s] // Backtrack
        
                // Exact zero-sum match pruning
                if (balances[i] + balances[s] == 0) break
            }
        }
        return minOps
    }

    return dfs(0)
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **NP-Hard Minimum Transaction Settlement Problem**:
  * Finding the optimal transaction settlement is equivalent to the **Minimum Subset Partition 
  Problem into Zero-Sum Subsets**, which is **NP-Hard**.
  * Greedy heuristic (e.g. matching largest debtor with largest creditor) DOES NOT guarantee optimal 
  minimum transactions! (E.g. `balances = [1, 2, 3, -1, -2, -3]`: Greedy might pair +3 with -3, +2 
  with -2, +1 with -1 ⇒ 3 transactions, but zero-sum submask DP finds G_max = 3 zero-sum groups ⇒ 
  6 - 3 = 3 transactions).
* **Bitmask DP Submask Enumeration Complexity (O(3ᵐ))**:
  * Sum of binomial coefficients: ∑ (M choose k) * 2^k = (1 + 2)ᵐ = 3ᵐ   for k = 0 to M .
  * For M <= 12, 3^12 = 531,441 operations. Guaranteed worst-case runtime that executes in 
  sub-milliseconds on the JVM!
* **Backtracking DFS + Opposite Sign Pruning Efficiency**:
  * Backtracking only pairs debtors with creditors (`balances[i] * balances[s] < 0`).
  * Adding exact zero-sum match pruning (`if (balances[i] + balances[s] == 0) break`) cuts 
  non-optimal branches early, making DFS blazingly fast in practice for M <= 12.

---

## Day 65 - LC 188. Best Time to Buy and Sell Stock IV
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Bounded k-Transaction State Machine**: Finding max profit with at most k transactions maps to 
    state array transitions:
    - buy[j] = max(buy[j], sell[j-1] - price) (holding j-th stock).
    - sell[j] = max(sell[j], buy[j] + price) (sold j-th stock).
  * **Unlimited Transactions Fast-Path Guard (k ≥ N / 2)**:
    If transaction capacity k ≥ N / 2, k is unconstrained because at most N / 2 buy-sell transaction 
    pairs can fit in N price days.
    The problem collapses to **LC 122 (Unlimited Transactions)**, solved greedily in O(N) 
    time and O(1) space!

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                                  |
 :--- | :--- | :--- | :--- |:-----------------------------------------------------------------------------------|
 **WQS Binary Search (Aliens' Trick)** | **O(N x log(maxPrice))** | **O(1)** | **Staff Level (Extreme)** | Extreme k >> 10^5; removes k dependency completely via penalty binary search.      |
 **2 1D DP Arrays + (k ≥ N / 2) Fast-Path** | **O(min(k, N/2) x N)** | **O(min(k, N/2))** | **Staff Level (Optimal DP)** | Standard production choice; fast-path executes O(N) greedy logic when k ≥ N / 2. |
 **2D DP Table (N x K)** | **O(k x N)** | **O(k x N)** | Baseline | Standard interview default for demonstrating full state space transitions.         |

### 3. Native Kotlin Syntax Pitfalls
* **32-Bit Integer Underflow Guard (`Int.MIN_VALUE / 2`)**: Initializing `buy` array with 
`Int.MIN_VALUE / 2` (or -10^9) prevents integer underflow when evaluating `sell[j-1] - price` during
day 1 transitions.
* **Direct Return `sell[k]`**: `sell[k]` is guaranteed to be **≥ buy[k]** because selling a stock 
yields non-negative profit over holding. Returning `sell[k]` directly avoids property getter 
evaluation `maxOf(buy.lastIndex, sell.lastIndex)`.

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming (2 1D Tables + Fast-Path Guard - Staff Level)
// Time Complexity: O(min(k, n/2) * n) | Auxiliary Space Complexity: O(min(k, n/2))
fun maxProfit(k: Int, prices: IntArray): Int {
    val n = prices.size
    if (n <= 1 || k <= 0) return 0

    // Unlimited transactions fast-path: if k >= n / 2, greedy sum positive gains (LC 122)
    if (k >= n / 2) {
        var maxProfit = 0
        for (i in 1 until n) {
            if (prices[i] > prices[i - 1]) {
                maxProfit += prices[i] - prices[i - 1]
            }
        }
        return maxProfit
    }

    val buy = IntArray(k + 1) { Int.MIN_VALUE / 2 } // Overflow-safe initial min value
    val sell = IntArray(k + 1)

    for (price in prices) {
        for (j in 1..k) {
            buy[j] = maxOf(buy[j], sell[j - 1] - price)
            sell[j] = maxOf(sell[j], buy[j] + price)
        }
    }

    return sell[k]
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Why k ≥ N / 2 Collapses to Unlimited Transactions**:
  * On an array of length N, a complete buy-sell transaction requires at least 2 days (Buy on day i, 
    Sell on day j > i).
  * The maximum possible non-overlapping transaction count is ⌊N / 2⌋.
  * If k ≥ N / 2, capacity constraint k is never binding! Bypassing DP to run the O(N) greedy loop 
    reduces time from O(k * N) to O(N) and space from O(k) to O(1).
* **WQS Binary Search / Aliens' Trick (O(N x log(maxPrice)) Time, O(1) Space)**:
  * **The Problem**: If k and N are massive (e.g., N = 10^5, k = 10^5), O(k * N) = 10^10 operations 
    cause Time Limit Exceeded (TLE).
  * **The Solution (Parametric Penalty Search)**:
    - Impose a fixed transaction penalty fee λ for every transaction.
    - As penalty λ increases, the number of transactions chosen greedily by the O(N) algorithm 
      decreases monotonically (convexity).
    - We **Binary Search** for penalty λ ∈ [0, maxPrice] such that exact transaction count equals k!
    - Achieves **(O(N x log(maxPrice)) time and O(1) space**, eliminating dependency 
      on k completely!

---

## Day 64 - LC 312. Burst Balloons
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Reverse Thinking / Matrix Chain Multiplication Interval DP**:
    Thinking about bursting the *first* balloon creates dependency coupling across sub-intervals 
    because adjacent neighbors vanish.
    **Reverse Thinking (The Last Balloon Burst Invariant)**: Consider which balloon k (i < k < j) is 
    burst **LAST** in interval (i, j)!
    If k is burst last, its boundary neighbors are balloon i and balloon j (padded with 1 at 0 and N+1).
    `Coins earned = numsEx[i] x numsEx[k] x numsEx[j] + DP[i][k] + DP[k][j]`.
  * **Zero Filtering Pruning**: Any balloon with nums[i] == 0 yields 0 coins. Filtering zeros before 
  padding reduces array length N to N', giving up to an **8x - 64x speedup** (O(N'³) vs O(N³))!

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time      | Space | Performance | Best Used When...                                                                 |
 :--- |:----------| :--- | :--- |:----------------------------------------------------------------------------------|
 **2D Interval DP + Zero Pruning** | **O(N³)** | **O(N'²)** | **Staff Level (Extreme)** | Filters zeros (N' ≤ N), achieving up to **8x - 64x** speedup on sparse arrays.    |
 **2D Interval DP (Bottom-Up)** | **O(N³)** | **O(N²)** | **Staff Level (Optimal DP)** | Standard interview choice for Matrix Chain Multiplication / Interval DP.          |
 **Top-Down Memoized DFS** | **O(N³)** | **O(N²)** matrix + stack | Peak | Clean divide-and-conquer interval logic.                                          |

*(where N = nums.size, N' = non-zero elements)*

### 3. Native Kotlin Syntax Pitfalls
* **Array Bounds Padding**: Creating 
        `val numsEx = IntArray(n + 2) { i -> if (i == 0 || i == n + 1) 1 else nums[i - 1] }` 
  simplifies boundary product evaluations (numsEx[i] x numsEx[k] x numsEx[j]) without out-of-bounds 
  branching.
* **Hoisting Constant Multiplications**: Computing `val outerCoins = numsEx[i] * numsEx[j]` before 
  entering inner loop k avoids re-evaluating `numsEx[i] x numsEx[j]` `j - i - 1` times per inner loop!

### 4. Code Block
```kotlin
// Approach 1: Bottom-Up Dynamic Programming + Zero-Filtering Pruning (Staff Level Extreme)
// Time Complexity: O(N'^3) where N' <= N non-zeros | Auxiliary Space Complexity: O(N'^2)
fun maxCoinsZeroPruned(nums: IntArray): Int {
    // Zero Pruning: Filter out zeros (0 coins earned, zero impact on adjacent product calculations)
    val nonZeros = nums.filter { it > 0 }.toIntArray()
    val n = nonZeros.size
    val numsEx = IntArray(n + 2) { i -> if (i == 0 || i == n + 1) 1 else nonZeros[i - 1] }
    val dp = Array(n + 2) { IntArray(n + 2) }

    for (i in n - 1 downTo 0) {
        for (j in i + 2..n + 1) {
            val outerCoins = numsEx[i] * numsEx[j] // Hoist outer product multiplication
            for (k in i + 1 until j) {
                dp[i][j] = maxOf(dp[i][j], dp[i][k] + dp[k][j] + (outerCoins * numsEx[k]))
            }
        }
    }

    return dp[0][n + 1]
}

// Approach 2: Bottom-Up Dynamic Programming (2D Table)
// Time Complexity: O(N^3) | Auxiliary Space Complexity: O(N^2)
fun maxCoins(nums: IntArray): Int {
    val n = nums.size
    val numsEx = IntArray(n + 2) { i -> if (i == 0 || i == n + 1) 1 else nums[i - 1] }
    val dp = Array(n + 2) { IntArray(n + 2) }

    for (i in n - 1 downTo 0) {
        for (j in i + 2 .. n + 1) {
            val outerCoins = numsEx[i] * numsEx[j]
            for (k in i + 1 until j) {
                dp[i][j] = maxOf(dp[i][j], dp[i][k] + dp[k][j] + (outerCoins * numsEx[k]))
            }
        }
    }

    return dp[0][n + 1]
}

// Approach 3: Top-Down Memoized DFS (Interval Divide & Conquer)
// Time Complexity: O(N^3) | Auxiliary Space Complexity: O(N^2)
fun maxCoinsMemoization(nums: IntArray): Int {
    val n = nums.size
    val numsEx = IntArray(n + 2) { i -> if (i == 0 || i == n + 1) 1 else nums[i - 1] }
    val memo = Array(n + 2) { IntArray(n + 2) }

    fun solve(i: Int, j: Int): Int {
        if (i + 1 >= j) return 0
        if (memo[i][j] != 0) return memo[i][j]

        var maxResult = 0
        val outerCoins = numsEx[i] * numsEx[j]
        for (k in i + 1 until j) {
            val coins = solve(i, k) + solve(k, j) + (outerCoins * numsEx[k])
            maxResult = maxOf(maxResult, coins)
        }

        memo[i][j] = maxResult
        return maxResult
    }

    return solve(0, n + 1)
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Why Reverse Thinking (Last Balloon) Works**:
  * If we pick the *first* balloon to burst, nums[k-1] and nums[k+1] become adjacent, creating 
  dependent subproblems that cannot be solved independently.
  * If we pick the *last* balloon k to burst in interval (i, j), balloon i and balloon j remain 
  intact until k is burst. Thus, subproblems (i, k) and (k, j) are completely independent and can be 
  solved recursively!
* **Zero Filtering Speedup**:
  * Bursting a zero-coin balloon yields 0 × left × right = 0 coins, leaving adjacent neighbor 
  multiplier values unchanged.
  * Removing zeros reduces array length N to N', which yields up to an **8x - 64x speedup** due to 
  cubic time complexity O(N³)!

---

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
