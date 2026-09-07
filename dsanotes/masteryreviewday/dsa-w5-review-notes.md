# LeetCodePrep Week 5 Mastery & Review Day

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 5: BINARY TREES (TRAVERSAL) & MONO STACK & LINKED LISTS && DEQUE (September 6)
# ===============================================================

## LC 142. Linked List Cycle II
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Identify Cycle Entry Point**: Requires not just detection, but the exact node where the loop starts.
  * **O(1) Space Requirement**: Forbids the use of a `HashSet`, triggering **Floyd’s Cycle-Finding 
  Algorithm** (Two Pointers).
  * **Two-Phase Convergence**: Phase 1 detects the cycle; Phase 2 finds the start of the cycle by 
  leveraging the mathematical equidistance property.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- | :--- | :--- | :--- | :--- |
| **Floyd's (Two-Pointer)** | **O(N)** | **O(1)** | **Peak** | **Optimal.** Memory efficiency is required. |
| **HashSet Tracking** | **O(N)** | **O(N)** | Moderate | Implementation speed is prioritized over memory. |

### 3. Native Kotlin Syntax Pitfalls
*   **Pointer Guarding**: Using `while (fast != null && fast.next != null)` ensures that the "Hare" 
can always leap two steps without a `NullPointerException`.
*   **Reference Equality**: Kotlin's `==` on `ListNode` objects correctly compares memory addresses 
to detect when pointers meet.

### 4. Code Block
```kotlin
fun detectCycle(head: ListNode?): ListNode? {
    // Phase 1: Detect if a cycle exists
    var slow = head
    var fast = head

    while (fast != null && fast.next != null) {
        slow = slow?.next
        fast = fast.next!!.next // Safe due to while condition

        if (slow == fast) {
            // Phase 2: Reset slow to head and move both at 1x speed
            // They are guaranteed to meet exactly at the cycle entry
            slow = head
            while (slow != fast) {
                slow = slow!!.next
                fast = fast!!.next
            }
            return slow
        }
    }
    return null
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)

*   **The Mathematical Proof**:
    *   **L_1** = distance from head to entry. **L_2** = distance from entry to meeting point. **C** = cycle length.
    *   **SlowDist = L_1 + L_2**.
    *   **FastDist = L_1 + L_2 + nC**.
    *   Since **Fast = 2 x Slow**: **L_1 + L_2 + nC = 2(L_1 + L_2) -> L_1 = nC - L_2**.
    *   This proves that moving **L_1** steps from the head is the same as moving **nC - L_2** steps 
    from the meeting point.
  
* **Destructive Detection**: One could potentially "mark" visited nodes by pointing their `next` 
to a sentinel, but this is destructive and violates typical production safety standards for shared data.

---

## LC 21. Merge Two Sorted Lists
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Sorted Input Invariant**: The fact that both lists are already sorted allows for a **Two-Pointer 
  Linear Scan** ($O(N+M)$).
  * **Dummy Head Pattern**: Using a sentinel node simplifies the initial link logic, ensuring we don't 
  need a special case for the first node of the merged list.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- | :--- | :--- | :--- | :--- |
| **Iterative** | **O(N + M)** | **O(1)** | **Peak** | Production code where stack depth is a concern. |
| **Recursive** | **O(N + M)** | **O(N + M)** | Moderate | Small lists where code conciseness is prioritized. |

*\*N, M = lengths of the two lists.*

### 3. Native Kotlin Syntax Pitfalls
*   **Keyword Collision**: Must use backticks (``node.`val` ``) to access the value.
*   **Elvis Merge**: `tail.next = l1 ?: l2` is the most idiomatic way to attach the remaining non-empty list.

### 4. Code Block
```kotlin
    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
    // Time O(M + N) Space O(1)
    val dummy = ListNode(-1)
    var next = dummy
    var l1 = list1
    var l2 = list2
    while (l1 != null && l2 != null) {
        if (l1.`val` <= l2.`val`) {
            next.next = l1; next = l1
            l1 = l1.next
        } else {
            next.next = l2; next = l2
            l2 = l2.next
        }
    }
    next.next = l1 ?: l2
    return dummy.next
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Recursive Stack Overflow**: On the JVM, recursive calls consume stack space. For lists with 
**>10000** nodes, the iterative approach is mandatory to prevent crashes.
*   **In-Place vs. Copying**: This solution is "In-Place," meaning it modifies the original nodes. 
If the source data must remain immutable (e.g., in a persistent event store), you would need to 
allocate new nodes, increasing space complexity to **O(N+M)**.

