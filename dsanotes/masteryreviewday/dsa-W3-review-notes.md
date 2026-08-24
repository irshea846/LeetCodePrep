# LeetCodePrep Week3 Mastery & Review Day

A collection of LeetCode problem solutions implemented in Kotlin.

# ===============================================================
# WEEK 3: TWO POINTERS & SLIDING WINDOWS (August 23)
# ===============================================================

## General 3Sum Scale (Both)
### Memory Pressure (JVM):
* **These algorithms are **O(1)** space, making them ideal for memory-constrained environments like 
Android devices. Unlike solutions using HashMap or HashSet, these will not trigger the Garbage 
Collector, ensuring smooth UI performance during calculation**.

### Approximate Algorithms:
* **For extremely large datasets where **O(N^2)** is too slow, you could use Locality Sensitive Hashing
(LSH) to find triplets that are "likely" to be close to the target in **O(N)** time with a small 
probability of error**.


## LC 16. 3Sum Closest
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Proximity Matching**: Unlike 3Sum (exact match), this asks for the "closest," signaling that 
  we must track a global minimum difference while traversing.
  * **Sorted Convergence**: By sorting the array, we can use the **Two-Pointer Squeeze** to 
  systematically move closer to the target from both ends, pruning the search space to **O(N^2)**.
  * **Short-Circuit Potential**: The smallest possible difference is 0. Finding a sum equal to the 
  target allows for an immediate early return.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time       | Space    | Best Used When... |
| :--- |:-----------|:---------| :--- |
| **Brute Force** | **O(N^3)** | **O(1)** | Array size is tiny. |
| **Sorting + Two Pointers** | **O(N^2)** | **O(1)** | **Optimal.** Standard for large-scale proximity search. |

### 3. Native Kotlin Syntax Pitfalls
*   **Infinite Loop on Equality**: In the convergence logic, ensure you handle the `sum == target` 
case by either moving both pointers or returning immediately. Failing to move pointers on equality 
will hang the loop.
*   **Initialization Trap**: Initializing `minDiff` to `Int.MAX_VALUE` is common, but tracking 
`closestSum` initialized to `nums[0] + nums[1] + nums[2]` is safer and avoids potential overflow 
logic errors when calculating differences.

