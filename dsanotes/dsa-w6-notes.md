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
