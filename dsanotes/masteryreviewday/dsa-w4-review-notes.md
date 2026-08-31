# LeetCodePrep Week4 Mastery & Review Day

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 4: MONO STACK & LINKED LISTS && QUEUES / DEQUE (August 30)
# ===============================================================

## Day 24 - LC 716. Max Stack
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **O(Log N)** Middle Deletion**: The requirement to pop the maximum value from *anywhere* in the
  stack (not just the top) is the "Hard" constraint.
  * **Hybrid Data Structure**: To maintain both "Push Order" and "Value Order," we must link two 
  structures: a **Doubly Linked List** (for **O(1)** stack ops) and a **TreeMap** (for **O(Log N)** 
  max lookup).
  * **Node Reference Mapping**: The TreeMap must store references to the actual nodes in the linked 
  list so they can be unlinked in constant time.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | `push`        | `pop`        | `popMax`     | Performance | Best Used When... |
| :--- |:--------------|:-------------|:-------------| :--- | :--- |
| **Two Stacks** | **O(1)**      | **O(1)**     | **O(N)**     | Low | Simplicity is key; few `popMax` calls. |
| **Lazy Deletion** | **O(Log N)**  | **O(Log N)** | **O(Log N)** | High | Memory is abundant; using Heap + Stack. |
| **DLL + TreeMap** | **O(Log N)** | **O(Log N)** | **O(Log N)** | **Peak** | **Optimal.** standard for high-performance MaxStacks. |

### 3. Native Kotlin Syntax Pitfalls
*   **Java TreeMap Interop**: Kotlin doesn't have a built-in `TreeMap` in `kotlin.collections`. 
You must import `java.util.TreeMap`.
*   **MutableList in Map**: Since multiple nodes can have the same value, the map must be 
`TreeMap<Int, MutableList<Node>>`. Always remove from the *end* of the list to preserve the 
"Last-In" part of "Last-In-First-Out" for duplicate max values.

### 4. Code Block
```kotlin
import java.util.TreeMap

class MaxStack() {
    // Hybrid Strategy: Doubly Linked List + TreeMap
    // Allows O(log N) for all operations by enabling O(1) removal from middle
    
    private class Node(val value: Int) {
        var prev: Node? = null
        var next: Node? = null
    }

    private val head = Node(0)
    private val tail = Node(0)
    private val map = TreeMap<Int, MutableList<Node>>()

    init {
        head.next = tail
        tail.prev = head
    }

    fun push(x: Int) {
        val node = Node(x)
        // Add to end of DLL (Stack top)
        node.next = tail
        node.prev = tail.prev
        tail.prev?.next = node
        tail.prev = node
        // Add to TreeMap for max lookup
        map.computeIfAbsent(x) { mutableListOf() }.add(node)
    }

    fun pop(): Int {
        val node = tail.prev!!
        removeNode(node)
        val list = map[node.value]!!
        list.removeAt(list.size - 1)
        if (list.isEmpty()) map.remove(node.value)
        return node.value
    }

    fun top(): Int = tail.prev!!.value

    fun peekMax(): Int = map.lastKey()

    fun popMax(): Int {
        val maxVal = map.lastKey()
        val list = map[maxVal]!!
        val node = list.removeAt(list.size - 1)
        if (list.isEmpty()) map.remove(maxVal)
        removeNode(node)
        return maxVal
    }

    private fun removeNode(node: Node) {
        node.prev?.next = node.next
        node.next?.prev = node.prev
    }
}
```

