# LeetCodePrep Week 7

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 7: Sliding Window & Min Heap (September 17 - September 19)
# ===============================================================

## Topics Covered
- [ ] Greedy Sliding Window Algorithms
- [ ] Min Heap (Priority Queue)

## Day 33 - LC 3. Longest Substring Without Repeating Characters
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Substring Requirement**: This problem expects to find the longest length of substring with no 
  repeating characters. Therefore, we need to have two pointers - one points to left and the other 
  points to the right which will keep scanning if there is no repeating character. "Sliding Window" 
  technique will play out.
  *  **Uniqueness Invariant**: We need to keep the logic of current string without any repeating 
  characters always true in the sliding window.
  *  **Fast Shifting Technique**: Instead of shrinking the left pointer one by one, we can jump it to
  the next index to the previous index of current duplicate character. 
  

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time       | Space    | Best Used When... |
| :--- |:-----------|:---------| :--- |
| **Brute Force** | **O(N^3)** | **O(M)** | Only for extremely small inputs. |
| **Sliding Window (Shrink)** | **O(2N)**  | **O(M)** | Beginner-friendly; uses a `while` loop to shrink. |
| **Sliding Window (Jump)** | **O(N)**   | **O(M)** | **Optimal.** Single pass with zero redundant checks. |

### 3. Native Kotlin Syntax Pitfalls
* **Char.toInt() vs Char.code**: While newer Kotlin uses `.code`, some enrironments use `.toInt()` 
to get the ASCII value of a `Char`. LeetCode environment has updated its environment to use `.code`.
* **Lazy vs. Eager Max**: Calculating `maxLen` only when a duplicate is found (Lazy) is faster than
calculating it every iteration (Eager), but requires a final check at the return statement.
*  **Charset Assumptions**: `IntArray(128)` is enough for standard ASCII, but **`256`** is safer for
extended sets to avoid `IndexOutOfBounds`. 
*  **Sparse Unicode Handling**: While `IntArray(65536)` works for Unicode, a **`HashMap<Char, Int>`** 
is more memory-efficient if the character set is sparse (e.g., only a few rare characters used), 
avoiding massive heap allocation for unused slots.

### 4. Code Block
```kotlin
import kotlin.math.max

fun lengthOfLongestSubstring(s: String): Int {
    val n = 128 // 256 for Extended ASCII
    val ascii = IntArray(n) { -1 }
    var maxLen = 0
    var left = 0
    
    for (right in s.indices) {
        val charCode = s[right].code
        
        // If character was seen within the current window, "jump" the left pointer
        if (ascii[charCode] >= left) {
            maxLen = max(maxLen, right - left)
            left = ascii[charCode] + 1
        }
        ascii[charCode] = right
    }
    
    // Mandatory final check: handles cases where the longest substring 
    // is at the very end of the input (e.g., "abc")
    return max(maxLen, s.length - left)
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Memory Capping**: In a system processing a stream of data (e.g., detecting duplicate user IDs), 
HashSet is strictly superior because its memory usage is capped by K. HashMap would grow indefinitely 
(O(N)).
* **IntArray vs HashMap**: `IntArray` is significantly faster than `HashMap` on the JVM due to zero 
object boxing and contiguous memory access.
* **CPU Write Optimization**: By using the "Lazy" update strategy, the frequency of `maxLen` 
calculations is minimized. This is a meaningful optimization in high-throughput systems processing gigabytes.
* **The "Tail" Edge Case**: The "Lazy" strategy requires a final comparison at the return statement 
because if the string ends with a valid non-repeating sequence (like "...xyz"), the loop completes 
without hitting a duplicate to trigger the internal `max()` update.
---
