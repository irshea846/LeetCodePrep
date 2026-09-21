# LeetCodePrep Week 7 Mastery & Review Day

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 7: SLIDING WINDOW & MIN HEAP & MONO STACK (September 19)
# ===============================================================

## LC 739. Daily Temperatures
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Days to Wait For Next Warmer Temperature**: The canonical feeling to solve this problem is 
  **Monotonic Stack**. Like finding the next greater element, we are looking for the next warmer 
  temperature.
  * **Monotonic Stack Invariant**: This logic to keep it working is always push the index of the 
  cooler temperatures on the top of stack. Once a warmer temperature is hit from the current index `i`
  and `temperature[i] > temperature[monoStack[top]]`, use `i - monoStack[top]` to get the days to wait
  for next warmer temperature.
  * **Backward Scanning**: We also can monotonic stack to do backward scanning. However, the logic 
  would be slightly different. We always push the index of the warmer temperatures on the top of 
  stack. Once a cooler temperature is hit from the current index `i` and `temperature[i] < 
  temperature[monoStack[top]]`, use `monoStack[top] - i` to get the days to wait for next warmer day.


### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- |:-----------|:---------| :--- | :--- |
| **Brute Force** | **O(N^2)** | **O(1)** | Low | **N** is tiny. |
| **Monotonic Stack** | **O(N)** | **O(N)** | **Peak** | **Optimal.** standard for "Next Greater Element" problems. |

### 3. Native Kotlin Syntax Pitfalls
* **Manual Stack Pointer**: Using a primitive array (`IntArray`) with a pointer (`top`) as a stack.
  the `top` is increment by 1 when pushing and decrement by 1 when popping. It will be faster than
  `ArrayDeque` or `Stack` because it avoids object boxing and dynamic resizing. We also know the length
  of result `IntArray` should be the same as `temperatures` array, so the size of stack can be directly
  used as `temperatures.size`.
* **Zero-Initialization**: Kotlin's `IntArray` defaults to zeros. Since the problem requires `0` for
  those cooler days with no warmer temperature, we can take this advantage of default value to skip
  checking any index still in the stack.

### 4. Code Block
```kotlin
fun dailyTemperatures(temperatures: IntArray): IntArray {
    val n = temperatures.size
    val result = IntArray(n)
    val monoStack = IntArray(n)
    var top = -1
    for (i in 0 until n) {
        while (top >= 0 && temperatures[i] > temperatures[monoStack[top]]) {
            result[monoStack[top]] = i - monoStack[top]
            top--
        }
        monoStack[++top] = i
    }
    return result
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Array-based vs. Collection-based Stack**: Use Array-based stack is easier to implement, faster
  to retrieve element and avoid the overhead of object boxing and unboxing. Collection-based stack needs
  more boilerplate code to check if it is full to push or empty to pop elements. Even though they have
  the same runtime complexity **O(N)**, Array-based stack is more memory-efficient.
---

## LC 23. Merge k Sorted Lists
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

 Approach | Time | Space | Performance | Best Used When... |
 :--- |:-----------|:---------| :--- | :--- |
 **PQ (Total Nodes)** | **O(N x Log N)** | **O(N)** | Moderate | Implementation speed is prioritized. |
 **PQ (K-Bounded)** | **O(N x Log k)** | **O(k)** | **Peak** | **Optimal.** Memory efficiency is required. |
 **Divide & Conquer** | **O(N x Log k)** | **O(Log k)** | **Peak** | **Fastest.** Better cache locality on JVM. |

### 3. Native Kotlin Syntax Pitfalls
*   **PriorityQueue Constructor**: Kotlin's `PriorityQueue` can take a `Comparator` directly. 
`compareBy { it.`val` }` is idiomatic but introduces **Autoboxing** overhead (converting primitive 
`Int` to `Integer`). Using `{ a, b -> a.`val` - b.`val` }` is safer for performance.
*   **Dummy Head Pattern**: Using a `dummy` node eliminates null-checks for the result head, 
making the `tail` logic much cleaner.

### 4. Code Block
```kotlin
fun mergeKLists(lists: Array<ListNode?>): ListNode? {
    // K-Bounded Min-Heap Strategy
    if (lists.isEmpty()) return null
    val pq = PriorityQueue<ListNode> { a, b -> a.`val` - b.`val` }

    for (list in lists) {
        list?.let { pq.add(it) }
    }

    val dummy = ListNode(0)
    var tail = dummy
    while (pq.isNotEmpty()) {
        val node = pq.poll()!!
        tail.next = node
        tail = node!!
        node.next?.let { pq.add(it) }
    }
    return dummy.next
}

fun mergeKListsDC(lists: Array<ListNode?>): ListNode? {
    // Divide & Conquer Strategy
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

fun mergeTwoListsIteration(l1: ListNode?, l2: ListNode?): ListNode? {
    val dummy = ListNode(0)
    var tail = dummy
    var p1 = l1; var p2 = l2
    while (p1 != null && p2 != null) {
        if (p1.`val` <= p2.`val`) {
            tail.next = p1; p1 = p1.next
        } else {
            tail.next = p2; p2 = p2.next
        }
        tail = tail.next!!
    }
    tail.next = p1 ?: p2
    return dummy.next
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Heap vs D&C**: D&C is often **faster** on JVM due to better cache locality (sequential access) 
and zero object overhead for the "sorting" logic. Heap is strictly iterative and better for 
**External Sorting** (merging data from disk).
* **Parallelization**: D&C is naturally parallelizable. Pairs of lists can be merged by different 
threads simultaneously.
---

## LC 3. Longest Substring Without Repeating Characters
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Constraint Requirement**: The problem requires us to find the length of the longest substring 
  without repeating character. It is a canonical use case for **Sliding Window** which needs two
  pointers. Use right pointer to expand the window and left pointer to shrink the window.
  *  **Loop Invariant**: In the loop, right pointer scans the string from 0 to the last index, a
  128-index(each character ASCII code as index) look-up table persists each character's last-index 
  position and left pointer is a referencer for the starting pointer in the current valid sliding
  window. If `ascii[s[right].code] >= left` means the previous index of current character right 
  pointer points to is in the sliding window, we need to calculate the length `right - left` and 
  update maxLength when the length is larger than the previous maxLength.   

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
fun lengthOfLongestSubstring(s: String): Int {
    val n = 128 //256 for extended ASCII
    val ascii = IntArray(128) { -1 }
    var left = 0
    var maxLength = 0
    for (right in s.indices) {
      val c = s[right].code
      if (ascii[c] >= left) {
        maxLength = max(maxLength, right - left)
        left = ascii[c] + 1
      }
      ascii[c] = right
    }
  
    return max(maxLength, s.length - left)
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