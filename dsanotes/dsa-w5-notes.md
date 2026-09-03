# LeetCodePrep Week 5

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 5: BINARY SEARCH & TREES (September 1 - September 7)
# ===============================================================

## Topics Covered
- [ ] Binary Search (Advanced)
- [ ] Binary Trees (Traversal & Construction)
- [ ] Binary Search Trees (BST)
- [ ] Trie (Prefix Tree)

## Day 25 - LC 207. Course Schedule
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Dependency Resolution**: The problem defines a set of "dependencies" (prerequisites) that must
  be satisfied before taking a course. This is the canonical signal for **Directed Acyclic Graph (DAG)** 
  validation.
  * **Cycle Detection**: If course A depends on B and B depends on A, neither can be finished. Thus,
  the problem reduces to: **"Does this directed graph contain a cycle?"**
  * **Topological Sort**: Both DFS (3-state) and BFS (Kahn's Algorithm) are standard techniques to 
  verify if a graph can be topologically sorted.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- |:-----------|:---------| :--- | :--- |
| **DFS (3-State)** | **O(V + E)** | **O(V + E)** | **Peak** | You need a recursive, concise implementation. |
| **BFS (Kahn's)** | **O(V + E)** | **O(V + E)** | **Peak** | You want an iterative solution or need the actual order. |

*\*V = numCourses, E = prerequisites.size*

### 3. Native Kotlin Syntax Pitfalls
*   **Adjacency List Construction**: Avoid `Array<MutableList<Int>>(V) { mutableListOf() }` if 
constraints are massive; consider primitive arrays if memory is tight. However, for most LeetCode 
tasks, `ArrayList` is fine.
*   **State Representation**: For DFS, using an `IntArray` (0=Unvisited, 1=Visiting, 2=Visited) is 
faster and more memory-efficient than a `Set` of visited nodes.
*   **Recursive Depth**: While $O(V)$ depth is usually fine on JVM for typical **V=2000**, extremely 
deep graphs might trigger `StackOverflowError`. BFS is safer for very deep graphs.

### 4. Code Block
```kotlin
fun canFinishDFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    // State representation: 0 = UNVISITED, 1 = VISITING (part of current path), 2 = SAFE (visited & no cycle)
    val state = IntArray(numCourses)
    val adj = Array(numCourses) { mutableListOf<Int>() }
    
    // Build Adjacency List: prerequisites[0] depends on prerequisites[1] (src -> dest)
    for (pre in prerequisites) {
        adj[pre[1]].add(pre[0])
    }

    // Helper: returns true if a cycle is detected
    fun hasCycle(u: Int): Boolean {
        if (state[u] == 1) return true // Hit a node currently being visited -> CYCLE!
        if (state[u] == 2) return false // Already fully processed -> SAFE

        state[u] = 1 // Mark as VISITING
        for (v in adj[u]) {
            if (hasCycle(v)) return true
        }
        state[u] = 2 // Mark as SAFE
        return false
    }

    for (i in 0 until numCourses) {
        if (state[i] == 0) {
            if (hasCycle(i)) return false
        }
    }
    return true
}
```

```kotlin
fun canFinishBFS(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    // Kahn's Algorithm (BFS-based Topological Sort)
    val adj = Array(numCourses) { mutableListOf<Int>() }
    val inDegree = IntArray(numCourses)

    for (pre in prerequisites) {
        val course = pre[0]
        val prereq = pre[1]
        adj[prereq].add(course)
        inDegree[course]++
    }

    // Queue for nodes with 0 in-degree (no dependencies)
    val queue = ArrayDeque<Int>()
    for (i in 0 until numCourses) {
        if (inDegree[i] == 0) queue.addLast(i)
    }

    var processedCount = 0
    while (queue.isNotEmpty()) {
        val u = queue.removeFirst()
        processedCount++

        for (v in adj[u]) {
            if (--inDegree[v] == 0) {
                queue.addLast(v)
            }
        }
    }

    // If we processed all nodes, no cycle exists
    return processedCount == numCourses
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Memory Efficiency**: In a massive graph where **V** is large but **E** is sparse, we could use
a `Map<Int, MutableList<Int>>` to store only active nodes. However, for a fixed **V**, `Array` is 
always faster.
*   **Parallelization**: Cycle detection is inherently sequential. However, Kahn's algorithm (BFS) 
allows for some level of parallel processing: multiple nodes with 0 in-degree can be processed by 
different threads simultaneously, as long as the in-degree updates are atomic.
*   **Large-Scale Persistence**: For graphs that don't fit in memory (e.g., package managers like 
NPM or Maven), you would use a graph database (Neo4j) or process the edges in batches using external
sort and merge.

---

## Day 24 - LC 239. Sliding Window Maximum
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Contiguous Window Maxima**: The requirement to find the maximum in every sliding window of 
  size **K**. This is the canonical signal for a **Monotonic Deque (Decreasing)**.
  * **Range Invalidation**: As the window slides, elements "expire." Storing **indices** instead of 
  values in the deque allows us to perform an **O(1)** check (`dq.first() <= i - k`) to remove elements 
  that have fallen out of range.
  * **Pruning Sub-optimal Candidates**: If a new element is larger than previous ones in the window,
  those previous elements can *never* be the maximum again. This allows us to maintain a deque where
  the largest element is always at the front.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time              | Space       | Performance | Best Used When...                                                   |
| :--- |:------------------|:------------| :--- |:--------------------------------------------------------------------|
| **Brute Force** | **O(N x K)**        | **O(1)**      | Low | Window size **K** is extremely small (e.g., 2).                       |
| **Priority Queue (Heap)** | **O(N x Log(N))** | **O(N)**    | Moderate | Memory is abundant and **N** is small.                              |
| **Monotonic Deque** | **O(N)**          | **O(K)**    | **Peak** | **Optimal.** standard for high-performance sliding window tasks.    |

### 3. Native Kotlin Syntax Pitfalls
*   **Loop Consolidation**: While two loops (init + slide) are correct, merging them into a single 
loop using `if (i >= k - 1)` is more idiomatic and reduces "tail" assignment logic.
*   **Strict vs. Non-Strict Monotonicity**: Using `nums[i] >= nums[dq.last()]` (non-strict) is 
slightly more efficient than `>` because it removes older equal values that will expire sooner than the current one.
*   **`ArrayDeque` Capacity**: Like in BFS, pre-sizing the `ArrayDeque(k)` prevents redundant array 
copies as the window fills up.

### 4. Code Block
```kotlin
fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
    // 1. "Senior" Implementation (Consolidated Loop & Pre-sized Deque)
    // Time Complexity: O(N) | Space Complexity: O(K)
    val n = nums.size
    val result = IntArray(n - k + 1)
    val dq = ArrayDeque<Int>(k) // Pre-size to avoid resizing overhead

    for (i in nums.indices) {
        // 1. Remove indices that have fallen out of the window
        if (dq.isNotEmpty() && dq.first() <= i - k) {
            dq.removeFirst()
        }

        // 2. Maintain Monotonic Decreasing property:
        // Remove elements smaller than current value from the back
        while (dq.isNotEmpty() && nums[i] >= nums[dq.last()]) {
            dq.removeLast()
        }

        dq.addLast(i)

        // 3. Once we've reached window size K, start recording results
        if (i >= k - 1) {
            result[i - k + 1] = nums[dq.first()]
        }
    }
    return result
}
```

```kotlin
fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
    // 2. Original Implementation (Two-Phase approach)
    val result = IntArray(nums.size - k + 1)
    val q = ArrayDeque<Int>()
    
    // Phase 1: Initialize the first window
    for (i in 0 until k) {
        while (q.isNotEmpty() && nums[i] > nums[q.last()]) {
            q.removeLast()
        }
        q.addLast(i)
    }

    // Phase 2: Slide the window
    for (i in k until nums.size) {
        val idx = i - k
        result[idx] = nums[q.first()]

        if (idx == q.first()) q.removeFirst()

        while (q.isNotEmpty() && nums[i] > nums[q.last()]) {
            q.removeLast()
        }
        q.addLast(i)
    }

    // Final tail assignment
    result[result.lastIndex] = nums[q.first()]
    return result
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Segment Trees / Sparse Tables**: For static arrays where you need to query *any* range 
(not just a sliding one), Segment Trees provide **O(Log N)** and Sparse Tables provide **O(1)** 
query time. They are more flexible but have higher preprocessing costs.
*   **Parallelization**: Sliding window is hard to parallelize because each step depends on the 
previous deque state. For massive data, one would "shard" the array into overlapping blocks, 
calculate local maxes, and merge.
*   **Memory vs. Latency**: In a real-time system (e.g., high-frequency trading), the Monotonic Deque 
is preferred because it provides the maximum for the *current* window with zero additional latency 
beyond the input arrival.

---

## Day 23 - LC 102. Binary Tree Level Order Traversal
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Level-by-Level Requirement**: The problem specifically asks for nodes grouped by their depth. 
  This is the canonical signal for **BFS (Breadth-First Search)**.
  * **Snapshot Size Invariant**: To separate levels in a single queue, we must capture the `queue.size` 
  at the start of each "while" iteration. This ensures we only process nodes belonging to the current 
  level before moving to the next.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- | :--- | :--- | :--- | :--- |
| **BFS (Queue)** | **O(N)** | **O(W)** | **Peak** | Tree is very deep but narrow; level order is required. |
| **DFS (Recursion)** | **O(N)** | **O(H)** | High | Memory is limited and tree is very wide but shallow. |

*\*N = total nodes, W = max width, H = height.*

### 3. Native Kotlin Syntax Pitfalls
*   **`LinkedList` vs `ArrayDeque`**: `LinkedList` is "Object-Heavy," allocating a new `Node` wrapper 
for every entry. `ArrayDeque` is backed by a circular array and is significantly more cache-friendly.
*   **The Resizing Penalty**: `ArrayDeque` can be slower for small inputs on LeetCode because it 
starts with a tiny capacity (10) and performs multiple array copies as it grows.
    *   **Senior Tip**: Initialize with `ArrayDeque<TreeNode>(2000)` if constraints are known.
*   **Null Safety & Backticks**: Accessing the value requires ``node.`val` `` because `val` is a 
reserved keyword in Kotlin. Use `node.left?.let { ... }` for idiomatic, null-safe queuing.

### 4. Code Block
```kotlin
fun levelOrder(root: TreeNode?): List<List<Int>> {
    if (root == null) return emptyList()

    // High-performance Queue using ArrayDeque (Circular Array)
    // Initial capacity set to 1024 to avoid resizing overhead for medium trees
    val queue = ArrayDeque<TreeNode>(1024)
    val result = mutableListOf<List<Int>>()
    
    queue.addLast(root)
    
    while (queue.isNotEmpty()) {
        val levelSize = queue.size
        val currentLevel = ArrayList<Int>(levelSize) // Pre-size for performance
        
        // Process current level only
        repeat(levelSize) {
            val node = queue.removeFirst()
            currentLevel.add(node.`val`)
            
            // Queue children for the next level
            node.left?.let { queue.addLast(it) }
            node.right?.let { queue.addLast(it) }
        }
        result.add(currentLevel)
    }
    
    return result
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Memory Locality**: `ArrayDeque` stores data in contiguous memory blocks. This allows the CPU 
to use "Prefetching" to load nodes into the L1 cache before the code even requests them. `LinkedList` 
nodes are scattered in RAM, leading to frequent "Cache Misses."
*   **BFS vs. DFS for Large Trees**:
    *   If a tree is **extremely wide** (e.g., a directory with 1 million files), BFS will consume 
    massive memory in the queue. DFS would be better.
    *   If a tree is **extremely deep** (e.g., a degenerate linked-list tree), DFS will cause a 
    `StackOverflowError`. BFS is the safer architectural choice.
*   **Concurrency**: In a distributed web crawler (which uses BFS), we would replace the local 
`ArrayDeque` with a distributed queue like **RabbitMQ** or **Kafka** to handle the scale.

---
