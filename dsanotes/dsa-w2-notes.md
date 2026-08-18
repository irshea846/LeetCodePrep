# LeetCodePrep Week 2

A collection of LeetCode problem solutions implemented in Kotlin.

## How to Run Tests
You can run the tests using Gradle:
```bash
./gradlew test
```

# ===============================================================
# WEEK 2: TWO POINTERS & SLIDING WINDOWS (August 11 - August 15)
# ===============================================================

## Topics Covered

- [ ] Arrays & Hashing
- [ ] Two Pointers
- [ ] Sliding Window

## Day 12 - LC 3 - Longest Substring Without Repeating Characters
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **Continuous Subarray/Substring Constraint**: The problem asks for a *substring*, which implies
      the elements must be contiguous. This is a classic signal for the **Sliding Window** pattern.
    * **Uniqueness Invariant**: Every character in the window must be unique. This requires a
      secondary data structure (like a `HashSet` or `Map`) to track character frequencies or their last
      seen indices within the current window.
    * **Dynamic Window Adjustment**: When a duplicate is encountered (violating the invariant), the
      left boundary of the window must "shrink" or "jump" to exclude the previous occurrence of that
      character. This allows us to find the maximum possible length in a single linear pass **O(N)**.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time       | Space            | Best Used When... |
| :--- |:-----------|:-----------------| :--- |
| **Brute Force** | **O(N^3)** | **O(min(N, M))** | String size is tiny. |
| **Sliding Window (HashSet)** | **O(N)**   | **O(min(N, M))** | Standard linear approach for any character set. |
| **Sliding Window (Array)** | **O(N)**   | **O(1)**         | **Optimal.** Fast jumps using last-seen index. |

* **Space** **O(M)** where **M** is the size of the character set:
    * **int[26]** for Letters 'a' - 'z' or 'A' - 'Z'
    * **int[128]** for ASCII
    * **int[256]** for extended ASCII)

### 3. Native Kotlin Syntax Pitfalls
* **`s.withIndex()`**: Extremely idiomatic for sliding windows as you often need both the character
  and its absolute position (`idx`) to update the "last seen" map.
* **`c.code`**: In Kotlin, use `.code` (or `.toInt()` in older versions) to get the ASCII value of a
  `Char` when using a fixed-size `IntArray` as a frequency/index map.
* **`IntArray(256) { -1 }`**: When tracking "last seen" indices, initialize with -1 to distinguish
  between index 0 and "not seen yet."
* **Eliminating Iterator Object Allocation in Hot Paths**:
    * **The Nuance**: Kotlin's .withIndex() extension on a CharSequence or String is highly idiomatic
      and readable.
    * **The Hot-Path Performance Catch**: Under the hood on the JVM, calling .withIndex() creates an
      intermediate IndexedValue iterator object wrapper that allocates memory on the heap for every loop
      iteration. In an ultra-hot path checking millions of streaming characters, this triggers garbage
      collection memory pressure.
    * **The Senior Fix**: Use a standard primitive indexing for loop (for (idx in 0 until s.length))
      and look up characters natively using s[idx]. This achieves the exact same mathematical index
      tracking while using zero object allocations, keeping data entirely on the CPU stack.
```kotlin
    for ((idx, char) in s.withIndex()) {
        // your code here
    }
```


### 4. Code Block
```kotlin
fun lengthOfLongestSubstring(s: String): Int {
    // 1. Highly Optimized Sliding Window (One-Pass with allocation-free index mapping)
    // Time Complexity: O(N) | Space Complexity: O(M)
    val lastSeen = IntArray(256) { -1 }
    var start = 0
    var maxLen = 0

    // FIXED: Bypasses .withIndex() object allocation by using primitive indexed iteration loops
    for (idx in 0 until s.length) {
        val charCode = s[idx].code

        // If we've seen this char inside the current window, jump 'start' forward instantly
        if (lastSeen[charCode] >= start) {
            start = lastSeen[charCode] + 1
        }

        lastSeen[charCode] = idx

        // Calculate max window distance inline
        val currentWindowLen = idx - start + 1
        if (currentWindowLen > maxLen) {
            maxLen = currentWindowLen
        }
    }
    return maxLen
}
```