### 4. Code Block
```kotlin
fun threeSumClosest(nums: IntArray, target: Int): Int {
    nums.sort()
    var closestSum = nums[0] + nums[1] + nums[2]
    
    for (i in 0 until nums.size - 2) {
        if (i > 0 && nums[i] == nums[i - 1]) continue
        
        var j = i + 1
        var k = nums.lastIndex
        
        while (j < k) {
            val curSum = nums[i] + nums[j] + nums[k]
            if (curSum == target) return target // Optimization: perfect match
            
            if (abs(target - curSum) < abs(target - closestSum)) {
                closestSum = curSum
            }
            
            if (curSum < target) j++ else k--
        }
    }
    return closestSum
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Precision vs. Latency**: The **O(N^2)** Two-Pointer approach is precise. For massive systems 
(e.g., matching billions of GPS points), a **Locality Sensitive Hashing (LSH)** approach could find 
"almost closest" sums in **O(N)** time, trading absolute accuracy for extreme throughput.

* **Early Exit Strategy**: The "Perfect Match" (**Diff=0**) check is a vital optimization. 

* **Precision vs. Throughput**:
  * **Binary Search Alternative**: Instead of Two Pointers, you could fix two numbers and Binary 
  Search for the third.
  * **Complexity**: **O(N^2 * Log(N))**.
  * **Trade-off**: Slower overall, but better if you can only afford to "scan" a small portion of 
  the array or if the array is stored on external storage where sequential pointer movement is 
  expensive.

* **Early Exit vs. Global Search**:
  * **The "Perfect Match" ($Diff=0$) early exit is critical for real-time systems. In a system 
  processing millions of coordinate pairs, returning immediately on a match can save billions of 
  instructions across the entire fleet**. In a distributed environment, if one shard finds a 
  zero-diff match, it can signal a global "stop" to all other nodes via a distributed lock or 
  message bus, saving significant compute costs.


## LC 259. 3Sum Smaller
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Inequality Counting (**sum < target**)**: We need to count triplets that satisfy a threshold.
  * **Batch Counting Property**: In a sorted array, if `nums[i] + nums[j] + nums[k] < target`, then 
  `nums[i] + nums[j]` paired with **any** element between index `j` and `k` is also guaranteed to be
  less than the target.
  * **Two-Pointer Range Rule**: This allows us to add `(k - j)` to our result in **O(1)** time rather 
  than iterating through the inner range, maintaining **O(N^2)** complexity.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time       | Space    | Best Used When... |
| :--- |:-----------|:---------| :--- |
| **Brute Force** | **O(N^3)** | **O(1)** | Simplest logic for tiny inputs. |
| **Sorting + Two Pointers** | **O(N^2)** | **O(1)** | **Optimal.** Essential for counting pairs/triplets in ranges. |

### 3. Native Kotlin Syntax Pitfalls
*   **Duplicate Skipping Error**: Unlike LC 15 (3Sum), we **must not** skip duplicate values in LC 259. 
The problem asks for the number of index triplets **(i, j, k)**, meaning elements with the same value 
at different indices are distinct contributors to the count.

### 4. Code Block
```kotlin
fun threeSumSmaller(nums: IntArray, target: Int): Int {
    nums.sort()
    var count = 0
    
    for (i in 0 until nums.size - 2) {
        var j = i + 1
        var k = nums.lastIndex
        
        while (j < k) {
            if (nums[i] + nums[j] + nums[k] < target) {
                // Key Insight: All elements from j+1 to k also work with nums[i] and nums[j]
                count += (k - j)
                j++
            } else {
                k--
            }
        }
    }
    return count
}
```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **Counting vs. Enumerating**: The current algorithm is $O(N^2)$ only because we are **counting** 
results via the `(k - j)` rule. If the system design requirement shifts to **listing** every triplet, 
the complexity is forced to $O(N^3)$, which may require a distributed processing framework like 
Apache Spark or MapReduce to handle the output volume.

* **Memory Pressure & primitive arrays**: In performance-critical Android apps, using `IntArray` 
avoids the object boxing overhead of `List<Int>`. This prevents Garbage Collection "stutters" that 
would otherwise occur if the system were forced to manage millions of individual triplet objects on 
the heap.

* **Counting vs. Materializing**:
  * **The Problem**: If the system needs to return the actual triplets instead of just the count, 
  the complexity jumps from **O(N^2)** to **O(N^3)** because you must iterate through the range to 
  list every item.
  * **The Trade-off**: The "Batch Counting" rule is only possible if you don't need to see the items. 
  This is a classic "Big Data" optimization: always count at the source before materializing results.
  
* **Distributed Range Counting**:
  * **The Strategy**: To scale this to 10 billion numbers, use a Histogram-based approach.
  * **The Logic**: Pre-calculate the distribution of numbers into buckets. Use the buckets to 
  estimate the count of triplets below the target without ever scanning the individual raw data 
  points.


## LC 220. Contains Duplicate III
### 1. Core Pattern Identifier
* **What specific constraint triggered the solution design?**
  * **Index Proximity (indexDiff >= |i - j| <= })**: This contiguous index constraint is a classic
  signal for the Sliding Window pattern. We only need to consider elements within a window of size 
  indexDiff.
  * **Value Proximity (valueDiff >= |nums[i] - nums[j]|)**: This "almost duplicate" value 
  constraint triggers the need for an efficient way to search for nearest neighbors, rather than 
  exact matches.
  * **Bucket Hashing Strategy**: By dividing values into buckets of size valueDiff + 1, any two 
  numbers that satisfy the constraint will either fall into the same bucket or adjacent buckets.
  * **Ordered Set Strategy**: Alternatively, a Balanced BST (TreeSet) allows us to use floor and 
  ceiling to find the closest values in $O(Log(K))$ time.
  * **Numerical Overflow** : The potential for large integer differences triggers the design choice 
  to use Long logic for all arithmetic.

### 2. Complexity Boundaries
* Comparison Matrix

| Approach | Time Complexity   | Space Complexity | Best Used When... |
| :--- |:------------------|:-----------------| :--- |
| **Bucket Hashing** | **O(N)**          | **O(K)**         | **Maximum Performance.** Best for high-frequency data where **O(1)** lookup is critical. |
| **TreeSet (BST)** | **O(N * Log(K))** | **O(K)**         | **Robustness.** Easier to implement; handles range logic without math-heavy bucket IDs. |
| **Brute Force** | **O(N * K)**      | **O(1)**         | Memory is strictly limited and the window (**K**) is tiny. |

*\*Where **N** is total elements and **K** is the `indexDiff` constraint.*

### 3. Native Kotlin Syntax Pitfalls
* **Integer Overflow on valueDiff + 1**: If valueDiff is Int.MAX_VALUE, the expression valueDiff + 1 
will wrap around to a negative number, breaking the bucket logic. You must cast to Long first: val bucketSize = valueDiff.toLong() + 1.
* **The "Mirroring" Division Bug**: In Kotlin/Java, integer division truncates toward zero 
(e.g., -1 / 5 = 0). This is a disaster for buckets because -1 and 1 would fall into the same bucket.
The formula if (num >= 0) num / size else (num + 1) / size - 1 is essential to ensure negative numbers 
have their own distinct, continuous buckets.
* **TreeSet API naming**: Kotlin's TreeSet inherits from Java. Don't confuse floor() (greatest 
element $\le$ current) and ceiling() (smallest element $\ge$ current). If you use the wrong one, 
you'll miss proximity matches.
* **Safe-call vs. Double-bang**: When checking adjacent buckets with bucketMap[id - 1], 
use ?.let { ... } or if (bucketMap.containsKey()). Avoid !! because the sliding window logic removes
elements, and assuming a bucket exists just because you calculated an ID will cause a NullPointerException.


### 4. Code Block
```kotlin
fun containsNearbyAlmostDuplicate(nums: IntArray, indexDiff: Int, valueDiff: Int): Boolean {
    // Optimal Bucket Hashing Approach - HashMap as Bucket using Value Difference as Bucket Size
    val bucketMap = HashMap<Long, Long>()
    // Use Long to prevent overflow during bucket size calculation
    val bucketSize = valueDiff.toLong() + 1
    
    for (i in nums.indices) {
        val num = nums[i].toLong()
        // Calculate bucket ID using Long logic
        val bucketId = if (num >= 0) num / bucketSize else (num + 1) / bucketSize - 1
    
        // Case 1: Same bucket exists (Difference is guaranteed to be <= valueDiff)
        if (bucketMap.containsKey(bucketId)) return true
    
        // Case 2: Adjacent buckets (Must check actual difference)
        bucketMap[bucketId - 1]?.let { if (num - it <= valueDiff) return true }
        bucketMap[bucketId + 1]?.let { if (it - num <= valueDiff) return true }
    
        // Maintain sliding window
        if (i >= indexDiff) {
            val head = nums[i - indexDiff].toLong()
            val headBucketId = if (head >= 0) head / bucketSize else (head + 1) / bucketSize - 1
            bucketMap.remove(headBucketId)
        }
    
        bucketMap[bucketId] = num
    }
    
    return false
}
```

```kotlin
fun containsNearbyAlmostDuplicate(nums: IntArray, indexDiff: Int, valueDiff: Int): Boolean {
    // TreeSet (Red-Black Tree) Sliding Window Approach
    val treeSet = TreeSet<Long>() // Use Long to prevent overflow in math

    for (j in nums.indices) {
        val num = nums[j].toLong()

        // Find the nearest values to 'num' in the current window
        val floor = treeSet.floor(num)     // Greatest element <= num
        val ceiling = treeSet.ceiling(num) // Smallest element >= num

        if ((floor != null && num - floor <= valueDiff) ||
            (ceiling != null && ceiling - num <= valueDiff)) {
            return true
        }

        treeSet.add(num)

        // Maintain the sliding window size
        if (j >= indexDiff) {
            treeSet.remove(nums[j - indexDiff].toLong())
        }
    }
    return false
}

