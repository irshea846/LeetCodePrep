# LeetCodePrep Week 4

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 4: STACKS & QUEUES (August 25 - August 29)
# ===============================================================

## Topics Covered
- [ ] Stacks
- [ ] Queues / Deque
- [ ] Monotonic Stack
- [ ] Linked Lists

## Day 20 - 739 - Daily Temperatures
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **"Next Warmer Day" Search**: The problem requires finding the distance to the next larger value
  in the future. This is the canonical signal for a **Monotonic Stack**.
  * **Strictly Decreasing Invariant**: We maintain a stack of indices where temperatures are in 
  strictly decreasing order. When we see a warmer temperature, we know it is the "next greater element" 
  for all indices currently on the stack that are colder.
  * **Index-Based Distance**: Storing indices on the stack (rather than raw values) allows us to 
  calculate the distance (`currentIdx - previousIdx`) in **O(1)** time.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- |:-----------|:---------| :--- | :--- |
| **Brute Force** | **O(N^2)** | **O(1)** | Low | **N** is tiny. |
| **Monotonic Stack** | **O(N)** | **O(N)** | **Peak** | **Optimal.** standard for "Next Greater Element" problems. |

### 3. Native Kotlin Syntax Pitfalls
*   **Manual Stack Pointer**: Using a primitive array (`IntArray`) with a pointer (`i`) as a stack 
is faster than `ArrayDeque` or `Stack` because it avoids object boxing and dynamic resizing. 
However, it requires careful manual index management (`mono[i--] = -1`).
*   **Zero-Initialization**: Kotlin's `IntArray` defaults to zeros. Since the problem requires `0` 
when no warmer day exists, we only need to write to the `waitingDays` array when a match is found.

### 4. Code Block
```kotlin
fun dailyTemperatures(temperatures: IntArray): IntArray {
    // Manual Stack Pointer Approach (Optimized)
    // Time Complexity: O(N) | Space Complexity: O(N)
    val n = temperatures.size
    val stack = IntArray(n) // Our manual stack
    val result = IntArray(n)
    var top = -1 // Pointer to the top of the stack

    for (currIdx in temperatures.indices) {
        // While stack is not empty and current temp is warmer than top of stack
        while (top >= 0 && temperatures[currIdx] > temperatures[stack[top]]) {
            val prevIdx = stack[top--]
            result[prevIdx] = currIdx - prevIdx
        }
        // Push current index onto the stack
        stack[++top] = currIdx
    }
    return result
}
```

```kotlin
fun dailyTemperatures(temperatures: IntArray): IntArray {
    // Backward Jumping Approach (Optimized)
    // Time Complexity: O(N) | Space Complexity: O(1)
    val n = temperatures.size
    val result = IntArray(n)

    // Start from the second-to-last day (last day is always 0)
    for (i in n - 2 downTo 0) {
        var j = i + 1

        // Use the result array to "jump" over colder days.
        // If temperatures[j] is colder than temperatures[i], 
        // we skip directly to the next warmer day for j.
        while (temperatures[i] >= temperatures[j] && result[j] > 0) {
            j += result[j]
        }

        // If we finally found a warmer day, store the distance
        if (temperatures[j] > temperatures[i]) {
            result[i] = j - i
        }
    }
    return result
}
```
### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Array-based vs. Collection-based Stack**:
    *   **Collection (ArrayDeque)**: cleaner, safer, and dynamic.
    *   **Array (Primitive)**: Faster (no boxing), but fixed size. In a performance-critical system 
    processing millions of "ticks," the primitive array saves significant Garbage Collection cycles.
*   **In-Place Space Optimization**: By iterating backwards, it is possible to use the result array 
itself to "jump" to the next warmer day without an explicit stack (**O(1)** extra space). This trades 
code complexity for memory efficiency.

---

## Day 19 - LC 155 - Min Stack
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **O(1)** Minimum Access**: The problem requires finding the minimum element in constant time. 
  A standard linear scan (**O(N)**) is forbidden.
  * **Temporal State Persistence**: The "minimum" of the stack changes as elements are popped. We 
  must "remember" what the minimum was before the current top element was pushed.
  * **Mirrored Stack Strategy**: This triggers the use of an **Auxiliary Stack**. While one stack 
  holds the data, the second stack holds the "minimum at this point in time," allowing us to 
  backtrack to previous minimums in **O(1)**.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | `push`   | `pop`    | `getMin`   | Space | Best Used When... |
