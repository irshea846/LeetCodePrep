# LeetCodePrep Week 6

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 6: GRAPHS & ADVANCED DATA STRUCTURES (September 8 - September 12)
# ===============================================================

## Topics Covered
- [ ] Graph Traversal (BFS/DFS)
- [ ] Lined List
- [ ] Binary Tree Traversal
- [ ] Queue / Deque

## Day 31 - LC 547. Number of Provinces
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Connectivity in Disjoint Sets**: The problem asks to count "provinces" (connected components). 
  This is the hallmark for **Graph Traversal (DFS/BFS)** or **Disjoint Set Union (DSU)**.

  * **Symmetric Adjacency Matrix**: The input `isConnected[i][j]` defines edges between cities. 
  Since it's an adjacency matrix, traversal is **O(N^2)**.
  
  * **Global Component Counting**: DSU is particularly elegant here because we start with **N** 
  provinces and decrement the count every time a successful `union` operation merges two previously 
  separate sets.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- |:-----------|:---------| :--- | :--- |
| **DFS / BFS** | **O(N^2)** | **O(N)** | High | Matrix is static; implementation speed is prioritized. |
| **Union-Find (DSU)** | **O(N^2 \cdot \alpha(N))** | **O(N)** | **Peak** | **Dynamic connectivity** (edges added over time). |

*\*N = number of cities.*

### 3. Native Kotlin Syntax Pitfalls
*   **Matrix Symmetry**: Don't check the whole matrix. `for (j in i + 1 until n)` avoids redundant 
checks and potential $O(N^2)$ work on the diagonal.
*   **Typo Alert**: Ensure `dfs` naming is standard. Avoid naming conflicts or typos like `DisjoinSetUnion` 
vs `DisjointSetUnion` which cause compilation failures.
*   **Path Compression**: In DSU, always use `parent[i] = find(parent[i])`. Without this, the tree 
height can become **O(N)**, degrading performance to **O(N^2)** for Union operations.
*   **BFS/DFS redundant starts**: Always wrap your traversal call in `if (!visited[i])`. Calling 
BFS/DFS on an already visited node won't break the logic, but it adds unnecessary **O(N)** scans to 
your total execution time.

### 4. Code Block
```kotlin
fun findCircleNum(isConnected: Array<IntArray>): Int {
    // DSU Simplified version
    val n = isConnected.size
    val parent = IntArray(n) { it }
    var provinces = n

    fun find(i: Int): Int {
        if (parent[i] == i) return i
        parent[i] = find(parent[i]) // Path compression
        return parent[i]
    }

    fun union(i: Int, j: Int) {
        val rootI = find(i)
        val rootJ = find(j)
        if (rootI != rootJ) {
            parent[rootI] = rootJ // Simple union (Rank/Size is optional for N=200)
            provinces--
        }
    }

    for (i in 0 until n) {
        for (j in i + 1 until n) {
            if (isConnected[i][j] == 1) union(i, j)
        }
    }

    return provinces
}
```

```kotlin
fun findCircleNumDSU(isConnected: Array<IntArray>): Int {
    // Union-Find Approach
    // Time Complexity: O(N^2 x A(N)) | Space Complexity: O(N)
    val cities = isConnected.size
    val dsu = DisjointSetUnion(cities)
    var provinces = cities

    for (i in 0 until cities) {
        for (j in i + 1 until cities) {
            if (isConnected[i][j] == 1) {
                if (dsu.union(i, j)) {
                    provinces--
                }
            }
        }
    }

    return provinces
}

class DisjointSetUnion(n: Int) {
    val parent = IntArray(n) { it }
    val rank = IntArray(n) { 1 }

    fun find(i: Int): Int {
        if (parent[i] == i) return i
        parent[i] = find(parent[i])
        return parent[i]
    }

    fun union(i: Int, j: Int): Boolean {
        val rootI = find(i)
        val rootJ = find(j)

        if (rootI != rootJ) {
            when {
                rank[rootI] > rank[rootJ] -> parent[rootJ] = rootI
                rank[rootI] < rank[rootJ] -> parent[rootI] = rootJ
                else -> {
                    parent[rootJ] = rootI
                    rank[rootI]++
                }
            }
            return true
        }
        return false
    }
}
```

