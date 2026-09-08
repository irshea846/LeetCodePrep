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
