# LeetCodePrep Week 8

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 8: BACKTRACKING & RECURSION (September 22 - September 26)
# ===============================================================

## Topics Covered
- [ ] Combinations
- [ ] Permutations
- [ ] Divide & Conquer
- [ ] Recursion & Backtracking Fundamentals

## Day 39 - LC 90. Subsets II
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Duplicate Candidates in Input Array**: Input contains duplicate numbers (`[1, 2, 2]`), but 
  output subsets must not contain duplicate combinations.
  * **Lexicographical Grouping via Sorting**: Sorting the input array (`nums.sort()`) groups identical 
  elements together, enabling **O(1) duplicate detection** during backtracking exploration.
  * **Decision Tree Level Pruning (`i > start && nums[i] == nums[i - 1]`)**: In combinations/subsets, 
  duplicates are pruned *at the same decision depth* (`i > start`). Choosing `nums[i]` when `nums[i]
  == nums[i - 1]` at index `i > start` would spawn an identical subtree to the one already explored 
  by `nums[i - 1]`.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When... |
 :--- | :--- | :--- | :--- | :--- |
 **Level Pruning (i > start)** | **O(N x 2^N)** | **O(N)** | **Staff Level (Optimal)** | Idiomatic combination backtracking; no auxiliary boolean array. |
 **Visited Array (used[i])** | **O(N x 2^N)** | **O(N)** | Good | Works for permutations; adds unnecessary $O(N)$ boolean array allocation for subsets. |

*\*Time includes **O(N)** cost of copying each subset into results. Auxiliary space excludes the output list.*

### 3. Native Kotlin Syntax Pitfalls
* **Used Array Redundancy**: The `used: BooleanArray` pattern is essential for **Permutations** 
(where elements can be selected out of order), but redundant for **Subsets/Combinations** (where 
elements move monotonically forward).
* **`removeLast()` Efficiency**: Use `path.removeLast()` or `removeAt(path.lastIndex)` instead of 
`removeAt(sublist.size - 1)` for clean **O(1)** stack pop semantics.
* **Input Mutation Overhead**: `nums.sort()` mutates the input array in-place. If API contract 
prohibits mutating inputs, sort a copy (`nums.clone().apply { sort() }`).