```kotlin
fun findCircleNum(isConnected: Array<IntArray>): Int {
    // DFS Approach
    // Time Complexity: O(N^2) | Space Complexity: O(N)
    val cities = isConnected.size
    val visited = BooleanArray(cities)

    var provinces = 0
    for (i in 0 until cities) {
        if (visited[i]) continue
        dfs(i, isConnected, visited)
        provinces++
    }
    return provinces
}

fun dfs(city: Int, neighbors: Array<IntArray>, visited: BooleanArray) {
    visited[city] = true
    for (i in neighbors[city].indices) {
        if (!visited[i] && 1 == neighbors[city][i]) {
            dfs(i, neighbors, visited)
        }
    }
}
```

```kotlin
fun findCircleNum(isConnected: Array<IntArray>): Int {
    // BFS Approach
    // Time Complexity: O(N^2) | Space Complexity: O(N)
    val n = isConnected.size
    val visited = BooleanArray(n)
    var provinces = 0

    for (city in 0 until n) {
        if (!visited[city]) {
            provinces++
            // Inline BFS for tighter scoping
            val queue = ArrayDeque<Int>(n)
            queue.addLast(city)
            visited[city] = true
            while (queue.isNotEmpty()) {
                val i = queue.removeFirst()
                for (j in 0 until n) {
                    if (isConnected[i][j] == 1 && !visited[j]) {
                        visited[j] = true
                        queue.addLast(j)
                    }
                }
            }
        }
    }
    return provinces
}
```
### 5. Alternative Trade-offs (For System Design Dialogues)
*   **DFS vs. DSU**: 
    *   **DFS** is recursive and has a stack depth risk for extremely large graphs (not an issue for 
    **N = 200** here). It is faster for one-time connectivity checks.
    *   **DSU** is iterative and handles "Online" queries. If cities and connections were being added 
    one-by-one in a live stream, DSU could maintain the province count in near-constant time per update.
*   **Space Optimization**: For a massive, sparse graph (not a matrix), the adjacency list would 
save significant space. In a distributed system (e.g., social networks), we use DSU with a 
"Distributed Lock" or "Consul" to manage connected components across servers.

---

## Day 30 - LC 103. Binary Tree Zigzag Level Order Traversal
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Standardized Traversal**: To maximize performance, we maintain a **Monotonic BFS Traversal** 
  (always Left-to-Right). This is superior to the "Two-Stack" approach because it keeps the code 
  predictable for the CPU branch predictor.
  * **Zigzag via Sublist**: The alternating order is handled entirely within the **result sublist** 
  rather than by changing the tree traversal logic.
  * **Zero-Reversal Constraint**: By using a **Deque** for the level result, we can perform `addFirst` 
  or `addLast` in $O(1)$ time, achieving a zigzag effect without ever calling `list.reverse()`.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- |:---------|:---------| :--- |:-----------------------------------------------------------|
| **BFS + Reversal** | O(N) | O(W) | Moderate | Simplicity is preferred over absolute speed; K is small. |
| **Two-Stack / Deque** | O(N) | O(W) | High | Traditional textbook implementation. |
| **Standard BFS + Deque Sublist** | **O(N)** | **O(W)** | **Peak** | **The Professional Choice.** Optimal for hardware and maintenance. |

*\*N = nodes, W = max width.*

### 3. Native Kotlin Syntax Pitfalls
*   **The `ArrayDeque` Secret**: In Kotlin 1.4+, `ArrayDeque` implements `MutableList<T>`. This 
allows you to add it directly to your `List<List<Int>>` result without any expensive conversions or
memory copies.
*   **Monotonic vs. Oscillating Traversal**: Traditional "Two-Stack" solutions "ping-pong" between 
ends of deques. A pure BFS traversal is more cache-friendly and easier to debug.
*   **Initial Capacity**: Initializing the queue with `1024` nodes (or similar) prevents redundant 
internal array copies during the BFS growth.

