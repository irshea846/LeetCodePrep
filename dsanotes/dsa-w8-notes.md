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

*\*Time includes $O(N)$ for copying each permutation. Space excludes output list.*

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
