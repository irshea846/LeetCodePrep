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

## Day 25 - LC 102. Binary Tree Level Order Traversal
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