```kotlin
fun lengthOfLongestSubstring(s: String): Int {
    // 2. Sliding Window Approach with Fixed-Size Array Strategy
    // Time Complexity: O(N) | Space Complexity: O(1)
    val chars = IntArray(256) { -1 }
    var start = 0
    var maxSubStrLen = 0
    for ((idx, c) in s.withIndex()) {
        val ascii = c.code
        val pos = chars[ascii]
        val len = idx - start + if (pos < start) 1 else 0
        if (len > maxSubStrLen) maxSubStrLen = len
        if (pos >= start) {
            start = pos + 1
        }
        chars[ascii] = idx
    }
    return maxSubStrLen
}
```

```kotlin
fun lengthOfLongestSubstring(s: String): Int {
    // 3. Sliding Window Approach with HashMap Strategy
    // Time Complexity: O(N) | Space Complexity: O(N)
    val map = HashMap<Char, Int>()
    var start = 0
    var maxSubStrLen = 0
    for ((idx, c) in s.withIndex()) {
        if (!map.containsKey(c)) {
            map[c] = idx
            maxSubStrLen = maxOf(maxSubStrLen, idx - start + 1)
        } else {
            val pos = map[c]!!
            val len = idx - start + if (pos < start) 1 else 0
            if (len > maxSubStrLen) maxSubStrLen = len
            if (pos >= start) {
                start = pos + 1
            }
        }
        map[c] = idx
    }
    return maxSubStrLen

}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **HashMap vs. IntArray**:
    *   **HashMap**: Handles any Unicode character (emojis, multi-language). Higher memory
        overhead due to object boxing (`Character` and `Integer` objects).
    *   **IntArray(256)**: Extremely fast (O(1) lookups, zero boxing). Limited to Extended ASCII.
        Ideal for standard web/text processing where character range is known.
*   **Two-Pointer Shrinking vs. Index Jumping**:
    *   **Shrinking (while loop)**: The left pointer moves one by one until the duplicate is
        removed. Easier to reason about for beginners.
    *   **Jumping (lastSeen map)**: The left pointer "jumps" directly to the valid position.
        Mathematically cleaner and ensures exactly **N** iterations.


## Day 11 - LC 121 - Best Time to Buy and Sell Stock
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **Temporal Ordering Constraint**: You must buy before you can sell. This prevents simply
      finding the absolute min and max of the array (as the max might occur before the min).
    * **Greedy Extremum Tracking**: To calculate the max profit at any given day, you only need to
      know the most extreme price seen in the "other" direction (either the minimum price seen *before*
      today or the maximum price seen *after* today).
    * **Backward Iteration (Current Choice)**: By iterating from the end, we maintain a "Global
      Maximum to the Right." For every price, the potential profit is `maxRight - currentPrice`.
      This avoids nested loops and reduces complexity to linear time.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time     | Space    | Best Used When...                         |
| :--- |:---------|:---------|:------------------------------------------|
| **Brute Force** | **O(N^2)** | **O(1)**   | Array size is tiny (N < 100).             |
| **One Pass (Greedy)** | **O(N)** | **O(1)** | **Optimal.** Standard for any input size. |

### 3. Native Kotlin Syntax Pitfalls
* **`if-else` vs `maxOf`**: While `maxOf(a, b)` is idiomatic and concise, it delegates to
  `Math.max(a, b)` under the hood. In extremely tight loops (processing millions of records), a
  manual `if (a > b)` statement can be slightly faster by avoiding the overhead of a function call
  and providing a flatter execution path for the JIT compiler.
* **`prices.lastIndex`**: Always prefer `lastIndex` over `size - 1` for idiomatic Kotlin clarity.
* **Index Safety**: When iterating `downTo 0`, ensure the initial value handles the boundary
  correctly (starting at `lastIndex - 1` since `lastIndex` is used as the initial `bestPrice`).
* **The Monotonic Equality Slip**: Look closely at your conditional branching rules in 2:
    * **The Catch**: You use an explicit if check for < bestPrice and an else if check for >
      bestPrice.
    * **The Edge Case**: What happens if prices == bestPrice? Because it fails both conditions, the
      code falls out of the branch entirely and executes nothing for that step.
    * **The Operational Impact**: While a flat price equality won't corrupt your maxDiff or break
      your tracking parameters directly (since a flat value can't yield a larger profit delta or shift
      your max price limits), leaving equality branches unhandled inside an explicit if / else if tree
      can leave your compiler vulnerable to unexpected branch-prediction penalties or miss updates if
      you ever decide to track indices alongside values.
    * **The Senior Fix**: Change your second condition to a flat else branch, or integrate equality
      smoothly to ensure every possible CPU evaluation falls into a clean, predictable control path.

``` kotlin
if (prices[i] < bestPrice) {
    val diff = bestPrice - prices[i]
    if (diff > maxDiff) maxDiff = diff
} else if (prices[i] > bestPrice) {
    bestPrice = prices[i]
}
```

### 4. Code Block
```kotlin
fun maxProfit(prices: IntArray): Int {
    // 1. Greedy if-else Approach (Optimized for Branch Prediction & Unboxed Execution)
    // Time Complexity: O(N) | Space Complexity: O(1)
    if (prices.isEmpty()) return 0

    var maxDiff = 0
    var bestPrice = prices[prices.lastIndex]

    for (i in prices.lastIndex - 1 downTo 0) {
        val currentPrice = prices[i]

        if (currentPrice < bestPrice) {
            val diff = bestPrice - currentPrice
            if (diff > maxDiff) maxDiff = diff
        } else {
            // FIXED: Flat fallback branch handles equality safely, ensuring the
            // CPU register layout optimizes branch predictions predictably.
            bestPrice = currentPrice
        }
    }
    return maxDiff
}
```

```kotlin
fun maxProfit(prices: IntArray): Int {
    // 2. Greedy if-else Approach (Optimized for Branch Prediction)
    // Time Complexity: O(N) | Space Complexity: O(1)
    if (prices.isEmpty()) return 0
    var maxDiff = 0
    var bestPrice = prices[prices.lastIndex]
    for (i in prices.lastIndex - 1 downTo 0) {
        if (prices[i] < bestPrice) {
            val diff = bestPrice - prices[i]
            if (diff > maxDiff) maxDiff = diff
        } else if (prices[i] > bestPrice) { 
            bestPrice = prices[i]
        }
    }
    return maxDiff
}
```
```kotlin
fun maxProfit(prices: IntArray): Int {
    // 3. Greedy maxOf Approach (Idiomatic Kotlin)
    // Time Complexity: O(N) | Space Complexity: O(1)
    if (prices.isEmpty()) return 0
    var maxDiff = 0
    var bestPrice = prices[prices.lastIndex]
    for (i in prices.lastIndex - 1 downTo 0) {
        maxDiff = maxOf(maxDiff, bestPrice - prices[i])
        bestPrice = maxOf(bestPrice, prices[i])
    }
    return maxDiff
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Forward vs. Backward Iteration**:
    *   **Forward**: Tracks `minPriceSoFar` and calculates `profit = currentPrice - minPrice`. This
        is the most intuitive way (simulating time passing).
    *   **Backward**: Tracks `maxPriceSoFar` and calculates `profit = maxPrice - currentPrice`.
        Mathematically identical, but useful if the data is being streamed from a buffer or log
        where reading backwards is more efficient.
*   **Kadane’s Algorithm Variant**:
    *   The problem can be modeled as finding the maximum subarray sum of the **differences**
        between adjacent days.
    *   Example: `[7, 1, 5, 3, 6, 4]` becomes `[-6, 4, -2, 3, -2]`. The max subarray sum is `4 + (-2) + 3 = 5`.
    *   *Trade-off*: More complex to implement and reason about, but highlights a deep connection
        between different array patterns.
*   **Streaming Data / Memory Management**:
    *   In a real-time trading system, you wouldn't store the whole `IntArray`. You would process
        each price as it arrives (**O(1)** space, **O(1)** per tick). The "Forward" iteration is
        mandatory in this "Online" scenario.


## Day 10 - LC 11 - Container With Most Water
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **The "Shortest Bar" Bottleneck**: The area of the container is strictly limited by the height
      of the shorter bar (`min(height[left], height[right])`). The area is calculated as width * min(h1,
      h2). Since moving any pointer inward always decreases the width, the only way to potentially
      increase the total area is to find a taller height.

    * **Width always decreases**: As we move pointers inward, the width (`right - left`) decreases at
      every step.

    * **Greedy Decision**: The shorter bar is the limiting factor (the bottleneck). To find a larger
      area despite the shrinking width, we *must* find a taller bar. Moving the taller bar's pointer is
      guaranteed to result in a smaller area (same or smaller bottleneck, smaller width). Thus, we must
      move the pointer at the **shorter height** in hopes of finding a taller one to potentially overcome
      the width loss.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time       | Space    | Best Used When...                         |
| :--- |:-----------|:---------|:------------------------------------------|
| **Brute Force** | **O(N^2)** | **O(1)** | Array size is very small (N < 100).       |
| **Two Pointers (Greedy)** | **O(N)**   | **O(1)** | **Optimal.** Standard for any input size. |

### 3. Native Kotlin Syntax Pitfalls
* **`minOf` / `maxOf` vs. manual `if`**: Kotlin's `minOf` and `maxOf` are highly readable and can
  take multiple arguments, which is idiomatic. However, in extremely tight loops, a manual `if`
  statement might avoid a tiny bit of function call overhead (though usually inlined by JIT).
    * **The Performance Catch**: Because `minOf` is marked as **`inline`**, the Kotlin compiler copies
      the body of the function directly into your loop, which helps prevent call-stack creation overhead.
      However, it delegates the check directly to **`Math.min(a, b)`** (a native Java helper).
    * **The JIT Trap**: While modern JVM Just-In-Time (JIT) compilers are smart enough to optimize
      `Math.min` directly down to hardware instructions (like `CMOV` or conditional moves), it still
      requires an extra compilation step. In extremely tight loops (like checking billions of integers
      in a sliding window), an `if-else` statement avoids any intermediate library delegation entirely,
      ensuring the flattens execution path runs directly on the CPU register layout.
    * **How to Ace this Question in an Interview**: If an interviewer watches you write an `if-else`
      block instead of using `minOf()` and asks why, deliver this exact response to showcase your
      mastery of memory management and system design:
        * *While Kotlin’s `minOf()` is highly readable and inlined at compile-time, it delegates under
          the hood to Java's `Math.min()`. In standard applications, this is perfectly fine. However, in
          hot, tight execution loops—such as processing high-frequency data streams or graphics
          calculations—a manual `if-else` statement compiles directly down to raw primitive conditional
          jump bytecodes on the CPU stack, bypassing library delegation and guaranteeing maximum
          low-latency performance.*

* **`height.lastIndex`**: Always prefer `lastIndex` over `size - 1` for idiomatic clarity and
  conciseness.
* **Range safety**: In the `else -> { right--; left++ }` case, ensure that the pointers don't
  cross or go out of bounds if the logic was inside a nested loop. Here, the outer `while (left < right)`
  handles it safely.
* **Value vs Index Confusion**: A common logic error is comparing indices (`left < right`)
  vs comparing values (`height[left] < height[right]`). Kotlin's strong typing helps, but logic
  errors can still slip through.

### 4. Code Block
```kotlin
fun maxArea(height: IntArray): Int {
    // 1. Greedy Two-Pointer Convergence Model Approach
    var mostWater = 0
    var left = 0
    var right = height.lastIndex
    while (left < right) {
        mostWater = maxOf(mostWater,
            minOf(height[left], height[right]) * (right - left))

        when {
            height[left] > height[right] -> right--
            height[left] < height[right] -> left++
            else -> { right--; left++ }
        }
    }
    return mostWater
}
```

```kotlin
fun maxArea(height: IntArray): Int {
    // 2. Highly Optimized Greedy Two-Pointer Convergence Model
    // Time Complexity: O(N) | Space Complexity: O(1)
    var mostWater = 0
    var left = 0
    var right = height.lastIndex

    while (left < right) {
        val width = right - left

        // Calculate the area inside the conditional branches to eliminate redundant minOf checks
        if (height[left] < height[right]) {
            val currentArea = height[left] * width
            if (currentArea > mostWater) mostWater = currentArea
            left++
        } else if (height[left] > height[right]) {
            val currentArea = height[right] * width
            if (currentArea > mostWater) mostWater = currentArea
            right--
        } else {
            val currentArea = height[left] * width
            if (currentArea > mostWater) mostWater = currentArea
            left++
            right--
        }
    }
    return mostWater
}

```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Time vs. Complexity**:
    * The **Two-Pointer** approach is the gold standard (**O(N)**), but it assumes you have random
      access to the entire array (memory-resident).
    * If the data is too large for memory (e.g., 100 Billion heights), you would need to store it
      in a distributed database. However, this specific "Most Water" problem is hard to parallelize
      directly because the pointers converge from ends based on local decisions.

* **Equal Heights Optimization**:
    * **Correctness**: When `height[left] == height[right]`, moving just one pointer (either `left++`
      or `right--`) is mathematically sufficient to find the global maximum.
    * **Efficiency**: Moving **both pointers** simultaneously is a safe optimization. Since the new
      container's height would be capped by the unmoved equal height, and the width has decreased, a
      single move is guaranteed to produce a smaller area. Pruning both bars is a "greedy skip" that
      saves one iteration.

* **Micro-Optimization**:
    * **Reason**: The implementation is completely correct and will pass LeetCode
      verification with zero issues. However, if you are interviewing for a low-latency or
      high-performance engineering role (such as processing high-frequency data streams on a mobile
      device), there is a minor optimization we can make to your inner loop execution as in **2.
      Highly Optimized Greedy Two-Pointer Convergence Model**.
    * **Pruning Redundant Calculations**: Look closely at your area calculation line inside the
      while loop: `mostWater = maxOf(mostWater, minOf(height[left], height[right]) * (right - left))`
        * **The Nuance**: You are calling minOf and calculating the area on every single iteration.
        * **The Performance Catch**: In the else branch of your when statement, both heights are
          identical, so you can pick either one. But in the first two branches, you already know which
          bar is shorter because your when conditions check `height[left] > height[right]` and
          `height[left] < height[right]`.
        * **The Senior Optimization**: By calculating the area inside the branches of your when block,
          you save yourself a redundant height comparison on every step. This flattens the execution
          path and allows the JVM to inline the operations more efficiently.

* **Comparison to Trapping Rain Water (LC 42)**:
    * While both use two pointers, **Container With Most Water** looks for a single global maximum
      area between two bars. **Trapping Rain Water** calculates the cumulative volume trapped across
      all bars.
    * Container: Greedy movement of the shorter bar.
    * Trapping: Keeping track of left/right max heights to calculate local depth.

* **Monotonic Stack?**:
    * Some might wonder if a monotonic stack works here (like in LC 84 - Largest Rectangle in Histogram).
      In LC 84, the width is determined by all bars being taller than the current one. Here, the width
      is simply the distance between two specific bars. The two-pointer approach is more efficient
      for this specific constraint.

## Day 9 - LC 15 - Three Sum
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **No Duplicate Triplets**: This is the most critical constraint. It triggers two design decisions:
        1.  **Sorting**: We must sort the array first O(N * Log(N)) to bring duplicates together, making
            them easy to skip.
        2.  **Skipping Logic**: We skip the "anchor" element if it matches the previous one, and we also
            skip the `left`/`right` pointers after finding a valid triplet to avoid identical combinations.
    * **Sum to Zero (a + b + c = 0)**: This triggers a **"Fix One, Search Two"** strategy. By fixing
      `a`, the problem transforms into finding two numbers that sum to `-a` (**Two Sum II**).
    * **Efficiency Requirement**: Brute force is $O(N^3)$. Sorting allows us to use a nested
      **Two-Pointer Squeeze**, reducing the complexity to **O(N^2)** time and **O(1)** space
      (ignoring sorting overhead).

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time   | Space        | Best Used When... |
| :--- |:-------|:-------------| :--- |
| **Brute Force** | **O(N^3)** | **O(1)**     | Array is tiny and you want the simplest possible logic. |
| **Sorting + Two Pointers** | **O(N^2)** | **O(Log N)** | **Optimal.** Most efficient for both time and memory in a sorted context. |
| **Sorting + HashSet** | **O(N^2)** | **O(N)**     | You want to use a `Set` to handle deduplication automatically. |
| **No-Sort HashSet** | **O(N^2)** | **O(N)**         | You are strictly forbidden from sorting or modifying the input array. |

*\*Space complexity for sorting is typically **O(Log(N))** or **O(N)** depending on the
language/library implementation.*

### 3. Native Kotlin Syntax Pitfalls
* **`nums.sort()` vs. `nums.sorted()`**: Use `nums.sort()` to modify the array in-place (O(1) extra
  space). `nums.sorted()` creates a new `List<Int>`, which adds $O(N)$ space overhead and involves
  object boxing.
* **Return Type Boxing**: The required return type `List<List<Int>>` forces object boxing of every
  primitive `Int` into a `java.lang.Integer` object. In performance-critical systems, a flat `IntArray`
  or a custom primitive collection would be preferred.
* **`listOf()` vs. `intArrayOf()`**: Use `listOf(a, b, c)` because the return type is `List<Int>`.
  However, be aware that this creates a new `ArrayList` object for every triplet found.
* **Early Exit Optimization**: Since the array is sorted, if `nums[i] > 0`, it is impossible for
  `nums[i] + nums[j] + nums[k]` to equal 0 (as all subsequent elements are also positive). You can
  `break` the loop early.
* **Skip Logic**: Ensure the skipping `while` loops (e.g., `while (j < k && nums[j] == nums[j - 1])`)
  are placed *after* the `j++` increment to avoid infinite loops or re-processing the same value.

### 4. Code Block
```kotlin
fun threeSum(nums: IntArray): List<List<Int>> {
    // 1. Sort + Two-Pointer Approach (Manual Deduplication)
    // Time Complexity: O(N^2) | Space Complexity: O(Log(N))
    nums.sort()
    val triplets = mutableListOf<List<Int>>()
    for (i in nums.indices) {
        // Optimization: If the smallest number is > 0, sum can't be 0
        if (nums[i] > 0) break
        
        // Skip duplicate anchor elements
        if (i == 0 || nums[i] != nums[i - 1]) {
            val target = -nums[i]
            var left = i + 1
            var right = nums.lastIndex
            while (left < right) {
                val sum = nums[left] + nums[right]
                when {
                    sum == target -> {
                        triplets.add(listOf(nums[i], nums[left++], nums[right--]))
                        // Skip duplicates for the second element
                        while (left < right && nums[left] == nums[left - 1]) left++
                    }
                    sum < target -> left++
                    else -> right--
                }
            }
        }
    }
    return triplets
}
```

```kotlin
fun threeSum(nums: IntArray): List<List<Int>> {
    // 2. Sort + Two-Pointer Approach (Deduplication via Set)
    // Time: O(N^2) | Space: O(N) for the Set
    nums.sort()
    val triplets = mutableSetOf<List<Int>>()
    for (i in nums.indices) {
        if (nums[i] > 0) break
        val target = -nums[i]
        var left = i + 1
        var right = nums.lastIndex
        while (left < right) {
            val sum = nums[left] + nums[right]
            when {
                sum == target -> triplets.add(listOf(nums[i], nums[left++], nums[right--]))
                sum < target -> left++
                else -> right--
            }
        }
    }
    return triplets.toList()
}
```

```kotlin
fun threeSum(nums: IntArray): List<List<Int>> {
    // 3. Sort + Two-Pointer Approach (Post-Generation .distinct())
    // Time: O(N^2) | Space: O(N) for triplets list
    // Note: Least efficient due to overhead of collecting duplicates and distinct() call.
    nums.sort()
    val triplets = mutableListOf<List<Int>>()
    for (i in nums.indices) {
        if (nums[i] > 0) break
        val target = -nums[i]
        var left = i + 1
        var right = nums.lastIndex
        while (left < right) {
            val sum = nums[left] + nums[right]
            when {
                sum == target -> triplets.add(listOf(nums[i], nums[left++], nums[right--]))
                sum < target -> left++
                else -> right--
            }
        }
    }
    return triplets.distinct()
}
```
### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Sorting vs. HashSet (In-place vs. Extra Space)**:
    *   **Sorting**: Achieves the optimal **O(N^2)** time with **O(1)** space. However, it requires
        modifying the input array (side effect) or making a copy (**O(N)** space).
    *   **HashSet (No-Sort)**: Useful if the input array is strictly read-only, and you cannot afford
        even **O(Log(N))** extra space for sorting. You would use a HashSet to find the complement, but
        handling duplicate triplets without sorting is much more complex and memory-intensive **O(N)**
        space for tracking seen combinations.

*   **Large Scale / Distributed Data**:
    *   **Scenario**: What if the array has 10 billion numbers?
    *   **Solution**: **MapReduce / Sharding**.
        *   **Step 1**: Sort the massive dataset globally (External Merge Sort).
        *   **Step 2**: Distribute the data across machines by value ranges.
        *   **Step 3**: Each machine takes an "anchor" element `a` and searches for `b + c = -a`.
        *   **Challenge**: Machines need access to overlapping ranges because `b` and `c` could be
            anywhere in the sorted dataset.

*   **Early Exit & Search Pruning**:
    *   Sorting enables powerful pruning. If `nums[i] > 0`, we stop the entire process because no
        three positive numbers can sum to zero. In a real-time system, this "Early Exit" can save
        massive amounts of compute time on skewed datasets.

*   **Memory Pressure (JVM Specific)**:
    *   The standard `List<List<Int>>` return type is very "heavy." Each `Int` is boxed into an
        `Integer` object, and each triplet is an `ArrayList` object. For millions of results, this could
        trigger **GC (Garbage Collection) Thrashing**.
    *   **System Design Fix**: Use a single flat `IntArray` where every three elements represent a
        triplet, or a specialized primitive collection library to keep data on the stack/contiguous memory.


## Day 8 - LC 125 - Valid Palindrome
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
    * **Ignoring Non-Alphanumeric Characters**: The problem forces us to process a string while
      "skipping" irrelevant data (spaces, punctuation). This triggers the need for a **conditional
      pointer movement** strategy.
    * **Space Constraint (Optional but critical)**: If the interviewer mentions "large-scale data"
      or "memory efficiency," the constraint shifts from a simple `filter` to an **In-Place Two-Pointer**
      approach to avoid $O(N)$ memory allocation.
    * **Case Insensitivity**: This requires a **normalized comparison** (`ignoreCase = true` or
      `lowercase()`), ensuring that 'A' and 'a' are treated as identical.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach                    | Time     | Space    | Best Used When... |
|:----------------------------|:---------|:---------| :--- |
| **Filtered + Two Pointers** | **O(N)** | **O(N)** | You want readable, simple code for small/medium strings. |
| **Two Pointers In-Place**   | **O(N)** | **O(1)** | You are dealing with massive data or memory-constrained environments. |
| **Reverse and Compare**     | **O(N)** | **O(N)** | You are writing a quick script and don't care about performance. |

### 3. Native Kotlin Syntax Pitfalls
* Use `Char.equals(other, ignoreCase = true)` instead of `char.lowercase()`. `lowercase()` in Kotlin
  returns a `String` (to handle complex Unicode cases), which creates unnecessary object allocations
  inside a loop.
* `s.lastIndex` is the idiomatic Kotlin way to write `s.length - 1`.

### 4. Code Block
```kotlin
fun isPalindrome(s: String): Boolean {
    // Optimized Two-Pointer In-Place Approach
    // Time Complexity: O(N) | Space Complexity: O(1)
    // Using if/continue provides a flatter control flow that is often
    // faster on the JVM due to better JIT optimization and branch prediction.
    var i = 0
    var j = s.lastIndex
    while (i < j) {
        if (!s[i].isLetterOrDigit()) {
            i++
            continue
        }
        if (!s[j].isLetterOrDigit()) {
            j--
            continue
        }
        if (!s[i++].equals(s[j--], ignoreCase = true)) return false
    }
    return true
}
```

```kotlin
fun isPalindrome(s: String): Boolean {
    // Filtering + Two Pointers Approach
    // Time Complexity: O(N) | Space Complexity: O(N)
    val str = s.filter { it.isLetterOrDigit() }
    val lastIdx = str.length - 1
    for (i in 0 until str.length / 2) {
      if (str[i].lowercase() != str[lastIdx - i].lowercase()) {
        return false
      }
    }
    return true
}
```

```kotlin
fun isPalindrome(s: String): Boolean {
    // Filtering + Reverse and Compare Approach
    // Time Complexity: O(N) | Space Complexity: O(N)
    val str = s.filter { it.isLetterOrDigit() }.lowercase()
    return str == str.reversed()
}
```
### 5. Alternative Trade-offs (For System Design Dialogues)
* **Horizontal Scan vs. Vertical Scan**: In Palindrome problems, we always do a "Horizontal Scan"
  (from both ends). A "Vertical Scan" isn't applicable here, but you can discuss **Parallelization**
  for extremely long strings by splitting the string into chunks and checking symmetry across the
  split points in a distributed system.
* **Early Exit (Short-Circuiting)**: The `if/continue` approach is superior to `filter().reversed()`
  because it can return `false` as soon as the first mismatch is found (potentially after checking
  only 2 characters), whereas `filter().reversed()` must process the entire string twice before
  starting the comparison.

---

## Day 8 - LC 167 - Two Sum II (Input Array Is Sorted)
### 1. Core Pattern Identifier
* What specific constraint triggered the solution design?
* **Input Array is sorted**: This is the "Golden Constraint." Because the array is ordered, we know
  that moving a pointer to the right always increases the sum, and moving to the left always decreases
  it. This triggers the **Two-Pointer Squeeze** pattern (a common term in algorithm interviews).
* **Constant Extra Space O(1)**: The problem usually forbids using a HashMap. This constraint forces
  us to use the structure of the array itself (the sorting) rather than external memory to find the
  complement.
* **1-Indexed Result**: A minor constraint that requires us to add +1 to our zero-based indices before
  returning.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach                    | Time         | Space    | Best Used When... |
|:----------------------------|:-------------|:---------| :--- |
| **Two Pointers Squeeze**    | **O(N)**     | **O(1)** | **Optimal.** Standard for sorted arrays. |
| **Binary Search**           | **O(N log N)** | **O(1)** | You are only allowed to move one pointer or for specific "jumping" optimizations. |
| **HashMap**                 | **O(N)**     | **O(N)** | Array is **not** sorted (standard Two Sum). Wastes space here. |
| **Brute Force**             | **O(N^2)**   | **O(1)** | Array is tiny and you want zero extra logic. |

### 3. Native Kotlin Syntax Pitfalls
* **Avoid `arrayOf()`**: The problem requires an `IntArray` result. `arrayOf(1, 2)` creates an `Array<Int>` (boxed `Integer` objects), which will fail to compile against the required return type. Use **`intArrayOf(i + 1, j + 1)`**.
* **Idiomatic Range End**: Use **`numbers.lastIndex`** instead of `numbers.size - 1`.
* **Binary Search Indices**: Kotlin's `IntArray.binarySearch(element, fromIndex, toIndex)` treats `toIndex` as **exclusive**. If you use binary search, ensure the range is correctly bounded to avoid re-checking the same element.
* **Int Overflow**: When calculating `numbers[i] + numbers[j]`, be mindful of potential `Int` overflow if the numbers are close to $2^{31}-1$. A safer comparison in large-scale systems is `numbers[i] == target - numbers[j]`.

### 4. Code Block
```kotlin
fun twoSumSorted(numbers: IntArray, target: Int): IntArray {
    // 1. Two-Pointer In-Place Approach
    // Time Complexity: O(N) | Space Complexity: O(1)
    var i = 0
    var j = numbers.lastIndex
    while (i < j) {
      val complement = target - numbers[i]
      when {
        numbers[j] == complement -> return intArrayOf(i + 1, j + 1)
        numbers[j] > complement -> j--
        else -> i++
      }
    }
    return intArrayOf()
}
```

```kotlin
fun twoSumSorted(numbers: IntArray, target: Int): IntArray {
    // 2. One Pointer + Binary Search Approach
    // Time Complexity: O(N * Log(N)) | Space Complexity: O(1)
    for (i in numbers.indices) {
      val complement = target - numbers[i]
      val idx = numbers.binarySearch(complement, i + 1, numbers.size)
      if (idx >= 0) { // instead of `if (idx > i)`
        return intArrayOf(i + 1, idx + 1)
      }
    }
    return intArrayOf()
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Galloping (Exponential) Search**: If the `target` is extremely far from the average, you can use
  Binary Search to "jump" the `right` pointer rather than moving it by `j--`. This changes the
  complexity from O(N) to O(K * Log(N)) where K is the number of jumps.
* **Cache Locality**: The Two-Pointer Squeeze is extremely cache-friendly because it accesses memory
  sequentially. Binary Search jumps between memory locations, which can cause cache misses on very
  large arrays.
* **Functional vs. Imperative**: While `numbers.withIndex().firstOrNull { ... }` looks cleaner,
  the imperative `while` loop is preferred here for precise control over two simultaneous pointers.
