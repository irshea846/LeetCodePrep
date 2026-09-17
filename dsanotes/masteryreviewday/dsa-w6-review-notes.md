# LeetCodePrep Week 6 Mastery & Review Day

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 6: GRAPHS & ADVANCED DATA STRUCTURES (September 13)
# ===============================================================

## LC 150. Evaluate Reverse Polish Notation
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **LIFO (Last-In-First-Out) Ordering**: Operators in RPN apply to the most recently seen numbers.
  This is the definitive signal for a **Stack**.
  * **Operand Dependency**: Non-commutative operations (subtraction/division) require precise 
  tracking of operand order—the first element popped is always the right-hand operand.
  * **Token Iteration**: Single-pass linear scan **O(N)** is sufficient because RPN eliminates the 
  need for operator precedence or parenthesis tracking.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- | :--- | :--- | :--- | :--- |
| **Stack (ArrayDeque)** | **O(N)** | **O(N)** | **Peak** | **Standard.** Ideal for expression evaluation and parsing. |
| **Manual IntArray Stack** | **O(N)** | **O(N)** | **Extreme** | Zero-allocation requirements (avoids Integer boxing). |

*\*N = total tokens.*

### 3. Native Kotlin Syntax Pitfalls
*   **Operand Order**: The stack returns the **right-hand** operand first. For `-` and `/`, you must
capture `op2` before `op1` to ensure correct arithmetic (`op1 / op2`).
*   **Boxing Overhead**: `ArrayDeque<Int>` actually stores `java.lang.Integer` objects. In a 
high-frequency system, this creates GC pressure.
*   **Capacity Management**: Initializing `ArrayDeque(tokens.size)` as you did is a "Senior" move—it 
prevents internal array resizing and redundant copies.

### 4. Code Block
```kotlin
fun evalRPN(tokens: Array<String>): Int {
    // Zero-allocation, overflow-safe stack
    val stack = LongArray(tokens.size)
    var top = -1

    for (token in tokens) {
        when (token) {
            "+" -> {
                val sum = stack[top--] + stack[top--]
                stack[++top] = sum
            }
            "-" -> {
                val op2 = stack[top--]
                val op1 = stack[top--]
                stack[++top] = op1 - op2
            }
            "*" -> {
                val prod = stack[top--] * stack[top--]
                stack[++top] = prod
            }
            "/" -> {
                val op2 = stack[top--]
                val op1 = stack[top--]
                stack[++top] = op1 / op2
            }
            else -> stack[++top] = token.toLong()
        }
    }
    // Final result is the only remaining element on the stack
    return stack[0].toInt()
}
```

```kotlin
fun evalRPN(tokens: Array<String>): Int {
    val dq = ArrayDeque<Int>(tokens.size)
    tokens.forEach { it ->
        when (it) {
            "+" -> dq.addLast(dq.removeLast() + dq.removeLast())
            "-" -> {
                val op2 = dq.removeLast(); val op1 = dq.removeLast()
                dq.addLast(op1 - op2)
            }
            "*" -> dq.addLast(dq.removeLast() * dq.removeLast())
            "/" -> {
                val op2 = dq.removeLast(); val op1 = dq.removeLast()
                dq.addLast(op1 / op2)
            }
            else -> dq.addLast(it.toInt())
        }
    }
    return dq.removeLast()
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Extensibility**: While `when` is fast, a `Map<String, (Int, Int) -> Int>` would make it easier
to support new operators (like `^` or `%`) without modifying the core loop.
*   **Numerical Safety**: In production, intermediate results might exceed `Int.MAX_VALUE`. Consider
using `Long` or `BigInteger` for the stack if the math domain is large.
*   **Zero-Allocation Pattern**: If processing millions of expressions per second, swapping the 
`ArrayDeque` for a pre-allocated `IntArray` with a `top` pointer eliminates all object allocations.

---