```

### 5. Alternative Trade-offs (For System Design Dialogues)
* **The "Alternative Trade-offs" section should focus on the tension between mathematical precision 
and algorithmic efficiency (Buckets) and structural robustness (Trees), especially at scale.**

  * **Bucket Hashing vs. Balanced BST (TreeSet)**:
    * **Buckets (O(N))**: Superior in throughput. However, they require a fixed valueDiff to define 
    the bucket size. If the requirements change to allow a dynamic or variable "proximity" threshold, 
    the bucket logic must be completely recalculated and the map rebuilt.
    * **TreeSet (O(N \log K))**: More architecturally flexible. Since it maintains a sorted order, 
    it can handle varying range queries without needing to know the "bucket size" upfront. You trade
    a log-factor in speed for significant flexibility in search logic.
  
  * **Skedded Data & Load Balancing**:
    * **The Risk**: In Bucket Hashing, if your data is highly clustered (e.g., a million numbers 
    between 0 and 10), you might end up with "Hot Buckets" where one bucket contains massive 
    amounts of data.
    * **The System Fix**: A Balanced BST (TreeSet) inherently handles clustered data better by 
    maintaining a tree height of $\log K$. In a distributed system, you would use a consistent 
    hashing layer to distribute these buckets across multiple nodes to prevent single-node failure.
    
  * **Memory Pressure & Object Boxing**:
    * **The Problem**: On the Android/JVM platform, HashMap<Long, Long> and TreeSet<Long> are 
    "object-heavy." Every number is wrapped in a Long object, creating significant pressure on the 
    Garbage Collector (GC).
    * **The Optimization**: For a high-performance system (like a real-time trading engine), you 
    would use a Primitive Collection Library (like fastutil or Koloboke) or an Array-based Min-Heap 
    to keep data unboxed and contiguous in memory, drastically reducing latency spikes caused by GC 
    pauses.

  * **Online vs. Offline Processing**:
    * **Online (Streaming)**: The Sliding Window approach is mandatory for real-time streams where 
    you only have access to a buffer of the last **K** elements.
    * **Offline (Batch)**: If the entire dataset is available, an alternative is to Sort by Value. 
    You would then only need to check adjacent elements in the sorted list whose original indices 
    are within **K**. This changes the problem to **O(N * Log(N))** time but simplifies the proximity 
    check significantly.