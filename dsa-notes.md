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

## Day 7 - 238 - Product of Array Except Self
### 1. Core Pattern Identifier
#### Two-Array approach - Three Loops with Two Arrays

* The first loop is to accumulate the prefix product and save in one array. The second loop is to 
accumulate the suffix product and save in another array. The third loop is to multiply the prefix 
product and suffix product for each index.

####  In-Place Approach - Two Loops and One Array for Result

* The first loop is to accumulate the prefix product and store it in the result array before the last 
element. The last element of the result is the current prefix product. And then we create a variable
to persist the current suffix product and is initialized to the last element of the input array. 
The second loop is to update the result by multiplying the current suffix product with the result 
from previous index. 

####  Prefix/Suffix Approach - Two Pointers in One Pass

One array is to accumulate the prefix product directd by left pointer from left to right. The other 
is to accumulate the suffix product direct by right pointer from right to left. The product of array
except self starts from left and right point in the same middle element for odd length array or left
point is on the right of right point for even length array. Prod[N] = L[N-1] * R[N+1]

### 2. Complexity Boundaries
* Two-Array approach - O(N) time and O(N) space.
* In-Place Approach - O(N) time and O(1) space
* Prefix/Suffix Approach - O(N) time and O(N) space.

### 3. Native Kotlin Syntax Pitfalls
Since the implementation is very trivial with standard Kotlin syntax, there are no native Kotlin 
syntax pitfalls.

### 4. Code Block
#### Two-Array approach - Three Loops with Two Arrays
```kotlin
    fun productExceptSelf(nums: IntArray): IntArray {
        val rProd = IntArray(nums.size)
        val lProd = IntArray(nums.size)
        nums.size - 1
        lProd[0] = nums[0]
        for (i in 1 until nums.size) {
          lProd[i] = lProd[i - 1] * nums[i]
        }
        rProd[nums.size - 1] = nums[nums.size - 1]
        for (i in nums.size - 2 downTo 0) {
          rProd[i] = rProd[i + 1] * nums[i]
        }

        val output = IntArray(nums.size)
        output[0] = rProd[1]
        output[nums.size - 1] = lProd[nums.size - 2]
        for (i in 1 until nums.size - 1) {
          output[i] = lProd[i - 1] * rProd[i + 1]
        }
        return output
    }
```

####  In-Place Approach - Two Loops and One Array for Result
```kotlin
    fun productExceptSelf(nums: IntArray): IntArray {
        val size = nums.size
      
        // Allocate the distinct output container requested by the problem
        val output = IntArray(size)

        // Forward Pass: Accumulate left-side prefix products directly into output
        output[0] = 1
        for (i in 1 until size) {
          output[i] = output[i - 1] * nums[i - 1]
        }
      
        // Backward Pass: Accumulate right-side suffix products inline via a primitive tracker
        var rightSuffixProduct = 1
        for (i in size - 1 downTo 0) {
          output[i] = output[i] * rightSuffixProduct // Combines prefix and suffix
          rightSuffixProduct *= nums[i] // Updates running product for the next leftward step
        }
        return output
    }
```

####  Prefix/Suffix Approach - Two Pointers in One Pass
```kotlin
    fun productExceptSelf(nums: IntArray): IntArray {
        val rProd = IntArray(nums.size)
        val lProd = IntArray(nums.size)
        var l = 0
        var r = nums.size - 1
        lProd[l] = nums[l]
        rProd[r] = nums[r]
        while (++l < --r) {
          lProd[l] = lProd[l - 1] * nums[l]
          rProd[r] = rProd[r + 1] * nums[r]
        }
      
        while (l < nums.size - 1 && r > 0) {
          lProd[l] = lProd[l - 1] * nums[l]
          rProd[r] = rProd[r + 1] * nums[r]
          if (l == r) {
            nums[l] = lProd[l - 1] * rProd[r + 1]
          } else {
            nums[l] = rProd[l + 1] * lProd[l - 1]
            nums[r] = lProd[r - 1] * rProd[r + 1]
          }
          l++; r--
        }
        nums[r] = rProd[r + 1]
        nums[l] = lProd[l - 1]
        return nums
    }
```

