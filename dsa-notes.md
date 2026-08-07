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

## Day 4 - LC 14 - Longest Common Prefix
### 1. Core Pattern Identifier
* What specific constraint triggered the solution design?
* **Sort and compare the first and last string**
Sort the Array<String> and use `commonPrefixWith()` to find the common prefix.

* **Horizontal Linear Scan**
Either using for-loop or .reduceOrNull() do linear scan. Inside both of loops, using 
`commonPrefixWith()` finds the common prefix. Finally adding short-circuit check for 
early exit when no prefix happens.

### 2. Complexity Boundaries
* **Sort and compare the first and last string**

O(M * N(Log(N))) time complexity where N is the number of strings and M is the maximum length of 
a string. sorting strings instead of simple integers, every single comparison step requires checking
the characters inside the strings up to length M.

O(1) space complexity.

* **Horizontal Linear Scan**

O(S) time complexity where S is the sum of all characters in all strings.

O(N) space complexity. `fun longestCommonPrefixMemoryOptimized` can make it O(1) space complexity. 

### 3. Native Kotlin Syntax Pitfalls
* Use `String.commonPrefixWith()` to find the common prefix.
* Use `strs.reduceOrNull { prefix, s -> prefix.commonPrefixWith(s) }` to scan each string.

### 4. Code Block
* Sort and compare the first and last string 
```kotlin
    fun longestCommonPrefix(strs: Array<String>): String {
        if (strs.isEmpty()) return ""
        val sortedList = strs.sorted()
        return sortedList.first().commonPrefixWith(sortedList.last())
    }
```
* Horizontal Linear Scan
```kotlin
    fun longestCommonPrefix(strs: Array<String>): String {
        return strs.reduceOrNull { prefix, s ->
            if (prefix.isEmpty()) return ""
            prefix.commonPrefixWith(s)
        } ?: ""
    }
```
```kotlin
    fun longestCommonPrefixMemoryOptimized(strs: Array<String>): String {
        if (strs.isEmpty()) return ""
        if (strs.size == 1) return strs[0]
    
        // Track the absolute alphabetical minimum and maximum values inline
        var first = strs[0]
        var last = strs[0]
    
        // A single pass identifies the boundaries without allocating a new array container
        for (s in strs) {
            if (s < first) first = s
            if (s > last) last = s
        }
    
        // Compare only the two extreme strings in O(1) space!
        return first.commonPrefixWith(last)
    }   
```
### 5. Alternative Trade-offs (For System Design Dialogues)
* Horizontal Scan (Current Choice):

**Pros:** Best when the common prefix is short or non-existent. It short-circuits early, saving CPU 
cycles by not looking at the tail end of the strings or the rest of the array.

**Cons:** If all strings are identical and very long, it performs redundant comparisons across the 
entire array.

* Sorting Approach:

**Pros:** Minimal code footprint. By sorting, you only need to compare the two most different 
strings (first and last).

**Cons:** O(M * N(Log(N))) overhead. Sorting modifies the original data (side effect) and is much slower 
if the number of strings (N) is large, even if the strings themselves are short.

* Divide and Conquer:

**Pros:** This can be parallelized. In a distributed system (like processing logs in MapReduce), 
you could find the common prefix of two halves of the data on different CPU cores/machines 
simultaneously and then merge the results.

**Time Complexity:** Still O(S), but significantly lower latency on multi-core systems.

**Key Takeaway:** Horizontal Scan is the "General Purpose" winner, while Divide and Conquer is the "Scale/Parallel" winner.

## Day 3 - LC 1 - Two Sum
### 1. Core Pattern Identifier
* What specific constraint triggered the solution design?
* Using HashMap<Int, Int> - `visited[complement] = index` to record the complement and its index in 
one pass is the best solution. It does not only collect the passing complement and index but also 
directly grab it when the matching complement is found. 
* Using MutableIntIntMap - `visited[value] = index` to record the value and its index in one pass. 
The difference is to reduce the object allocation overhead and cause garbage collection pressure.

### 2. Complexity Boundaries
O(N) time and O(N) space to store each value and its index in the HashMap and MutableIntIntMap.

### 3. Native Kotlin Syntax Pitfalls
* Use `HashMap.containsKey(key)` to find the complement in the HashMap.
* Use `mutableIntIntMapOf()` to initialize the HashMap and no need to add <key, value> pair. Others
syntax are the same as HashMap.

### 4. Code Block
```kotlin
    fun twoSum(nums: IntArray, target: Int): IntArray {
        val visited = HashMap<Int, Int>() // Format: Value -> Index

        for ((index, value) in nums.withIndex()) {
            val complementIndex = visited[target - value]
            if (complementIndex != null) {
              return intArrayOf(complementIndex, index)
            }
            visited[value] = index
        }
        return intArrayOf()
    }
```
```kotlin
    fun twoSumPrimitive(nums: IntArray, target: Int): IntArray {
        val visited = mutableIntIntMapOf() // Format: Value -> Index
        for ((index, value) in nums.withIndex()) {
            val complement = target - value
            if (visited.containsKey(complement)) {
                return intArrayOf(visited[complement], index)
            }
            visited[value] = index
        }
        return intArrayOf()
    }
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Brute Force**: Two loops, O(N^2) time and O(1) space.
* **HashMap**: Two passes - One to add <value, index> to the HashMap and another to find the 
matching complement, O(N) time and O(N) space.
*  **HashMap**: One pass - O(N) time and O(N) space. Both operations of adding and finding the 
complement are done in one iteration. 
* **MutableIntIntMap**: One pass - O(N) time and O(N) space. 
While a standard HashMap<Integer, Integer> achieves a theoretical O(N) linear runtime complexity, 
it introduces massive object allocation overhead on the heap due to Java primitive type erasure. 
By utilizing Jetpack's unboxed MutableIntIntMap, the system stores keys and values sequentially 
inside primitive data buffers under the hood. This eliminates Garbage Collector pressure, completely 
stops object boxing overhead, and ensures high cache locality within performance-critical data 
pipelines.

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


