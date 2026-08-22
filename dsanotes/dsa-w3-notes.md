# LeetCodePrep Week3

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 3: TWO POINTERS & SLIDING WINDOWS (August 18 - August 22)
# ===============================================================

## Topics Covered
- [ ] Arrays & Hashing
- [ ] Heap / Priority Queue
- [ ] Two Pointers
- [ ] Sliding Window

## Day 17 - LC 3 - Longest Substring Without Repeating Characters
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Substring Requirement**: The problem asks for a contiguous range, which is the primary signal 
  for a **Sliding Window**.
  * **Uniqueness Invariant**: Each character in the window must be unique. This triggers the need 
  for a "last seen" index tracker.
  * **Optimal Shifting**: To achieve true **O(N)** efficiency, we use an **Index Jumping** strategy. 
  Instead of shrinking the left pointer one by one, we jump it directly to the index after the 
  duplicate's last known position.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time       | Space    | Best Used When... |
| :--- |:-----------|:---------| :--- |
| **Brute Force** | **O(N^3)** | **O(M)** | Only for extremely small inputs. |
| **Sliding Window (Shrink)** | **O(2N)**  | **O(M)** | Beginner-friendly; uses a `while` loop to shrink. |
| **Sliding Window (Jump)** | **O(N)**   | **O(M)** | **Optimal.** Single pass with zero redundant checks. |

### 3. Native Kotlin Syntax Pitfalls
*   **`.toInt()` vs `.code`**: While newer Kotlin uses `.code`, LeetCode's environment often 
requires **`.toInt()`** to get the ASCII value of a `Char`.
*   **Lazy vs. Eager Max**: Calculating `maxLen` only when a duplicate is found (Lazy) is slightly 
faster than calculating it every iteration (Eager), but requires a final check at the return statement.
*   **Charset Assumptions**: `IntArray(128)` is enough for standard ASCII, but **`256`** is safer 
for extended sets to avoid `IndexOutOfBounds`.