### 4. Code Block
```kotlin
// Approach 1: Level Pruning (Canonical & Staff Level Optimal)
// Time Complexity: O(N * 2^N) | Auxiliary Space Complexity: O(N) stack
fun subsetsWithDup(nums: IntArray): List<List<Int>> {
    val list = mutableListOf<List<Int>>()
    val path = ArrayList<Int>(nums.size)
    nums.sort() // Group identical elements together

    fun backtrack(start: Int) {
        list.add(ArrayList(path)) // Snapshot current state

        for (i in start until nums.size) {
            // Prune duplicate branches at the current decision level
            if (i > start && nums[i] == nums[i - 1]) continue
            path.add(nums[i])
            backtrack(i + 1)
            path.removeAt(path.lastIndex)
        }
    }

    backtrack(0)
    return list
}
```
```kotlin
// Approach 2: Used Array (Permutations Style)
// Time Complexity: O(N * 2^N) | Auxiliary Space Complexity: O(N) array + stack
fun subsetsWithDupUsedArray(nums: IntArray): List<List<Int>> {
    val list = mutableListOf<List<Int>>()
    val used = BooleanArray(nums.size)
    nums.sort()

    fun backtrack(idx: Int, sublist: MutableList<Int>) {
        list.add(ArrayList(sublist))

        for (i in idx until nums.size) {
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue
            used[i] = true
            sublist.add(nums[i])
            backtrack(i + 1, sublist)
            used[i] = false
            sublist.removeAt(sublist.size - 1)
        }
    }

    backtrack(0, mutableListOf())
    return list
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Subsets II vs. Permutations II Pruning Condition**:
  * **Subsets II**: We advance sequentially (`i + 1`). Duplicates are skipped when `i > start && 
  nums[i] == nums[i - 1]`.
  * **Permutations II**: We can pick any unvisited index (`used[i] == false`). Duplicates are skipped 
  when `i > 0 && nums[i] == nums[i - 1] && !used[i - 1]`.
* **Bitmask / Frequency Map Alternative**:
  * An alternative approach counts element frequencies (e.g. `Map<Int, Int>`) and chooses to include 
  `0..count` instances of each distinct element. This eliminates sorting overhead if input values are 
  bounded.

---

## Day 38 - LC 148. Sort List
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * ****O(N x Log N)** Time Constraint**: Sorting a singly linked list efficiently requires a 
  comparison-based algorithm operating in **O(N x Log N)** time.
  * **Min-Heap (PriorityQueue) Shortcut**: Storing all list nodes in a Min-Heap automatically sorts 
  them by node value in **O(N x Log N)** time, providing a quick implementation at the cost of 
  **O(N)** extra memory and heavy JVM Garbage Collection **(GC)** pressure.
  * **Divide & Conquer (Merge Sort)**: The canonical **O(1)** auxiliary space approach for linked lists. 
  Unlike arrays where Merge Sort requires **O(N)** auxiliary array space for merging, linked lists allow 
  **O(1)** pointer reassignments without element shifting, making **Merge Sort** optimal.

### 2. Complexity Boundaries
* Comparison Matrix

 Approach | Time | Space | Performance | Best Used When...                                                            |
 :--- | :--- | :--- | :--- |:-----------------------------------------------------------------------------|
 **Bottom-Up Iterative Merge Sort** | **O(N log N)** | **O(1)** | **Staff Level (Optimal)** | Production systems; strict **O(1)** auxiliary space; prevents JVM `StackOverflowError`. |
 **Top-Down Merge Sort** | **O(N log N)** | **O(log N)** | **Peak (Canonical)** | Standard interview choice; clean recursive divide-and-conquer.               |
 **PriorityQueue (Min-Heap)** | **O(N log N)** | **O(N)** | Moderate | Rapid prototyping / small list sizes where auxiliary memory is acceptable. |

### 3. Native Kotlin Syntax Pitfalls
* **Comparator Subtraction Integer Overflow**: Direct subtraction `{ a, b -> a.`val` - b.`val` }` 
causes integer overflow if node values span negative and positive extremes (e.g., `Int.MIN_VALUE`). 
Always use **`compareBy { it.`val` }`** or `a.`val`.compareTo(b.`val`)`.
* **Stale Pointer Cleanup**: When rebuilding a linked list from heap nodes, setting `tail.next = null` 
after the polling loop is crucial to prevent dangling references or cycle creation.
* **Escaped Keyword Identifier (`` `val` ``)**: Because `val` is a reserved keyword in Kotlin, 
accessing the `val` property on `ListNode` requires backticks (`` `val` ``).

### 4. Code Block
```kotlin
// Approach 1: PriorityQueue (Min-Heap)
// Time Complexity: O(N log N) | Space Complexity: O(N)
fun sortListPQ(head: ListNode?): ListNode? {
    if (head?.next == null) return head

    // Use compareBy to avoid integer overflow from subtraction { a, b -> a.val - b.val }
    val pq = PriorityQueue<ListNode>(compareBy { it.`val` })
    var listNode = head
    while (listNode != null) {
        pq.add(listNode)
        listNode = listNode.next
    }

    val dummy = ListNode(0)
    var tail = dummy
    while (pq.isNotEmpty()) {
        val node = pq.poll()!!
        tail.next = node
        tail = node
    }
    tail.next = null

    return dummy.next
}
```
```kotlin
// Approach 2: Divide & Conquer (Top-Down Merge Sort)
// Time Complexity: O(N log N) | Space Complexity: O(log N) auxiliary (recursion stack)
fun sortList(head: ListNode?): ListNode? {
    if (head?.next == null) return head

    // 1. Split list into two halves via Slow & Fast pointers
    var prev: ListNode? = null
    var slow = head
    var fast = head

    while (fast != null && fast.next != null) {
        prev = slow
        slow = slow?.next
        fast = fast.next?.next
    }
    prev?.next = null // Terminate left half

    // 2. Recursively sort both halves
    val l1 = sortList(head)
    val l2 = sortList(slow)

    // 3. Merge sorted halves
    return merge(l1, l2)
}