### 5. Alternative Trade-offs (For System Design Dialogues)

### Prefix & Suffix Arrays vs. In-place Space Optimization:
#### Two-Array approach:
* Pros: Much easier to read, debug, and implement. It separates the "Left Product" logic from the 
"Right Product" logic.
* Cons: Uses O(N) extra space. On a memory-constrained device (like an embedded sensor or an older 
Android phone), this could matter for massive arrays.
#### In-place approach:
* Pros: Achieves O(1) extra space (if the output array doesn't count as extra).
* Cons: Harder to maintain. The logic of reusing the result array to store prefix products and then 
multiplying it by suffix products in reverse is prone to "off-by-one" errors.

### Handling Zeros (Edge Case Strategy):
#### Division-based approach:
* Logic: Calculate the total product of the whole array, then for each element i, do total / nums[i].
* Pros: O(N) and very simple code.
* Cons: Fails completely with zeros. If there is one zero, all except one result will be 0. If there
are two zeros, all results will be 0. You have to write messy if statements to handle these.

#### Prefix/Suffix approach (Current):
* Pros: Naturally handles zeros without any special if logic.

### Numeric Overflow (Critical for System Design):
* The Problem: Multiplying many integers can easily exceed the 32-bit Int limit (approx. 2 billion).
* Trade-off:

  ▪ If the system requires high precision for very large arrays, you must use LongArray (64-bit) or 
BigInteger.

  ▪ In some signal processing contexts, we use the Logarithmic property: $Log(a \times b) = 
Log(a) + Log(b)$. You can sum the logs and then take the exponent (exp) of the sum.

  ▪ Pros of Logs: Prevents overflow.

  ▪ Cons of Logs: Floating point precision errors can occur (e.g., getting 9.99999 instead of 10).

### Parallelization (Large Scale Data):
* Scenario: What if the array has 10 billion numbers?
* Solution: Use a Prefix Sum / Prefix Product Tree.

  ▪ The array is divided into blocks. Each block calculates its own product. These products are then 
combined in a tree-like structure.

  ▪ Pros: Can be done in $O(\log N)$ time on a massive GPU or a cluster of servers.

### Summary for Notes:
* Interview Focus: Emphasize the In-place O(1) space optimization as the "senior dev" move.
* System Design Focus: Discuss Numeric Overflow (using Long) and the Logarithmic approach for 
extreme cases.


## Day 6 - LC 347 - Top K Frequent Elements
### 1. Core Pattern Identifier
* Idiomatic Kotlin solution

Purely using functional chain for frequency counting and efficient selection to identify the top k 
elements by frequency using .

* HashMap + Idiomatic Kotlin solution

Efficiently selecting the top k elements based on frequency counts gathered via hash map.

* HashMap + PriorityQueue solution

Frequency counting combined with dropping all entries in the Priority Queue but keeping the k size 
to get top K elements.

* HashMap + Bucket Sort solution

Frequency counting combined with linear time selection and Bucket Sort.

### 2. Complexity Boundaries
* Idiomatic Kotlin solution - O(N * Log(N)) time and O(N) space.
* HashMap + Idiomatic Kotlin solution - O(N * Log(N)) time and O(N) space.
* HashMap + PriorityQueue solution - O(N * Log(K)) time when K is top K elements and O(N) space.
* HashMap + Bucket Sort solution - O(N) time and O(N) space.


### 3. Native Kotlin Syntax Pitfalls
* Use `nums.asSequence().groupingBy { it }.eachCount()`. IntArray.asSequence() bypasses heavy list 
allocations by mapping elements through a lazy sequence iterator. Calling .toList() for large arrays
on a primitive array (IntArray) copies the elements into a heap-allocated collection wrapper 
(List<Int>). This causes massive object boxing overhead.
* Use `map.entries.sortedByDescending { it.value }.take(k).map { it.key }.toIntArray()` to sort by 
descending order, take top k elements, and map keys to IntArray.
* Use `PriorityQueue<Map.Entry<Int, Int>>(compareBy { it.value })` to initialize the 
PriorityQueue with descending order.

### 4. Code Block
* Idiomatic Kotlin solution
```kotlin
  fun topKFrequent(nums: IntArray, k: Int): IntArray { 
      return nums.asSequence().groupingBy { it }.eachCount().entries
          .sortedByDescending { it.value }.take(k).map { it.key }.toIntArray()
  }
```
* HashMap + Idiomatic Kotlin solution
```kotlin
  fun topKFrequent(nums: IntArray, k: Int): IntArray {
      val map = HashMap<Int, Int>()
      for (num in nums) {
          map[num] = map.getOrDefault(num, 0) + 1
      }
      return map.entries.sortedByDescending { it.value }.take(k)
          .map { it.key }.toIntArray()
  }
```
* HashMap + PriorityQueue solution
```kotlin
  fun topKFrequent(nums: IntArray, k: Int): IntArray {
      val map = HashMap<Int, Int>()
      nums.forEach {
        map[it] = map.getOrDefault(it, 0) + 1
      }
      val pq = PriorityQueue<Map.Entry<Int, Int>>(compareBy { it.value })
      for (entry in map.entries) {
        pq.add(entry)
        if (pq.size > k) {
          pq.poll()
        }
      }
      val res = IntArray(k)
      var i = 0
      while (pq.isNotEmpty()) {
        res[i++] = pq.poll()!!.key
      }
      return res
  }
```

* HashMap + Bucket Sort solution
```kotlin
  fun topKFrequent(nums: IntArray, k: Int): IntArray {
      val map = HashMap<Int, Int>()
      for (num in nums) {
        map[num] = map.getOrDefault(num, 0) + 1
      }
      val bucket = Array<MutableList<Int>>(nums.size + 1) { mutableListOf() }
      for (entry in map.entries) {
        bucket[entry.value].add(entry.key)
      }
    
      val res = IntArray(k)
      var i = 0
      for (j in bucket.size - 1 downTo 0) {
        for (num in bucket[j]) {
          res[i++] = num
        }
        if (i == k) break
      }
      return res
  }
```

### 5. Alternative Trade-offs (For System Design Dialogues)
### Heap (PriorityQueue) vs. Bucket Sort:
#### Heap approach:
* Pros: Better when K is very small (K < N). If you use a Min-Heap of size K, the 
complexity is O(N * Log(K)). It is also more memory-efficient because you only ever store 
K elements in the heap.
* Cons: Slower than Bucket Sort if K is large or close to N.
#### Bucket Sort approach:
* Pros: The fastest theoretical time O(N) linear time algorithm. It avoids the log 
factor entirely.
* Cons: Higher Memory Overhead. You must allocate an array of lists of size N + 1. If 
N=1,000,000 but you only have 2 unique elements, you still allocate a massive empty array.

### Quickselect (Hoare's Selection Algorithm):
* Pros: Faster than Heap and Bucket Sort. Average time complexity is O(N), and it uses 
O(1) extra space (in-place). This is what many standard libraries use under the hood 
for "partial sorting."
* Cons: Worst-case time is O(N^2) (though rare with random pivoting), and it's much 
harder to implement bug-free in a high-pressure interview.

### Streaming Data / Heavy Hitters (System Design Scale):
* The Problem: What if the data doesn't fit on one machine? (e.g., finding top K searched 
terms on Google today).
* Solution - Count-Min Sketch: A probabilistic data structure that uses a hash table to 
estimate frequencies with a tiny memory footprint.
> Trade-off: You get approximate counts rather than exact ones, but you save gigabytes of RAM.
* Solution - MapReduce / Spark:

▪ Step 1: Distribute the numbers across workers.

▪ Step 2: Each worker counts local frequencies (Map).

▪ Step 3: Aggregate frequencies globally (Reduce).

▪ Step 4: Use a Min-Heap on the final controller to get the top K.

### Offline vs. Online Processing:
* Offline: If you only need the report once a day, Bucket Sort on a single large-memory instance 
is fine.
* Online: If you need a "Trending Now" feature, use a Redis Sorted Set (ZSET). It maintains the 
elements in a skip-list/hash-map hybrid, allowing you to get the top K in O(Log(N)) time at any 
moment as data flows in.

### Summary for Notes:
* Small K: Use Min-Heap.
* Large N, limited Time: Use Bucket Sort.
* Massive/Distributed Data: Use MapReduce + Min-Heap or Count-Min Sketch for approximations.


## Day 5 - LC 49 - Group Anagrams
### 1. Core Pattern Identifier
* Idiomatic Kotlin solution
1. Sort each string lexicographically and group them together
2. Count each character's frequency and group them together
* Count each character's frequency in IntArray(26) and create the hashcode as a key in Map

### 2. Complexity Boundaries
* Idiomatic Kotlin solution
1. Sort: O(N * K(Log(K))) time and O(N * K) space.
2. Count: O(N * K) time and O(N * K) space.
* Count each character's frequency in IntArray(26) and create the hashcode as a key in Map -
O(N * K) time and O(N * K) space

