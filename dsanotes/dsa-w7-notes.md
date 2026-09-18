# LeetCodePrep Week 7

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 7: Sliding Window & Min Heap (September 17 - September 19)
# ===============================================================

## Topics Covered
- [ ] Greedy Sliding Window Algorithms
- [ ] Min Heap (Priority Queue)

## Day 34 - LC 23. Merge k Sorted Lists
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **K-Way Merge**: We have multiple sorted sources and need a single sorted output. This is the 
  canonical use case for a **Min-Heap (Priority Queue)**.
  * **Heap Invariant**: By keeping only the "heads" of the k lists in the heap, the smallest overall
  element is always at the top.
  * **Pointer Persistence**: Unlike merging two lists, we cannot easily use simple pointers for k 
  sources; the heap provides O(Log(k)) selection of the next element.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- |:-----------|:---------| :--- | :--- |
| **PQ (Total Nodes)** | $O(N \log N)$ | $O(N)$ | Moderate | Implementation speed is prioritized over efficiency. |
| **PQ (K-Bounded)** | **$O(N \log k)$** | **$O(k)$** | **Peak** | **Optimal.** standard for large-scale external merges. |
| **Divide & Conquer** | $O(N \log k)$ | $O(1)$ | High | You want to avoid the heap object overhead entirely. |

*\*N = total nodes, k = number of lists.*

### 3. Native Kotlin Syntax Pitfalls
*   **PriorityQueue Constructor**: Kotlin's `PriorityQueue` can take a `Comparator` directly. 
`compareBy { it.`val` }` is the most idiomatic way to define the sort order.
*   **Redundant Wrapping**: Avoid wrapping nodes in `Pair` or custom objects if you only need the 
value for comparison. It adds unnecessary heap allocation (**O(N)** objects).
*   **Dummy Head Pattern**: As with "Merge Two Lists," using a `dummy` node eliminates null-checks 
for the result head, making the `tail` logic much cleaner.
*   **`compareBy { it.`val` }` vs { a, b -> a.`val` - b.`val` }**: The former is more idiomatic but
introduces hidden autoboxing and unboxing overhead under the hood in Kotlin. It takes a selector 
function that returns a type extending Comparable<T>, because generics in Java/Kotlin cannot use raw 
primitives directly, your primitive Int value (it.val``) must be automatically converted into a boxed 
java.lang.Integer object so it can be treated as a `Comparable`.

### 4. Code Block
```kotlin
fun mergeKLists(lists: Array<ListNode?>): ListNode? {
    // 1. Divide & Conquer Strategy (Merge Sort Style)
    // Time Complexity: O(N log k) | Space Complexity: O(log k) stack
    if (lists.isEmpty()) return null
    return divideAndConquer(lists, 0, lists.lastIndex)
}

private fun divideAndConquer(lists: Array<ListNode?>, start: Int, end: Int): ListNode? {
    if (start == end) return lists[start]
    
    val mid = start + (end - start) / 2
    val left = divideAndConquer(lists, start, mid)
    val right = divideAndConquer(lists, mid + 1, end)
    
    return mergeTwoListsIteration(left, right)
}

fun mergeTwoListsIteration(list1: ListNode?, list2: ListNode?): ListNode? {
    val dummy = ListNode(0)
    var tail = dummy
    var l1 = list1
    var l2 = list2

    while (l1 != null && l2 != null) {
        if (l1.`val` <= l2.`val`) {
            tail.next = l1
            l1 = l1.next
        } else {
            tail.next = l2
            l2 = l2.next
        }
        tail = tail.next!!
    }
    tail.next = l1 ?: l2
    return dummy.next
}
```
```kotlin
import java.util.PriorityQueue

fun mergeKLists(lists: Array<ListNode?>): ListNode? {
    // K-Bounded Min-Heap Strategy
    // Time Complexity: O(N log k) | Space Complexity: O(k)
    if (lists.isEmpty()) return null
  
    // This will cause boxing/unboxing overhead
    // val pq = PriorityQueue<ListNode>(compareBy { it.`val` })
  
    
    val pq = PriorityQueue<ListNode> { a, b -> a.`val` - b.`val` }

    // Seed heap with heads of lists
    for (list in lists) {
        list?.let { pq.add(it) }
    }

    val dummy = ListNode(0)
    var tail = dummy

    while (pq.isNotEmpty()) {
        val node = pq.poll()!!
        tail.next = node
        tail = node

        // Push the next node from the same list into the heap
        node.next?.let { pq.add(it) }
    }

    return dummy.next
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Divide and Conquer vs. Heap**:
    *   **D&C**: Superior cache locality and zero heap allocation for sorting. However, it is recursive 
    and carries an **O(Log k)** stack space risk. 
    *   **Heap**: Strictly iterative and safer for massive **k**, but involves frequent **O(Log k)** 
    pointer re-balancing and object overhead.
*   **External Sorting**: This heap-based approach is the foundation of **External Merge Sort**. If 
the **k** lists were too large for RAM (e.g., 1TB each on disk), you would keep only one buffer-load
of each list in memory and use this **O(k)** heap to stream the sorted output to disk.
*   **Iterative D&C**: To achieve **O(1)** space (excluding output), the Divide & Conquer approach 
can be rewritten iteratively using a loop to merge pairs in-place within the original `lists` array.
*   **Thread Safety**: `java.util.PriorityQueue` is not thread-safe. If multiple threads were adding
lists to be merged, you would need `PriorityBlockingQueue` or explicit synchronization.

---

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
