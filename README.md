# Lock granularity and concurrent lists.

This repository contains an experiment to test how different locking strategies affect the performance of a concurrent data structure. I am building two versions of a thread safe sorted linked list in Java to compare their execution times under different thread loads.

## Coarse grained synchronization

The first implementation uses a single global lock to protect the entire linked list. This approach is straightforward to write because only one thread can enter the list at any given time. It prevents race conditions completely but creates a bottleneck since other threads are forced to wait even if they want to access completely different nodes. 

## Fine grained synchronization

The second implementation removes the global lock and instead gives every single node its own lock. This allows multiple threads to traverse and modify different sections of the list at the same time. To keep the structure safe during traversal, the implementation uses hand over hand locking. A thread locks the current node, locks the next node, and only then releases the lock on the previous node.

## Performance comparison

The final part of the experiment measures the execution time of both implementations. A workload of add, remove, and contains operations is run across configurations of 2, 4, 8, and 16 threads to see the actual cost of locking overhead versus the benefit of higher concurrency.