### 4. Code Block
```kotlin
fun lengthOfLongestSubstring(s: String): Int {
    // Greedy Sliding Window Approach (Index Jumping with Lazy Update)
    // Time Complexity: O(N) | Space Complexity: O(1)
    val lastSeen = IntArray(256) { -1 }
    var i = 0
    var longestSubstringLen = 0
    
    for (j in s.indices) {
        val charCode = s[j].code
        
        // If char was seen within the current window, measure and jump
        if (lastSeen[charCode] >= i) {
            longestSubstringLen = maxOf(longestSubstringLen, j - i)
            i = lastSeen[charCode] + 1
        }
        lastSeen[charCode] = j
    }
    
    // Final check for the last window in the string
    return maxOf(longestSubstringLen, s.length - i)
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Fixed Array vs. HashMap**: `IntArray` is significantly faster on the JVM due to zero object 
boxing and contiguous memory access. `HashMap` is only necessary if the input contains full Unicode 
(emojis/international text) where the range exceeds 256.
*   **CPU Write Optimization**: By using the "Lazy" update strategy, you minimize CPU writes to the 
`maxLen` variable, which can be a meaningful optimization in high-throughput systems processing 
gigabytes of text data.


## Day 16 - LC 219 - Contains Duplicate II
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Distance Constraint **|i - j| <= k**: This mathematical constraint implies that at any 
  position **i**, we only need to "remember" the previous **k** elements. This is a classic 
  **Fixed-Size Sliding Window** pattern.
  * **Uniqueness Invariant**: Using a `HashSet` allows us to check for duplicates within the 
  current window in **O(1)** time.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time                | Space              | Best Used When... |
| :--- |:--------------------|:-------------------| :--- |
| **Brute Force** | **O(N * Min(k,n))** | **O(1)**           | **K** is extremely small (e.g., 2). |
| **HashMap (Last Seen)** | **O(N)**            | **O(N)**           | Array is small; simple "last seen index" logic. |
| **HashSet (Sliding Window)** | **O(N)**            | **$O(Min(N, K))$** | **Optimal.** Memory-efficient for massive data. |

### 3. Native Kotlin Syntax Pitfalls
*   **`set.add()` Semantic**: Always leverage the return value of `add()`. It returns `false` if 
the element is already present, allowing you to "check and add" in a single atomic operation.
*   **Window Boundary**: Be precise with `i - k`. When `i == k`, the window currently contains 
indices **0** to **k** (which is **k+1** elements). Removing `nums[i - k]` correctly reduces the 
window back to size **k** for the next iteration.


### 4. Code Block
```kotlin
fun containsNearbyDuplicate(nums: IntArray, k: Int): Boolean {
    // HashSet Sliding Window Approach
    // Time Complexity: O(N) | Space Complexity: O(min(N, K))
    val set = HashSet<Int>()
    for (i in nums.indices) {
        // If add() returns false, a duplicate exists within the last 'k' steps
        if (!set.add(nums[i])) return true
        
        // Maintain window size by removing the oldest element
        if (i >= k) set.remove(nums[i - k])
    }
    return false
}
```

```kotlin
fun containsNearbyDuplicate(nums: IntArray, k: Int): Boolean {
    // Hash Map Approach
    // Time Complexity: O(N) | Space Complexity: O(N)
    val map = HashMap<Int, Int>()
    for (i in nums.indices) {
      if (map[nums[i]] != null) {
        val pos = map[nums[i]]!!
        if (i - pos <= k) return true
      }
      map[nums[i]] = i
    }
    return false
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Memory Capping**: In a system processing a stream of data (e.g., detecting duplicate user 
IDs in a real-time log), the HashSet approach is strictly superior because its memory usage is 
**capped by K**. The HashMap approach would grow indefinitely (**O(N)**).
* **Object Overhead**: The HashSet approach only stores the keys (the numbers), whereas a 
HashMap stores both keys and values (indices). This reduces heap pressure and GC frequency.
* **Large **K** Scenario**: If **K** is very large (close to **N**), the space complexity effectively 
becomes **O(N)** for both, but the HashSet still has lower constant-factor overhead.


## Day 16 - LC 121 - Best Time to Buy and Sell Stock
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Temporal Order (Buy before Sell)**: This constraint prevents simply finding the global min and
  max. You must buy on an earlier day and sell on a later one.
  * **Backward Greedy Tracking**: To find the max profit for a buy on Day **i**, you need the 
  **highest price to its right**. This triggers a strategy of tracking the "running maximum" from 
  right to left.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time       | Space    | Best Used When... |
| :--- |:-----------|:---------| :--- |
| **Brute Force** | **O(N^2)** | **O(1)** | Array is tiny and logic simplicity is the only goal. |
| **One Pass (Greedy)** | **O(N)**   | **O(1)** | **Optimal.** Standard for any input size. |

### 3. Native Kotlin Syntax Pitfalls
* **`prices.lastIndex` vs `prices.last()`**: Use `lastIndex` for loop boundaries. Note that calling 
`prices[prices.lastIndex]` on an empty array will throw an exception.
* **Safety Checks**: Professional code should handle `prices.size < 2` cases where no transaction 
is possible.

### 4. Code Block
```kotlin
fun maxProfit(prices: IntArray): Int {
    // Highly Optimized Backward Greedy Approach
    // Time Complexity: O(N) | Space Complexity: O(1)
    if (prices.size < 2) return 0
    
    var curHighestPrice = prices.last()
    var maxProfit = 0
    
    for (i in prices.lastIndex - 1 downTo 0) {
        val currentPrice = prices[i]
        if (currentPrice > curHighestPrice) {
            // New "Best Sell" candidate found
            curHighestPrice = currentPrice
        } else {
            // Check if current day is a better "Buy" candidate
            val profit = curHighestPrice - currentPrice
            if (profit > maxProfit) maxProfit = profit
        }
    }
    return maxProfit
}
```
```kotlin
fun maxProfit(prices: IntArray): Int {
    // Greedy Approach
    // Time Complexity: O(N) | Space Complexity: O(1)
    if (prices.size < 2) return 0

    var curHighestPrice = prices[prices.lastIndex]
    var maxProfit = 0

    for (i in prices.lastIndex - 1 downTo 0) {
        if (prices[i] > curHighestPrice) {
            curHighestPrice = prices[i]
        } else {
            val profit = curHighestPrice - prices[i]
            if (profit > maxProfit) maxProfit = profit
        }
    }
    return maxProfit
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Forward Tracking**: Tracking the `minPrice` from left-to-right is the standard approach. It 
simulates time passing and is mandatory for **streaming data** where the future prices aren't known 
yet.
*   **Backward Tracking**: This approach is mathematically identical but showcases a 
"post-processing" mindset. It can be faster if the data is stored in a way that makes tail-access 
more efficient (e.g., a stack or a log-structured file).
*   **Kadane’s Relationship**: This problem is essentially finding the maximum subarray sum of the 
**daily differences**. Understanding this connection allows you to solve more complex variants (like
LC 122 - Stock II or LC 123 - Stock III) using similar principles.


## Day 15 - LC 15 - 3 Sum
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **No Duplicate Triplets**: This is the most critical constraint. It triggers two design decisions:
    1.  **Sorting**: We must sort the array first (**O(N * Log(N))**) to bring duplicates together, 
    making them easy to skip.
    2.  **Skipping Logic**: We skip the "anchor" element if it matches the previous one, and we also
    skip the `left`/`right` pointers after finding a valid triplet to avoid identical combinations.
  * **Sum to Zero (a + b + c = 0)**: This triggers a **"Fix One, Search Two"** strategy. By fixing
  `a`, the problem transforms into finding two numbers that sum to `-a` (**Two Sum II**).

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time       | Space     | Best Used When... |
| :--- |:-----------|:----------| :--- |
| **Brute Force** | **O(N^3)** | **O(1)**  | Array is tiny and you want the simplest logic. |
| **Sorting + Two Pointers** | **O(N^2)** | **O(1)*** | **Optimal.** Most efficient for both time and memory. |
| **Sorting + HashSet** | **O(N^2)**   | **O(N)**    | You want to use a `Set` to handle deduplication automatically. |

*\*Space complexity for sorting is typically **O(log N)** or **O(N)** depending on the implementation.*

### 3. Native Kotlin Syntax Pitfalls
* **`nums.sort()` vs. `nums.sorted()`**: Use `nums.sort()` to modify the array in-place (O(1) extra 
space). `nums.sorted()` creates a new `List<Int>`, adding **O(N)** space overhead.
* **Return Type Boxing**: The required `List<List<Int>>` forces object boxing of every primitive `Int`. 
For millions of results, this could trigger **GC Thrashing**.
* **Range Check Overhead**: Using `i in 1..nums.lastIndex` is safe but slower than a simple `i > 0` 
check due to the creation of a `Range` object.
* **Early Exit**: Since the array is sorted, if `nums[i] > 0`, it is impossible for 
`nums[i] + nums[j] + nums[k]` to equal 0. Break the loop early.

### 4. Code Block
```kotlin
fun threeSum(nums: IntArray): List<List<Int>> {
    // Highly Optimized Triple-Greedy Squeeze (Manual Deduplication)
    // Time Complexity: O(N^2) | Space Complexity: O(1) (excluding output)
    nums.sort()
    val list = mutableListOf<List<Int>>()
    
    for (i in 0 until nums.size - 2) {
        // Optimization: if anchor is positive, no triplet can sum to 0
        if (nums[i] > 0) break
        
        // Skip duplicate anchor elements
        if (i > 0 && nums[i] == nums[i - 1]) continue

        val target = -nums[i]
        var left = i + 1
        var right = nums.lastIndex
        
        while (left < right) {
            val sum = nums[left] + nums[right]
            when {
                sum < target -> left++
                sum > target -> right--
                else -> {
                    list.add(listOf(nums[i], nums[left++], nums[right--]))
                    // Triple-Skip: Avoid re-processing the same values for left/right
                    while (left < right && nums[left] == nums[left - 1]) left++
                    while (left < right && nums[right] == nums[right + 1]) right--
                }
            }
        }
    }
    return list
}
```

```kotlin
fun threeSum(nums: IntArray): List<List<Int>> {
    // Sort + Triple Greedy Approach
    // Time Complexity: O(N^2) | Space Complexity: O(Log(N))
    nums.sort()
    val list = mutableListOf<List<Int>>()
    var i = 0; var j: Int; var k = nums.lastIndex
    while (i < k && nums[i] <= 0) {
        if (i > 0 && nums[i] == nums[i - 1]) {
            i++
            continue
        }
        val sum = 0 - nums[i]
        j = i + 1
        k = nums.lastIndex
        while (j < k) {
            val complement = sum - nums[j]
            when {
                complement > nums[k] -> j++
                complement < nums[k] -> k--
                else -> {
                    list.add(listOf(nums[i], nums[j], nums[k]))
                    j++; k--
                    while (j < k && nums[j] == nums[j - 1]) j++
                    while (j < k && nums[k] == nums[k + 1]) k--
                }
          }
        }
        i++
    }
    return list
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Deduplication Strategy**: Manual skipping is memory-efficient (**O(1)** extra space), whereas 
using a `HashSet<List<Int>>` is "safer" to implement but uses **O(N)** extra space and is slower due 
to hash calculations.
* **Distributed Scale**: To process 10 billion numbers, use MapReduce. Sort the data globally, then 
shard ranges across nodes. Each machine takes an anchor range and performs the search.
* **Memory Locality**: The two-pointer squeeze is cache-friendly because it accesses memory 
sequentially. However, allocating many small `ArrayList` objects for the output can fragment memory.


## Day 14 - LC 11 - Container With Most Water
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **The "Shortest Bar" Bottleneck**: The area is defined as `width * min(h1, h2)`. Since width 
  **always decreases** as pointers move inward, the only way to potentially increase the area is to 
  find a taller bottleneck.
  * **Greedy Decision Rule**: We always shift the pointer pointing to the **shorter** bar. Moving 
  the taller bar would decrease the width while the bottleneck height is guaranteed to stay the same
  or get even smaller.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Best Used When... |
| :--- | :--- | :--- | :--- |
| **Brute Force** | $O(N^2)$ | **O(1)** | Array is very small (N < 100). |
| **Two Pointers (Greedy)** | **O(N)** | **O(1)** | **Optimal.** Standard for high-performance linear scanning. |

### 3. Native Kotlin Syntax Pitfalls
* **Double Comparison Overhead**: Avoid calling `minOf()` and then performing a separate `when/if` 
check on the same bars. This forces the CPU to perform the same comparison twice.
* **Merged Logic Strategy**: Calculating the area **inside** the conditional branches (as shown 
below) is the fastest way to execute this loop on the JVM, as it flattens the jump table.

### 4. Code Block
```kotlin
fun maxArea(height: IntArray): Int {
    // Highly Optimized Greedy Two-Pointer (Merged Logic)
    // Time Complexity: O(N) | Space Complexity: O(1)
    var i = 0
    var j = height.lastIndex
    var mostWater = 0

    while (i < j) {
        val width = j - i
        
        // Combine height comparison with area calculation to minimize CPU jumps
        if (height[i] < height[j]) {
            val area = height[i] * width
            if (area > mostWater) mostWater = area
            i++
        } else if (height[i] > height[j]) {
            val area = height[j] * width
            if (area > mostWater) mostWater = area
            j--
        } else {
            // "Equal Heights" optimization: move both pointers as neither can
            // contribute to a larger container given the decreasing width.
            val area = height[i] * width
            if (area > mostWater) mostWater = area
            i++; j--
        }
    }
    return mostWater
}
```

```kotlin
fun maxArea(height: IntArray): Int {
    // Greedy Two-Pointer Approach (Original Implementation)
    // Time Complexity: O(N) | Space Complexity: O(1)
    var i = 0
    var j = height.lastIndex
    var mostWater = 0

    while (i < j) { 
        // In your current code, you perform the same comparison twice in every iteration of the loop:
        // 1. First in val minHeight = minOf(height[i], height[j])
        // 2. Second in when { height[i] > height[j] -> ... }
        // While modern JIT compilers might optimize this, from a clean-code and low-level performance 
        // perspective, it is better to merge the area calculation into the pointer logic.
        val minHeight = minOf(height[i], height[j])

        // Redundancy: The currWater variable is only used once; you can inline it directly into the 
        // maxOf or if check.
        val currWater = (j - i) * minHeight
        if (currWater > mostWater) mostWater = currWater
        when {
            height[i] > height[j] -> j--
            height[i] < height[j] -> i++
            else -> {
                i++; j--
            }
        }
    }
    return mostWater
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Equal Height Pruning**: Moving both pointers when heights are equal is a safe optimization. 
Since the new area is capped by the unmoved bar, moving only one is guaranteed to produce a smaller 
result.
* **Hardware Cache Locality**: This algorithm is cache-friendly because it reads from both ends 
toward the middle. However, on massive datasets (GBs of heights), reading from both ends can cause 
"Cache Thrashing" if the two memory locations are in different pages.
* **Parallelization**: Unlike sorting, the Two-Pointer convergence is hard to parallelize because 
the next move depends on the current result.


## Day 14 - LC 167 - Two Sum II - Input Array Is Sorted
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Input Array is sorted**: This is the "Golden Constraint." Because the array is ordered, we 
  know that moving a pointer to the right always increases the sum, and moving to the left always 
  decreases it. This triggers the **Two-Pointer Squeeze** pattern.
  * **Constant Extra Space O(1)**: Forces us to use the structure of the array itself (the sorting) 
  rather than external memory like a HashMap.
  * **1-Indexed Result**: Requires adding `+1` to zero-based indices before returning.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time               | Space | Best Used When... |
| :--- |:-------------------| :--- | :--- |
| **Two Pointers Squeeze** | **O(N)**           | **O(1)** | **Optimal.** Standard for sorted arrays. |
| **Binary Search** | **O(N * Log (N))** | **O(1)** | Only allowed to move one pointer or for "jumping" optimizations. |
| **HashMap** | **O(N)**           | **O(N)** | Array is **not** sorted. Wastes space in this specific problem. |

### 3. Native Kotlin Syntax Pitfalls
* **Avoid `arrayOf()`**: The return type must be `IntArray`. `arrayOf(1, 2)` creates an `Array<Int>` 
(boxed objects), which will fail to compile. Use **`intArrayOf(i + 1, j + 1)`**.
* **KSL `binarySearch()`**: Use the built-in `IntArray.binarySearch()` for zero-risk, optimized 
performance. It handles overflow and ranges perfectly.
* **Overflow Safety**: Calculating `numbers[i] + numbers[j]` can overflow if values are near 
$2^{31}-1$. A safer check is `numbers[j] == target - numbers[i]`.

### 4. Code Block
```kotlin
fun twoSum(numbers: IntArray, target: Int): IntArray {
    // 1. Two Pointers Squeeze (Optimal)
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
fun twoSum(numbers: IntArray, target: Int): IntArray {
    // 2. Binary Search (KSL Optimized)
    // Time Complexity: O(N log N) | Space Complexity: O(1)
    for (i in 0 until numbers.lastIndex) {
        val complement = target - numbers[i]
        val j = numbers.binarySearch(complement, fromIndex = i + 1, numbers.size)
        if (j >= 0) return intArrayOf(i + 1, j + 1)
    }
    return intArrayOf()
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Galloping (Exponential) Search**: If the `target` is extremely far from the average, you can 
use Binary Search to "jump" the pointers rather than moving them one-by-one.
* **Cache Locality**: The Two-Pointer Squeeze is extremely cache-friendly because it accesses 
memory sequentially. Binary Search jumps between memory locations, which can cause cache misses 
on very large arrays.
* **Stream Processing**: In a real-time system, if the sorted data is being streamed, two-pointer 
convergence requires buffering the whole stream. Binary Search might be more feasible if you can 
only "peek" into specific indexed offsets of a remote file.


## Day 13 - LC 680 - Valid Palindrome II
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Single Deletion Tolerance**: The constraint allows for **at most one** character to be removed. 
  This triggers a "Branching" logic: as soon as the first mismatch is found, we must explore two 
  parallel paths (skipping the left vs. skipping the right).
  * **Two-Pointer Convergence**: Since a palindrome is symmetrical, we use two pointers starting 
  from the boundaries. This is a **Two Pointers (Outside-In)** pattern, not a Sliding Window.
  * **Greedy with Single Look-ahead**: We only have one "life" (deletion) to spend. Once we branch, 
  the remaining paths must be perfect palindromes. This allows for an **O(N)** solution instead of 
  full backtracking.

### 2. Complexity Boundaries
* Comparison Matrix

| Feature | Greedy Iterative (#1) | Recursive Tolerance (#2)       |
| :--- | :--- |:-------------------------------|
| **Time Complexity** | **O(N)** | **O(N)**                       |
| **Space Complexity** | **O(1)** | **O(N)** (Stack Space)         |
| **Readability** | High (Clear logic) | Elegant (Generalizable)        |
| **Performance** | **Best** (No call overhead) | Moderate (Function call stack) |
| **Best Used When...** | Standard interviews | Allowing k > 1 deletions       |

### 3. Native Kotlin Syntax Pitfalls
* **Recursive Stack Overflow**: The recursive solution (#2) is elegant but dangerous on the JVM. 
Even though we only "branch" once, the path where characters match still consumes a stack frame for 
* every pair. For a string with *10^5* characters, this will likely trigger a StackOverflowError.
* **Senior Tip**: In production Android/JVM code, always prefer the iterative approach for problems 
with O(N) depth.
* **Substring Allocation**: A common mistake is to use s.substring(l + 1, r + 1) to check the inner 
palindrome. In modern Kotlin/Java, substring creates a new String object (**O(N)** time and space). 
Doing this inside a loop or branch will turn your $O(N)$ solution into $O(N^2)$. Always pass indices 
(l, r) instead.
* **s.lastIndex**: Always use s.lastIndex instead of s.length - 1. It is more idiomatic and less 
prone to off-by-one errors during quick implementation.
* **Char vs String Comparison**: Ensure you are comparing Char primitives (s[l]) rather than 
String objects (s.substring(l + 1, r + 1)) converting them to String. Comparing primitives is 
significantly faster and avoids object allocation.

### 4. Code Block
```kotlin
fun validPalindrome(s: String): Boolean {
    // 1. Greedy Iterative: This is the Optimal solution for interviews. It achieves O(N) time 
    // with true O(1) space. It is clean, avoids recursion overhead, and handles massive strings 
    // safely.
    var l = 0
    var r = s.lastIndex
    while (l < r) {
        if (s[l] != s[r]) {
            // Mismatch found! You have one chance to skip:
            // Path A: Skip s[l] and check the rest
            // Path B: Skip s[r] and check the rest
            return isPurePalindrome(s, l + 1, r) || isPurePalindrome(s, l, r - 1)
        }
        l++; r--
    }
    return true
}

private fun isPurePalindrome(str: String, l: Int, r: Int): Boolean {
    var i = l
    var j = r
    while (i < j) {
        if (str[i++] != str[j--]) return false
    }
    return true
}
```

```kotlin
fun validPalindrome(s: String): Boolean {
    // 2. Recursive Tolerance: This is an Elegant generalization. It's particularly useful if the 
    // problem were to change to "at most **K** characters removed." However, it uses **O(N)** stack 
    // space, which could be a bottleneck for extremely large strings on the JVM.
    return isValidPalindrome(s, 0, s.lastIndex, 0)
}

private fun isValidPalindrome(str: String, i: Int, j: Int, k: Int): Boolean {
    if (j <= i) return true

    if (str[i] != str[j]) {
        if (k > 0) return false
        return isValidPalindrome(str, i, j - 1, k + 1) ||
                isValidPalindrome(str, i + 1, j, k + 1)
    } else {
        return isValidPalindrome(str, i + 1, j - 1, k)
    }
}
```
### 5. Alternative Trade-offs (For System Design Dialogues)
* **Generalization to *K* Deletions**: 
  * **The Iterative Limit**: The greedy iterative approach is hard-coded for exactly one deletion. 
  If the requirement changes to "delete at most *K* characters," the iterative logic becomes a mess of 
  nested loops.
  * **The Design Choice**: In a flexible system (like a natural language processing engine), the 
  Recursive approach is superior because it generalizes to any *K* by simply changing the 
  `if (k > tolerance)` base case. You trade off stack memory for architectural flexibility.
* **Distributed Palindrome Check (Massive Scale)**:
  * **The Problem**: What if the "string" is a 100GB genomic sequence that doesn't fit on one machine?
  * **The Strategy**: You would use a MapReduce style approach. Split the string into blocks across 
  multiple nodes. Node 1 (starts from index 0) and Node *N* (starts from the end) compare their 
  buffers.
  * **The Complexity**: Unlike a standard palindrome, a single deletion in Palindrome II shifts all 
  subsequent indices. This makes distributed checking much harder because a mismatch at the start 
  or end of the string would require Node *N* to potentially "shift" its entire comparison logic by 
  one index.
* **Probabilistic Optimization (Rolling Hash)**:
  * **The Strategy**: For extremely frequent checks, you could use a Rolling Hash (like Rabin-Karp) 
  to compare the forward and backward strings.
  * **The Trade-off**: You can detect if a string is already a palindrome in *O(1)* time after *O(N)*
  preprocessing.
  * **Use Case**: This is useful if you are building a real-time "Palindrome Filter" that processes 
  millions of substrings per second. You trade "perfect accuracy" (due to hash collisions) for 
  "extreme throughput."
* **Memory Locality and Cache Performance**:
  * **Hardware Reality**: The two-pointer approach accesses the start and end of the string 
  simultaneously.
  * **The Bottleneck**: In modern hardware, these two memory addresses are in different CPU cache 
  lines. If the string is massive, the CPU will constantly "cache miss" as it jumps between the two 
  pointers.
  * **The Optimization**: For high-performance systems, we might stream the first half and the 
  reversed second half into the same buffer so the CPU can read them linearly (sequentially), 
  which is significantly faster.

* *"While the Greedy Iterative approach is optimal for **`K = 1`**, the Recursive approach is a more
robust design for varying tolerances. At system scale, distributed alignment and memory locality 
(cache misses) become the primary performance bottlenecks, not the algorithmic complexity."*

## Day 13 - LC 125 - Valid Palindrome
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