| :--- |:---------|:---------|:-----------| :--- | :--- |
| **Standard Stack** | **O(1)** | **O(1)** | **O(N)**   | **O(1)** | You rarely need the minimum. |
| **Two Stacks (Optimal)** | **O(1)** | **O(1)** | **O(1)**   | **O(N)** | **Optimal.** Standard for high-frequency min-tracking. |
| **Node with Min** | **O(1)**   | **O(1)**   | **O(1)**   | **O(N)** | Implementing a custom Stack with Linked List. |

### 3. Native Kotlin Syntax Pitfalls
*   **The `st.min()` Trap**: Avoid using `collection.min()` or `minOrNull()`. These are linear 
operations (**O(N)**) that scan the entire collection, violating the **O(1)** requirement.
*   **Boxing Overhead**: `ArrayDeque<Int>` boxes every primitive `Int` into a `java.lang.Integer` 
object. For extreme performance, a custom primitive array with a `top` pointer would be used.
*   **Duplicate Minimums**: When pushing to the `minStack`, use the **`<=`** operator. If you use `<`, 
popping a duplicate of the minimum will leave the `minStack` out of sync.

### 4. Code Block

```kotlin
class MinStack {
    // Auxiliary Stack Strategy
    // Time Complexity: O(1) for all operations | Space Complexity: O(N)
    private val st = ArrayDeque<Int>()
    private val minSt = ArrayDeque<Int>()

    fun push(value: Int) {
        st.addLast(value)
        // Record the new minimum. Use <= to handle duplicate mins.
        if (minSt.isEmpty() || value <= minSt.last()) {
            minSt.addLast(value)
        }
    }

    fun pop() {
        if (st.isEmpty()) return
        // If the element we are removing is the current minimum, 
        // remove it from the minStack as well.
        // EXPLICIT UNBOXING: Capture the popped element. 
        // Comparing it directly to minSt.last() ensures zero reference ambiguity.
        val poppedValue = st.removeLast()
        if (poppedValue == minSt.last()) {
            minSt.removeLast()
        }
    }

    fun top(): Int = st.last()

    fun getMin(): Int = minSt.last()
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Space Optimization (Sparse MinStack)**: Instead of pushing to the `minStack` every time, we 
only push when a *new* minimum is found. This saves space when the input is largely increasing.
*   **One Stack (Value Encoding)**: You can store the difference between the value and the min in a 
single stack (`value - min`). This achieves **O(1)** min without an extra stack but is prone to 
**Integer Overflow** and is much harder to maintain/read.
*   **Custom Node (Linked List)**: Each node in a linked-list implementation of a stack can store a 
`min` field: `Node(val: Int, min: Int, next: Node?)`. This is cleaner for object-oriented designs 
but has higher per-element pointer overhead.

---

## Day 18 - LC 20 - Valid Parentheses
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Nested LIFO Structure**: Brackets must be closed in the exact reverse order they were opened. 
  This "Last-In, First-Out" requirement is a textbook signal for a **Stack** data structure.
  * **Immediate Validation**: Each closing bracket must correspond precisely to the most recent 
  unmatched opening bracket. The stack allows us to "remember" the open ones in the correct order.
  * **Expected Character Strategy**: Instead of pushing the character seen (e.g., `'('`), we can 
  push what we **expect** to see later (e.g., `')'`). This simplifies the "pop" phase logic by 
  reducing the comparison to a single equality check (`pop() == char`).

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time | Space | Performance | Best Used When... |
| :--- |:-----------|:---------| :--- | :--- |
| **Brute Force (Replace)** | **O(N^2)** | **O(N)** | Low | **N** is tiny and you want zero logic. |
| **Stack + HashMap** | **O(N)** | **O(N)** | High | General purpose; handles any bracket set dynamically. |
| **Stack + CharArray** | **O(N)** | **O(N)** | **Peak** | **Fastest.** Minimizes CPU cycles and memory traffic. |

### 3. Native Kotlin Syntax Pitfalls
*   **Legacy `java.util.Stack`**: Avoid using this class. It is a legacy Java 1.0 collection that is
**synchronized**, adding unnecessary locking overhead to every operation. Use 
**`kotlin.collections.ArrayDeque`** for a modern, high-performance stack.
*   **`indexOf()` Search Overhead**: Avoid searching a separate array of brackets to find a match. 
This turns an **O(1)** lookup into **O(M)** (where **M** is the number of bracket types). Use a **`when`**
expression or a **`Map`** for direct mapping.
*   **Repeated Map Allocation**: If using the "Expected Character" strategy with a `Map`, move the 
map to a `private val` outside the function. Creating a new map on every function call adds 
significant object allocation and Garbage Collection overhead in high-frequency validation systems.
*   **CharArray vs. Map Speed**: Using a **`CharArray`** for character mapping is the most 
performant strategy. It leverages **Direct CPU Addressing** (raw memory offset) which is faster 
than hashing a key. It also avoids all **Object Boxing** (converting primitive `Char` to `Character`), 
keeping the CPU cache and heap cleaner.

### 4. Code Block
```kotlin
private val EXPECTED_CLOSER = CharArray(128).apply {
    this['('.code] = ')'
    this['['.code] = ']'
    this['{'.code] = '}'
}