private fun merge(list1: ListNode?, list2: ListNode?): ListNode? {
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
// Approach 3: Bottom-Up Iterative Merge Sort (Staff/Principal Level Optimal)
// Time Complexity: O(N log N) | Space Complexity: O(1) strictly constant space
fun sortListIterative(head: ListNode?): ListNode? {
    if (head?.next == null) return head

    // Compute total length
    var length = 0
    var curr = head
    while (curr != null) {
        length++
        curr = curr.next
    }

    val dummy = ListNode(0)
    dummy.next = head

    // Step sizes: 1, 2, 4, 8, ... up to length
    var step = 1
    while (step < length) {
        var prev = dummy
        var curr = dummy.next

        while (curr != null) {
            // Split left sublist of length 'step'
            val left = curr
            val right = split(left, step)
            curr = split(right, step) // Remaining list for next iteration

            // Merge left and right halves, attaching to prev
            prev.next = mergeIterative(left, right)

            // Advance prev pointer to the end of merged sublist
            while (prev.next != null) {
                prev = prev.next!!
            }
        }
        step = step shl 1
    }

    return dummy.next
}

// Splits list after 'step' nodes; returns head of remainder list
private fun split(head: ListNode?, step: Int): ListNode? {
    var curr = head
    for (i in 1 until step) {
        if (curr == null) break
        curr = curr.next
    }
    if (curr == null) return null

    val nextHead = curr.next
    curr.next = null // Sever list
    return nextHead
}

private fun mergeIterative(l1: ListNode?, l2: ListNode?): ListNode? {
    val dummy = ListNode(0)
    var tail = dummy
    var p1 = l1
    var p2 = l2

    while (p1 != null && p2 != null) {
        if (p1.`val` <= p2.`val`) {
            tail.next = p1
            p1 = p1.next
        } else {
            tail.next = p2
            p2 = p2.next
        }
        tail = tail.next!!
    }
    tail.next = p1 ?: p2
    return dummy.next
}
```

### 5. Alternative Trade-offs & Staff-Level Architectural Discussions
* **Merge Sort vs. QuickSort vs. Timsort for Linked Lists**:
  * **QuickSort**: Requires **O(1)** random access (`arr[pivot]`), which degrades to **O(N)** on linked 
  lists. Choosing a good pivot on linked lists is expensive, leading to **O(N^2)** worst-case time complexity.
  * **Merge Sort**: Optimal for linked lists because splitting via fast/slow pointers takes **O(N)** 
  and merging takes **O(1)** auxiliary space without memory relocation.
  * **Timsort (`java.util.Arrays.sort`)**: Operates on contiguous memory. If converted to an array first, 
  it runs in **O(N x Log N)** time with spatial locality benefit, but introduces **O(N)** allocation and 
  value copying overhead.
* **JVM Cache Locality & Hardware Prefetching**:
  * Linked list nodes are scattered across the JVM heap (`ListNode` object header = 16 bytes + 
  payload/references = ~24–32 bytes per node), causing frequent CPU **L1/L2 cache misses**.
  * Array-based sorting operates on contiguous memory blocks, leveraging CPU cache lines and SIMD 
  instructions. In real-world micro-benchmarks with **N < 10,000**, copying a linked list to an `IntArray`, 
  sorting with Dual-Pivot Quicksort / Timsort, and writing back values can often beat pure 
  pointer-manipulation Merge Sort due to cache locality!
* **Stack Depth vs. Iterative Safety**:
  * Top-down recursion uses **O(N x Log N)** call stack frames. For **N = 10^6**, stack depth is **∼20**,
  which easily fits in default JVM stack size (`-Xss1m`). However, for extremely constrained embedded 
  JVMs or deeper recursion structures, **Bottom-Up Iterative Merge Sort** guarantees no 
  `StackOverflowError` with strict **O(1)** space.
* **Concurrency & In-Place Pointer Side Effects**:
  * Pointer manipulation mutates the list structure directly. If the input list is shared across 
  coroutines or threads without synchronization, this causes structural race conditions. In multi-threaded 
  Kotlin/Java services, creating a new sorted list or using immutable structures is safer despite 
  GC allocation cost.

---

## Day 37 - LC 46. Permutations
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Exhaustive Ordering (N!)**: The goal is to generate all possible orderings of the input set. 
  Since order matters, and we use every element, this identifies as a **Permutation** problem within 
  the **Backtracking** paradigm.
  * **Availability Management**: Unlike combinations where we move a "start" pointer forward, 
  permutations require visiting all indices in every step. This triggers the need for a **"Visited" 
  tracker** (BooleanArray) to ensure each element is used exactly once per path.
  * **Path Reconstruction**: Using a **Deque** (Stack behavior) allows for efficient **O(1)** 
  addition and removal of elements as we traverse the decision tree.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time          | Space | Performance | Best Used When... |
| :--- |:--------------| :--- | :--- | :--- |
| **Backtracking (Used Array)** | **O(N x N!)** | **O(N)** | **Peak** | Input elements are distinct; clear logic. |
| **Backtracking (Swapping)** | **O(N x N!)** | **O(1)** | High | You want to avoid the auxiliary $O(N)$ used array. |

*\*Time includes **O(N)** for copying each permutation. Space excludes output list.*

### 3. Native Kotlin Syntax Pitfalls
*   **The `toList()` Snapshot**: Just like subsets, adding the `dq` directly would result in empty 
lists. `dq.toList()` is the idiomatic way to create a shallow copy of the current state.
*   **`ArrayDeque` Efficiency**: In Kotlin, `ArrayDeque` is the modern, non-synchronized replacement 
for `Stack`. Using `addLast` and `removeLast` provides consistent **O(1)** path management.
*   **Capturing Scope**: Defining `backtrack` as a **nested function** allows it to capture `nums`, 
`used`, and `list` without passing them as arguments, keeping the recursive call stack lighter.

### 4. Code Block
```kotlin
fun permute(nums: IntArray): List<List<Int>> {
    // If you want to keep the explicit used array and path approach (e.g. when lexicographical 
    // order is desired), here is the cleaned up idiomatic version:
    val list = mutableListOf<List<Int>>()
    val used = BooleanArray(nums.size)
    val path = ArrayList<Int>(nums.size)

    fun backtrack() {
        if (path.size == nums.size) {
            list.add(ArrayList(path))
            return
        }

        for (i in nums.indices) {
            if (used[i]) continue
            used[i] = true
            path.add(nums[i])
            backtrack()
            path.removeLast()
            used[i] = false
        }
    }

    backtrack()
    return list
}
```
```kotlin
fun permuteDQ(nums: IntArray): List<List<Int>> {
    // If you want to keep the explicit used array and path approach (e.g. when lexicographical 
    // order is desired), here is the cleaned up idiomatic version:
    // Highly Optimized Backtracking Approach
    // Time Complexity: O(N * N!) | Space Complexity: O(N * N!)
    val list = mutableListOf<List<Int>>()
    val used = BooleanArray(nums.size)
    val dq = ArrayDeque<Int>(nums.size)

    fun backtrack() {
        if (dq.size == nums.size) {
            list.add(dq.toList())
            return
        }

        for (i in nums.indices) {
            if (used[i]) continue
            dq.addLast(nums[i])
            used[i] = true
            backtrack()
            dq.removeLast()
            used[i] = false
        }

    }

    backtrack()
    return list
}
```

```kotlin
fun permuteInPlace(nums: IntArray): List<List<Int>> {
    // Swapped order (non-lexicographical)
    // Space-Optimized Backtracking (Swapping Approach)
    // Time Complexity: O(N * N!) | Space Complexity: O(1) auxiliary
    val list = mutableListOf<List<Int>>()

    fun swap(i: Int, j: Int) {
        val temp = nums[i]
        nums[i] = nums[j]
        nums[j] = temp
    }

    fun backtrack(start: Int) {
        if (start == nums.size) {
            // Snapshot current state
            list.add(nums.toList())
            return
        }

        for (i in start until nums.size) {
            swap(start, i)      // Choose
            backtrack(start + 1) // Explore
            swap(start, i)      // Un-choose (Backtrack)
        }
    }

    backtrack(0)
    return list
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Used Array vs. Swapping**: 
    *   The **Used Array** (your approach) is more intuitive and easier to adapt to problems with 
    duplicates (LC 47).
    *   The **Swapping** approach (in-place `swap(nums, i, j)`) avoids the **O(N)** extra space of the 
    boolean array, which might be critical in extremely memory-constrained environments.
*   **Heap's Algorithm**: A non-backtracking, iterative alternative for generating permutations. It 
is often faster for raw generation but harder to implement with additional constraints (like pruning).
*   **Recursive Limit**: For **N > 10**, the number of permutations **N!** becomes massive 
**10! ≈ 3.6M**, making the **O(N!)** space for the output the actual bottleneck rather than the 
recursion depth.

---

## Day 36 - LC 78. Subsets
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Explosive Search Space (2^N)**: The requirement to generate every possible subset (the 
  **Power Set**) identifies this as a non-polynomial problem, triggering a **Backtracking** approach
  to explore the decision tree.
  * **Local Decision Invariant**: At any depth `start`, the current state represents a valid subset.
  To avoid duplicates and ensure exhaustive search, we only consider elements at indices `j >= start`, 
  effectively "shrinking" the available candidates as we go deeper.
  * **State Management**: The "Choose-Explore-Unchoose" pattern is required to maintain a single 
  mutable path, keeping space complexity at **O(N)** instead of **O(N * 2^N)**.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time           | Space | Performance | Best Used When... |
| :--- |:---------------| :--- | :--- | :--- |
| **Backtracking** | **O(N x 2^N)** | **O(N)** | **Peak** | Generating all possible combinations/subsets. |
| **Bit Masking** | **O(N x 2^N)** | **O(N)** | High | You need a non-recursive, iterative solution. |

*\*Time includes copying each subset (**O(N)**) into the final list. Space excludes output list.*

### 3. Native Kotlin Syntax Pitfalls
*   **Mutable List reference**: Adding a mutable list directly to a result list (e.g.,`list.add(sublist)`) 
will result in a list of empty lists at the end because all references point to the same object. 
Always use **`.toList()`** or **`ArrayList(sublist)`** to create a deep copy of the current state.
*   **Remove Efficiency**: When backtracking, always remove from the **last index** 
(`sublist.removeAt(sublist.lastIndex)`). Removing from the front or by value would turn an **O(1)** 
operation into **O(N)**.
*   **Recursion Scoping**: Defining the backtracking function as a **nested function** inside the 
main entry point allows it to capture the result list and input array from the outer scope, reducing
the number of parameters passed on the stack.

### 4. Code Block
```kotlin
fun subsets(nums: IntArray): List<List<Int>> {
    // Highly Optimized Backtracking Approach
    // Time Complexity: O(N * 2^N) | Space Complexity: O(N)
    val result = mutableListOf<List<Int>>()
    
    fun backtrack(start: Int, currentPath: MutableList<Int>) {
        // 1. Add a copy of the current state to results
        result.add(ArrayList(currentPath))
        
        // 2. Explore further combinations
        for (i in start until nums.size) {
            currentPath.add(nums[i])      // Choose
            backtrack(i + 1, currentPath)  // Explore
            currentPath.removeAt(currentPath.lastIndex) // Un-choose (Backtrack)
        }
    }
    
    backtrack(0, mutableListOf())
    return result
}
```
```kotlin
fun subsets(nums: IntArray): List<List<Int>> {
    // Original Implementation (Choose-Explore-Unchoose)
    // Time Complexity: O(N * 2^N) | Space Complexity: O(N × 2^N)
    val list = mutableListOf<List<Int>>()
    findCombinations(nums, 0, mutableListOf<Int>(), list)
    return list
}

fun findCombinations(nums: IntArray, i: Int, sublist: MutableList<Int>, list: MutableList<List<Int>>) {
    list.add(sublist.toList())
    for (j in i .. nums.lastIndex) {
        sublist.add(nums[j]) // Choose
        findCombinations(nums, j + 1, sublist, list) // Explore
        sublist.removeAt(sublist.lastIndex) // Un-choose (Backtrack)
    }
    return
}
```


### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Backtracking vs. Bitmasking**: 
    *   **Backtracking** is more intuitive for problems with complex constraints (e.g., pruning). 
    *   **Bitmasking** uses the binary representation of numbers from **0** to **2^N - 1** to decide 
    which elements to include. It is iterative and avoids recursion stack overhead, making it useful 
    in low-memory environments.
*   **Immutable Lists**: If the problem allowed for immutable data structures, we could use a 
functional approach where each recursive call creates a new list. However, this increases memory 
allocation significantly compared to the mutable "Choose-Explore-Unchoose" pattern.

---