### 3. Native Kotlin Syntax Pitfalls
* Use `String.toCharArray().apply { sort() }.concatToString()` to sort the string.
* Use `String.groupingBy { it }.eachCount()` to group the string and check each char's count.
* Use `val keyString = letterCnt.joinToString(",")` to create the key by using the IntArray 
for each word's letter count.
* Use `map.getOrPut(keyString) { mutableListOf() }.add(str)` instead of
`if (map.containsKey(keyString)) { map[keyString]?.add(str) } else { map[keyString] = mutableListOf(str) }`

### 4. Code Block
* Idiomatic Kotlin solution - Sort + Grouping
```kotlin
    fun groupAnagrams(strs: Array<String>): List<List<String>> {
        return strs.groupBy {
            it.toCharArray().apply { sort() }.concatToString()
        }.values.toList()
    }
```
* Idiomatic Kotlin solution - Counting + Grouping
```kotlin
    fun groupAnagrams(strs: Array<String>): List<List<String>> {
        return strs.groupBy { str ->
            str.groupingBy { it }.eachCount()
        }.values.toList()
    }
```
* Count each character's frequency in IntArray(26) and create the hashcode as a key in Map
```kotlin
    fun groupAnagrams(strs: Array<String>): List<List<String>> {
        val letterCnt = IntArray(26)
        val map = HashMap<Int, MutableList<String>>()
        var hashCode: Int
        for (str in strs) {
          for (c in str) {
            letterCnt[c - 'a']++
          }
          // Convert the frequency array to a distinct string signature key
          val keyString = letterCnt.joinToString(",")
          map.getOrPut(keyString) { mutableListOf() }.add(str)
          letterCnt.fill(0)
        }
        return map.values.toList()
    }
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Sorting + Grouping**: Idiomatic Kotlin solution. We could group them by sorting each word 
lexicographically.
* **Counting + Grouping**: Idiomatic Kotlin solution. We could group them by the same appearing 
letters with the same frequency
* **InArray(26) + HashMap**: We could create an IntArray(26) counter from 'a' to 'z' to store 
appearing frequency from each letter in a word and use the whole array as a key in a HashMap.
IntArray(26).joinToString(",") instead of IntArray(26).contentHashCode() to avoid collision.  


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