fun isValid(s: String): Boolean {
    val stack = ArrayDeque<Char>()

    for (i in s.indices) {
        val char = s[i]
        val charCode = char.code

        // 1. O(1) Check: See if this character is a known opener
        if (charCode < 128 && EXPECTED_CLOSER[charCode] != '\u0000') {
            stack.addLast(EXPECTED_CLOSER[charCode])
        } else {
            // 2. It's a closer, so it must match the top expected character on the stack
            if (stack.isEmpty() || stack.removeLast() != char) {
                return false
            }
        }
    }
    return stack.isEmpty()
}

```
```kotlin
private val OPEN_TO_CLOSE = mapOf('(' to ')', '[' to ']', '{' to '}')
fun isValid(s: String): Boolean {
    // Expected Character strategy
    val stack = ArrayDeque<Char>()

    for (char in s) {
        when (char) {
            // If it's an opener, push the bracket we EXPECT to see later
            '(', '[', '{' -> stack.addLast(OPEN_TO_CLOSE[char]!!)
            ')', ']', '}' -> {
                // If it's a closer, it MUST match the top of the stack
                if (stack.isEmpty() || stack.removeLast() != char) {
                    return false
                }
            }
            else -> continue
        }
    }
    return stack.isEmpty()
}

```
```kotlin
fun isValid(s: String): Boolean {
    // Optimized Stack Approach using ArrayDeque (non-synchronized)
    // Time Complexity: O(N) | Space Complexity: O(N)
    val stack = ArrayDeque<Char>()
    
    for (char in s) {
        when (char) {
            // Push open brackets to the stack
            '(', '[', '{' -> stack.addLast(char)
            
            // For closing brackets, pop and verify the match
            ')' -> if (stack.isEmpty() || stack.removeLast() != '(') return false
            ']' -> if (stack.isEmpty() || stack.removeLast() != '[') return false
            '}' -> if (stack.isEmpty() || stack.removeLast() != '{') return false
            
            // Ignore non-bracket characters
            else -> continue
        }
    }
    
    // String is valid only if all brackets were closed
    return stack.isEmpty()
}
```
```kotlin
// Consider: If the string only contained one type of bracket, say just parentheses, could you solve
// this without using a stack to save space?
fun isValidOnlyOneType(s: String): Boolean {
    var counter = 0

    for (char in s) {
        when (char) {
            '(' -> counter++
            ')' -> {
                counter--
                // Early exit: cannot close what was never opened
                if (counter < 0) return false
            }
            else -> continue
        }
    }

    return counter == 0
}
```
### 5. Alternative Trade-offs (For System Design Dialogues)
*   **Single Bracket Type Optimization**: If the string only contains one type of bracket (e.g., 
    just `()`), the Stack can be replaced by a simple **Counter**.
    *   **Space**: Reduces from **O(N)** to **O(1)**.
    *   **Logic**: Increment for `(`, decrement for `)`. If the counter ever becomes negative, 
    return `false` immediately (prevents `")("` case).
*   **Space Optimization (Small Strings)**: If the string length is small and fixed, you can use a 
primitive `CharArray` with a `top` pointer index. This avoids all object allocations (boxing `Char` 
into the Deque), which is ideal for performance-critical embedded or mobile systems.
*   **Memory Safety**: In a system processing untrusted input, you should cap the maximum size of 
the stack. A malicious string like `((((...` (10 million long) could cause an `OutOfMemoryError` by 
filling the heap with `Char` objects.
