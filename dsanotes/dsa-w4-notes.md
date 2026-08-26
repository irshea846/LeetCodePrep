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

---