```kotlin
import java.util.TreeMap

class MaxStack() {
    // Two TreeMaps Strategy
    // Uses time-based IDs to replace the DLL, maintaining O(log N) for all ops.
    
    private val valueMap = TreeMap<Int, MutableList<Int>>() // Value -> List of seqIDs
    private val timeMap = TreeMap<Int, Int>() // seqID -> Value
    private var seqID = 0

    fun push(x: Int) {
        seqID++
        timeMap[seqID] = x
        valueMap.computeIfAbsent(x) { mutableListOf() }.add(seqID)
    }

    fun pop(): Int {
        val id = timeMap.lastKey()
        val x = timeMap.remove(id)!!
        val ids = valueMap[x]!!
        ids.removeAt(ids.size - 1)
        if (ids.isEmpty()) valueMap.remove(x)
        return x
    }

    fun top(): Int = timeMap.lastEntry().value

    fun peekMax(): Int = valueMap.lastKey()

    fun popMax(): Int {
        val x = valueMap.lastKey()
        val ids = valueMap[x]!!
        val id = ids.removeAt(ids.size - 1)
        if (ids.isEmpty()) valueMap.remove(x)
        timeMap.remove(id)
        return x
    }
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Two TreeMaps vs. DLL + TreeMap**: 
    *   **Complexity**: Both are $O(\log N)$.
    *   **Performance**: The DLL approach is slightly faster in practice because pointer manipulation ($O(1)$) has lower constant overhead than tree rebalancing ($O(\log N)$) during standard stack operations (`push`, `pop`, `top`).
    *   **Maintainability**: The Two TreeMaps strategy is easier to implement as it relies entirely on standard library collections, avoiding custom linked-list logic.
*   **Lazy Deletion (Two Heaps)**: Instead of a DLL, you can use a standard `Stack` and a 
`PriorityQueue`. When you "pop" from one, you don't immediately remove from the other. Instead, 
you keep a "Deleted Set" and clean up the tops of both structures lazily. This is easier to 
implement but uses more memory.
*   **Memory Fragmentation**: The DLL approach involves creating many small `Node` objects. In a 
high-throughput system, this can lead to memory fragmentation. A primitive-array based Max-Heap with
"handles" (pointers to array indices) would be more memory-efficient.

---

## LC 142. Linked List Cycle II
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Entry Point Requirement**: Unlike simple cycle detection, we must identify the exact node 
  where the cycle begins.
  * **Two-Phase Floyd's Algorithm**: This triggers a two-step process:
    1. **Detection**: Standard fast/slow pointer meeting point.
    2. **Entry Finding**: Leveraging the mathematical fact that the distance from the head to the 
    entry is equal to the distance from the meeting point to the entry (moving forward).
  * ****O(1)** Space Constraint**: Precludes the use of a `HashSet`, forcing the two-pointer pointer 
  approach with a convergence strategy.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time     | Space    | Performance | Best Used When... |
| :--- |:---------|:---------| :--- | :--- |
| **HashSet Tracking** | **O(N)**   | **O(N)**   | Moderate | Implementation speed is prioritized over memory. |
| **Two-Phase Floyd's** | **O(N)** | **O(1)** | **Peak** | **Optimal.** Memory efficiency is critical (standard in interviews). |

### 3. Native Kotlin Syntax Pitfalls
*   **Loop Guarding**: Use `while (fast != null && fast.next != null)` to protect Phase 1. 
This avoids the need for manual `!!` or excessive null-checking inside the body.
*   **Redundant Safe Calls**: Once `slow == fast` is true, a cycle is guaranteed. Safe calls (`?.`) 
in Phase 2 are technically unnecessary but keep the compiler happy if the pointers are nullable.
*   **Reference Equality**: Kotlin's `==` on `ListNode` objects (which don't override `equals`) 
correctly checks if two references point to the same memory address.

### 4. Code Block
```kotlin
fun detectCycle(head: ListNode?): ListNode? {
    // Highly Optimized Two-Phase Floyd's Algorithm
    // Time Complexity: O(N) | Space Complexity: O(1)
    var slow = head
    var fast = head

    // Phase 1: Detect Cycle
    while (fast != null && fast.next != null) {
        slow = slow?.next
        fast = fast.next!!.next
        
        if (slow == fast) {
            // Phase 2: Find Entry Point
            // Reset entry pointer to head; move both at 1x speed
            var entry = head
            while (entry != slow) {
                entry = entry?.next
                slow = slow?.next
            }
            return entry
        }
    }

    return null
}
```
* **Syntax & Idiomatic Kotlin Review**
   * **Boolean Flag Overhead**: Using var meet = false and an if (meet) check is correct but adds 
   overhead to the control flow. In a "Senior" implementation, we typically nest Phase 2 inside the 
   if (slow == fast) block or return early to flatten the control flow.
   * **The !! Operator**: Your use of hare.next!!.next is safe because hare.next is checked in the 
   while condition. However, avoiding !! where possible is a common Kotlin best practice to prevent 
   potential runtime crashes if the logic changes.
   * **Redundant Null Checks**: In Phase 2, you use tortoise?.next and hare?.next. Since Phase 2 only 
   runs if a cycle is confirmed, these pointers will never be null.

```kotlin
    fun detectCycle(head: ListNode?): ListNode? {
        // Floyd's Cycle-Finding Algorithm
        // Time Complexity: O(N) | Space Complexity: O(1)
        var tortoise = head
        var hare = head
        var meet = false

        while (hare != null && hare.next != null) {
            hare = hare.next!!.next
            tortoise = tortoise?.next
            if (tortoise != null && tortoise == hare) {
                tortoise = head
                meet = true
                break
            }
        }

        if (meet) {
            while (tortoise != hare) {
                tortoise = tortoise?.next
                hare = hare?.next
            }
            return tortoise
        } else {
            return null
        }
    }
