# LeetCodePrep

A collection of LeetCode problem solutions implemented in Kotlin.

## How to Run Tests
You can run the tests using Gradle:
```bash
./gradlew test
```

## Week 1 Problem Bank

## Topics Covered
- [ ] Arrays & Hashing

## Day 1 - LC 217 - Contains Duplicates

### 1. Core Pattern Identifier
* What specific constraint triggered the solution design?
* Finding any value in an unsorted IntArray appearing at least twice implies to use HashSet to store
the reading element values. If the current value is already in the HashSet, return true.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 

### 2. Complexity Boundaries
* **Time Complexity**: O(N) where N is array length.
* **Space Complexity**: O(N) to store each value in the HashSet.

### 3. Native Kotlin Syntax Pitfalls
* Use `HashSet.add(value)` to add a value to the HashSet.
* Use `HashSet.contains(value)` to check for duplicates.

### 4. Code Block
```kotlin
fun containsDuplicate(nums: IntArray): Boolean {
    val visited = HashSet<Int>()
    for (num in nums) {
        if (!visited.add(num)) {
            return true
        }
    }
    return false
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Sorting**: We could sort the array first (O(N log N) time) and then check adjacent elements (O(1) space). This is better if memory is strictly limited.
* **Idiomatic Kotlin**: Use `any` and the fact that `HashSet.add()` returns `false` if the element already exists:
  ```kotlin
  fun containsDuplicate(nums: IntArray) = HashSet<Int>().let { set -> 
      nums.any { !set.add(it) } 
  }
  ```