---

## LC 207. Course Schedule
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Dependency Resolution**: The prerequisite structure defines a **Directed Graph**.
  * **Cycle Detection**: The problem of "can I finish" is equivalent to asking "is the graph a **DAG** 
  (Directed Acyclic Graph)?"
  * **Topological Sort**: Both BFS (Kahn's) and DFS (3-state) can detect cycles by attempting a 
  topological sort. Kahn's is preferred for its iterative safety.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- | :--- | :--- | :--- | :--- |
| **Kahn's (BFS)** | **O(V + E)** | **O(V + E)** | **Peak** | Graph might be deep; iterative safety is required. |
| **3-State (DFS)** | **O(V + E)** | **O(V + E)** | High | You need a recursive, more concise solution. |

*\*V = nodes, E = edges (prerequisites).*

### 3. Native Kotlin Syntax Pitfalls
*   **`ArrayDeque` Capacity**: Like in Sliding Window, initializing with `numCourses` avoids redundant 
array copies as nodes are queued.
*   **Adjacency List Boxing**: `Array(V) { mutableListOf<Int>() }` is an array of objects. For massive 
graphs with millions of edges, primitive-specialized collections would be needed to reduce GC pressure.

### 4. Code Block
```kotlin
fun canFinish(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
    // Time O(V + E) Space(V + E) Khan's or DFS
    val neighbors = Array(numCourses) { mutableListOf<Int>() }
    val inDegree = IntArray(numCourses)

    for (preq in prerequisites) {
        neighbors[preq[1]].add(preq[0])
        inDegree[preq[0]]++
    }

    val dq = ArrayDeque<Int>(numCourses)
    var coursesCnt = 0
    for (i in inDegree.indices) {
        if (inDegree[i] == 0) dq.addLast(i)
    }

    while (dq.isNotEmpty()) {
        val course = dq.removeFirst()
        coursesCnt++
        for (neighbor in neighbors[course]) {
            inDegree[neighbor]--
            if (inDegree[neighbor] == 0) {
                dq.addLast(neighbor)
            }
        }
    }

    return coursesCnt == numCourses
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **BFS vs. DFS**:
    *   **Kahn's (BFS)**: Better for finding the *actual* topological order and is safe from `StackOverflowError`.
    *   **DFS**: Often faster to implement and uses less memory if the graph is very sparse but very wide.
*   **Matrix vs. List**: An **Adjacency Matrix** is $O(V^2)$ space, which is unacceptable for a course 
schedule of 2000 nodes but sparse edges. The **Adjacency List** is strictly superior here.
*   **Parallelization**: Kahn's algorithm is partially parallelizable. Once multiple nodes have an 
`inDegree` of 0, they can be processed by separate workers simultaneously.

---

## LC 239. Sliding Window Maximum
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Window Maxima**: The need to find the maximum in a contiguous subarray that slides forward 
  triggers the **Sliding Window** pattern.
  * **Range Expiration**: Since elements "fall out" of the window, we need a way to track which value 
  is currently the largest. This triggers the **Monotonic Deque (Decreasing)**.
  * **O(1) Expiration**: Storing **indices** (instead of raw values) allows us to determine if the 
  current front of the deque is still inside the window boundaries in constant time.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time              | Space | Performance | Best Used When... |
| :--- |:------------------| :--- | :--- | :--- |
| **Brute Force** | **O(N x K)**      | **O(1)** | Low | Window size **K** is extremely small. |
| **Priority Queue** | **O(N x Log(N))** | **O(N)** | Moderate | General range max queries; memory isn't an issue. |
| **Monotonic Deque** | **O(N)**          | **O(K)** | **Peak** | High-performance, real-time sliding window data. |

*\*N = total elements, K = window size.*

### 3. Native Kotlin Syntax Pitfalls
*   **Capacity Management**: Initializing `ArrayDeque(k)` prevents repeated memory allocations as 
the window "warms up."
*   **Loop Consolidation**: Using a single loop with `if (i >= k - 1)` is more robust than a 
two-phase loop because it avoids duplicating the monotonic logic.
*   **Index Math**: Be careful with the offset `i - k + 1`. This is the index in the result array 
and also determines the "expiration deadline" for values in the deque.

### 4. Code Block
```kotlin
fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
    // Time O(N) Space O(K)
    val maxdq = ArrayDeque<Int>(k)
    val result = IntArray(nums.size - k + 1)

    for (i in nums.indices) {
        while (maxdq.isNotEmpty() && nums[i] > nums[maxdq.last()]) {
            maxdq.removeLast()
        }
        maxdq.addLast(i)
        val idx = i - k + 1
        if (idx >= 0) {
            if (maxdq.first() < idx) maxdq.removeFirst()
            result[idx] = nums[maxdq.first()]
        }
    }
    return result
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Segment Trees**: For a static array where you need the maximum of *any* arbitrary range 
(not just sliding), a Segment Tree or Sparse Table provides $O(\log N)$ or $O(1)$ queries. 
*   **Distributed Streams**: In a system like Apache Flink processing millions of events per second, 
the monotonic deque is the standard way to calculate "Rolling Maxima" because it provides zero-latency 
results as soon as an event arrives.
*   **Memory Pressure**: If $K$ is very large (e.g., $10^7$), the deque might consume significant RAM. 
In that case, we might trade $O(N)$ time for a more memory-efficient block-based approximation.

---

## LC 102. Binary Tree Level Order Traversal
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Layered Output**: The requirement to return nodes grouped by level (**List<List<Int>>**) triggers 
  **BFS (Breadth-First Search)**.
  * **Level Boundaries**: To distinguish between levels in a single queue, we use the 
  **Queue Snapshot** pattern: capturing `q.size` at the start of each level's processing.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- | :--- | :--- | :--- | :--- |
| **BFS (Queue)** | **O(N)** | **O(W)** | **Peak** | Tree is deep but narrow; level-by-level output is required. |
| **DFS (Recursion)** | **O(N)** | **O(H)** | High | Tree is very wide but shallow; recursion depth is safe. |

*\*N = nodes, W = max width, H = tree height.*

### 3. Native Kotlin Syntax Pitfalls
*   **Keyword Collision**: Must use backticks (``node.`val` ``) to access the node's value because 
`val` is a reserved keyword in Kotlin.
*   **Allocation Awareness**: Using `ArrayDeque` with an **initial capacity** prevents multiple array 
copy operations during growth. Similarly, pre-sizing the level `ArrayList(times)` avoids resizing overhead.
*   **Redundant Unwrapping**: `node.left?.let { q.addLast(node.left!!) }` works but `node.left?.let 
{ q.addLast(it) }` is more idiomatic and safer by using the it reference.

### 4. Code Block
```kotlin
fun levelOrder(root: TreeNode?): List<List<Int>> {
    // Time O(N) Space O(W)
    if (root == null) return emptyList()

    val q = ArrayDeque<TreeNode>(2000)
    val wholeTreeList = mutableListOf<List<Int>>()
    q.addLast(root)

    while (q.isNotEmpty()) {
        val times = q.size
        val list = ArrayList<Int>(times)
        repeat(times) {
            val node = q.removeFirst()
            node.left?.let { q.addLast(node.left!!) }
            node.right?.let { q.addLast(node.right!!) }
            list.add(node.`val`)
        }
        wholeTreeList.add(list)
    }
    return wholeTreeList
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Memory Pressure (BFS vs DFS)**: For an extremely wide tree (e.g., **10^9** nodes in a single level), 
BFS could cause an `OutOfMemoryError`. DFS would be safer for memory but harder to implement for 
grouped level-order results (requires passing level indices).
*   **Serializing Large Trees**: In a distributed system, BFS is the standard way to serialize a 
tree for wire transfer (e.g., JSON or Protobuf) because it captures the structure level-by-level, 
which is easier for streaming consumers to process.

---