```

```kotlin
fun detectCycle(head: ListNode?): ListNode? {
    // HashSet Approach - Simple Cycle Detection
    // Time Complexity: O(N) | Space Complexity: O(N)

    // Initialize an empty hash set
    val nodesSeen = HashSet<ListNode?>()

    // Start from the head of the linked list
    var node = head
    while (node != null) {
        // If the current node is in nodesSeen, we have a cycle
        if (nodesSeen.contains(node)) {
            return node
        } else {
            // Add this node to nodesSeen and move to the next node
            nodesSeen.add(node)
            node = node.next
        }
    }

    // If we reach a null node, there is no cycle
    return null
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **The Mathematical Proof**:
    *   Let **L_1** be distance from head to entry.
    *   Let **L_2** be distance from entry to meeting point.
    *   Let **C** be the cycle length.
    *   **Slow_Dist = L_1 + L_2**
    *   **Fast_Dist = L_1 + L_2 + nC**
    *   Since **Fast = 2 x Slow**: **L_1 + L_2 + nC = 2(L_1 + L_2) -> L_1 = nC - L_2**.
    *   This proves that starting from the meeting point and moving **L_1** steps (the same distance 
    as head to entry) will land you exactly at the cycle entry.
*   **Destructive Marking**: In environments where you can modify the `ListNode` class, you could 
add a `visited` flag. This simplifies the logic to **O(N)** but violates the "In-place, 
non-destructive" requirement usually expected in interviews.

---

## LC 496. Next Greater Element I
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **"Next Greater" search in linear time**: Finding the first larger element to the right of every
  index signals a **Monotonic Stack**.
  * **Subset Query**: Since `nums1` is a subset of `nums2`, we can pre-calculate all "Next Greater" 
  values for `nums2` and store them in a **HashMap** for **O(1)** retrieval.
  * **Strict Decreasing Order**: The stack maintains elements that are waiting for a larger neighbor. 
  When a larger number is found, it "resolves" multiple smaller numbers on the stack simultaneously.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time             | Space      | Performance | Best Used When... |
| :--- |:-----------------|:-----------| :--- | :--- |
| **Brute Force** | **O(N_1 x N_2)** | **O(1)**     | Low | Arrays are very small. |
| **Monotonic Stack + Map** | **O(N_1 + N_2)** | **O(N_2)** | **Peak** | **Optimal.** Standard for high-performance searches. |

### 3. Native Kotlin Syntax Pitfalls
*   **Double Map Lookup**: Avoid `if (map.contains(k)) map[k]`. This performs the hash/lookup logic 
twice. Use **`map.getOrDefault(key, -1)`** for a single, atomic $O(1)$ operation.
*   **Boxing Overhead**: Standard collections like `Stack<Int>` box primitives into `Integer` objects. 
A manual **`IntArray` stack** with a pointer is faster and keeps the memory footprint contiguous.
*   **Stack Indices vs Values**: Since elements in `nums2` are unique, storing **values** directly 
on the stack is simpler than storing indices, reducing one level of array indirection.

### 4. Code Block
```kotlin
fun nextGreaterElement(nums1: IntArray, nums2: IntArray): IntArray {
    // 1. Highly Optimized Monotonic Stack (Manual Pointer)
    // Time Complexity: O(N1 + N2) | Space Complexity: O(N2)
    val stack = IntArray(nums2.size)
    var top = -1
    val nextGreaterMap = HashMap<Int, Int>(nums2.size)

    // Phase 1: Build the map of "Next Greater" for all elements in nums2
    for (num in nums2) {
        // While current is greater than the top element, we found its next greater
        while (top >= 0 && num > stack[top]) {
            nextGreaterMap[stack[top--]] = num
        }
        stack[++top] = num
    }

    // Phase 2: Populate result for nums1 using the pre-calculated map
    val result = IntArray(nums1.size)
    for (i in nums1.indices) {
        result[i] = nextGreaterMap.getOrDefault(nums1[i], -1)
    }
    
    return result
}
```

```kotlin
fun nextGreaterElement(nums1: IntArray, nums2: IntArray): IntArray {
  // 2. Manual Stack Pointer Approach (Optimized)
  // Time Complexity: O(nums1.size + nums2.size) | Space Complexity: O(nums1.size)
  val stack = IntArray(nums2.size)
  val map = HashMap<Int, Int>()
  val result = IntArray(nums1.size)
  var top = -1

  for (curr in nums2.indices) {
    while (top >= 0 && nums2[curr] > nums2[stack[top]]) {
      val prev = stack[top--]
      map[nums2[prev]] = nums2[curr]
    }
    stack[++top] = curr
  }

  for (i in nums1.indices) {
    result[i] = if (map.contains(nums1[i])) map[nums1[i]]!! else -1
  }
  return result
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Memory vs Speed**: The Monotonic Stack is fast (**O(N)**) but costs **O(N)** memory for the map. 
If memory is critically limited (e.g., an IoT device), the Brute Force **O(N_1 x N_2)** approach 
with **O(1)** space might be necessary.
*   **Streaming Input**: If `nums2` was a continuous stream of data, the Monotonic Stack could still
work. You would maintain the stack and emit pairs as they are resolved, showing its suitability for 
**Real-time Event Processing**.
*   **Batching Queries**: If you had multiple different `nums1` arrays to check against the same 
`nums2`, the Map-based approach is superior because the $O(N_2)$ construction cost is paid only once.

---


