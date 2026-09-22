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
