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

## Day 2 - LC 242 - Valid Anagram

### 1. Core Pattern Identifier
* What specific constraint triggered the solution design?
* Finding the same frequency of letters in s and t strings can be done in the following ways:
1. Sort both of the strings and compare each element
2. Group the letters in both strings and compare each group letter count. It's good solution 
when considering the Unicode case
3. Create an IntArray(26) counter from 'a' to 'z', increment for s and decrement for t. 
Then check if all elements in the counter array are 0.
4. Create a HashMap<Char, Int>, increment for s and decrement for t. Then check if all key's 
value are 0.

### 2. Complexity Boundaries
* Let's check each solution's time and space complexity.
1. Sort: O(N log N) time and O(1) space.
2. Grouping: O(N) time and O(1) space.
3. Counter: O(N) time and O(1) space.
4. HashMap: O(N) time and O(1) space.

### 3. Native Kotlin Syntax Pitfalls
* Let's check each solution for syntax and edge cases.
1. Sort: Use `String.toCharArray().apply { sort() }.concatToString()` to sort the string. 
2. Grouping: Use `String.groupingBy { it }.eachCount()` to group the string and check each 
char's count.
3. Counter: Use `for (i in s.indices)` to iterate through s string to increase the letter count and 
decrease it through t string.
4. HashMap: Use `HashMap[s[i]] = HashMap.getOrDefault(s[i], 0) + 1` to increase the letter count 
and `HashMap[t[i]] = HashMap.getOrDefault(t[i], 0) - 1` to decrease it.

### 4. Code Block
  1. Sort
```kotlin
fun isAnagram(s: String, t: String): Boolean {
    if (s.length != t.length) return false
    return s.toCharArray().apply { sort() }.concatToString() == 
            t.toCharArray().apply { sort() }.concatToString()
}
```
  2. Grouping
```kotlin
fun isAnagram(s: String, t: String): Boolean {
    if (s.length != t.length) return false
    return s.groupingBy { it }.eachCount() == t.groupingBy { it }.eachCount()
}
```
  3. Counter
```kotlin
fun isAnagram(s: String, t: String): Boolean {
    if (s.length != t.length) return false
    val letterCnt = IntArray(26)
    for (i in s.indices) {
        letterCnt[s[i] - 'a']++
        letterCnt[t[i] - 'a']--
    }
    return letterCnt.all { it == 0 }
}
```
  4. HashMap
```kotlin
fun isAnagram(s: String, t: String): Boolean {
    if (s.length != t.length) return false
    val letterCnt = HashMap<Char, Int>()
    for (i in s.indices) {
        letterCnt[s[i]] = letterCnt.getOrDefault(s[i], 0) + 1
        letterCnt[t[i]] = letterCnt.getOrDefault(t[i], 0) - 1
    }
    return letterCnt.all { it.value == 0 }
}
```
### 5. Alternative Trade-offs (For System Design Dialogues)
1. **Sorting**: We could sort the array first (O(N log N) time) and then check adjacent elements 
(O(1) space). This is better if memory is strictly limited.
2. **Grouping**: We could use group the letters in both strings and compare each group letter count.
(O(N) time and O(1) space)
3. **Counter**: We could create an IntArray(26) counter from 'a' to 'z', increment for s and 
decrease for t (O(N) time and O(1) space).
4. **HashMap**: We could create a HashMap<Char, Int>, increment for s and decrease for t (O(N) time 
and O(1) space).


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