### 4. Code Block
```kotlin
fun zigzagLevelOrderDFS(root: TreeNode?): List<List<Int>> {
    val result = mutableListOf<ArrayDeque<Int>>()
    dfs(root, 0, result)
    return result
}

private fun dfs(node: TreeNode?, level: Int, result: MutableList<ArrayDeque<Int>>) {
    if (node == null) return

    // Dynamic Growth: Only allocate levels as needed
    if (level == result.size) {
        result.add(ArrayDeque())
    }

    // Zero-Reversal Zigzag: use addLast for L->R, addFirst for R->L
    if (level % 2 == 0) {
        result[level].addLast(node.`val`)
    } else {
        result[level].addFirst(node.`val`)
    }

    dfs(node.left, level + 1, result)
    dfs(node.right, level + 1, result)
}
```

```kotlin
fun zigzagLevelOrderBFS(root: TreeNode?): List<List<Int>> {
    // Supreme Approach: Standard BFS + Deque-based Sublist
    // Achieves zero reversals and peak CPU performance through monotonic traversal
    if (root == null) return emptyList()

    val result = mutableListOf<List<Int>>()
    val queue = ArrayDeque<TreeNode>(1024) // Initial capacity for performance
    queue.addLast(root)
    var leftToRight = true

    while (queue.isNotEmpty()) {
        val levelSize = queue.size
        // Use ArrayDeque for the sublist to allow O(1) addFirst/addLast
        // Note: ArrayDeque implements List<Int>, making it zero-copy compatible with the result
        val sublist = ArrayDeque<Int>(levelSize)

        repeat(levelSize) {
            val node = queue.removeFirst()
            
            // Handle zigzag by choosing insertion end in the sublist
            if (leftToRight) {
                sublist.addLast(node.`val`)
            } else {
                sublist.addFirst(node.`val`)
            }

            // Traversal is ALWAYS standard Left-to-Right (Monotonic)
            node.left?.let { queue.addLast(it) }
            node.right?.let { queue.addLast(it) }
        }

        result.add(sublist) 
        leftToRight = !leftToRight
    }
    return result
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Standardizing on BFS**: By maintaining a monotonic traversal, we reduce the mental overhead of
the algorithm. In production, code that is easier to reason about is often more valuable than 
micro-optimizations, especially when both achieve the same Big-O.
*   **The "Zero-Copy" Deque**: Because Kotlin's `ArrayDeque` implements `List`, we avoid the **O(K)** 
overhead of converting a queue to a list at the end of every level. This is a language-specific 
optimization that beats standard Java implementations.
*   **Hardware CPU Optimization**: Monotonic traversal patterns are easier for the CPU to prefetch 
and predict. Oscillating between two stacks (the traditional approach) can cause branch mispredictions 
and cache thrashing on very large trees.
*   **Memory Pressure**: For extremely wide trees, storing entire levels in deques can trigger 
**O(W)** memory issues. If memory is more constrained than time, a **DFS** approach that passes the 
level index can be used, though it is less intuitive for grouping results.
*   **The DFS Static Allocation Trap**: Avoid pre-allocating a large fixed number of levels 
(e.g., 2000) with a large fixed capacity (e.g., 1024). This wastes massive heap memory. Always grow 
your result collections dynamically based on the actual height of the tree.

---

## Day 29 - LC 61. Rotate List
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Relative Shift**: The problem asks to move the end of the list to the front **k** times. This 
  is equivalent to finding a new "break point" in the list.
  * **k > Length**: The constraint that **k** can be larger than the list length triggers the need 
  for the **Modulo Operator** (**k % L**) to find the effective rotation count.
  * **Link Re-pointing**: To keep space at **O(1)**, we must manipulate existing pointers. The 
  **Circular Ring pattern** is the most efficient way to handle the wrap-around.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time     | Space    | Performance | Best Used When... |
| :--- |:---------|:---------| :--- | :--- |
| **Circular Ring** | **O(N)** | **O(1)** | **Peak** | **Optimal.** standard for list rotation. |
| **Array/List Backup** | **O(N)** | **O(N)** | Low | Quick implementation; memory is not a concern. |

*\*N = number of nodes in the list.*

### 3. Native Kotlin Syntax Pitfalls
*   **The Modulo Edge Case**: Always check if `length` is 0 before performing `k % length` to avoid 
`ArithmeticException`.
*   **Non-null Assertions**: When traversing after a null check, `node!!.next` is safe but `node?.next` 
is more idiomatic. Using `repeat(n)` for traversal is cleaner than manual `while` counters.
*   **Early Returns**: Handling `head == null` or `k == 0` early flattens the logic and avoids 
unnecessary length calculations.

### 4. Code Block
```kotlin
fun rotateRight(head: ListNode?, k: Int): ListNode? {
    // Optimized Circular Ring Approach
    // Time Complexity: O(N) | Space Complexity: O(1)
    if (head == null || head.next == null || k == 0) return head

    // 1. Find length and actual tail
    var length = 1
    var tail = head
    while (tail.next != null) {
        length++
        tail = tail.next!!
    }

    // 2. Effective rotation (handles k >= length)
    val effectiveK = k % length
    if (effectiveK == 0) return head

    // 3. Connect tail to head to form a ring
    tail.next = head

    // 4. Find new tail: (length - effectiveK) steps from head
    var newTail = head
    repeat(length - effectiveK - 1) {
        newTail = newTail?.next
    }

    // 5. Set new head and break the ring
    val newHead = newTail?.next
    newTail?.next = null

    return newHead
}
```

```kotlin
fun rotateRightOriginal(head: ListNode?, k: Int): ListNode? {
    var nodes = 0
    var ptr = head
    var moves = k

    while (ptr != null && ptr.next != null) {
        nodes++
        ptr = ptr.next
    }
    nodes++
    val tail = ptr
    ptr = head

    if (nodes == moves || nodes == 0) return head
    moves = if (nodes < moves) nodes - (moves % nodes) - 1
    else nodes - moves - 1
    nodes = 0
    while (ptr != null && nodes++ < moves) {
        ptr = ptr.next
    }

    tail?.next = head
    val temp = ptr
    ptr = ptr?.next
    temp?.next = null

    return ptr
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Ring vs. Disconnected Traversal**:
    *   **Ring**: Easier to reason about as you only calculate the "New Tail" position.
    *   **Disconnected**: Requires tracking two separate pointers (like the "Remove Nth Node from 
    End" pattern) to find the break point without forming a cycle.

*   **Immutability**: In some functional systems, you are not allowed to modify `ListNode`. In that 
case, you would copy the nodes into a new structure (like an `ArrayList`), rotate the list indices, 
and rebuild the linked list. This would cost **O(N)** space.

*   **Data Integrity**: Forming a circular list, even temporarily, can be dangerous if the code crashes 
before the ring is broken (it creates an infinite loop for other readers). In mission-critical 
multithreaded apps, the "Two-Pointer" (disconnected) approach is safer.

---

## Day28 - LC 150. Evaluate Reverse Polish Notation
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Postfix Notation (RPN)**: The requirement to process operators that apply to the *most recent* 
  two numbers is a classic signal for a **Stack (LIFO)** data structure.
  * **Immediate Computation**: Unlike infix notation (which might need operator precedence logic like 
  Shunting-yard), RPN allows for immediate computation as soon as an operator is encountered.
  * **Operand Dependency**: Since subtraction and division are non-commutative, the stack removal 
  order (**op2** before **op1**) is a critical constraint.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- | :--- | :--- | :--- | :--- |
| **Stack (ArrayDeque)** | **O(N)** | **O(N)** | **Peak** | **Standard.** The most efficient and idiomatic approach. |
| **Recursion** | O(N) | O(N) | Moderate | You want to showcase functional recursion (risky stack depth). |

*\*N = total number of tokens.*

### 3. Native Kotlin Syntax Pitfalls
*   **The Lambda Allocation**: In the higher-order version, defining lambdas inside the function body 
creates object allocations on every call. **Senior Tip**: Move static operators to a `private val` 
map outside the function to reuse them.
*   **`inline` Power**: Using `inline` for the `calculate` helper is an expert move. It tells the 
compiler to replace the function call with actual bytecode, eliminating the overhead of a higher-order 
function call.
*   **Operand Order**: The stack returns the *right-hand* operand first. `val op2 = st.removeLast(); 
val op1 = st.removeLast()` is essential for `op1 / op2`.
*   **`toInt()` vs. `toIntOrNull()`**: 
    *   **`toIntOrNull()`**: Idiomatic and safe, but returns `Int?`, which forces an immediate box to `Integer`.
    *   **`toInt()` after manual check**: Faster because simple string comparison (`str != "+"`) is cheaper than a failed parse attempt inside `toIntOrNull()`.
*   **Generic Boxing**: Note that `ArrayDeque<Int>` is actually `ArrayDeque<Integer>`. Even if you use `toInt()`, the value is boxed when added to the queue. For **True Zero Allocation**, a manual `IntArray` with a `top` pointer is required.

### 4. Code Block
```kotlin
fun evalRPNZeroBoxing(tokens: Array<String>): Int {
    // True Zero Allocation / Zero Boxing Approach
    // Uses a primitive IntArray as a stack to avoid Integer object creation (Boxing)
    val st = IntArray(tokens.size)
    var top = -1

    for (str in tokens) {
        // 1. Manual check is cheaper than a failed toIntOrNull() parse
        if (str != "+" && str != "-" && str != "*" && str != "/") {
            // 2. Direct primitive assignment (No Boxing)
            st[++top] = str.toInt()
        } else {
            val op2 = st[top--]
            val op1 = st[top--]
            st[++top] = when (str) {
                "+" -> op1 + op2
                "-" -> op1 - op2
                "*" -> op1 * op2
                else -> op1 / op2
            }
        }
    }
    return st[0]
}
```

```kotlin
fun evalRPN(tokens: Array<String>): Int {
    val st = ArrayDeque<Int>(tokens.size)
    var op1: Int
    var op2: Int

    for (str in tokens) {
        if (str != "+" && str != "-" && str != "*" && str != "/") {
            st.addLast(str.toInt())
        } else {
            op2 = st.removeLast()
            op1 = st.removeLast()
            when (str) {
                "+" -> st.addLast(op1 + op2)
                "-" -> st.addLast(op1 - op2)
                "*" -> st.addLast(op1 * op2)
                else -> st.addLast(op1 / op2)
            }
        }
    }
    return st.removeLast()
}
```

```kotlin
inline fun calculate(x: Int, y: Int, operation: (Int, Int) -> Int): Int {
    return operation(x, y)
}

val plus: ((Int, Int) -> Int) = { a, b -> a + b }
val minus: ((Int, Int) -> Int) = { a, b -> a - b }
val times: ((Int, Int) -> Int) = { a, b -> a * b }
val divide: ((Int, Int) -> Int) = { a, b -> a / b }

fun evalRPNHigherOrderFunction(tokens: Array<String>): Int {

    val st = ArrayDeque<Int>(tokens.size)
    var op1: Int
    var op2: Int

    for (str in tokens) {
        val validNumber = str.toIntOrNull()
        if (validNumber != null) {
            st.addLast(validNumber)
        } else {
            op2 = st.removeLast()
            op1 = st.removeLast()
            when (str) {
                "+" -> st.addLast(calculate(op1, op2, plus))
                "-" -> st.addLast(calculate(op1, op2, minus))
                "*" -> st.addLast(calculate(op1, op2, times))
                else -> st.addLast(calculate(op1, op2, divide))
            }
        }
    }
    return st.removeLast()
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Function Mapping vs. When-Switch**:
    *   **When-Switch**: Slightly faster for a fixed set of small operators due to JVM `TABLESWITCH`
    optimization.
    *   **Map of Lambdas**: Better for extensibility (e.g., a calculator that supports plugins).
*   **Overflow Protection**: For real-world financial systems, `Int` might be too small. You would 
use `Long` or `BigDecimal` to prevent silent overflows during intermediate steps.
*   **Pre-allocation**: Initializing `ArrayDeque(tokens.size)` prevents redundant array doubling and 
    copying, which is critical for processing massive mathematical expressions in real-time.
*   **Primitive Stack (IntArray)**: For systems where Garbage Collection pauses must be zero, using 
    `IntArray` with a `top` pointer is the ultimate optimization. It removes all `Integer` boxing 
    overhead that generic collections like `ArrayDeque` or `Stack` inherently have.

---
